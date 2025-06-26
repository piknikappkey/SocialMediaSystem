import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('userDataStore', () => {
  const userData = ref({
    logginStatus: false,
    userId: 0,
    userName: '未登录',
    userEmail: 'xxx@xx.xxx',
    avatarUrl: 'none',
    logTime: Date(),
  })
  function userDataInit() {
    userData.value.logginStatus = false
    userData.value.userName = '未登录'
    userData.value.userId = 0
    userData.value.userEmail = 'xxx@xx.xxx'
    userData.value.avatarUrl = 'none'
    userData.value.logTime = Date()
  }
  return { userData, userDataInit }
})
