<template>
  <el-dialog v-model="showRegister" class="zq-dialog">
    <zq-add-user-dialog></zq-add-user-dialog>
  </el-dialog>
  <el-dialog v-model="showChangePassword" class="zq-dialog">
    <zq-change-password-dialog></zq-change-password-dialog>
  </el-dialog>
  <el-menu :default-active="activeMenu" :ellipsis="true" mode="horizontal" router
           style="position: static; right: 0; left: 0">
      <span class="hidden-sm-and-down"
            style="font-size: 20px; text-align: center; display: block; line-height: 55px; margin-left: 20px; margin-right: 20px">
        ZQDownloader
      </span>
    <el-menu-item :route="{path: '/status'}" index="status">{{ $t('status') }}</el-menu-item>
    <el-menu-item :route="{path: '/tasks'}" index="tasks">{{ $t('tasks') }}</el-menu-item>
    <el-menu-item :route="{path: '/about'}" index="about">{{ $t('about') }}</el-menu-item>
    <el-dropdown style="margin-left: 20px; cursor: default">
    <span style="height: 55px; align-items: center; display: flex">
      {{ $t('account') }}
      <el-icon class="el-icon--right">
        <arrow-down/>
      </el-icon>
    </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item>{{ $t('account') }}: {{ username }}</el-dropdown-item>
          <el-dropdown-item @click="showRegister = true">{{ $t('register') }}</el-dropdown-item>
          <el-dropdown-item @click="showChangePassword = true">{{ $t('change_passwd') }}</el-dropdown-item>
          <el-dropdown-item @click="clickHandler">{{ $t('logout') }}</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </el-menu>
  <el-scrollbar style="height: calc(100vh - 65px)">
    <router-view/>
  </el-scrollbar>
</template>
<script>
import 'element-plus/theme-chalk/display.css';
import axios from "axios";
import {ref} from "vue";
import {ArrowDown} from '@element-plus/icons-vue'
import ZqAddUserDialog from "@/components/zq-add-user-dialog.vue";
import ZqChangePasswordDialog from "@/components/zq-change-password-dialog.vue";

export default {
  data() {
    return {
      activeMenu: null,
      newTaskVisible: false,
      publicPath: '/down'
    }
  },
  provide() {
    return {
      publicPath: this.publicPath
    }
  },
  name: 'App',
  components: {
    ZqChangePasswordDialog,
    ZqAddUserDialog,
    ArrowDown
  },
  methods: {},
  mounted() {
    let that = this;
    let splits = window.location.pathname.split('/')
    if (splits[2] === '') that.$router.push('/status')
    this.activeMenu = window.location.pathname.split('/')[2]
  },
  setup() {
    let account = ref('');
    axios.get("api/user/username").then(r => {
      account.value = r.data
    })
    let clickHandler = () => {
      window.open('logout', '_self')
    }
    return {
      username: account,
      clickHandler: clickHandler,
      showRegister: ref(false),
      showChangePassword: ref(false)
    }
  }
}
</script>

<style>
.zq-dialog {
  max-width: 400px;
  min-width: 300px;
}

.blanks {
  width: max-content;
  margin-left: 20px;
}

#task-btn {
  width: max-content;
  margin-left: 20px;
}

#main {
  margin-top: 10px;
}

#z-title {
  color: #000000;
  margin-top: 60px;
}

#username {
  float: right;
  color: #3f3f3f;
}

#header {
  background: var(--el-color-primary-light-8);
  height: 150px;
}

#container {
  padding-bottom: 20px;
  display: flex;
  min-height: 100vh;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #626262;
}

.scrollbar-demo-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  margin: 10px;
  text-align: center;
  border-radius: 4px;
  background: #8dffcf;
  color: #8dffcf;
}

body {
  margin: 0;
}
</style>