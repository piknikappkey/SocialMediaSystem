<template>
  <div class="input-box">
    <textarea
      ref="textareaRef"
      v-model="content"
      class="input-box-input"
      rows="1"
      @input="adjustHeight"
      maxlength="65535"
      placeholder="发送消息"
    ></textarea>
    <Button class="input-box-btn" title="发送" @click="sendInput" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import Button from '../Button.vue'
import { sendMessages } from '@/utils/request'
const { chatFriend, chatMessages } = defineProps(['chatFriend', 'chatMessages'])
const emit = defineEmits(['update:chatMessages'])
const content = ref('')
const textareaRef = ref(null) // 自动调整输入框高度
// 动态调整高度函数
const adjustHeight = () => {
  const textarea = textareaRef.value
  textarea.style.height = 'auto' // 重置高度以获取正确scrollHeight
  textarea.style.height = `${textarea.scrollHeight + 2}px`
}
// 初始设置
onMounted(() => {
  textareaRef.value.style.minHeight = '40px' // 设置最小高度
})
// 发送消息
function sendInput() {
  if (!content.value) return
  const message = {
    senderId: chatFriend.requesterId,
    receiverId: chatFriend.addresseeId,
    content: content.value,
    messageType: 'text',
    read: 0,
  }
  sendMessages(message)
  content.value = ''
  message.createdTime = Date()
  emit('update:chatMessages', [...chatMessages, message])
}
</script>

<style scoped lang="scss">
.input-box {
  background-color: rgba($color: $main-color-lllight, $alpha: 1);
  border-radius: 5px;
  &-input {
    vertical-align: middle;
    width: calc(100% - 70px);
    padding: 8px;
    resize: none; /* 禁用手动调整 */
    // white-space: nowrap; /* 默认不换行 */
    overflow-y: hidden; /* 隐藏垂直滚动条 */
    outline: none;
    max-height: 100px; /* 限制最大高度 */
    border: 1px solid $main-color-llight;
    border-radius: 5px;
    color: $main-color;
    line-height: 18px; /* 需与字体大小匹配 */
    font-size: 18px;
    &:focus,
    &:not(:placeholder-shown) {
      white-space: pre-wrap;
      overflow-y: auto;
      border: 1px solid #bf9dff;
    }
    &::-webkit-scrollbar {
      width: 6px;
    }
    &::-webkit-scrollbar-track {
      border-radius: 10px;
    }
    &::-webkit-scrollbar-thumb {
      border-radius: 10px;
      border: 2px solid $main-color-llight;
    }
  }
  &-btn {
    vertical-align: middle;
    margin: 0 10px;
    color: $main-color;
    padding: 5px 10px;
  }
}
</style>
