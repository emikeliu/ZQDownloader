<template>
  <div style="text-align: center; margin-top: 40px">
    <el-card shadow="never" style="margin: 40px 20px 10px; text-align: center">
      <span style="font-size: 40px; color: black">ZQDownloader</span>
      <br/>
      <br/>
      <br/>
      <br/>
      <span>---- {{ $t('desc') }}</span>
      <br/>
      <br/>
      <br/>
      <span>{{ $t('license') }}: <a href="https://en.wikipedia.org/wiki/MIT_License"
                                    style="color: #00b700">MIT</a></span>
      <br/>
    </el-card>
    <el-card shadow="never" style="margin: 40px 20px 10px; text-align: center">
        <span>
        Powered by:
          <el-space>
            <a href="https://vuejs.org/" style="color: #00b700">Vue.js</a>
            <a href="https://element-plus.org/" style="color: #00b700">ElementUI</a>
            <a href="https://spring.io/" style="color: #00b700">SpringBoot</a>
            <a href="https://mybatis.org/" style="color: #00b700">MyBatis</a>
          </el-space>

      </span>
    </el-card>
  </div>

</template>

<script>
export default {
  name: "zq-about"
}
</script>

<style scoped>

</style>