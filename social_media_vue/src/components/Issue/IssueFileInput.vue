<template>
  <section>
    <div class="file-box-cover">
      <IssueTitle title="编辑封面" />
      <FileInput
        fileType=".png,.jpg,.jpeg,.webp"
        :fileLen="1"
        title="封面"
        :fileMaxSize="fileSet.imgSize"
        v-model="modelCover"
      />
    </div>
    <div class="file-box-content">
      <IssueTitle :title="'选择要上传的' + (inputType === 'image' ? '图片' : '视频')" />
      <FileInput
        v-if="inputType === 'image'"
        class="file-box-content-input"
        fileType=".png,.jpg,.jpeg,.webp"
        :fileLen="fileSet.imgLen"
        title="图片"
        :fileMaxSize="fileSet.imgSize"
        v-model="modelImages"
      />
      <FileInput
        v-if="inputType === 'video'"
        class="file-box-content-input"
        fileType=".mp4,.mov,.m4v"
        :fileLen="fileSet.vidLen"
        title="视频"
        :fileMaxSize="fileSet.vidSize"
        v-model="modelVideos"
      />
    </div>
  </section>
</template>
<script setup lang="ts">
import { computed } from 'vue'
import FileInput from '../FileInput.vue'
import IssueTitle from '@/components/Issue/IssueTitle.vue'
const { inputType, fileSet, cover, images, videos } = defineProps([
  'inputType',
  'fileSet',
  'cover',
  'images',
  'videos',
])
const emit = defineEmits(['update:cover', 'update:images', 'update:videos'])
const modelCover = computed({
  get: () => cover,
  set: (val) => emit('update:cover', val),
})
const modelImages = computed({
  get: () => images,
  set: (val) => emit('update:images', val),
})
const modelVideos = computed({
  get: () => videos,
  set: (val) => emit('update:videos', val),
})
</script>
<style scoped>
.file-box-cover {
  float: left;
  /* display: inline-block; */
  margin-right: 100px;
}
.file-box-content {
  float: left;
  /* display: inline-block; */
}
h1 {
  margin: 25px 0;
}
</style>
