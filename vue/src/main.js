import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import zhCn from 'element-plus/es/locale/lang/zh-cn'

import '@/assets/css/global.css'

import print from 'vue3-print-nb'

const app = createApp(App)
app.use(print)
app.mount('#app')

createApp(App).use(store).use(router).use(ElementPlus, {locale: zhCn,}).mount('#app')
