import request from '@/config/axios'

export interface SysUserVO {
  id?: number
  userName: string
  nickName: string
  password?: string
  status: number
  email?: string
  phone?: string
  sex?: string
  avatar?: string
  userType?: string
  createBy?: number
  createTime?: string
  updateBy?: number
  updateTime?: string
  roleIdList?: number[]
}

export interface SysUserPageParams {
  userName?: string
  current?: number
  size?: number
}

// 分页查询用户列表
export const getUserPage = (params: SysUserPageParams) => {
  return request.get({ url: '/user/page', params })
}

// 获取用户详情
export const getUserInfo = (userId: number) => {
  return request.get({ url: `/user/info/${userId}` })
}

// 新增用户
export const createUser = (data: SysUserVO) => {
  return request.post({ url: '/user/save', data })
}

// 修改用户
export const updateUser = (data: SysUserVO) => {
  return request.post({ url: '/user/update', data })
}

// 删除用户
export const deleteUser = (userIds: number[]) => {
  return request.post({ url: '/user/delete', data: userIds })
}
