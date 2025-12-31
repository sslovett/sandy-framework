/**
 * 系统菜单
 * 对应后端的 SysMenu 实体
 */
export interface SysMenu {
  id: number // 菜单ID
  parentId: number // 父菜单ID
  name?: string // 菜单名称（前端格式）
  menuName?: string // 菜单名称（后端格式）
  path: string // 路由路径
  component: string | null // 组件路径
  type: number // 菜单类型：0-目录 1-菜单 2-按钮
  status?: string // 状态
  perms: string | null // 权限标识
  icon: string // 菜单图标
  orderNum: number // 排序
  children?: SysMenu[] // 子菜单（前端格式）
  list?: SysMenu[] | null // 子菜单（后端格式）
  createBy?: string | null
  createTime?: string
  updateBy?: string | null
  updateTime?: string | null
  remark?: string
}

/**
 * 菜单导航响应数据
 * 对应后端 /menu/nav 接口返回的数据结构
 */
export interface MenuNavResponseData {
  menuList: SysMenu[] // 菜单列表（树形结构，不包括按钮）
  authorities: string[] // 权限列表
}

/**
 * 菜单类型枚举
 */
export enum MenuType {
  CATALOG = 0, // 目录
  MENU = 1, // 菜单
  BUTTON = 2 // 按钮
}
