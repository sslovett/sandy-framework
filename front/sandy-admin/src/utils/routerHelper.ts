import type { RouteLocationNormalized, Router, RouteRecordNormalized } from 'vue-router'
import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import { isUrl } from '@/utils/is'
import { cloneDeep, omit } from 'lodash-es'
import qs from 'qs'
import type { SysMenu } from '@/api/menu/types'
import { MenuType } from '@/api/menu/types'

const modules = import.meta.glob('../views/**/*.{vue,tsx}')

/**
 * 将后端的 SysMenu 转换为前端的 AppCustomRouteRecordRaw
 * @param menu 后端菜单对象
 */
export const transformMenuToRoute = (menu: SysMenu): AppCustomRouteRecordRaw => {
  // 后端字段映射：menuName -> name, list -> children
  const menuName = menu.menuName || menu.name || ''
  const children = menu.list || menu.children
  return {
    path: menu.path || '',
    name: menuName,
    component: menu.component || '',
    redirect: '',
    icon: menu.icon || '',
    meta: {
      title: menuName,
      icon: menu.icon,
      hidden: menu.type === MenuType.BUTTON // 按钮类型隐藏
    },
    children: children ? children.map(transformMenuToRoute) : undefined,
    keepAlive: true,
    visible: menu.type !== MenuType.BUTTON, // 按钮不可见
    parentId: menu.parentId,
    alwaysShow: children && children.length > 0
  }
}

/**
 * 过滤掉按钮类型的菜单（type === 2）
 * @param menus 菜单列表
 */
export const filterButtonMenus = (menus: SysMenu[]): SysMenu[] => {
  return menus
    .filter((menu) => menu.type !== MenuType.BUTTON)
    .map((menu) => {
      const children = menu.list || menu.children
      return {
        ...menu,
        children: children ? filterButtonMenus(children) : undefined
      }
    })
}

/**
 * 从扁平菜单列表构建树结构
 * @param menus 扁平菜单列表
 * @param parentId 父菜单ID，默认为0（根节点）
 */
export const buildMenuTree = (menus: SysMenu[], parentId: number = 0): SysMenu[] => {
  const tree: SysMenu[] = []
  for (const menu of menus) {
    if (menu.parentId === parentId) {
      const children = buildMenuTree(menus, menu.id)
      if (children.length > 0) {
        menu.children = children
      }
      tree.push(menu)
    }
  }
  return tree
}

/**
 * 注册一个异步组件
 * @param componentPath 例:/bpm/oa/leave/detail
 */
export const registerComponent = (componentPath: string) => {
  for (const item in modules) {
    if (item.includes(componentPath)) {
      // 使用异步组件的方式来动态加载组件
      // @ts-ignore
      return defineAsyncComponent(modules[item])
    }
  }
}
/* Layout */
export const Layout = () => import('@/layout/Layout.vue')

export const getParentLayout = () => {
  return () =>
    new Promise((resolve) => {
      resolve({
        name: 'ParentLayout'
      })
    })
}

// 按照路由中meta下的rank等级升序来排序路由
export const ascending = (arr: any[]) => {
  arr.forEach((v) => {
    if (v?.meta?.rank === null) v.meta.rank = undefined
    if (v?.meta?.rank === 0) {
      if (v.name !== 'home' && v.path !== '/') {
        console.warn('rank only the home page can be 0')
      }
    }
  })
  return arr.sort((a: { meta: { rank: number } }, b: { meta: { rank: number } }) => {
    return a?.meta?.rank - b?.meta?.rank
  })
}

export const getRawRoute = (route: RouteLocationNormalized): RouteLocationNormalized => {
  if (!route) return route
  const { matched, ...opt } = route
  return {
    ...opt,
    matched: (matched
      ? matched.map((item) => ({
          meta: item.meta,
          name: item.name,
          path: item.path
        }))
      : undefined) as RouteRecordNormalized[]
  }
}

