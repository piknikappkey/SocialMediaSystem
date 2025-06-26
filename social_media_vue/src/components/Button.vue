<template>
  <button v-if="title" class="btn">
    {{ title }}
  </button>
</template>
<script setup lang="ts">
defineProps(['title'])
</script>

<style scoped lang="scss">
@use 'sass:color';
.btn {
  margin: 0;
  display: inline-block;
  padding: 6px 18px;
  background-color: $main-color-lllight;
  border: 1px solid $main-color-llight;
  border-radius: 5px;
  box-shadow: 2px 2px 5px 1px rgba(160, 160, 160, 0.5);
  transition: all 0.2s;
  &:hover {
    background-color: color.adjust($main-color-llight, $lightness: -0.15%);
    box-shadow: 2px 2px 8px 2px rgba(160, 160, 160, 0.5);
    border: 1px solid #bf9dff;
  }
  &:active {
    background-color: $main-color-lllight;
    box-shadow: 2px 2px 5px 1px rgba(160, 160, 160, 0.5);
    border: 1px solid $main-color-llight;
  }
}
</style>
