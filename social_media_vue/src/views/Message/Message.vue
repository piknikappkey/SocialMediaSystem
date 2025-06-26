<template>
  <section class="section" v-if="userData.logginStatus">
    <NavBtn :btns="btns" v-model:navnum="navnum" />
    <PageBox class="page" v-model:pagenum="navnum" />
  </section>
  <Button v-if="!userData.logginStatus" class="log-tips" title="请先登录" @click="logTipsDown" />
</template>

<script setup lang="ts">
import { useUserStore } from '@/store/userData'
import { storeToRefs } from 'pinia'
import Button from '@/components/Button.vue'
import NavBtn from '@/components/NavBtn.vue'
import PageBox from '@/components/Message/PageBox.vue'
import { useLogStore } from '@/store/log'
import { ref, watch } from 'vue'
const { userData } = storeToRefs(useUserStore()) // 用户数据
const { logData } = storeToRefs(useLogStore()) // 登录数据
// 点击“请先登录”按钮
const emit = defineEmits(['update:navpath'])
function logTipsDown() {
  // 跳转到“我的”
  emit('update:navpath', '/me')
  logData.value.showLog = true // 打开登录界面
}

// 导航
const btns = [
  { name: '聊天', id: 0 },
  { name: '好友动态', id: 1 },
]
const navnum = ref(0)
// watch(navnum, () => {
//   console.log(navnum.value)
// })
</script>

<style scoped lang="scss">
.log-tips {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 30px;
  font-weight: 400;
  color: $main-color;
}
.section {
  padding: 25px 50px;
  position: relative;
}
.page {
  margin-top: 25px;
  height: calc(100vh - 40px - 50px - 25px);
  width: 100%;
  max-width: 1000px;
  position: absolute;
  left: 50%;
  transform: translate(-50%, 0);
}
</style>
