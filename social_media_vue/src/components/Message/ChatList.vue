<template>
  <div class="chat-list">
    <input
      class="chat-list-input"
      type="radio"
      :id="`${friendData.addresseeId}${radioId}`"
      name="chat-list"
      :value="`${friendData.addresseeId}${radioId}`"
      @input="inputData(friendData)"
    />
    <label :for="`${friendData.addresseeId}${radioId}`" class="chat-list-label">
      <AvatarBox class="chat-list-label-avatar" :avatar-url="friendData.addresseeUser.avatarUrl" />
      <!-- 用户名 -->
      <span class="chat-list-label-name">{{ friendData.addresseeUser.username }}</span>
      <!-- 最近活跃时间 -->
      <div v-if="!friendData?.isOnline && friendData?.updatedTime" class="chat-list-label-time">
        {{ getTime(friendData.updatedTime) }}
      </div>
      <!-- 在线情况 -->
      <div v-if="friendData?.isOnline" class="chat-list-label-online">在线</div>
      <!-- 未读消息数 -->
      <div v-if="friendData?.notReadMessages" class="chat-list-label-notRead">
        <div class="chat-list-label-notRead-num">{{ friendData.notReadMessages }}</div>
      </div>
    </label>
  </div>
</template>

<script setup>
import useGetTime from '@/hooks/useGetTime'
import AvatarBox from '../AvatarBox.vue'
const { getTime } = useGetTime()
const { friendData, chatFriend, radioId } = defineProps([
  'friendData',
  'chatFriend',
  'radioId',
  'chatMessages',
  'loadContent',
])
const emit = defineEmits(['update:chatFriend', 'update:chatMessages', 'update:loadContent'])
function inputData(friendData) {
  friendData.notReadMessages = 0
  if (chatFriend == friendData) return
  emit('update:chatFriend', friendData)
  emit('update:chatMessages', [])
  emit('update:loadContent', '正在加载中')
}
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
    &-time {
      position: absolute;
      right: 5px;
      top: 0px;
      font-size: 12px;
      color: rgba($color: $main-color, $alpha: 0.7);
    }
    &-online {
      position: absolute;
      right: 5px;
      top: 0px;
      font-size: 12px;
      color: rgba($color: $main-color, $alpha: 0.7);
      color: rgb(0, 160, 0);
    }
    &-notRead {
      position: absolute;
      right: 5px;
      top: 55%;
      transform: translate(0, -50%);
      background-color: rgb(205, 38, 38);
      height: 12px;
      width: 12px;
      border-radius: 50%;
      &-num {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        font-size: 10px;
        color: rgb(255, 255, 255);
      }
    }
  }
  &-input:checked + &-label {
    outline-offset: 3px;
    outline: 2px solid color.adjust($main-color-llight, $lightness: -3%);
    border: 1px solid color.adjust($main-color-llight, $lightness: -5%);
    background-color: color.adjust($main-color-lllight, $lightness: -3%);
  }
}
</style>
