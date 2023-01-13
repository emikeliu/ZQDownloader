<template>
  <el-container direction="vertical" style="text-align: left">
    {{ $t('link') }}:
    <el-input v-model="requestDownUrl" clearable @input="check"></el-input>
    <span v-show="notSupportedScheme"
          style="margin-top: 10px; margin-left: 20px; color: red">* {{ $t('unknown_scheme') }}</span>
    <span v-show="notSecure"
          style="margin-top: 10px; margin-left: 20px; color: red">* {{ $t('not_secure') }}</span><br/>
    <el-button id="sub" v-loading="dialogLoading" round @click="requestDown">{{ $t('start') }}</el-button>
  </el-container>
</template>
<script>
import axios from "axios";
import {ref} from "vue";

export default {
  name: "zqRequestDownDialog",
  data() {
    return {
      notSupportedScheme: false,
      notSecure: false,
      dialogLoading: false,
      requestDownUrl: ref('')
    }
  },
  inject: ['doCallbackAlert', 'dismissDialog'],
  methods: {
    check() {
      this.notSupportedScheme = !this.requestDownUrl.toString().startsWith("http://")
          && !this.requestDownUrl.toString().startsWith("https://")
          && this.requestDownUrl.toString().trim() !== '';
      this.notSecure = this.requestDownUrl.toString().startsWith("http://");
    },
    requestDown() {
      this.dialogLoading = true
      axios.post("api/request-down", {url: ref(this.requestDownUrl)['value']}).then(r => {
        if (r.data['success'] === '0') {
          this.doCallbackAlert('Task has been created successfully!')
          this.requestDownUrl = ""
          this.dialogLoading = false
        } else {
          this.doCallbackAlert('Failed! Maybe there exists file the same as it?')
          this.dialogLoading = false
        }
      }).catch(reason => {
        this.dialogLoading = false;
        this.doCallbackAlert('Error occurred: ' + reason)
      }).finally(() => {
        this.dismissDialog();
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