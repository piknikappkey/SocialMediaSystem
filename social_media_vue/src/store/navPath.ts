import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useNavStory = defineStore('navPathStory', () => {
  const navPath = ref('')
  return { navPath }
})
