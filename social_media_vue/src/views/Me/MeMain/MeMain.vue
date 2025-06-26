<template>
  <div class="me-box">
    <div class="me-box_link">
      <RouterLink v-for="r in routeArr" :key="r.id" class="routerLink" :to="{ path: r.routePath }">
        {{ r.title }}
      </RouterLink>
    </div>
    <div class="me-box_main">
      <RouterView></RouterView>
    </div>
  </div>
</template>

<script setup lang="ts">
// 路由数组
const routeArr = [
  { id: 1, title: '作品', routePath: '/me/production' },
  { id: 2, title: '喜欢', routePath: '/me/love' },
  { id: 3, title: '收藏', routePath: '/me/collect' },
  { id: 4, title: '设置', routePath: '/me/set' },
]
</script>

<style scoped lang="scss">
.me-box {
  background-color: $back-white-color;
}
.me-box_link {
  border-top: 1px solid rgba(0, 0, 0, 0.4);
  border-bottom: 1px solid rgba(0, 0, 0, 0.4);
  border-radius: 5px;
  text-align: center;
}

.routerLink {
  display: inline-block;
  text-decoration: none;
  padding: 1px 20px;
  margin: 10px 0px 10px 35px;
  border-radius: 5px;
  border-bottom: 2px solid $font-white-color;
  box-shadow: 2px 2px 5px $box-show-color;
  color: $font-black-color;
  background-color: $back-white-color;
  font-size: 25px;
  transition: all 0.2s;
}
.routerLink:hover {
  background-color: $back-black-color;
  color: $font-white-color;
}

.me-box_main {
  border-radius: 5px;
  height: calc(100% - 61px);
}
</style>
