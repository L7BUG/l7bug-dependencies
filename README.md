# l7bug-dependencies

L7BUG 微服务公共依赖库：统一响应体、错误码/异常体系、分页 DTO，以及开箱即用的 Web / Database 两个 Spring Boot Starter。

基于 **Java 25**、**Spring Boot 4.0.6** 构建，供 L7BUG 内部微服务项目复用，保证各服务的响应格式、异常处理、数据库行为一致。

## 模块总览

| 模块 | 用途 | 依赖 |
|------|------|------|
| `l7bug-common` | 统一响应体、错误码、异常体系、分页 DTO、Header 契约 | 无（纯 Java，零 Spring 依赖） |
| `l7bug-web-spring-boot-starter` | Web 层自动配置：MDC 链路追踪、全局异常处理、Jackson 序列化、`@HasAuthorities` 权限校验 | `l7bug-common` |
| `l7bug-database-spring-boot-starter` | 数据库层自动配置：MyBatis-Plus 分页/自动填充、JPA 审计、雪花 ID、双 ORM 实体基类 | `l7bug-common` |

## 快速开始

### 1. 构建并安装到本地仓库

```bash
# 编译所有模块
mvn compile

# 安装到本地 Maven 仓库（跳过测试）
mvn install -DskipTests
```

### 2. 在业务项目中引入

引入对应的 Starter 依赖即可，自动配置会通过 `AutoConfiguration.imports` 生效（Spring Boot 3.4+ 机制，无需手动 `@Import`）：

```xml
<dependency>
    <groupId>com.l7bug</groupId>
    <artifactId>l7bug-web-spring-boot-starter</artifactId>
    <version>4.0.6-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>com.l7bug</groupId>
    <artifactId>l7bug-database-spring-boot-starter</artifactId>
    <version>4.0.6-SNAPSHOT</version>
</dependency>
```

> `l7bug-common` 会被两个 Starter 自动传递引入，一般无需显式声明。

## 核心用法

### 统一响应体 `Result<T>`

所有接口统一返回 `Result<T>`，结构固定：

```json
{
  "requestId": "8F3A2C1B...",
  "code": "0",
  "message": "success",
  "data": {}
}
```

| 字段 | 说明 |
|------|------|
| `requestId` | 请求 ID，链路追踪用，由 Web Starter 的 AOP 自动填充 |
| `code` | 状态码，`"0"` 表示成功 |
| `message` | 提示信息 |
| `data` | 业务数据 |

```java
@GetMapping("/user/{id}")
public Result<UserVO> getUser(@PathVariable Long id) {
    return Results.success(userService.getById(id));
}

@GetMapping("/error")
public Result<Void> error() {
    return Results.failure(ClientErrorCode.NOT_AUTHENTICATION);
}
```

### 错误码体系

`BaseErrorCode` 接口由三个枚举实现，错误码按前缀分类，**全局唯一**：

| 枚举 | 前缀 | 场景 |
|------|------|------|
| `ClientErrorCode` | `C` | 客户端/用户错误（未登录、无权限、参数错误等） |
| `ServerErrorCode` | `S` | 服务端内部错误（系统异常、超时等） |
| `RemoteErrorCode` | `R` | 第三方服务调用错误（邮件服务失败、远程降级等） |

```java
public enum OrderErrorCode implements BaseErrorCode {
    ORDER_NOT_EXIST("C400001", "订单不存在"),
    ;

    private final String code;
    private final String message;
    // getter...
}
```

> `ClientErrorCodeTest` 通过单元测试强制校验错误码不重复，新增错误码后跑 `mvn test -pl l7bug-common` 即可验证。

### 异常体系

`AbstractException`（继承 `RuntimeException`，携带 `code + message`）派生出三个具体异常，抛出后由 Web Starter 的全局异常处理器自动映射为 `Result.failure(code, message)`：

```java
// 业务代码中直接抛出
throw new ClientException(ClientErrorCode.ACCESS_DENIED);
throw new ClientException(ClientErrorCode.ORDER_NOT_EXIST);
throw new ServerException(ServerErrorCode.SERVER_ERROR, e);

// 调用第三方服务失败时
throw new RemoteException(RemoteErrorCode.EMAIL_CLIENT_ERROR, e);
```

### 分页

API 层使用 `PageQuery` 接收分页参数，数据库层用 `PageUtils` 转换为 MyBatis-Plus 的 `Page`，返回统一使用 `PageData<T>`：

```java
public PageData<UserVO> page(PageQuery query) {
    Page<UserDO> page = PageUtils.buildMybatisPlusPage(query);
    Page<UserDO> result = userMapper.selectPage(page, null);
    return new PageData<>(result.getTotal(), result.getRecords());
}
```

```json
{
  "requestId": "...",
  "code": "0",
  "message": "success",
  "data": {
    "total": 100,
    "data": []
  }
}
```

## Web Starter 能力

