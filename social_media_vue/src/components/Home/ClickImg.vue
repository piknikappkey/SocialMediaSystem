<template>
  <img :src="srcs[showImg]" alt="" @click="Down" />
</template>
<script setup lang="ts">
import { ref } from 'vue'
const showImg = ref(0)
const { srcs } = defineProps(['srcs'])
function Down() {
  showImg.value = (showImg.value + 1) % srcs.length
}
</script>
<style scoped lang="scss">
img {
  cursor: pointer;
}
</style>
