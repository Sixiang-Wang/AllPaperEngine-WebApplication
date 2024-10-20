<script setup>
import {Menu} from "@element-plus/icons-vue";
</script>

<template>
  <div class="personal-portal-header">
    <el-row>
      <el-col :span="2">
        <el-icon><Menu /></el-icon>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.personal-portal-header {
  padding: 1% 1%;
  //border: #7a7a7a 1px solid;
  //border-radius: 5px;
  //box-shadow: 1px 1px 1px #7a7a7a;
}


</style>