<template>
  <div class="type">
    <input
      type="radio"
      id="image"
      name="type"
      value="image"
      @input="emit('update:modelValue', (<HTMLInputElement>$event.target).value)"
      checked
    />
    <label for="image" class="label">图片</label>
    <input
      type="radio"
      id="video"
      name="type"
      value="video"
      @input="emit('update:modelValue', (<HTMLInputElement>$event.target).value)"
    />
    <label for="video" class="label">视频</label>
  </div>
</template>
<script setup lang="ts">
const emit = defineEmits(['update:modelValue'])
</script>
<style scoped lang="scss">
input {
  display: none;
}
.label {
  display: inline-block;
  padding: 5px 12px;
  margin: 15px;
  margin-right: 30px;
  background-color: #f1e9ff;
  border: 1px solid #dbc7fd;
  color: #955dfd;
  border-radius: 5px;
  // border: 1px solid rgba(200, 200, 200, 0.5);
  box-shadow: 2px 2px 5px 1px rgba(160, 160, 160, 0.5);
  outline-offset: 3px;
  outline-color: rgba(255, 255, 255, 0.8);
  transition: all 0.1s;
  &:hover {
    background-color: #ddcbff;
    border: 1px solid #bf9dff;
  }
}
input[type='radio']:checked + .label {
  outline: 2px solid #bf9dff;
  border: 1px solid #bf9dff;
  background-color: #ddcbff;
}
</style>
