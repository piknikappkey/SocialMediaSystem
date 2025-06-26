<template>
  <div ref="page" class="page">
    <PageChat class="page-chat" />
    <PageAddress class="page-address" />
  </div>
</template>

<script setup lang="ts">
import PageChat from './PageChat.vue'
import PageAddress from './PageAddress.vue'
import { ref, watch } from 'vue'
const { pagenum } = defineProps(['pagenum'])
const emit = defineEmits(['update:pagenum'])
const page = ref()

const stopWatch = watch(page, () => {
  if (page.value.children) {
    for (let i = 0; i < page.value.children.length; i++) {
      page.value.children[i].style.left = `${i * 100}%`
    }
    stopWatch()
  }
})

watch(
  () => pagenum,
  () => {
    for (let i = 0; i < page.value.children.length; i++) {
      page.value.children[i].style.left = `${(i - pagenum) * 100}%`
    }
  },
)
</script>

<style scoped lang="scss">
.page {
  position: relative;
  overflow: hidden;
  &-chat {
    position: absolute;
    top: 0;
    width: 100%;
    height: 100%;
    transition: all 0.5s;
  }
  &-address {
    position: absolute;
    top: 0;
    width: 100%;
    height: 100%;
    transition: all 0.5s;
  }
}
</style>
