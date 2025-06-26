<template>
  <div class="nav">
    <button
      ref="navBtns"
      class="nav-btn"
      v-for="btn in btns"
      :key="btn.id"
      @click="btnDown($event, btn.id)"
    >
      {{ btn.name }}
    </button>
    <div ref="navShadow" class="nav-shadow"></div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, ref, watch } from 'vue'
const { btns, navnum } = defineProps(['btns', 'navnum'])
const emit = defineEmits(['update:navnum'])
const navBtns = ref()
const navShadow = ref()
let mutationObserver
// 观察页面尺寸变化
const initMutationObserver = () => {
  mutationObserver = new MutationObserver(() => {
    navShadow.value.style.width = `${navBtns.value[navnum].offsetWidth}px`
    navShadow.value.style.left = `${navBtns.value[navnum].offsetLeft}px`
  })
  mutationObserver.observe(document.body, {
    attributes: true,
    childList: true,
    subtree: true,
  })
}

function btnDown(e, id: Number) {
  emit('update:navnum', id)
  navBtns.value.forEach((element) => {
    element.style.color = `#9a60ff`
    if (element == e.target) {
      console.log(element)
      navShadow.value.style.width = `${element.offsetWidth}px`
      navShadow.value.style.left = `${element.offsetLeft}px`
      element.style.color = `#7525ff`
    }
  })
}
const stopWatch = watch(navBtns, () => {
  if (navBtns.value[navnum].offsetWidth && navBtns.value[navnum].offsetLeft) {
    initMutationObserver()
    navShadow.value.style.width = `${navBtns.value[navnum].offsetWidth}px`
    navShadow.value.style.left = `${navBtns.value[navnum].offsetLeft}px`
    stopWatch()
  }
})
onBeforeUnmount(() => {
  mutationObserver?.disconnect()
})
</script>

<style scoped lang="scss">
.nav {
  position: relative;
  height: 40px;
  line-height: 40px;
  text-align: center;
  &-btn {
    background-color: transparent;
    border: none;
    padding: 6px 15px;
    font-size: 16px;
    color: $main-color-light;
    transition: all 0s;
  }
  &-shadow {
    position: absolute;
    top: 0;
    background-color: $main-color-lllight;
    border-radius: 20px;
    height: 100%;
    z-index: -1;
    transition: all 0.3s;
  }
}
</style>
