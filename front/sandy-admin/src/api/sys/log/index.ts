import request from '@/config/axios'

export interface SysLogVO {
  id?: number
  username: string
  operation: string
  method: string
  params?: string
  time: number
  ip: string
  createDate?: string
}

export interface SysLogPageParams {
  username?: string
  operation?: string
  current?: number
  size?: number
}

// 分页获取操作日志列表
export const getLogPage = (params: SysLogPageParams) => {
  return request.get({ url: '/log/page', params })
}
