/**
 * 用户登录请求 VO
 * 对应后端的 CaptchaAuthenticationDTO
 */
export type UserLoginVO = {
  userName: string // 用户名（后端使用 userName）
  password: string // 密码
  captchaVerification: string // 验证码验证token
  socialType?: string
  socialCode?: string
  socialState?: string
}

/**
 * 验证码认证 DTO
 * 对应后端的 CaptchaAuthenticationDTO
 */
export type CaptchaAuthenticationDTO = {
  userName: string // 用户名
  password: string // 密码
  captchaVerification: string // 验证码验证token
}

/**
 * 登录响应数据
 * 后端返回的 accessToken
 */
export type LoginResponseData = {
  accessToken: string // 访问令牌
}

/**
 * Token 类型（保留用于兼容性）
 * @deprecated 后端只返回 accessToken，不再使用此类型
 */
export type TokenType = {
  id: number // 编号
  accessToken: string // 访问令牌
  refreshToken: string // 刷新令牌
  userId: number // 用户编号
  userType: number //用户类型
  clientId: string //客户端编号
  expiresTime: number //过期时间
}

/**
 * 用户信息 VO
 */
export type UserVO = {
  id: number
  userName?: string // 添加 userName 字段以匹配后端
  username: string // 保留用于兼容性
  nickname: string
  deptId: number
  email: string
  mobile: string
  sex: number
  avatar: string
  loginIp: string
  loginDate: string
}

/**
 * 注册请求 VO
 */
export type RegisterVO = {
  tenantName: string
  userName: string // 改为 userName 以匹配后端
  password: string
  captchaVerification: string
}

/**
 * 验证码 VO
 * 对应后端的 CaptchaVO
 */
export type CaptchaVO = {
  captchaType?: string
  clientUid?: string
  ts?: number
  captchaVerification?: string
}

/**
 * 验证码响应模型
 * 对应后端的 ResponseModel
 */
export type CaptchaResponseModel = {
  repCode: string
  repData: {
    originalImageBase64: string
    jigsawImageBase64: string
    token: string
    secretKey: string
  }
  repMsg: string
  success: boolean
}
