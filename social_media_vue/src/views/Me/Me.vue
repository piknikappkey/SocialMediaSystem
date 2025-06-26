<template>
  <UserBox class="user-box" />
  <!-- <button class="log-btn" @click="logBtnDown">
    {{ userData.logginStatus ? '切换账号' : '登录/注册' }}
  </button> -->
  <Button class="log-btn" :title="title" @click="logBtnDown" />
  <Log />
  <MeMain class="me-main" />
</template>

<script setup lang="ts">
import { useUserStore } from '@/store/userData'
import { useLogStore } from '@/store/log'
import { storeToRefs } from 'pinia'
import Button from '@/components/Button.vue'
import Log from '@/components/Me/Log.vue'
import UserBox from '@/components/Me/UserBox.vue'
import MeMain from './MeMain/MeMain.vue'
import { computed } from 'vue'
const { logData } = storeToRefs(useLogStore()) // 登录数据
const { userData } = storeToRefs(useUserStore()) // 用户数据
const title = computed(() => (userData.value.logginStatus ? '切换账号' : '登录/注册')) // 按钮标题

function logBtnDown() {
  logData.value.showLog = true
}
</script>

<style scoped>
.log-btn {
  position: fixed;
  top: 15px;
  right: 15px;
}
.user-box {
  margin: 50px 70px 0;
}
.me-main {
  margin-top: 20px;
  height: calc(100% - 170px);
}
</style>
