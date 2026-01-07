import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    console.log('Request URL:', config.url)
    console.log('Request Method:', config.method)
    console.log('Request Headers:', config.headers)
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    const res = response.data
    console.log('Response data:', res)
    // 检查响应是否是一个有效的对象
    if (typeof res === 'object' && res !== null) {
      // 如果响应有code字段，按业务逻辑处理
      if (res.code !== undefined) {
        if (res.code === 200) {
          // 将code转换为success字段，方便前端统一处理
          return { ...res, success: true }
        } else {
          return Promise.reject(new Error(res.msg || '请求失败'))
        }
      } else {
        // 如果没有code字段，可能是直接返回的数据数组或对象
        console.warn('Response has no code field, returning raw data')
        return { code: 200, success: true, data: res }
      }
    } else {
      return Promise.reject(new Error('无效的响应格式'))
    }
  },
  error => {
    console.error('Response error:', error)
    return Promise.reject(error)
  }
)

export default request
