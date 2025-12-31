import request from '@/config/axios'

export interface SysRoleVO {
  id?: number
  name: string
  status: string // 1:正常 0:停用
  remark?: string
  createTime?: string
  menuIdList?: number[] // 菜单权限ID列表
}

export interface SysRolePageParams {
  roleName?: string
  current?: number
  size?: number
}

// 获取角色列表（全部）
export const getRoleList = () => {
  return request.get({ url: '/role/list' })
}

// 分页获取角色列表
export const getRolePage = (params: SysRolePageParams) => {
  return request.get({ url: '/role/page', params })
}

// 获取角色详情
export const getRoleInfo = (roleId: number) => {
  return request.get({ url: `/role/info/${roleId}` })
}

// 新增角色
export const createRole = (data: SysRoleVO) => {
  return request.post({ url: '/role/save', data })
}

// 修改角色
export const updateRole = (data: SysRoleVO) => {
  return request.post({ url: '/role/update', data })
}

// 删除角色
export const deleteRole = (roleIds: number[]) => {
  return request.post({ url: '/role/delete', data: roleIds })
}
