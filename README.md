**一个基于spring boot、spring security、mybatis plus、redis的轻量级、前后端分离、认证授权权限基础框架，开箱即用**

## 当前已具备的功能
- 用户登录+滑块验证码
- 菜单管理
- 角色管理
- 用户管理
- 权限管理，包括菜单和按钮权限
- 操作日志
- 在线接口文档

## 技术选型

| 技术 | 版本 | 说明 |
| --- | --- | --- |
| JDK |    1.8 |    开发环境 |
| Spring Boot |    2.6.7 |  MVC核心框架 |
| MyBatis |    3.5.0 |  ORM框架 |
| MyBatisPlus |    3.5.5 |  基于mybatis，使用lambda表达式的 |
| knife4j |    3.0.3 |  在线接口文档 |
| redisson |   3.19.3 | 对redis进行封装、集成分布式锁等 |
| hikari | 3.2.0 |  数据库连接池 |
| logback |    1.2.11 | log日志工具 |
| lombok | 1.18.24 |    简化对象封装工具 |
| hutool | 5.8.15 | 更适合国人的java工具集 |
| fastjson |   1.2.83 | json序列化与反序列化 |
| jwt |    0.9.1 |  token管理 |
| captcha |    1.3.0 |  滑块验证码 |

## 部署教程
### 1.项目结构
| 项目 | 说明 |
| --- |---|
doc  | 数据库脚本                     
front | 后台管理系统前端代码                
sandy-admin | 后台管理系统后端代码                
sandy-common | 工具包                       
sandy-security | 基于spring security的认证和授权代码 
### 2.开发环境
| 工具| 版本|
| --- | --- |
jdk  |  1.8
mysql | 5.7+
redis | 3.2+
### 3.启动
- 推荐使用idea，安装lombok插件，使用idea导入maven项目
- 将doc/framework.sql导入到mysql中
- 修改 application-dev.yml datasource.url、user、password
- 修改 application-dev.yml redis.host、redis.port
- 通过 sandy-admin项目WebApplication启动项目后台接口
- 超级管理员账号：admin/123456