// 后端控制路由生成
export const generateRoute = (
  routes: AppCustomRouteRecordRaw[],
  parentPath: string = ''
): AppRouteRecordRaw[] => {
  const res: AppRouteRecordRaw[] = []
  const modulesRoutesKeys = Object.keys(modules)

  for (const route of routes) {
    // 1. 生成 meta 菜单元数据
    const meta = {
      title: route.name,
      icon: route.icon,
      hidden: !route.visible,
      noCache: !route.keepAlive,
      alwaysShow:
        route.children &&
        route.children.length > 0 &&
        (route.alwaysShow !== undefined ? route.alwaysShow : true)
    } as any
    // 特殊逻辑：如果后端配置的 MenuDO.component 包含 ?，则表示需要传递参数
    // 此时，我们需要解析参数，并且将参数放到 meta.query 中
    // 这样，后续在 Vue 文件中，可以通过 const { currentRoute } = useRouter() 中，通过 meta.query 获取到参数
    if (route.component && route.component.indexOf('?') > -1) {
      const query = route.component.split('?')[1]
      route.component = route.component.split('?')[0]
      meta.query = qs.parse(query)
    }

    // 2. 生成 data（AppRouteRecordRaw）
    // 路由地址转首字母大写驼峰，作为路由名称，适配keepAlive
    // 处理路径：如果是目录且路径为空，使用第一个子菜单的路径作为基础
    let routePath =
      route.path.indexOf('?') > -1 && !isUrl(route.path) ? route.path.split('?')[0] : route.path

    // 如果是目录（有子菜单）且路径为空，生成一个基于名称的路径
    let parentBasePath = ''
    if (route.children?.length && !routePath) {
      // 使用第一个子菜单路径的第一段作为目录路径
      const firstChildPath = route.children[0]?.path || ''
      const pathSegments = firstChildPath.split('/')
      parentBasePath = pathSegments[0] || ''
      routePath =
        '/' + (parentBasePath || route.name?.toLowerCase().replace(/\s+/g, '-') || 'menu')
    }

    // 确保顶级路由以 / 开头
    if (route.parentId === 0 && routePath && !routePath.startsWith('/')) {
      routePath = '/' + routePath
    }

    let data: AppRouteRecordRaw = {
      path: routePath,
      name:
        route.componentName && route.componentName.length > 0
          ? route.componentName
          : toCamelCase(route.path || routePath, true),
      redirect: route.redirect,
      meta: meta
    }
    //处理顶级非目录路由
    if (!route.children && route.parentId == 0 && route.component) {
      data.component = Layout
      data.meta = {
        hidden: meta.hidden
      }
      data.name = toCamelCase(route.path, true) + 'Parent'
      data.redirect = ''
      meta.alwaysShow = true
      const childrenData: AppRouteRecordRaw = {
        path: '',
        name:
          route.componentName && route.componentName.length > 0
            ? route.componentName
            : toCamelCase(route.path, true),
        redirect: route.redirect,
        meta: meta
      }
      const index = route?.component
        ? modulesRoutesKeys.findIndex((ev) => ev.includes(route.component))
        : modulesRoutesKeys.findIndex((ev) => ev.includes(route.path))
      childrenData.component = modules[modulesRoutesKeys[index]]
      data.children = [childrenData]
    } else {
      // 目录
      if (route.children?.length) {
        data.component = Layout
        data.redirect = getRedirect(routePath, route.children)
        // 外链
      } else if (isUrl(route.path)) {
        data = {
          path: '/external-link',
          component: Layout,
          meta: {
            name: route.name
          },
          children: [data]
        } as AppRouteRecordRaw
        // 菜单
      } else {
        // 处理子路由路径：如果有父路径，需要去掉父路径前缀，变成相对路径
        const componentSearchPath = route.path
        if (parentPath && route.path.startsWith(parentPath.replace('/', '') + '/')) {
          // 子路由路径去掉父路径前缀，变成相对路径
          const relativePath = route.path.substring(parentPath.replace('/', '').length + 1)
          data.path = relativePath
        }

        // 对后端传component组件路径和不传做兼容
        let index = -1
        if (route?.component) {
          // 如果后端传了 component，直接匹配
          index = modulesRoutesKeys.findIndex((ev) => ev.includes(route.component))
        } else {
          // 优先匹配 path/index.vue 格式
          const searchPath = componentSearchPath.startsWith('/')
            ? componentSearchPath.slice(1)
            : componentSearchPath
          index = modulesRoutesKeys.findIndex((ev) => ev.includes(`${searchPath}/index.vue`))
          
          // 如果没找到 index.vue，再尝试匹配 path.vue
          if (index === -1) {
            index = modulesRoutesKeys.findIndex((ev) => ev.endsWith(`${searchPath}.vue`))
          }
        }

        if (index !== -1) {
          data.component = modules[modulesRoutesKeys[index]]
        }
      }
      if (route.children) {
        data.children = generateRoute(route.children, routePath)
      }
    }
    res.push(data as AppRouteRecordRaw)
  }

  return res
}
export const getRedirect = (parentPath: string, children: AppCustomRouteRecordRaw[]) => {
  if (!children || children.length == 0) {
    return parentPath
  }
  const path = generateRoutePath(parentPath, children[0].path)
  // 递归子节点
  if (children[0].children) return getRedirect(path, children[0].children)
}
const generateRoutePath = (parentPath: string, path: string) => {
  if (parentPath.endsWith('/')) {
    parentPath = parentPath.slice(0, -1) // 移除默认的 /
  }
  if (!path.startsWith('/')) {
    path = '/' + path
  }
  return parentPath + path
}
export const pathResolve = (parentPath: string, path: string) => {
  if (isUrl(path)) return path
  if (!path) return parentPath // 修复 path 为空时返回 parentPath，避免拼接出错 https://t.zsxq.com/QVr6b
  const childPath = path.startsWith('/') ? path : `/${path}`
  return `${parentPath}${childPath}`.replace(/\/+/g, '/')
}

