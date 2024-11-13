<script setup>
import { onMounted, onBeforeUnmount, ref } from "vue";
import { ArrowRight } from "@element-plus/icons-vue";
import defaultAvatar from "@/assets/image/user.gif";

const avatar = ref({
  defaultAvatar: defaultAvatar,
  url: defaultAvatar,
});

const activeName = ref('first')
</script>

<template>
  <el-card style="max-width: 75%">
    <el-row>
      <el-col :span="12">
        <el-row>
          <el-avatar
              :src="avatar.url"
              :size="70"
              shape="square"
              fit="cover"
              class="user-avatar"
          />
          <div style="margin-left: 10%; margin-top: 5%">
            <el-text style="font-size: large">姓名</el-text><br>
            <el-text style="font-size: small">学者ID：1000000</el-text>
          </div>
        </el-row>
        <el-row style="margin-top: 15%">
          <el-text style="font-size: small">关注：0</el-text>
          <el-text style="font-size: small; margin-left: 2%">粉丝：0</el-text>
          <el-text style="font-size: small; margin-left: 2%">浏览：0</el-text>
        </el-row>
      </el-col>
      <el-col :span="12">
        <el-divider direction="vertical" />
        <el-text style="font-size: medium">根据您所认领的文献计算，各项指标如下：</el-text>
        <el-row style="margin-top:3%">
          <el-col :span="7" style="font-size:small">总发文量</el-col>
          <el-col :span="5" style="font-size:small">0</el-col>
          <el-col :span="7" style="font-size:small">第一作者发文量</el-col>
          <el-col :span="5" style="font-size:small">0</el-col>
        </el-row>
        <el-row style="margin-top:3%">
          <el-col :span="7" style="font-size:small">总发文量</el-col>
          <el-col :span="5" style="font-size:small">0</el-col>
          <el-col :span="7" style="font-size:small">第一作者发文量</el-col>
          <el-col :span="5" style="font-size:small">0</el-col>
        </el-row>
        <el-row style="margin-top:3%">
          <el-col :span="7" style="font-size:small">总发文量</el-col>
          <el-col :span="5" style="font-size:small">0</el-col>
          <el-col :span="7" style="font-size:small">第一作者发文量</el-col>
          <el-col :span="5" style="font-size:small">0</el-col>
        </el-row>
        <el-row style="margin-top:3%">
          <el-col :span="7" style="font-size:small">总发文量</el-col>
          <el-col :span="5" style="font-size:small">0</el-col>
          <el-col :span="7" style="font-size:small">第一作者发文量</el-col>
          <el-col :span="5" style="font-size:small">0</el-col>
        </el-row>
      </el-col>
    </el-row>
  </el-card>

  <el-card style="max-width: 75%; margin-top: 1.5%">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick" type="border-card">
      <el-tab-pane label="学术文献" name="first">
        <el-row style="margin-top:3%">
          <el-col :span="6">123</el-col>
          <el-col :span="18">123</el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="学术统计" name="second">Config</el-tab-pane>
      <el-tab-pane label="学术关系网" name="third">Role</el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<style scoped>

</style>