<template>
  <h1>{{ title }}</h1>
</template>

<script setup lang="ts">
const { title } = defineProps(['title'])
</script>

<style scoped lang="scss">
h1 {
  font-size: 30px;
  margin: 0;
  margin-bottom: 15px;
}
</style>
