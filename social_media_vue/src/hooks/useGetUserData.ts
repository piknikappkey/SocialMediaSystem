import { service, WebSocketInit } from '@/utils/request'
import { useUserStore } from '@/store/userData'
import { storeToRefs } from 'pinia'
import { ElMessage } from 'element-plus'

export default function () {
  const { userData } = storeToRefs(useUserStore())
  const { userDataInit } = useUserStore()

  // 获取用户数据
  function getUserData() {
    service.get(`/users?userId=${userData.value.userId || 0}`).then((res) => {
      if (res.data.state === 0) {
        ElMessage.error(res.data.content)
        return
      }
      userData.value.logginStatus = res.data.state
      userData.value.userName = res.data.username
      userData.value.userId = res.data.userId
      userData.value.userEmail = res.data.email
      userData.value.avatarUrl = res.data.avatarUrl
      userData.value.logTime = Date()
    })
  }

  // 初始化用户数据
  function initUserData() {
    // 获取用户数据（如果浏览器有缓存）
    const rawData = localStorage.getItem('userToken')
    const data = rawData ? JSON.parse(rawData) : null
    // console.log(data)
    if (!data?.userId) {
      userDataInit()
      return
    }
    const now = new Date()
    const logTime = new Date(data.logTime)
    // 登出时间超过三天
    if ((now - logTime) / (1000 * 60 * 60 * 24) > 3) {
      userDataInit()
      return
    }
    userData.value.logginStatus = data.logginStatus
    userData.value.userName = data.userName
    userData.value.userId = data.userId
    userData.value.userEmail = data.userEmail
    userData.value.avatarUrl = data.avatarUrl
    userData.value.logTime = Date()
    // console.log(userData.value)

    // 当成功获取用户数据时，新建WebSocket连接
    WebSocketInit(userData.value.userId)
  }
  // 保存用户数据
  function saveUserData() {
    if (!userData.value.userId) return
    localStorage.setItem('userToken', JSON.stringify(userData.value))
  }
  // 删除数据
  function delUserData() {
    localStorage.removeItem('userToken')
    userDataInit()
  }

  // 兼容性处理：获取标准化的事件名和 visibilityState 属性
  const getVisibilityInfo = () => {
    if (typeof document.hidden !== 'undefined') {
      return { hidden: 'hidden', visibilityChange: 'visibilitychange', state: 'visibilityState' }
    } else if (typeof document.mozHidden !== 'undefined') {
      return {
        hidden: 'mozHidden',
        visibilityChange: 'mozvisibilitychange',
        state: 'mozVisibilityState',
      }
    } else if (typeof document.msHidden !== 'undefined') {
      return {
        hidden: 'msHidden',
        visibilityChange: 'msvisibilitychange',
        state: 'msVisibilityState',
      }
    } else if (typeof document.webkitHidden !== 'undefined') {
      return {
        hidden: 'webkitHidden',
        visibilityChange: 'webkitvisibilitychange',
        state: 'webkitVisibilityState',
      }
    }
    return null
  }

  // 添加事件（当用户进入页面时执行保存用户数据函数）
  const setupVisibilityListener = () => {
    const visibilityInfo = getVisibilityInfo()
    if (!visibilityInfo) {
      console.warn('当前浏览器不支持 Page Visibility API')
      return
    }

    const handler = () => {
      if (document[visibilityInfo.state] === 'visible') {
        saveUserData() // 页面可见时执行函数
      }
    }

    document.addEventListener(visibilityInfo.visibilityChange, handler)
    return () => document.removeEventListener(visibilityInfo.visibilityChange, handler) // 返回移除函数
  }

  return { getUserData, initUserData, saveUserData, delUserData, setupVisibilityListener }
}
