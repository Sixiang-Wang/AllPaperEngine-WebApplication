<script setup>
import { onMounted, onBeforeUnmount, ref } from "vue";
import {ArrowRight, Delete, StarFilled} from "@element-plus/icons-vue";
import httpUtil from "@/api/http.js";

// 导入 Element UI 的图标
import { ElButton } from 'element-plus';
import router from "@/router/index.js";
import {useRoute} from "vue-router";

const activeName = ref('1')
const route = useRoute();
const OnDeleteButtonClicked = () => {

}
const goToPaper = (id)=> {
  // console.log(id)
  router.push({path: '/paper', query: {id: id, input: route.query.input}})
}
const tableData = ref()
const UpdateFavorite = async () => {
  const res = await httpUtil.get('/openalex/get/page', {
    page: 1
  });
  console.log(res.data.works);
  tableData.value = res.data.works;
};
onMounted(async () => {
  await UpdateFavorite();
})
</script>

<template>
  <el-card style="max-width: 800px">
    <div class="card-header">
      <span>浏览历史</span>
    </div>
    <br>
    <el-collapse v-model="activeName" accordion>
      <el-collapse-item>
        <template #title>
          <span class="custom-title">今天</span>
        </template>
        <div>
          <el-table :data="tableData" style="width: 100%" max-height="800">
            <el-table-column label="标题" width="500">
              <template #default="scope">
                <span class="history-title" @click="goToPaper(scope.row.id)">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="" label="收藏日期" width="180" />
            <el-table-column label="操作" #default="scope">
              <el-button circle class="collect-button" :icon="StarFilled" @click="OnDeleteButtonClicked(scope.row.id)"/>
            </el-table-column>

          </el-table>
        </div>
      </el-collapse-item>
      <el-collapse-item>
        <template #title>
          <span class="custom-title">本周</span>
        </template>

      </el-collapse-item>
      <el-collapse-item>
        <template #title>
          <span class="custom-title">本月</span>
        </template>

      </el-collapse-item>
      <el-collapse-item>
        <template #title>
          <span class="custom-title">今年</span>
        </template>

      </el-collapse-item>
      <el-collapse-item>
        <template #title>
          <span class="custom-title">更早以前</span>
        </template>

      </el-collapse-item>
    </el-collapse>
  </el-card>

</template>

<style scoped>
@import "@/css/user.css";
.el-table__body tr:last-child {
  border-bottom: none !important;
}
.history-title {
  font-weight: 500;
  font-size: 14px;
  color: #006994;
  cursor: pointer;
}
.history-title:hover {
  text-decoration: underline;
}
.date-range {
  font-size: 20px;
}
.el-table__body tr:last-child {
  border-bottom: none !important;
}
.history-title {
  font-weight: 500;
  font-size: 14px;
  color: #006994;
  cursor: pointer;
}
.history-title:hover {
  text-decoration: underline;
}
.el-collapse-item__header .el-collapse-item__name {
  color: #606266; /* 文字颜色 */
  font-size: 16px; /* 字体大小 */
}
.custom-title {
  color: #03072c; /* 修改标题颜色 */
  font-size: 16px; /* 修改字体大小 */
}

.collect-button{
  border: none;
  color: #fff14e;
  background-color: transparent;
  font-size: 30px;
}

.collect-button:hover{
  border: none;
  color: #717171;
  background-color: transparent;
  font-size: 24px;
}
</style>