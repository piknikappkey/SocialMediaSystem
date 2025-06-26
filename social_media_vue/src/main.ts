import './assets/main.css'
import 'element-plus/dist/index.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import ElementPlus from 'element-plus'
axios.defaults.withCredentials = true

const app = createApp(App)

// 将axios挂载到全局
app.config.globalProperties.$axios = axios

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app')
