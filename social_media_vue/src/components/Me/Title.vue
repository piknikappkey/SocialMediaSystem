<template>
  <div class="u-margin-bottom-medium">
    <h2 class="heading-secondary">
      <span>{{ title }}</span>
    </h2>
  </div>
</template>

<script setup lang="ts">
const { title } = defineProps(['title'])
</script>

<style scoped lang="scss">
.u-margin-bottom-medium {
  margin-bottom: 40px !important;
}
.heading-secondary {
  font-size: 35px;
  font-weight: 700;
  display: inline-block;
  background-image: linear-gradient(to right, $main-color-light, $secend-color-light);
  -webkit-background-clip: text;
  color: transparent;
  letter-spacing: 5px;
  transition: all 0.2s;
}

@media only screen and (max-width: 37.5em) {
  .heading-secondary {
    font-size: 25px;
  }
}

.heading-secondary span {
  display: block;
}
</style>
