import { defineStore } from 'pinia'
import { store } from '@/store'
import { cloneDeep } from 'lodash-es'
import remainingRouter from '@/router/modules/remaining'
import {
  flatMultiLevelRoutes,
  generateRoute,
  transformMenuToRoute,
  filterButtonMenus
} from '@/utils/routerHelper'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import type { SysMenu } from '@/api/menu/types'

const { wsCache } = useCache()

export interface PermissionState {
  routers: AppRouteRecordRaw[]
  addRouters: AppRouteRecordRaw[]
  menuTabRouters: AppRouteRecordRaw[]
}

export const usePermissionStore = defineStore('permission', {
  state: (): PermissionState => ({
    routers: [],
    addRouters: [],
    menuTabRouters: []
  }),
  getters: {
    getRouters(): AppRouteRecordRaw[] {
      return this.routers
    },
    getAddRouters(): AppRouteRecordRaw[] {
      // 注意：不能使用 cloneDeep，因为它会破坏组件函数的引用
      // 直接返回 addRouters，因为 flatMultiLevelRoutes 只处理多级路由（层级>2）
      // 我们的路由只有2级，所以不需要扁平化处理
      return this.addRouters
    },
    getMenuTabRouters(): AppRouteRecordRaw[] {
      return this.menuTabRouters
    }
  },
  actions: {
    async generateRoutes(): Promise<unknown> {
      return new Promise<void>(async (resolve) => {
        // 获得菜单列表，它在登录的时候，setUserInfoAction 方法中已经进行获取
        // 后端返回的是 SysMenu[] 格式
        let res: AppCustomRouteRecordRaw[] = []
        const roleRouters = wsCache.get(CACHE_KEY.ROLE_ROUTERS)
        if (roleRouters) {
          // 如果是后端的 SysMenu 格式，需要转换
          if (Array.isArray(roleRouters) && roleRouters.length > 0) {
            // 检查是否是 SysMenu 格式（有 type 字段）
            if ('type' in roleRouters[0]) {
              // 过滤掉按钮类型的菜单
              const filteredMenus = filterButtonMenus(roleRouters as SysMenu[])
              // 转换为前端路由格式
              res = filteredMenus.map(transformMenuToRoute)
            } else {
              // 已经是 AppCustomRouteRecordRaw 格式
              res = roleRouters as AppCustomRouteRecordRaw[]
            }
          }
        }
        const routerMap: AppRouteRecordRaw[] = generateRoute(res)
        // 动态路由，404一定要放到最后面
        // preschooler：vue-router@4以后已支持静态404路由，此处可不再追加
        this.addRouters = routerMap.concat([
          {
            path: '/:path(.*)*',
            // redirect: '/404',
            component: () => import('@/views/Error/404.vue'),
            name: '404Page',
            meta: {
              hidden: true,
              breadcrumb: false
            }
          }
        ])
        // 渲染菜单的所有路由
        this.routers = cloneDeep(remainingRouter).concat(routerMap)
        resolve()
      })
    },
    setMenuTabRouters(routers: AppRouteRecordRaw[]): void {
      this.menuTabRouters = routers
    }
  },
  persist: false
})

export const usePermissionStoreWithOut = () => {
  return usePermissionStore(store)
}
