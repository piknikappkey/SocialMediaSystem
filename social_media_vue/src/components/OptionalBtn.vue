<template>
  <div class="btn">
    <input
      class="btn-input"
      type="radio"
      :id="returnValue"
      :name="name"
      :value="returnValue"
      :checked="modelValue === returnValue"
      @input="emit('update:modelValue', (<HTMLInputElement>$event.target).value)"
    />
    <label :for="returnValue" class="btn-label">{{ title }}</label>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(['update:modelValue'])
const { returnValue, title, name, modelValue } = defineProps([
  'returnValue',
  'title',
  'name',
  'modelValue',
])
</script>

<style scoped lang="scss">
@use 'sass:color';
.btn {
  margin: 0;
  &-input {
    display: none;
  }
  &-label {
    display: inline-block;
    padding: 6px 18px;
    background-color: $main-color-lllight;
    border: 1px solid $main-color-llight;
    color: $main-color;
    border-radius: 5px;
    box-shadow: 2px 2px 5px 1px rgba(160, 160, 160, 0.5);
    outline-offset: 0px;
    outline-color: rgba(255, 255, 255, 0);
    transition: all 0.1s;
    &:hover {
      background-color: color.adjust($main-color-llight, $lightness: -0.25%);
      border: 1px solid color.adjust($main-color-llight, $lightness: -10%);
    }
  }
  &-input:checked + &-label {
    outline-offset: 3px;
    outline: 2px solid color.adjust($main-color-llight, $lightness: -10%);
    border: 1px solid color.adjust($main-color-llight, $lightness: -10%);
    background-color: color.adjust($main-color-llight, $lightness: -0.25%);
  }
}
</style>
