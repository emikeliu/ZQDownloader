<template>
  <el-dialog v-model="newTaskVisible" :close-on-click-modal="false" :title="$t('new_simple')"
             onclose="newTaskVisible = false" style="min-width: 400px">
    <zq-request-down-dialog/>
  </el-dialog>
  <el-dialog v-model="newDirectVisible" :close-on-click-modal="false" :title="$t('new_direct')"
             onclose="newDirectVisible = false" style="min-width: 400px">
    <zq-request-p2p-dialog/>
  </el-dialog>
  <el-dialog v-model="createCallback">
    {{ createStatus }}
  </el-dialog>
  <el-button-group style="display: block; margin: 10px">
    <el-button round @click="newTaskVisible = !newTaskVisible">{{ $t('new_simple') }}</el-button>
    <el-button round @click="newDirectVisible = !newDirectVisible">{{ $t('new_direct') }}</el-button>
  </el-button-group>
  <div id="ct-box" v-loading="false">
    <SingleTask v-for="task in taskList" :key="task"
                :down-status="task['status']"
                :per="task['percent']"
                :sha256="task['sha256']"
                :shared="task['shared']"
                :size="task['size']"
                :task-name="task['task_name']"
                :url="task['url']"
                :uuid="task['uuid']"/>
  </div>

</template>
<script>
import SingleTask from "@/components/zq-single-task";
import ZqRequestDownDialog from "@/components/zq-request-down-dialog";
import ZqRequestP2pDialog from "@/components/zq-request-direct-dialog.vue";

export default {
  name: "zq-task-view",
  components: {ZqRequestP2pDialog, ZqRequestDownDialog, SingleTask},
  data() {
    return {
      ws: WebSocket,
      loading: true,
      createCallback: false,
      createStatus: undefined,
      timer: undefined,
      newTaskVisible: false,
      newDirectVisible: false,
      taskList: [
        //{file_name:1, status: 'FINISHED', shared: true}
      ]
    }
  },
  props: {
    mode: String,
  },
  provide() {
    return {
      doCallbackAlert: this.doCallbackAlert,
      dismissDialog: this.dismissDialog
    }
  },
  methods: {
    createWs() {
      return new WebSocket((window.location.protocol === 'https:' ? "wss://" : "ws://") + window.location.host + this.publicPath + "/ws/info");
    },
    dismissDialog() {
      this.newDirectVisible = false;
      this.newTaskVisible = false;
    },
    doCallbackAlert(info) {
      this.createStatus = info;
      this.createCallback = true;
    }
  },
  inject: ['publicPath'],
  mounted() {
    let that = this;
    const initWs = (ws) => {
      ws.onopen = () => {
        ws.onmessage = (message) => {
          if (message.data === 'OK') return;
          if (that.loading === that) that.loading = false;
          message = eval(message.data)
          message.forEach(t => {
            let m = that.taskList.find(m => m.uuid === t.uuid)
            if (m === undefined) {
              that.taskList.push(t)
            } else {
              m.status = t.status;
              m.percent = t.percent;
              m.sha256 = t.sha256;
              m.shared = t.shared;
            }
          })
        }
      }
      ws.onclose = () => {
        reconnect();
      }
    }
    let ws = that.createWs();
    initWs(ws);

    function reconnect() {
      ws = that.createWs()
      initWs(ws)
    }

    this.ws = ws;
  },
  unmounted() {
    let that = this;
    that.ws.close();
    clearInterval(that.timer)
  }
}
</script>

<style scoped>
#ct-box {
  margin-left: 20px;
  margin-right: 20px;
  margin-top: 40px;
  display: grid;
  grid-template-columns: repeat(auto-fit, 450px);
  grid-row-gap: 40px;
  grid-column-gap: 40px;
  justify-content: space-evenly;
  align-content: center;

}
</style>