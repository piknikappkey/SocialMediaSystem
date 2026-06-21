# 社交媒体系统

基于 Vue3 + Spring Boot + MyBatis + MySQL 的社交媒体平台。

## 介绍

本系统为社交媒体平台，采用模块化设计，支持 Web 端访问。核心模块包括用户管理、内容管理、社交互动及消息系统。

前端使用 Vue.js 框架，后端采用 Spring Boot，数据库为 MySQL 9.2。

本仓库仅展示系统的源码、原型、以及数据库中的各个表数据，原始的图片、视频等数据不在本仓库。

## 软件架构

- 前端：Vue 3
- 后端：Spring Boot
- 数据库：MySQL 9.2

## 安装教程

1. 使用 VSCode 打开 `social_media_vue` 文件夹，在控制台输入 `npm install`
2. 修改 `src/requests.ts` 文件中的 `baseUrl` 为后端地址
3. 使用 IDEA 打开 `social_media_springboot`，下载对应 jar 包。本系统使用 Spring Boot 版本 3.0.4，Java 版本为 Java 21
4. 配置好环境后，Vue 使用 `npm run dev` 运行，Spring Boot 运行 `SmSpringbootApplication.java`

## 使用说明

1. `社交媒体系统原型.rp` 文件为本项目制作的系统原型
2. `social_media_vue` 和 `social_media_springboot` 为源代码
3. `数据库表格数据` 文件夹中存储的是成品系统使用的数据库数据

## 成品图片

1. 首页
![首页](%E6%88%90%E5%93%81%E5%9B%BE%E7%89%87/image0.png.png)
2. 首页动态详情
![首页动态详情](%E6%88%90%E5%93%81%E5%9B%BE%E7%89%87/image1.png.png)
3. 分享
![分享](%E6%88%90%E5%93%81%E5%9B%BE%E7%89%87/image2.png.png)
4. 消息
![消息](%E6%88%90%E5%93%81%E5%9B%BE%E7%89%87/image3.png.png)
5. 登录
![登录](%E6%88%90%E5%93%81%E5%9B%BE%E7%89%87/image4.png.png)