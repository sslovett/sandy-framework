import request from '@/config/axios'
import type { MenuNavResponseData, SysMenu } from './types'

/**
 * 获取用户所拥有的菜单和权限（不包括按钮）
 * 对应后端 /menu/nav 接口
 */
export const getMenuNav = () => {
  return request.get<MenuNavResponseData>({ url: '/menu/nav' })
}

/**
 * 获取用户所拥有的菜单（不包括按钮）
 * 对应后端 /menu/list 接口
 */
export const getMenuList = () => {
  return request.get<SysMenu[]>({ url: '/menu/list' })
}

/**
 * 获取菜单列表（包括按钮）
 * 对应后端 /menu/table 接口
 */
export const getMenuTable = () => {
  return request.get<SysMenu[]>({ url: '/menu/table' })
}
