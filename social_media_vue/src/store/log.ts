import { defineStore } from 'pinia'
import { ref } from 'vue'

// logMode: 0:登录 1:注册 2:验证码 3:遗忘登录

export const useLogStore = defineStore('logStore', () => {
  const logData = ref({
    logMode: 0,
    showLog: false,
    email: '',
    password: '',
    password2: '',
    code: '',
    codeTime: 0,
  })
  function clearInput() {
    logData.value.email = logData.value.password = logData.value.password2 = logData.value.code = ''
  }
  return {
    logData,
    clearInput,
  }
})
