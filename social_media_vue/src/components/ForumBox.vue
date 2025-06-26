<template>
  <div class="box">
    <div class="img-box">
      <img :src="imgUrl" :alt="<string>forum" @error="handleImageError" />
    </div>
    <p class="title">{{ forum.forumTitle }}</p>
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref } from 'vue'
import { baseUrl } from '@/utils/request'
const { forum } = defineProps(['forum'])
// 多类型图片显示
const baseimgUrl = `${baseUrl}/${forum.forumContentUrl}/cover`
const imgUrl = ref(`${baseUrl}/${forum.forumContentUrl}/cover.webp`)
const supportType = ['.jepg', '.jpg', '.png']
const handleImageError = () => {
  if (supportType.length === 0) {
    imgUrl.value = '/images/forumError.png'
    return
  }
  imgUrl.value = baseimgUrl + supportType.pop()
}
</script>

<style scoped lang="scss">
.img-box {
  width: 100%;
  height: 85%;
  border-radius: 11px;
  background-color: black;
}

img {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  object-fit: cover;
  user-select: none;
  &:hover {
    opacity: 0.8; /* 设置图片透明度 */
    filter: alpha(opacity=80); /* 兼容旧版IE */
  }
}

.title {
  color: #393939;
}
</style>
