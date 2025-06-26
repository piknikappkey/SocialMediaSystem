<template>
  <div class="issue-tips-box">
    <div class="issue-tips-col">
      <p class="issue-tips-title">图片</p>
      <p class="issue-tips">可上传图片数量：{{ fileSet.imgLen }}</p>
      <p class="issue-tips">可上传图片大小：{{ fileSet.imgSize }}MB</p>
      <p class="issue-tips">支持的图片类型：.png,.jpg,.jpeg,.webp</p>
    </div>
    <div class="issue-tips-col">
      <p class="issue-tips-title">视频</p>
      <p class="issue-tips">可上传视频数量：{{ fileSet.vidLen }}</p>
      <p class="issue-tips">可上传视频大小：{{ fileSet.vidSize }}MB</p>
      <p class="issue-tips">支持的视频类型：.mp4,.mov,.m4v</p>
    </div>
    <div class="issue-tips-col">
      <p class="issue-tips-title">标题</p>
      <p class="issue-tips">限制字数：40字</p>
    </div>
    <div class="issue-tips-col">
      <p class="issue-tips-title">内容</p>
      <p class="issue-tips">限制字数：1000字</p>
      <p class="issue-tips">可添加话题如：#游戏#编程#二次猿</p>
      <p class="issue-tips">话题可增加相同爱好者读到的概率</p>
    </div>
  </div>
</template>
<script setup lang="ts">
const { fileSet } = defineProps(['fileSet'])
</script>
<style scoped>
.issue-tips-box {
  position: fixed;
  bottom: 0px;
  overflow: hidden;
  width: 60%;
  margin: 0px 20%;
}
.issue-tips-col {
  float: left;
  width: calc((100% - 25px * 3) / 4);
  height: 90px;
}
.issue-tips-col:not(:last-child) {
  border-right: 2px dashed rgba(0, 0, 0, 0.2);
  margin-right: 25px;
}
.issue-tips-title {
  font-size: 20px;
  color: #1e1e1ed7;
}
.issue-tips {
  font-size: 12px;
  font-weight: 400;
  color: rgb(85, 85, 85);
}
</style>
