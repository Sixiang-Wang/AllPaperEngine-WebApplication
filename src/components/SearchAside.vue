<script setup>
import {ref} from "vue";

const activeName = ref(["1"]);
const activeTheme = ref([]);
const themes = ref([
  {
    label: "性能研究",
    value: "1"
  },
  {
    label: "复合材料",
    value: "2"
  },
  {
    label: "光催化",
    value: "3"
  }
])
</script>

<template>
  <div class="search-aside">
    <el-collapse v-model="activeName">
      <el-collapse-item title="主题" name="1" style="background-color:#C3EEFD;">
        <div style="display: flex">
          <el-checkbox-group v-model="activeTheme">
            <el-checkbox v-for="theme in themes" :key="theme.value" :label="theme.label" :value="theme.value" style="display: block; padding-top: 10px;">
              {{ theme.label }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </el-collapse-item>
    </el-collapse>
    <el-collapse v-model="activeName">
      <el-collapse-item title="主题" name="1" style="background-color:#C3EEFD;">
        <div style="display: flex">
          <el-checkbox-group v-model="activeTheme">
            <el-checkbox v-for="theme in themes" :key="theme.value" :label="theme.label" :value="theme.value" style="display: block; padding-top: 10px;">
              {{ theme.label }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </el-collapse-item>
    </el-collapse>
    <el-collapse v-model="activeName">
      <el-collapse-item title="主题" name="1" style="background-color:#C3EEFD;">
        <div style="display: flex">
          <el-checkbox-group v-model="activeTheme">
            <el-checkbox v-for="theme in themes" :key="theme.value" :label="theme.label" :value="theme.value" style="display: block; padding-top: 10px;">
              {{ theme.label }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
<!--  :deep(.el-collapse-item__header) {-->
<!--  background-color: #C3EEFD;-->
<!--  padding-left: 13%;-->
<!--  }-->
</template>

<style scoped>
@import "@/css/search.css";

</style>