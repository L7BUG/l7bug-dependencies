# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 构建与测试命令

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

## 项目架构

这是一个 Maven 多模块 **依赖管理** 项目（`packaging: pom`），为 L7BUG 提供共享的 Spring Boot 4.0 Starter，供其他微服务项目引用。基于
Java 25，Spring Boot 4.0.6。

### 模块总览

| 模块                                   | 用途                                                              | 依赖             |
|--------------------------------------|-----------------------------------------------------------------|----------------|
| `l7bug-common`                       | 错误码、异常类、`Result<T>` 统一响应体、分页 DTO                                | 无（独立模块）        |
| `l7bug-database-spring-boot-starter` | 数据库层自动配置：MyBatis-Plus + JPA + ShardingSphere + 雪花 ID            | `l7bug-common` |
| `l7bug-web-spring-boot-starter`      | Web 层自动配置：MDC 链路追踪、全局异常处理、Jackson 配置、`@HasAuthorities` 权限校验 AOP | `l7bug-common` |

### 自动配置入口

两个 Starter 均使用 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`（Spring Boot 3.4+
风格，非旧版 `spring.factories`）。

- **Database**: `DataBaseAutoConfiguration` — 注册 MyBatis-Plus 分页插件、自动填充处理器（`createBy`、`updateBy`、
  `createTime`、`updateTime`、`delFlag`）、JPA 审计（`AuditorAware`、`DateTimeProvider`）。数据库类型通过
  `l7bug.database.dbType` 配置，默认 PostgreSQL。
- **Web**: `WebAutoConfiguration` + `JacksonConfig` + `BaseExceptionHandler` — 请求拦截器从 Header 提取认证/追踪信息写入
  MDC，AOP 切面自动为 `Result` 响应设置 `requestId`，`@HasAuthorities` 注解校验权限，全局异常处理器捕获 `AbstractException`
  和 `Throwable`。

### 核心设计模式

- **错误码体系**: `BaseErrorCode` 接口，由三个枚举实现 — `ClientErrorCode`（C 开头，客户端/用户错误）、`ServerErrorCode`（S
  开头，服务端内部错误）、`RemoteErrorCode`（R 开头，第三方服务调用错误）。每个错误码必须唯一，`ClientErrorCodeTest` 通过测试强制校验。
- **异常体系**: `AbstractException` 继承 `RuntimeException`，携带 `code` + `message`。三个具体类型 — `ClientException`、
  `ServerException`、`RemoteException` — 各自绑定对应的错误码枚举。抛出对应异常类型后，`BaseExceptionHandler` 自动将其映射为
  `Result.failure(code, message)`。
- **微服务 Header 契约**: 自定义 Header（`L7-BUG-REQUEST-ID`、`L7-BUG-TOKEN-GUB_7L`、`L7-BUG-USERNAME`、`L7-BUG-USER-ID`、
  `L7-BUG-authorities`）在微服务间传递认证和链路追踪信息。Web Starter 的拦截器负责读取；下游服务应透传这些 Header。
- **CurrentUserId 抽象**: `CurrentUserId` 是接口，默认实现返回 `-1L`。消费方通过声明一个实现 `CurrentUserId` 的
  `@Component` 来提供真实实现（如从 JWT/Session 中提取用户 ID）。
- **双 ORM 基类**: `BaseNotDeleDo` 提供共享字段（`id`、`createBy`、`updateBy`、`createTime`、`updateTime`），同时标注 JPA 和
  MyBatis-Plus 注解。`BaseDo` 在此基础上增加 `delFlag`（逻辑删除，通过 `@TableLogic` 实现）。需要软删除的表继承 `BaseDo`，否则继承
  `BaseNotDeleDo`。
- **雪花 ID**: `BaseNotDeleDo.id` 上的 `@SnowflakeId` 注解使用 Hibernate 的 `@IdGeneratorType` 委托给
  `MplusSnowflakeGenerator`（内部调用 MyBatis-Plus 的 `IdWorker.getId()`），确保 JPA 和 MyBatis-Plus 两种持久化路径生成的
  ID 一致。
- **分页**: `PageQuery`（来自 common）是 API 层的分页请求 DTO。`PageUtils.buildMybatisPlusPage()` 将其转换为 MyBatis-Plus 的
  `Page` 对象。返回时使用 `PageData<T>`（同样来自 common）作为分页响应体。

### Commit 规范

使用中文的 Conventional Commits：`feat(模块):`、`fix(模块):`、`refactor(模块):`、`test(模块):`、`chore(deps):`。模块名：
`common`、`database`、`web`、`error`。

### 代码风格

- 缩进：Java、XML、YAML 使用 Tab；其他文件使用 4 空格（见 `.editorconfig`）
- UTF-8 编码，LF 换行
- 全项目使用 Lombok（根 POM 中 scope 为 provided）
