# 社交媒体系统

基于 Vue 3 + Spring Boot + MyBatis + MySQL 的社交媒体平台。

## 介绍

本系统为社交媒体平台，采用模块化设计，支持 Web 端访问。核心功能包括用户管理、内容发布、社交互动及实时消息系统。

> 本仓库仅展示系统的源码、原型以及数据库各表的数据，原始的图片、视频等媒体资源不在本仓库范围内。

### 功能特性

- **用户管理**：注册、登录（含邮箱验证码）、个人信息维护
- **内容管理**：动态发布、热门推荐、动态详情查看
- **社交互动**：点赞、收藏、关注、评论
- **消息系统**：基于 WebSocket 的实时聊天、好友在线状态

## 软件架构

- **前端**：Vue 3 + TypeScript + Vue Router + Pinia + Element Plus + Axios + Sass
- **后端**：Spring Boot 3.4.4 + MyBatis + Lombok + Jackson + WebSocket + JavaMail
- **数据库**：MySQL 9.2

## 环境要求

| 依赖 | 版本 |
| --- | --- |
| Node.js | 18+（推荐 LTS 版本） |
| JDK | 21 |
| Maven | 3.6+ |
| MySQL | 9.2 |

## 安装教程

### 1. 准备数据库

1. 创建数据库 `social_media_java`（字符集建议 utf8mb4）
2. 参照 `数据库表格数据` 文件夹下的各 `.xlsx` 建立对应的表结构，并导入数据

### 2. 启动后端

1. 使用 IDEA 打开 `social_media_springboot/sm_springboot`
2. 修改 `src/main/resources/application.yml`：
   - 数据库连接地址、用户名、密码
   - 邮箱账号及授权码（用于发送验证码）
   - `spring.web.resources.static-locations` 中的媒体资源存储路径
3. 通过 Maven 下载项目依赖
4. 运行主类 `SmSpringbootApplication.java`，后端默认监听 `8080` 端口

### 3. 启动前端

1. 使用 VSCode 打开 `social_media_vue` 文件夹
2. 在控制台执行 `npm install` 安装依赖
3. 修改 `src/utils/request.ts` 中的 `baseUrl` 为后端地址（如 `http://localhost:8080`）
4. 执行 `npm run dev` 启动开发服务器

## 项目结构

```
.
├── social_media_vue/              # 前端源代码（Vue 3）
├── social_media_springboot/       # 后端源代码（Spring Boot）
│   └── sm_springboot/             # Maven 项目根
├── 数据库表格数据/                 # 各数据表的示例数据（.xlsx）
├── 成品图片/                      # 系统成品截图
├── 社交媒体系统原型.rp            # Axure 原型文件
└── README.md
```

前端主要页面（`social_media_vue/src/views/`）：

- `Home/` 首页与动态详情
- `Hot/` 热门
- `Issue/` 发布动态
- `Message/` 消息
- `Me/` 个人中心（作品 / 喜欢 / 收藏 / 设置）

## 使用说明

- `社交媒体系统原型.rp` 需使用 [Axure](https://www.axure.com/) 打开查看
- `social_media_vue` 和 `social_media_springboot` 分别为前后端源代码
- `数据库表格数据` 文件夹存储成品系统使用的数据库数据，需手动建表导入

## 成品图片

1. 首页
   ![首页](成品图片/image0.png)
2. 首页动态详情
   ![首页动态详情](成品图片/image1.png)
3. 分享
   ![分享](成品图片/image2.png)
4. 消息
   ![消息](成品图片/image3.png)
5. 登录
   ![登录](成品图片/image4.png)

## License

本项目仅用于学习交流。
