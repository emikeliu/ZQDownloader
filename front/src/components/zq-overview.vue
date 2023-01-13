<template>
  <div id="overview">
    <el-card :header="$t('storage_title')" shadow="never" style="width: 350px;height: 250px">
      <el-progress v-loading="percentage == null" :color="percentage > 90 ? 'red':''" :percentage="percentage"
                   type="circle">
        <p style="font-family: 'Microsoft YaHei',serif">{{ percentage ? percentage + "%" : "" }}</p>
        <p style="font-family:serif;font-size: small">{{
            used ? Math.round(used) + "G /" + Math.round(total) + "G" : ""
          }}</p>
      </el-progress>
    </el-card>
    <el-card :header="$t('download_title')" shadow="never" style="width: 350px;height: 250px">
      {{ $t('this_time_run') }}<br/>
      <span style="font-size: 60px; font-weight: bolder; color: #00dc7f"><nobr>{{
          totalDownload ? totalDownload : ''
        }}</nobr></span><br/>
      {{ $t('this_time_run2') }}
    </el-card>
  </div>


</template>

<script>


import axios from "axios";

export default {
  data() {
    return {
      used: null,
      total: null,
      percentage: null,
      timer: undefined,
      totalDownload: null
    }
  },
  name: 'zq-overview',
  props: {},
  mounted() {
    const partitionHandler = () => {
      axios.get("api/partition").then(r => {
        this.used = r.data.split(" ")[0]
        this.total = r.data.split(" ")[1]
        this.percentage = Math.round(parseFloat(this.used) / parseFloat(this.total) * 100)
      })
      return partitionHandler;
    }
    const staticsHandler = () => {
      axios.get("api/statistic").then(r => {
        let t;
        let s = r.data
        if (s > 1024) {
          t = 'GB'
          s = (s * 1.0 / 1024).toFixed(1)
        } else {
          s = (s * 1.0).toFixed(1)
          t = 'MB'
        }
        this.totalDownload = s + ' ' + t;
      })
      return staticsHandler;
    }
    this.timer = setInterval(partitionHandler(), 10000)
    this.timer2 = setInterval(staticsHandler(), 10000)
  },
  unmounted() {
    clearInterval(this.timer)
    clearInterval(this.timer2)
  },
  methods: {},
}
</script>

<style scoped>
#overview {
  margin-left: 20px;
  margin-right: 20px;
  display: grid;
  margin-top: 40px;
  grid-template-columns: repeat(auto-fit, 350px);
  grid-row-gap: 20px;
  grid-column-gap: 20px;
  justify-content: space-evenly;
  align-content: center;
}

h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 0;
}

a {
  color: #42b983;
}

</style>
