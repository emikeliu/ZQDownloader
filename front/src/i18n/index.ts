import App from '@/App.vue'
import {createApp} from 'vue'
import {createI18n} from 'vue-i18n'
import enLocale from 'src/i18n/en.js';
import zhLocale from 'src/i18n/zh.js';

const messages = {
    zh: zhLocale,
    cn: zhLocale,
    en: enLocale,
    us: enLocale,

}

const localLang = navigator.language.split('-')[0];
const storageLang = window.localStorage.getItem('locale')?.split('"')[1].split('"')[0].toLocaleLowerCase() || 'en';
const c = (storageLang.toLocaleLowerCase() !== 'zh' && storageLang.toLocaleLowerCase() !== 'en') ? 'en' : storageLang;

const i18n = createI18n({
    globalInjection: true,
    locale: c || localLang || 'en',
    messages,
    legacy: false,
})

const app = createApp(App)
app.use(i18n)