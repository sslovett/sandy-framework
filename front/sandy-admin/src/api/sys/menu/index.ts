import request from '@/config/axios'

export interface SysMenuVO {
  id?: number
  parentId: number
  menuName: string
  path?: string
  component?: string
  type: number // 0:目录 1:菜单 2:按钮
  status: string // 0:正常 1:停用
  perms?: string
  icon?: string
  orderNum: number
  remark?: string
  createTime?: string
  list?: SysMenuVO[] // 子菜单
}

// 菜单类型
export const MenuType = {
  CATALOG: 0, // 目录
  MENU: 1, // 菜单
  BUTTON: 2 // 按钮
}

// 获取菜单列表（树形，包括按钮）
export const getMenuTable = () => {
  return request.get({ url: '/menu/table' })
}

// 获取菜单列表（不包括按钮，用于选择上级菜单）
export const getMenuList = () => {
  return request.get({ url: '/menu/list' })
}

// 获取菜单详情
export const getMenuInfo = (menuId: number) => {
  return request.get({ url: `/menu/info/${menuId}` })
}

// 新增菜单
export const createMenu = (data: SysMenuVO) => {
  return request.post({ url: '/menu/save', data })
}

// 修改菜单
export const updateMenu = (data: SysMenuVO) => {
  return request.post({ url: '/menu/update', data })
}

// 删除菜单
export const deleteMenu = (menuId: number) => {
  return request.post({ url: `/menu/delete/${menuId}` })
}
