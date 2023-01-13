import {createApp} from 'vue'
import App from './App.vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import {createRouter, createWebHistory} from "vue-router/dist/vue-router";
import ZqOverview from "@/components/zq-overview";
import ZqTaskView from "@/components/zq-task-view";
import ZqAbout from "@/components/zq-about";
import enLocale from '/src/i18n/en.ts';
import zhLocale from '/src/i18n/zh.ts';
import {createI18n} from "vue-i18n";

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

const messages = {
    zh: zhLocale,
    en: enLocale,
}

const i18n = createI18n({
    globalInjection: true,
    locale: navigator.language.startsWith('zh') ? 'zh' : 'en',
    messages,
    legacy: false,
})

const routes = [
    {path: '/status', component: ZqOverview},
    {path: '/tasks', component: ZqTaskView},
    {path: '/about', component: ZqAbout}
];

const router = createRouter({
    history: createWebHistory("/down"),
    routes
})

app.use(i18n)

app.use(router)

app.mount('#app')

