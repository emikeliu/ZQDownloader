<template>
  <el-container direction="vertical" style="text-align: left">
    {{ $t('old_passwd') }}:
    <el-input v-model="old" clearable show-password style="margin-bottom: 15px"></el-input>
    {{ $t('new_passwd') }}:
    <el-input v-model="newPassword" clearable show-password></el-input>

    <el-button id="sub" round @click="submit">{{ $t('change_passwd') }}</el-button>
    <br/>
  </el-container>
</template>
<script>
import axios from "axios";

export default {
  name: "zqChangePasswordDialog",
  data() {
    return {
      old: '',
      newPassword: ''
    }
  },
  mounted() {

  },
  methods: {
    submit() {
      this.doCallbackAlert('abc')
      axios.post("api/user/change-password", {old_password: this.old, new_password: this.newPassword}).then(r => {
            if (r.data.success === '0') alert("OK")
            else alert("failed");
          }
      )
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