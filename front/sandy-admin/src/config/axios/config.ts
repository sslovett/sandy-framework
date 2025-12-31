const config: {
  base_url: string
  result_code: string // 改为字符串类型以匹配后端
  default_headers: AxiosHeaders
  request_timeout: number
} = {
  /**
   * api请求基础路径
   */
  base_url: import.meta.env.VITE_BASE_URL + import.meta.env.VITE_API_URL,
  /**
   * 接口成功返回状态码（后端使用字符串 "200"）
   */
  result_code: '200',

  /**
   * 接口请求超时时间
   */
  request_timeout: 30000,

  /**
   * 默认接口请求类型
   * 可选值：application/x-www-form-urlencoded multipart/form-data
   */
  default_headers: 'application/json'
}

export { config }
