<template>
  <section>
    <IssueTitle title="正文内容" />
    <el-input
      class="input-title"
      type="text"
      :clearable="true"
      :maxlength="40"
      :show-word-limit="true"
      placeholder="请输入标题"
      v-model="modelTitle"
    />
    <el-input
      class="input-content"
      type="textarea"
      :maxlength="2048"
      :show-word-limit="true"
      size="large"
      :rows="10"
      resize="none"
      placeholder="请输入正文内容"
      v-model="modelContent"
    />
  </section>
</template>
<script setup lang="ts">
import { computed } from 'vue'
import IssueTitle from '@/components/Issue/IssueTitle.vue'
const { title, content } = defineProps(['title', 'content'])
const emit = defineEmits(['update:title', 'update:content'])
const modelTitle = computed({
  get: () => title,
  set: (val) => emit('update:title', val),
})
const modelContent = computed({
  get: () => content,
  set: (val) => emit('update:content', val),
})
</script>
<style scoped lang="scss">
/* 同时控制普通输入框和文本域输入框 */
:deep(.el-input),
:deep(.el-textarea) {
  --el-input-focus-border-color: #9a60ff;
  --el-textarea-focus-border-color: #9a60ff;
  --el-input-hover-border-color: #c4a2ff;
  --el-textarea-hover-border-color: #c4a2ff;
}

.input {
  &-title {
    width: 400px;
    margin-bottom: 15px;
  }
  &-content {
    width: 810px;
  }
}
</style>
