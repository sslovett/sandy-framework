/**
 * 后端统一响应包装器
 * 对应后端的 ServerResponseEntity<T> 类
 */
export interface ServerResponseEntity<T = any> {
  code: string // 响应码，"200" 表示成功
  msg: string // 响应消息
  data: T // 响应数据
}

/**
 * 类型守卫：检查响应是否成功
 */
export function isSuccessResponse<T>(
  response: ServerResponseEntity<T>
): response is ServerResponseEntity<T> & { code: '200' } {
  return response.code === '200'
}

/**
 * 类型守卫：检查响应是否为错误
 */
export function isErrorResponse<T>(
  response: ServerResponseEntity<T>
): response is ServerResponseEntity<T> & { code: string } {
  return response.code !== '200'
}

/**
 * 从响应中提取数据
 */
export function extractData<T>(response: ServerResponseEntity<T>): T {
  if (isSuccessResponse(response)) {
    return response.data
  }
  throw new Error(response.msg || '请求失败')
}
