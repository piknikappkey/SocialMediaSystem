<template>
  <div class="avatar-box">
    <img v-if="avatarUrl?.search('none') != -1" :src="`/images/${avatarUrl}.png`" alt="头像" />
    <img v-if="avatarUrl?.search('none') == -1" :src="baseUrl + '/' + avatarUrl" alt="头像" />
  </div>
</template>
<script setup lang="ts">
import { baseUrl } from '@/utils/request'
const { avatarUrl } = defineProps(['avatarUrl'])
</script>
<style scoped lang="scss">
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
</style>
