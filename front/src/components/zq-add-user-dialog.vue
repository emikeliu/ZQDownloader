<template>
  <el-container direction="vertical" style="text-align: left">
    {{ $t('account') }}:
    <el-input v-model="account" clearable style="margin-bottom: 15px"></el-input>
    {{ $t('passwd') }}:
    <el-input v-model="password" clearable show-password></el-input>
    <el-button id="sub" round @click="register">{{ $t('register') }}</el-button>
    <br/>
  </el-container>
</template>
<script>
import axios from "axios";

export default {
  name: "zqAddUserDialog",
  data() {
    return {
      password: '',
      account: ''
    }
  },
  mounted() {

  },
  methods: {
    register() {
      axios.post("api/user/register", {username: this.account.trim(), password: this.password}).then(r => {
        if (r.data.success === '0') alert('OK')
        else alert("Failed")
      })
    }
  }
}
</script>

<style scoped>
#sub {
  margin: 20px 20px 0;
  height: 40px;
}
</style>