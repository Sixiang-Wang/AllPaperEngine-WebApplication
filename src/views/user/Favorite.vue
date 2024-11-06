<script setup>
import { onMounted, onBeforeUnmount, ref } from "vue";
import {ArrowRight, Delete, Star, StarFilled} from "@element-plus/icons-vue";
import httpUtil from "@/api/http.js";

// 导入 Element UI 的图标
import {ElButton, ElMessageBox, ElMessage} from 'element-plus';
import router from "@/router/index.js";
import {useRoute} from "vue-router";

const activeName = ref('1')
const route = useRoute();
function OnDeleteButtonClicked(id){
  // 弹出确认消息
  ElMessageBox.confirm('确认要取消该收藏吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 用户点击确认后执行的代码
    // router.push({path: '/favorite/delete', query: {id: id}}}) // 调用删除请求接口
    console.log(`确定删除,id: ${id}`);
    UpdateFavorite();
  }).catch(() => {
    // 用户点击取消后执行的代码
  });
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
      <span>个人收藏</span>
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
                  <span class="favorite-title" @click="goToPaper(scope.row.id)">{{ scope.row.title }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="" label="收藏日期" width="180" />
              <el-table-column label="操作" #default="scope">
                <el-button type="info" circle :icon="StarFilled" @click="OnDeleteButtonClicked(scope.row.id)"/>
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
.favorite-title {
  font-weight: 500;
  font-size: 14px;
  color: #006994;
  cursor: pointer;
}
.favorite-title:hover {
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
</style>