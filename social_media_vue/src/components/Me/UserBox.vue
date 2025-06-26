<template>
  <div class="user-box">
    <AvatarBox :avatarUrl="userData.avatarUrl" />
    <div class="user-box-right">
      <div class="user-box-data">
        <p class="user-box_name">{{ userData.userName }}</p>
        <p class="user-box_email">邮箱：{{ userData.userEmail }}</p>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { baseUrl } from '@/utils/request'
import { useUserStore } from '@/store/userData'
import { storeToRefs } from 'pinia'
import AvatarBox from '../AvatarBox.vue'
let { userData } = storeToRefs(useUserStore())
</script>
<style scoped>
.user-box {
  overflow: hidden;
}
.avatar-box {
  width: 100px;
  height: 100px;
  overflow: hidden;
  border-radius: 50%;
  float: left;
  user-select: none;
}
img {
  width: 100%;
  object-fit: cover;
}
.user-box-right {
  height: 100px;
  float: left;
  position: relative;
}
.user-box-data {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  margin-left: 25px;
}
.user-box_name {
  margin-bottom: 15px;
}
.user-box_email {
  /* 取消自动换行 */
  white-space: nowrap;
}
</style>
