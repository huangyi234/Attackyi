# Attackyi 家电销售管理系统（骨架版）

基于 Spring Boot 的家电销售管理系统后端骨架，覆盖用户与角色、商品、库存、订单、基础分析等模块，并预留 Redis 缓存与 JWT 安全链路。

## 功能概览
- **认证与授权**：JWT + Spring Security，注册/登录接口；基于角色的接口权限。
- **用户与角色**：用户模型、角色模型、用户角色关联。
- **商品管理**：商品基础字段（SKU、品牌、型号、能效、尺寸等）增改查，支持上下架。
- **库存管理**：按仓库、序列号追踪可用/预占/损坏数量，并将可售量写入 Redis 缓存键 `stock:{sku}`。
- **订单管理**：下单（含多商品明细）与订单列表查询。
- **数据概览**：简单的订单/商品/用户计数指标示例。
- **审计记录**：审计记录实体与服务入口，可按需扩展调用。

## 技术栈
- Spring Boot 3.2.x、Spring Security、Spring Data JPA、Validation、Actuator
- MySQL/H2（默认嵌入式 H2，兼容 MySQL 模式）、Redis（缓存热销库存）
- JJWT（JWT 令牌生成/解析）、Lombok（可选）

## 快速启动（本地开发）
1. **JDK 17+ & Maven**：需要本地具备 JDK17 与 Maven。
2. **数据库**：默认使用 H2 内存库，可直接运行；切换 MySQL 时在 `src/main/resources/application.yml` 中修改 `spring.datasource` 配置。
3. **Redis**：配置在 `spring.data.redis`，若未启用 Redis，可临时启动本地实例或改为禁用缓存写入逻辑。
4. **运行**：
   ```bash
   mvn spring-boot:run
   ```
5. **接口示例**：
   - `POST /api/auth/register` / `POST /api/auth/login`
   - `GET /api/products`，`POST /api/products`（管理员/运营）
   - `POST /api/inventory`（管理员/运营），`GET /api/inventory/product/{productId}`
   - `POST /api/orders`（销售/管理员），`GET /api/orders`
   - `GET /api/analytics/overview`（管理员）

## 安全配置
- JWT 密钥与过期时间在 `security.jwt` 下配置。
- 角色判断采用 `ROLE_{code}` 约定，默认注册角色为 `USER`。

## 目录结构
- `src/main/java/com/attackyi`：应用入口与配置
- `config/`：安全配置
- `controller/`：REST 接口
- `domain/`：实体定义
- `dto/`：请求/响应 DTO
- `repository/`：数据访问层
- `security/`：JWT 与用户细节实现
- `service/`：业务服务
- `src/main/resources/application.yml`：默认配置

## 备注
- 当前为骨架实现，未包含消息队列、微服务拆分、序列号全链路追踪、安装/售后子流程等深度能力，可在此基础上按业务扩展。
- 若拉取依赖受网络限制，可配置内网 Maven 仓库或缓存依赖后再执行构建。本仓库 `pom.xml` 已预置阿里云、腾讯云、华为云与 Spring 官方仓库作为镜像/备用源，仍无法下载时请检查网络出口或使用本地私服代理。
