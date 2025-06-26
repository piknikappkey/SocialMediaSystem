import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useChatStore = defineStore('chatDataStore', () => {
  const onlineFriends = ref([])
  const friendMessages = ref([])
  return { onlineFriends, friendMessages }
})
