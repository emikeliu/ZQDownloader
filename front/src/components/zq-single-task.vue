<template>
  <el-dialog v-model="shareShow" @close="shareShow = false">
    {{ $t('link_work') }}<br/>
    <el-input-number v-model="minutes" max="999999" min="1" style="margin: 10px"/>
    <br/>
    {{$t('minutes')}}
    <br/>
    <el-button style="margin-top: 40px" @click="makeShare">{{ $t('share') }}</el-button>
    <p v-show="shared">{{$t('shared')}}<a :href="'share?uuid=' + uuid">Use This!</a></p>
  </el-dialog>
  <el-dialog v-model="detailedInfo" :title="taskName" style="text-align: left; min-width: 400px">
    Task Name: {{ taskName }}<br/>
    Percent: {{ per }}<br/>
    Status: {{ downStatus }}<br/>
    File Length: {{ size }}<br/>
    UUID: {{ uuid }}<br/>
    URL: {{ url }}<br/>
    SHA256: {{ sha256 }}<br/>
    Shared: {{ shared }}<br/>
  </el-dialog>
  <el-card v-show="!del" id="card" shadow="never">
    <p id="task-name">{{ taskName }}</p>
    <el-progress :percentage="Math.round(per * 100)"
                 :status="downStatus === 'ERROR' ? 'error' : (downStatus === 'FINISHED' ? 'success' : '')"/>
    <el-button-group style="margin-top: 20px">
      <el-button icon="Delete" round size="large" style="cursor: default" @click="deleteTask">{{$t('delete')}}</el-button>
      <el-button :disabled="downStatus !== 'FINISHED'" icon="Download" round size="large" style="cursor: default"
                 @click="downTask">
        {{$t('fetch')}}
      </el-button>
      <el-button icon="Document" round size="large" style="cursor: default" @click="detailedInfo = !detailedInfo">
        {{$t('info')}}
      </el-button>
      <el-button :disabled="downStatus !== 'FINISHED'" icon="Share" round size="large" style="cursor: default"
                 @click="shareTask">
        {{$t('share')}}
      </el-button>

    </el-button-group>
  </el-card>
</template>

<style lang="css" scoped>
#progress {
  align-self: center;
}

#btn-group {
  align-items: center;
  align-content: center;
  text-align: center;
  margin-top: 19px;
  margin-bottom: 20px;
}


#card {
  display: inline-block;
  min-width: 450px;
  height: 160px;
  padding: 0;
  border-radius: 3px;
}

#task-name {
  max-width: content-box;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}


.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 4px;
}

.default-cursor {
  cursor: default;
}
</style>
<script>
import axios from "axios";

export default {
  name: "zq-single-task",
  data() {
    return {
      del: false,
      detailedInfo: false,
      shareShow: false,
      minutes: 60,
      origin: undefined
    }
  },
  props: {
    size: Number,
    per: Number,
    taskName: String,
    downStatus: String,
    uuid: String,
    url: String,
    sha256: String,
    shared: Boolean
  },
  methods: {
    deleteTask() {
      axios.get("api/request-cancel?uuid=" + this.uuid)
      this.del = true;
    },
    downTask() {
      window.open("api/fetch?uuid=" + this.uuid)
    },
    shareTask() {
      this.shareShow = true
    },
    makeShare() {
      axios.get("api/share?uuid=" + this.uuid + '&minutes=' + this.minutes)
    },
  },
  mounted() {
    this.origin = window.location.origin
  }
}
</script>
