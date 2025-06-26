<template>
  <div class="chat-list" v-if="display">
    <div class="chat-list-label">
      <AvatarBox class="chat-list-label-avatar" :avatar-url="user.avatarUrl" />
      <!-- 用户名 -->
      <span class="chat-list-label-name">{{ user.username }}</span>
    </div>
  </div>
</template>
<script setup>
import AvatarBox from '../AvatarBox.vue'
import { ref } from 'vue'
const { user, friendData, myUserId } = defineProps(['user', 'friendData', 'myUserId'])
const display = ref(true)
function showComputed(user) {
  friendData.forEach((fri) => {
    if (user.userId == fri.addresseeId || user.userId == myUserId) {
      display.value = false
    }
  })
}
showComputed(user)
</script>
<style scoped lang="scss">
@use 'sass:color';
.chat-list {
  margin: 15px 0;
  &-input {
    display: none;
  }
  &-label {
    display: inline-block;
    position: relative;
    width: 80%;
    text-align: left;
    padding: 6px 18px;
    background-color: $main-color-lllight;
    border: 1px solid $main-color-llight;
    color: $main-color;
    border-radius: 5px;
    box-shadow: 2px 2px 5px 1px rgba(160, 160, 160, 0.4);
    outline-offset: 0px;
    outline-color: rgba(255, 255, 255, 0);
    transition: all 0.1s;
    &:hover {
      background-color: color.adjust($main-color-lllight, $lightness: -3%);
      border: 1px solid color.adjust($main-color-llight, $lightness: -5%);
    }
    &-avatar {
      height: 30px;
      width: 30px;
    }
    &-name {
      position: absolute;
      top: 50%;
      transform: translate(0, -50%);
      margin-left: 8px;
    }
  }
}
</style>