// 路由降级
export const flatMultiLevelRoutes = (routes: AppRouteRecordRaw[]) => {
  const modules: AppRouteRecordRaw[] = cloneDeep(routes)
  for (let index = 0; index < modules.length; index++) {
    const route = modules[index]
    if (!isMultipleRoute(route)) {
      continue
    }
    promoteRouteLevel(route)
  }
  return modules
}

// 层级是否大于2
const isMultipleRoute = (route: AppRouteRecordRaw) => {
  if (!route || !Reflect.has(route, 'children') || !route.children?.length) {
    return false
  }

  const children = route.children

  let flag = false
  for (let index = 0; index < children.length; index++) {
    const child = children[index]
    if (child.children?.length) {
      flag = true
      break
    }
  }
  return flag
}

// 生成二级路由
const promoteRouteLevel = (route: AppRouteRecordRaw) => {
  let router: Router | null = createRouter({
    routes: [route as RouteRecordRaw],
    history: createWebHashHistory()
  })

  const routes = router.getRoutes()
  addToChildren(routes, route.children || [], route)
  router = null

  route.children = route.children?.map((item) => omit(item, 'children'))
}

// 添加所有子菜单
const addToChildren = (
  routes: RouteRecordNormalized[],
  children: AppRouteRecordRaw[],
  routeModule: AppRouteRecordRaw
) => {
  for (let index = 0; index < children.length; index++) {
    const child = children[index]
    const route = routes.find((item) => item.name === child.name)
    if (!route) {
      continue
    }
    routeModule.children = routeModule.children || []
    if (!routeModule.children.find((item) => item.name === route.name)) {
      routeModule.children?.push(route as unknown as AppRouteRecordRaw)
    }
    if (child.children?.length) {
      addToChildren(routes, child.children, routeModule)
    }
  }
}
const toCamelCase = (str: string, upperCaseFirst: boolean) => {
  str = (str || '')
    .replace(/-(.)/g, function (group1: string) {
      return group1.toUpperCase()
    })
    .replaceAll('-', '')

  if (upperCaseFirst && str) {
    str = str.charAt(0).toUpperCase() + str.slice(1)
  }

  return str
}
