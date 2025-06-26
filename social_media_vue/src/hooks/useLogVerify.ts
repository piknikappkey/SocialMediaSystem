import { useLogStore } from '@/store/log'
import { useUserStore } from '@/store/userData'
import { storeToRefs } from 'pinia'

export default function () {
  let { logData } = storeToRefs(useLogStore())
  let { userData } = storeToRefs(useUserStore())

  // 检查email
  function verEmail(email: string) {
    const regex =
      /^((1\d{10}@qq\.com)|([1-9]\d{4,14}@(qq|vip\.qq|foxmail)\.com)|([a-zA-Z][\w\-.]{2,19}@(qq|vip\.qq|foxmail)\.com))$/i
    return regex.test(email)
  }

  // 检查密码
  function verPassword(pass: string) {
    return pass.length >= 6
  }

  //检查二次密码
  function verSamePass(pass1: string, pass2: string) {
    return pass1 === pass2
  }

  // 检查验证码
  function verCode(code: string) {
    return code.length == 6
  }

  // 登录、发送验证码检查
  function verSendLog() {
    // 检查email
    if (!verEmail(logData.value.email)) return { state: false, content: 'QQ邮箱号错误！' }
    // 检查email是否相同（禁止重复登录）
    if (userData.value.userEmail == logData.value.email)
      return { state: false, content: '您已登录！' }
    return { state: true, content: 'success!' }
  }

  // 发送验证码检查
  function verSendCode() {
    // 检查email
    if (!verEmail(logData.value.email)) return { state: false, content: 'QQ邮箱号错误！' }
    return { state: true, content: 'success!' }
  }

  // 注册、遗忘登录检查
  function verSendReg() {
    // 检查email
    if (!verEmail(logData.value.email)) return { state: false, content: 'QQ邮箱号错误！' }
    // 检查密码
    if (!verPassword(logData.value.password))
      return { state: false, content: '密码长度应大于6位！' }
    // 检查二次密码
    if (!verSamePass(logData.value.password, logData.value.password2))
      return { state: false, content: '两次输入的密码不同！' }
    // 检查验证码
    if (!verCode(logData.value.code)) return { state: false, content: '验证码长度应为6位！' }
    return { state: true, content: 'success!' }
  }
  return { verSendLog, verSendReg, verSendCode }
}
