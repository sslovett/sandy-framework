// APP 链接分组
export interface AppLinkGroup {
  // 分组名称
  name: string
  // 链接列表
  links: AppLink[]
}

// APP 链接
export interface AppLink {
  // 链接名称
  name: string
  // 链接地址
  path: string
  // 链接的类型
  type?: APP_LINK_TYPE_ENUM
}

// APP 链接类型（需要特殊处理的链接类型）
export const enum APP_LINK_TYPE_ENUM {
  // 自定义页面详情
  CUSTOM_PAGE_DETAIL,
  // 详情页面（需要传递ID参数）
  DETAIL_PAGE
}

// APP 链接列表
// 注意：这是一个示例配置，请根据你的实际业务需求修改
export const APP_LINK_GROUP_LIST = [
  {
    name: '基础页面',
    links: [
      {
        name: '首页',
        path: '/pages/index/index'
      },
      {
        name: '个人中心',
        path: '/pages/user/index'
      },
      {
        name: '关于我们',
        path: '/pages/about/index'
      },
      {
        name: '帮助中心',
        path: '/pages/help/index'
      },
      {
        name: '设置',
        path: '/pages/setting/index'
      }
    ]
  },
  {
    name: '用户相关',
    links: [
      {
        name: '用户信息',
        path: '/pages/user/profile'
      },
      {
        name: '消息通知',
        path: '/pages/user/message'
      },
      {
        name: '我的收藏',
        path: '/pages/user/favorite'
      },
      {
        name: '浏览历史',
        path: '/pages/user/history'
      }
    ]
  },
  {
    name: '其他',
    links: [
      {
        name: '搜索页面',
        path: '/pages/search/index'
      },
      {
        name: '列表页面',
        path: '/pages/list/index'
      },
      {
        name: '详情页面',
        path: '/pages/detail/index',
        type: APP_LINK_TYPE_ENUM.DETAIL_PAGE
      },
      {
        name: '自定义页面',
        path: '/pages/custom/index',
        type: APP_LINK_TYPE_ENUM.CUSTOM_PAGE_DETAIL
      }
    ]
  }
] as AppLinkGroup[]
