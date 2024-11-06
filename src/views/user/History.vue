<script setup>
import { onMounted, onBeforeUnmount, ref } from "vue";
import {ArrowRight, Delete} from "@element-plus/icons-vue";
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
      <el-collapse-item title="今天" name="1" class="date-range">
        <div>
          <el-table :data="tableData" style="width: 100%" max-height="800">
            <el-table-column label="标题" width="500" >
              <template #default="scope">
                <span class="favorite-title" @click="goToPaper(scope.row.id)">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="" label="收藏日期" width="180" />
            <el-table-column label="操作">
              <el-button type="info" :icon="Delete" circle/>
            </el-table-column>
          </el-table>
        </div>
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
  font-size: 10px;
  color: #009adc;
  cursor: pointer;
}
.favorite-title:hover {
  text-decoration: underline;
}
.date-range {
  font-size: 20px;
}
</style>