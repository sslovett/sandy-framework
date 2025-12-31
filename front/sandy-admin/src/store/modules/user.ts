import { store } from '@/store'
import { defineStore } from 'pinia'
import { getAccessToken, removeToken } from '@/utils/auth'
import { CACHE_KEY, useCache, deleteUserCache } from '@/hooks/web/useCache'
import { getInfo, loginOut } from '@/api/login'

const { wsCache } = useCache()

interface UserVO {
  id: number
  avatar: string
  nickname: string
  deptId: number
}

interface UserInfoVO {
  // USER 缓存
  permissions: Set<string>
  roles: string[]
  isSetUser: boolean
  user: UserVO
}

export const useUserStore = defineStore('admin-user', {
  state: (): UserInfoVO => ({
    permissions: new Set<string>(),
    roles: [],
    isSetUser: false,
    user: {
      id: 0,
      avatar: '',
      nickname: '',
      deptId: 0
    }
  }),
  getters: {
    getPermissions(): Set<string> {
      return this.permissions
    },
    getRoles(): string[] {
      return this.roles
    },
    getIsSetUser(): boolean {
      return this.isSetUser
    },
    getUser(): UserVO {
      return this.user
    }
  },
  actions: {
    async setUserInfoAction() {
      if (!getAccessToken()) {
        this.resetState()
        return null
      }
      let userInfo = wsCache.get(CACHE_KEY.USER)
      if (!userInfo) {
        userInfo = await getInfo()
      } else {
        // 特殊：在有缓存的情况下，进行加载。但是即使加载失败，也不影响后续的操作，保证可以进入系统
        try {
          userInfo = await getInfo()
        } catch (error) {}
      }

      // 后端返回的数据结构：{ code: "00000", data: { menuList, authorities } }
      // 响应拦截器返回的是整个响应对象，所以需要从 data 中提取
      const responseData = userInfo.data || userInfo
      // authorities 是权限字符串数组，存储为 permissions
      this.permissions = new Set(responseData.authorities || [])
      // 后端可能不提供 roles，使用空数组兜底
      this.roles = responseData.roles || []
      // 用户信息可能在 data 中或直接在 userInfo 中
      this.user = responseData.user || {
        id: 0,
        avatar: '',
        nickname: '',
        deptId: 0
      }
      this.isSetUser = true
      wsCache.set(CACHE_KEY.USER, userInfo)

      // 递归过滤停用的菜单（树形结构）
      const filterActiveMenus = (menus: any[]): any[] => {
        return menus
          .filter((menu: any) => menu.status === '1')
          .map((menu: any) => ({
            ...menu,
            list: menu.list ? filterActiveMenus(menu.list) : null
          }))
      }

      // 存储菜单列表用于路由生成（过滤掉停用的菜单）
      const menuList = responseData.menuList || []
      const activeMenuList = filterActiveMenus(menuList)
      wsCache.set(CACHE_KEY.ROLE_ROUTERS, activeMenuList)
    },
    async setUserAvatarAction(avatar: string) {
      const userInfo = wsCache.get(CACHE_KEY.USER)
      // NOTE: 是否需要像`setUserInfoAction`一样判断`userInfo != null`
      this.user.avatar = avatar
      userInfo.user.avatar = avatar
      wsCache.set(CACHE_KEY.USER, userInfo)
    },
    async setUserNicknameAction(nickname: string) {
      const userInfo = wsCache.get(CACHE_KEY.USER)
      // NOTE: 是否需要像`setUserInfoAction`一样判断`userInfo != null`
      this.user.nickname = nickname
      userInfo.user.nickname = nickname
      wsCache.set(CACHE_KEY.USER, userInfo)
    },
    async loginOut() {
      await loginOut()
      removeToken()
      deleteUserCache() // 删除用户缓存
      this.resetState()
    },
    resetState() {
      this.permissions = new Set<string>()
      this.roles = []
      this.isSetUser = false
      this.user = {
        id: 0,
        avatar: '',
        nickname: '',
        deptId: 0
      }
    }
  }
})

export const useUserStoreWithOut = () => {
  return useUserStore(store)
}
