<script setup>
import {ArrowRight} from "@element-plus/icons-vue";
import Advertise from "@/components/Advertisement.vue";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import * as httpUtil from "@/api/http.js";
import * as cookieUtil from "@/utils/cookie.js";

const tableData = [
  {
    feature: "密码",
    value: "**********"
  }
]
const tableData2 = [
  {
    feature: "个人信息公开"
  },
  {
    feature: "登录时邮箱验证"
  }
]

const editPassword = ref(false);
const oldPassword = ref('');
const newPassword = ref('');
const newPassword2 = ref('');
const clickPassword = async () => {
  editPassword.value=true
};
const quitPassword = async () => {
  editPassword.value=false
};
const updatePassword = async () => {
  if(newPassword.value!==newPassword2.value){
    ElMessage.error("两次密码输入不一致")
    return
  }

  const res = await httpUtil.post2WithHeader('/user/changePassword',
      { oldPassword: oldPassword.value,newPassword: newPassword.value},
       {
    Authorization: cookieUtil.getCookie("token")
  });

  if(res.data.code===414){
    ElMessage.error('旧密码不正确!')
    return
  }

  ElMessage.success('密码修改完成!')
  editPassword.value=false
};


</script>

<template>
  <div style="display: flex">
    <div>
      <el-card style="max-width: 800px">
        <div class="card-header">
          <span>账号安全</span>
        </div>
        <el-table :data="tableData" style="width: 100%" show-header="false">
          <el-table-column prop="feature" width="100px"></el-table-column>
          <el-table-column prop="value" width="600px"></el-table-column>
          <el-table-column width="50px">
            <template #default="scope">
              <arrow-right @click="clickPassword" style="width: 25px; height: 25px;"/>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <el-card style="max-width: 800px;margin-top: 5%">
        <div class="card-header">
          <span>设置</span>
        </div>
        <el-table :data="tableData2" style="width: 100%" show-header="false">
          <el-table-column prop="feature" width="700px"></el-table-column>
          <el-table-column prop="value" width="70px">
            <template #default="scope">
              <el-switch
                  v-model="scope.row.value"
                  class="ml-2"
                  style="--el-switch-on-color: #13ce66;"
              />
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    <div style="margin-left: 5%">
<!--      <Advertise/>-->
    </div>
  </div>

  <el-dialog
      title="更改密码"
      v-model="editPassword"
      width="400px"
  >
    <div style="display: flex;margin-top:15px;flex-direction: column;align-items: center;margin-bottom: 30px;">
      <el-form-item label="输入原密码">
        <el-input
            v-model="oldPassword"
            style="width: 240px"
            placeholder="输入原密码"
            clearable
            show-password
        />
      </el-form-item>

      <el-form-item label="输入新密码">
        <el-input
            v-model="newPassword"
            style="width: 240px"
            placeholder="输入新密码"
            clearable
            show-password
        />
      </el-form-item>

      <el-form-item label="确认新密码">
        <el-input
            v-model="newPassword2"
            style="width: 240px"
            placeholder="确认新密码"
            clearable
            show-password
        />
      </el-form-item>
    </div>

    <span slot="footer" style="display: flex;justify-content: center;gap: 20px;margin-top: 20px;">
      <el-button @click="quitPassword">取消</el-button>
      <el-button type="primary" @click="updatePassword">确定</el-button>
    </span>
  </el-dialog>
</template>

<style scoped>
@import "@/css/user.css";
@import "@/css/basic.css";
</style>