引入 `l7bug-web-spring-boot-starter` 后自动获得：

- **MDC 链路追踪**：拦截器从 Header 提取认证/追踪信息写入 SLF4J MDC（`traceId`、`username`、`userId`、`token`、`authorities`），并在请求结束自动清理；日志中可直接使用 `%X{traceId}` 输出。
- **`requestId` 自动填充**：`SetRequestAspect` 切面在 Controller 返回 `Result` 后自动回填 `requestId`。
- **全局异常处理**：`BaseExceptionHandler` 统一捕获 `AbstractException` 与未知异常，返回标准 `Result`，避免异常堆栈泄漏给前端。
- **统一 Jackson 配置**：
  - 日期格式 `yyyy-MM-dd HH:mm:ss` / `yyyy-MM-dd`
  - `Long` 序列化为字符串（防止前端 JS 精度丢失）
  - 时区 `Asia/Shanghai`，忽略未知属性，null 字段不输出
- **`@HasAuthorities` 权限校验**（AOP）：

```java
@GetMapping("/admin")
@HasAuthorities("system:user:delete")
public Result<Void> deleteUser() {
    return Results.success();
}
```

> 未认证用户（`userId == -1`）自动跳过校验，避免网关未透传身份时误拦截。

## Database Starter 能力

引入 `l7bug-database-spring-boot-starter` 后自动获得：

- **MyBatis-Plus 分页插件**：`PaginationInnerInterceptor`，数据库类型通过配置指定（默认 PostgreSQL）。
- **公共字段自动填充**：插入时自动填充 `createBy / updateBy / createTime / updateTime / delFlag`，更新时自动更新 `updateBy / updateTime`。
- **JPA 审计**：`@EnableJpaAuditing` + `AuditorAware`（操作人取自 `CurrentUserId`）+ `DateTimeProvider`。
- **雪花 ID**：`@SnowflakeId` 注解（基于 Hibernate `@IdGeneratorType`），JPA 与 MyBatis-Plus 两条持久化路径生成一致的雪花 ID。

### 实体基类

```java
// 需要逻辑删除的表继承 BaseDo（自动带 delFlag 逻辑删除字段）
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class UserDO extends BaseDo {
    private String username;
}

// 不需要软删除的表继承 BaseNotDeleDo（id、createBy、updateBy、createTime、updateTime）
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_log")
public class LogDO extends BaseNotDeleDo {
    private String content;
}
```

两个基类同时标注了 JPA（`@MappedSuperclass`、`@CreatedBy` 等）与 MyBatis-Plus（`@TableId`、`@TableField(fill=...)` 等）注解，同一实体可被两种持久化方式使用。

### 自定义当前用户

`CurrentUserId` 接口用于提供"当前操作人"（自动填充与 JPA 审计取数），默认实现返回 `-1L`。业务项目声明一个 `@Component` 覆盖即可：

```java
@Component
public class JwtCurrentUserId implements CurrentUserId {
    @Override
    public Long getCurrentUserId() {
        return MdcUserInfoContext.getMdcUserId()...; // 从你的上下文提取
    }
}
```

### 配置项

| 配置 | 默认值 | 说明 |
|------|--------|------|
| `l7bug.database.dbType` | `POSTGRE_SQL` | 分页插件数据库类型（`com.baomidou.mybatisplus.annotation.DbType` 枚举） |

```yaml
l7bug:
  database:
    dbType: MYSQL   # 使用 MySQL 时
```

## 微服务 Header 契约

跨服务调用时，网关/上游服务需透传以下 Header，Web Starter 的拦截器会自动读取并写入 MDC：

| Header | 说明 |
|--------|------|
| `L7-BUG-REQUEST-ID` | 请求 ID（缺失时自动生成 UUID） |
| `L7-BUG-TOKEN-GUB_7L` | 认证 Token |
| `L7-BUG-USERNAME` | 用户名 |
| `L7-BUG-USER-ID` | 用户 ID |
| `L7-BUG-authorities` | 权限标识（逗号分隔） |

## 构建与测试

```bash
# 编译所有模块
mvn compile

# 运行所有测试
mvn test

# 运行单个模块的测试
mvn test -pl l7bug-common

# 运行单个测试类
mvn test -pl l7bug-common -Dtest=ClientErrorCodeTest

# 生成 JaCoCo 覆盖率报告（test 阶段自动生成）
mvn test
# 报告路径: target/site/jacoco/index.html

# 安装到本地 Maven 仓库（跳过测试）
mvn install -DskipTests
```

## 环境要求

- JDK 25+
- Maven 3.9+（父 POM 基于 `spring-boot-starter-parent` 4.0.6）
- 如需部署到远程仓库，同步修改根 POM 中的 `deploy.version`（当前 `4.0.6-SNAPSHOT`）

## 相关文档

- [CLAUDE.md](CLAUDE.md) — 项目开发指南（构建命令、模块说明、Commit 规范）
