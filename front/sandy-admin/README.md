# 通用管理后台框架模板

> 基于芋道开源项目（yudao-ui-admin-vue3）抽取的通用管理后台框架模板

## 🎯 项目简介

这是一个基于 **Vue 3 + Vite + Element Plus + TypeScript** 的通用管理后台框架模板。

已移除所有业务模块（CRM、ERP、商城、工作流等），保留了完整的 UI 框架、组件库、布局系统和权限系统，可快速开发各类管理后台系统。

## ✨ 核心特性

- 🎨 **50+ 通用组件** - 表单、表格、编辑器、图表等开箱即用
- 🎭 **完整布局系统** - 多种布局模式、主题切换、多语言支持
- 🔐 **权限系统** - 动态路由、按钮级权限控制
- 🛠️ **丰富工具** - useTable、useForm、useCrudSchemas 等 Hooks
- 📚 **开发示例** - 系统管理模块作为完整参考
- 📖 **详细文档** - 完善的使用指南和示例代码
- 🚀 **快速开发** - 专注业务逻辑，UI 层面开箱即用

## 🔧 技术栈

| 框架 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5.12 | 渐进式 JavaScript 框架 |
| Vite | 5.1.4 | 下一代前端构建工具 |
| Element Plus | 2.11.1 | Vue 3 组件库 |
| TypeScript | 5.3.3 | JavaScript 的超集 |
| Pinia | 2.1.7 | Vue 状态管理 |
| Vue Router | 4.4.5 | Vue 路由 |
| UnoCSS | 0.58.5 | 原子化 CSS |
| Iconify | 3.1.1 | 图标库 |

## 📦 快速开始

### 环境要求

- Node.js >= 16.18.0
- pnpm >= 8.6.0

### 安装依赖

```bash
pnpm install
```

### 开发运行

```bash
# 本地开发
pnpm dev

# 开发服务器
pnpm dev-server
```

### 构建部署

```bash
# 生产环境
pnpm build:prod

# 测试环境
pnpm build:test

# 预发布环境
pnpm build:stage
```

## 📖 文档

- **[快速开始](./QUICK_START.md)** - 5分钟上手指南
- **[框架说明](./TEMPLATE_README.md)** - 详细的特性介绍
- **[使用指南](./TEMPLATE_GUIDE.md)** - 完整的开发教程
- **[结构说明](./TEMPLATE_STRUCTURE.md)** - 目录结构详解
- **[转换报告](./转换完成报告.md)** - 框架转换详情

## 🎨 核心组件

### 表单相关
Form、FormCreate、Search、InputPassword、ColorInput、Cropper、UploadFile

### 表格相关
Table、Pagination、Descriptions

### 编辑器
Editor（WangEditor）、JsonEditor、MarkdownView

### 布局相关
ContentWrap、ContentDetailWrap、Card、Dialog、Sticky

### 数据展示
Echart、CountTo、DictTag、Icon、Qrcode、ImageViewer

### 其他工具
Backtop、Highlight、IFrame、Infotip、Tooltip、Verifition、Crontab、RouterSearch

## 📂 项目结构

```
├── src/
│   ├── api/              # API 接口
│   │   ├── login/       # 登录相关
│   │   └── system/      # 系统管理
│   ├── components/       # 通用组件（50+）
│   ├── hooks/           # 组合式函数
│   ├── layout/          # 布局组件
│   ├── router/          # 路由配置
│   ├── store/           # 状态管理
│   ├── utils/           # 工具函数
│   ├── views/           # 页面视图
│   │   ├── Login/      # 登录页
│   │   ├── Home/       # 首页
│   │   └── system/     # 系统管理（示例）
│   └── styles/         # 全局样式
├── QUICK_START.md       # 快速开始
├── TEMPLATE_README.md   # 框架说明
├── TEMPLATE_GUIDE.md    # 使用指南
└── package.json         # 依赖配置
```

## 🚀 开发示例

### 创建一个列表页面

```vue
<template>
  <ContentWrap>
    <Search :schema="searchSchema" @search="setSearchParams" />
    <Table
      :columns="columns"
      :data="tableObject.tableList"
      :loading="tableObject.loading"
      @register="register"
    />
  </ContentWrap>
</template>

<script setup lang="ts">
import { useTable } from '@/hooks/web/useTable'
import * as YourApi from '@/api/your-module'

const { register, tableObject, methods } = useTable({
  getListApi: YourApi.getList
})

methods.getList()
</script>
```

详细示例请查看 [使用指南](./TEMPLATE_GUIDE.md)

## 🎯 适用场景

- ✅ 企业管理后台
- ✅ 数据管理系统
- ✅ 内容管理系统（CMS）
- ✅ 运营管理平台
- ✅ 监控管理系统
- ✅ 配置管理平台

## 📝 常用命令

```bash
# 安装依赖
pnpm install

# 开发运行
pnpm dev

# 类型检查
pnpm ts:check

# 代码格式化
pnpm lint:format

# 代码检查
pnpm lint:eslint

# 构建生产
pnpm build:prod
```

## 🌟 核心优势

1. **开箱即用** - 完整的 UI 组件库和布局系统
2. **快速开发** - 丰富的示例代码和工具函数
3. **规范统一** - 成熟的代码结构和开发规范
4. **易于扩展** - 清晰的目录结构，方便添加新功能
5. **文档完善** - 详细的使用指南和示例代码
6. **类型安全** - 完整的 TypeScript 支持

## 🔗 相关链接

- 原项目地址：https://gitee.com/yudaocode/yudao-ui-admin-vue3
- 原项目文档：https://doc.iocoder.cn
- Vue 3 文档：https://cn.vuejs.org/
- Element Plus 文档：https://element-plus.org/zh-CN/
- Vite 文档：https://cn.vitejs.dev/

## 📄 License

MIT

---

**开始你的项目开发吧！** 🚀

如有问题，请查看 [使用指南](./TEMPLATE_GUIDE.md) 或参考 `src/views/system/` 下的示例代码。
