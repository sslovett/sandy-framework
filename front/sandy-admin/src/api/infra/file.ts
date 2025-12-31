import request from '@/config/axios'
import { AxiosProgressEvent } from 'axios'

// 文件预签名 URL 响应
export interface FilePresignedUrlRespVO {
  configId: number
  url: string
  uploadUrl: string
}

// 文件信息
export interface FileVO {
  id: number
  configId: number
  path: string
  name: string
  url: string
  type: string
  size: number
  createTime: Date
}

/**
 * 获取文件预签名地址
 * @param fileName 文件名称
 * @param directory 目录
 */
export const getFilePresignedUrl = (fileName: string, directory?: string) => {
  return request.get<FilePresignedUrlRespVO>({
    url: '/infra/file/presigned-url',
    params: { fileName, directory }
  })
}

/**
 * 创建文件
 * @param data 文件信息
 */
export const createFile = (data: any) => {
  return request.post({ url: '/infra/file/create', data })
}

/**
 * 上传文件
 * @param data 文件数据
 * @param onUploadProgress 上传进度回调
 */
export const updateFile = (
  data: { file: File; directory?: string },
  onUploadProgress?: (progressEvent: AxiosProgressEvent) => void
) => {
  const formData = new FormData()
  formData.append('file', data.file)
  if (data.directory) {
    formData.append('directory', data.directory)
  }
  
  return request.upload({
    url: '/infra/file/upload',
    data: formData,
    onUploadProgress
  })
}

/**
 * 删除文件
 * @param id 文件ID
 */
export const deleteFile = (id: number) => {
  return request.delete({ url: '/infra/file/delete?id=' + id })
}

/**
 * 获取文件分页
 * @param params 查询参数
 */
export const getFilePage = (params: any) => {
  return request.get({ url: '/infra/file/page', params })
}
