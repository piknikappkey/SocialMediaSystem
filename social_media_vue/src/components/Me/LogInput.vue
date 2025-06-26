<template>
  <div class="form__group">
    <input
      v-model="logData.email"
      type="email"
      class="form__input"
      placeholder="QQ邮箱"
      name="email"
      required
    />
    <label for="email" class="form__label">QQ邮箱</label>
  </div>
  <div class="form__group">
    <input
      v-model="logData.password"
      type="password"
      class="form__input"
      placeholder="密码"
      maxlength="16"
      required
    />
    <label class="form__label">密码</label>
  </div>
  <div class="form__group" v-if="logData.logMode == 1 || logData.logMode == 2">
    <input
      v-model="logData.password2"
      type="password"
      class="form__input"
      placeholder="再次输入密码"
      maxlength="16"
      required
    />
    <label class="form__label">再次输入密码</label>
  </div>
  <div class="form__group" v-if="logData.logMode == 1 || logData.logMode == 2">
    <input
      v-model="logData.code"
      type="text"
      class="form__input"
      placeholder="验证码"
      maxlength="6"
      onkeyup="value=value.replace(/[^\d\.]/g,'')"
      required
    />
    <label class="form__label">验证码</label>
  </div>
</template>

<script setup lang="ts">
import { useLogStore } from '@/store/log'
import { storeToRefs } from 'pinia'
let { logData } = storeToRefs(useLogStore())
</script>

<style scoped lang="scss">
.u-font-error {
  color: red !important;
}

.form__group:not(:last-child) {
  margin-bottom: 20px;
}

.form__input {
  font-size: 15px;
  color: inherit;
  font-family: inherit;
  padding: 15px 20px;
  border-radius: 3px;
  background-color: rgba(255, 255, 255, 0.6);
  border: none;
  width: 75%;
  display: inline-block;
  transition: outline-color 0.2s;
  user-select: none;
}

.form__input:focus {
  outline: solid 3px rgba(120, 255, 255, 0.6);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

/* #ff7835 */
.form__input:focus:invalid {
  outline: solid 3px $main-color;
}

.form__input::-webkit-input-placeholder {
  color: #999;
}

.form__label {
  font-size: 14px;
  font-weight: 400;
  margin-left: 20px;
  margin-top: 7px;
  display: inline-block;
  transition: all 0.3s;
  text-align: left;
  width: 75%;
  margin-left: 40px;
  user-select: none;
}

.form__input:placeholder-shown + .form__label {
  opacity: 0;
  visibility: hidden;
  transform: translateY(-40px);
}
</style>
