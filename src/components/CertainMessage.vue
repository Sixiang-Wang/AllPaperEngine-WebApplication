<script setup>
import { defineEmits } from "vue";
import {ArrowLeft} from "@element-plus/icons-vue";
import {ref} from "vue";
import defaultAvatar from "@/assets/image/user.gif";
import * as httpUtil from "@/api/http.js";
import * as cookieUtil from "@/utils/cookie.js";
import {ElMessage} from "element-plus";
const props = defineProps({
  id: String,
  userName: String,
  date: String,
  index: String
})
const avatar = ref({
  defaultAvatar: defaultAvatar,
  url : defaultAvatar
})
const emit = defineEmits();
const goBack = () => {
  emit("go-back");
}
const deleteVisible = ref(false);
const deleteMessage = async()=>{
  try{
    const res = await httpUtil.get('/message/delete',{
      messageId: props.id
    },{Authorization: cookieUtil.getCookie("token")});
    if(res.data.msg === 'success'){
      ElMessage.success("删除成功！");
      setTimeout(()=>{
        location.reload();
      },500);
    }else{
      ElMessage.error("删除失败；请稍后再试");
    }
  }catch (e){
    ElMessage.error(e);
    console.error(e);
  }
}
</script>

<template>
  <div style="display:flex; flex-direction: column;">
  <div style="display: flex; flex-direction: row; align-items: center;" @click="goBack">
    <el-icon><arrow-left/></el-icon>
    <span>返回</span>
  </div>
  <div style="display: flex; flex-direction: column;">
    <div style="display:flex; flex-direction: row; align-items: center; margin-top: 3%">
      <el-avatar :src="avatar.url"/>
      <span style="margin-left: 4%; font-size: 18px">{{ props.userName }}</span>
      <div style="margin-left: auto;">
        <el-popover placement="bottom" content="删除信息">
          <template #reference>
            <img src="@/assets/svg/delete.svg" style="width: 20px;" @click="deleteVisible = true"/>
          </template>
        </el-popover>
      </div>
    </div>
    <div style="margin-left: 14%">
      <span class="certain-message-date">{{ props.date }}</span>
    </div>
    <div style="margin-top: 3%">
      <span>{{ props.index }}</span>
    </div>
  </div>
  </div>
  <el-dialog v-model="deleteVisible" title="删除信息" width="400" align-center>
    <span>您确定要删除该条信息吗？</span>
    <template #footer>
      <el-button @click="deleteVisible = false">返回</el-button>
      <el-button @click="deleteMessage" type="primary">删除</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.certain-message-date {
  font-size: 12px;
  color: #7a7a7a;
}
</style>