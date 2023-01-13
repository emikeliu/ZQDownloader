<template>
  <el-container direction="vertical" style="text-align: left">
    {{ $t('link') }}:
    <el-input v-model="requestDownUrl" clearable></el-input>
    <el-button id="sub" v-loading="dialogLoading" :disabled="dialogLoading" round @click="requestDown">
      {{ $t('start') }}
    </el-button>
    <br/>
    {{ $t('direct_desc') }}
  </el-container>
</template>
<script>
import {ref} from "vue";

export default {
  name: "zqDirectDialog",
  data() {
    return {
      dialogLoading: false,
      requestDownUrl: ref('')
    }
  },
  mounted() {

  },
  inject: ['dismissDialog'],
  methods: {
    requestDown() {
      window.open("api/direct-connect?url=" + encodeURI(this.requestDownUrl.toString()))
      this.dismissDialog()
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