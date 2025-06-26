<template>
  <div class="form__group">
    <a
      v-if="logData.logMode == 1 || logData.logMode == 2"
      class="btn btn-main-color u-margin-rignt"
      @click="getCode"
      >{{ logData.codeTime || '发送验证码' }}</a
    >
    <a
      v-if="logData.logMode == 1 || logData.logMode == 0"
      class="btn btn-main-color u-margin-rignt"
      @click="getRegister"
      >注册</a
    >
    <a v-if="logData.logMode == 0" class="btn btn-main-color" @click="getLog">登录</a>
    <a v-if="logData.logMode == 2" class="btn btn-main-color" @click="getForgetLog">遗忘登录</a>
    <a
      v-if="logData.logMode == 1 || logData.logMode == 2"
      class="section-log_return"
      @click="logReturn"
      >&larr;</a
    >
    <a v-if="!userData.logginStatus && logData.logMode == 0" class="forget" @click="getForget"
      >忘记密码</a
    >
    <a v-if="userData.logginStatus && logData.logMode == 0" class="forget" @click="logOut"
      >退出账号</a
    >
  </div>
</template>
<script setup lang="ts">
import { useLogStore } from '@/store/log' // 登录界面信息
import { storeToRefs } from 'pinia'
import { service, WebSocketInit, WebSocketOnClose } from '@/utils/request' // 向服务端发送请求
import useLogVerify from '@/hooks/useLogVerify' // 检查登录输入用
import { useUserStore } from '@/store/userData'
import { ElMessage } from 'element-plus'
import useGetUserData from '@/hooks/useGetUserData'
const { logData } = storeToRefs(useLogStore()) // 登录界面pinia数据
const { userData } = storeToRefs(useUserStore()) // 用户数据
const { saveUserData, delUserData } = useGetUserData()
const { clearInput } = useLogStore() // 登录界面pinia初始化
const { verSendLog, verSendReg, verSendCode } = useLogVerify() // 检查登录输入用

// 向服务器发送信息
function sendLogData() {
  const postData = {
    logMode: logData.value.logMode,
    email: logData.value.email,
    password: logData.value.password,
    password2: logData.value.password2,
    code: logData.value.code,
  }
  service
    .post('/send_log', postData)
    .then((res) => {
      if (res.data.state === 0) {
        ElMessage.error(res.data.content)
        return
      }
      // 获取用户数据
      userData.value.logginStatus = res.data.state
      userData.value.userName = res.data.data.username
      userData.value.userId = res.data.data.userId
      userData.value.userEmail = res.data.data.email
      userData.value.avatarUrl = res.data.data.avatarUrl
      // 关闭登录界面函数
      logData.value.showLog = false
      // 清空输入数据
      clearInput()
      // 初始化logMode
      logData.value.logMode = 0
      // 提示登录成功
      ElMessage.success('登录成功！')
      // 保存数据
      saveUserData()
      // 创建WebSocket链接
      WebSocketInit(userData.value.userId)
    })
    .catch((err) => console.log(err))
}

// 登录
function getLog() {
  let verData = verSendLog()
  // 检查登录输入是否正确
  if (!verData.state) {
    // 不正确
    ElMessage.error(verData.content)
    return
  }
  sendLogData()
}

// 注册
function getRegister() {
  if (logData.value.logMode == 0) {
    clearInput() // 清空input
    logData.value.logMode = 1 // 修改模式
    return
  }
  // 检查注册输入是否正确
  const verData = verSendReg()
  if (!verData.state) {
    // 不正确
    ElMessage.error(verData.content)
    return
  }
  sendLogData()
}

// 忘记密码登录
function getForgetLog() {
  const verData = verSendReg() // 遗忘登录
  if (!verData.state) {
    // 不正确
    ElMessage.error(verData.content)
    return
  }
  sendLogData()
}

// 获取验证码
function getCode() {
  const verData = verSendCode()
  // 检查登录输入是否正确
  if (!verData.state) {
    // 不正确
    ElMessage.error(verData.content)
    return
  }
  // 检查验证码时间
  if (logData.value.codeTime !== 0) return
  logData.value.codeTime = 300
  // 设置定时器
  const codeTime = setInterval(() => {
    logData.value.codeTime -= 1
    if (logData.value.codeTime === 0) clearInterval(codeTime)
  }, 1000)
  // 发送验证码
  service
    .get(`/send_code?email=${logData.value.email}`)
    .then((res) => {
      if (res.data.state === 0) {
        ElMessage.error(res.data.content)
        return
      }
      // 提示验证码发送成功成功
      ElMessage.success('验证码发送成功成功！')
    })
    .catch((err) => console.log(err))
}

// 忘记密码
function getForget() {
  clearInput() // 清空input
  logData.value.logMode = 2
}

// 点击返回
function logReturn() {
  clearInput() // 清空input
  logData.value.logMode = 0
}

// 退出账号
function logOut() {
  delUserData()
  ElMessage.success('已退出登录')
  logData.value.showLog = false
  WebSocketOnClose()
}
</script>

<style scoped lang="scss">
.u-margin-rignt {
  margin-right: 40px !important;
}

@media (max-width: 37.5em) {
  .u-margin-rignt {
    margin-right: 15px !important;
  }
}

.form__group:not(:last-child) {
  margin-bottom: 20px;
}

.btn {
  user-select: none;
}

.btn,
.btn:link,
.btn:visited {
  /* 去除下划线（字体样式） */
  text-decoration: none;
  padding: 12px 40px;
  /* 块元素 */
  display: inline-block;
  /* 添加圆角 */
  border-radius: 100px;
  /* 过度 */
  transition: all 0.2s;
  position: relative;
  border: none;
  cursor: pointer;
  font-size: 16px;
}

.btn:hover {
  transform: translateY(-3px);
  /* 盒子阴影 */
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
  /* 悬停后放大与消失 */
}

.btn:hover::after {
  /* 放大 */
  transform: scaleX(1.4) scaleY(1.6);
  /* 不透明度 */
  opacity: 0;
}

.btn:active,
.btn:focus {
  outline: none;
  transform: translateY(-1px);
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.3);
}

.btn:active::after,
.btn:focus::after {
  transform: scale(1);
  opacity: 1;
}

.btn::after {
  /* ::after内容属性 */
  content: '';
  /* 内联块 */
  display: inline-block;
  height: 100%;
  width: 100%;
  border-radius: 100px;
  position: absolute;
  top: 0%;
  left: 0%;
  /* 索引（将元素放在其他元素后方） */
  z-index: -1;
  transition: all 0.2s;
}

.btn-main-color {
  background-color: $main-color-light;
  color: #fff;
}

.btn-main-color::after {
  background-color: $main-color-light;
}

.section-log_return {
  user-select: none;
  position: absolute;
  top: 0px;
  left: 10px;
  transition: all 0.2s;
  font-size: 25px;
}

.section-log_return:link,
.section-log_return:visited {
  color: #777;
  text-decoration: none;
  font-size: 25px;
}

.section-log_return:hover {
  color: $secend-color;
}

.forget {
  position: absolute;
  top: 5px;
  left: 10px;
  font-size: 12px;
  border-bottom: 1px solid $font-black-color-light;
  cursor: pointer;
  color: $font-black-color-light;
}

.forget:hover {
  border-bottom: 1px solid $secend-color;
  color: $secend-color;
}
</style>
