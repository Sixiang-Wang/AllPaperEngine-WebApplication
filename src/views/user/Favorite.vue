<script setup>
import { onMounted, onBeforeUnmount, ref } from "vue";
import {ArrowRight, Delete, Edit, Plus, Remove, Star, StarFilled} from "@element-plus/icons-vue";
import httpUtil from "@/api/http.js";

// 导入 Element UI 的图标
import {ElButton, ElMessageBox, ElMessage} from 'element-plus';
import router from "@/router/index.js";
import {useRoute} from "vue-router";
import testhttp from "@/api/http.js";

const activeName = ref('1')

// const userId = ref(localStorage.getItem("userId"));
const userId = ref(1);
let tags = ref([]);
const selectedTags = ref([]);
const route = useRoute();
let buttonLabels = ref([]);
const buttonStates = ref(Array(12).fill(false));
// 设置四种按钮的颜色
const buttonColors = ref(['#9c69fc', '#5bc8f3', '#ffab73', '#a4f65c']);
const tableData = ref()
const test = ['test1'];
const ChangeTagList = (index) => {
  if(buttonStates.value[index]){
    selectedTags.value.push(buttonLabels.value[index]);
    console.log(selectedTags.value)
  } else {
    selectedTags.value = selectedTags.value.filter((item) => item !== buttonLabels.value[index]);
  }
  UpdateFavorite();
}
const ClearTagList = async () => {
  for(let i = 0; i < buttonStates.value.length; i++){
    buttonStates.value[i] = false;
  }
  selectedTags.value = [];
  await UpdateFavorite();
}
function OnDeleteButtonClicked(id){
  // 弹出确认消息
  ElMessageBox.confirm('确认要取消该收藏吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 用户点击确认后执行的代码
    await httpUtil.get('user/deleteUserFavorite', {
      userId: userId.value,
      publicationId: id
    });
    console.log(`确定删除,id: ${id}`);
    ClearTagList();
    await UpdateFavorite();
  }).catch(() => {
    // 用户点击取消后执行的代码
    console.log("Delete Error");
  });
}
const goToPaper = (id)=> {
  // console.log(id)
  router.push({path: '/paper', query: {id: id, input: route.query.input}})
}
const UpdateFavorite = async () => {
  let res = ([]);
  let res2 = ([]);
  res2 = await httpUtil.get('user/viewAllTags', {
    userId: userId.value
  });
  if(res2.data.code === 200)
    tags.value = res2.data.tags
  tags.value.forEach((item, index) => {
    buttonLabels.value[index] = item.tag;
  });
  if(selectedTags.value.length === 0)
    res = await httpUtil.get('user/viewAllFavoritesByUser', {
      userId: userId.value
    });
  else {
    res = await httpUtil.post('user/viewAllFavoritesWithTags', {
        userId: userId.value,
        tags: [...selectedTags.value]
    });
    console.log(res.data.favoriteList);
  }
  if(res.data.code === 200)
  {
    // 截取res.data.timestamp的前10位
    res.data.favoriteList.forEach((item) => {
      item.timestamp = item.timestamp.substring(0, 10);
    });
    tableData.value = res.data.favoriteList;
  }
};
onMounted(async () => {
  await UpdateFavorite();
})
</script>

<template>
  <el-container>
    <el-card style="width: 800px">
      <div class="card-header">
        <span>个人收藏</span>
      </div>
      <br>
        <el-table :data="tableData" style="width: 100%" max-height="800" :header-cell-style="{'text-align':'center'}">
          <el-table-column label="标题" width="500">
            <template #default="scope">
              <span class="favorite-title" @click="goToPaper(scope.row.publicationid)">{{ scope.row.title }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="tags" label="标签" width="180" align="center">
            <template #default="scope">
              <el-button v-for="(tag, index) in scope.row.tags.split(',').slice(0, 4)"
                         :key="tag"
                         style="font-size: 16px; padding: 3px 6px; margin: 3px;"
                         :color="buttonColors[index]">
                <span style="color: white">{{ tag }}</span>
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="操作" #default="scope" align="center">
            <el-button circle class="collect-button" :icon="StarFilled" @click="OnDeleteButtonClicked(scope.row.publicationid)"/>
          </el-table-column>

        </el-table>
    </el-card>
    <div style="height: 600px">
      <el-card class="card-tag" style="width: 360px; max-height: 500px">
        <template #header>
          <div class="card-tag-header">
            <span style="font-size: 20px; font-weight: bold;">按标签筛选<span style="font-weight: bold;">😏</span></span>
            <div style="display: flex; justify-content: flex-end; width: 170px">
              <el-tooltip
                  class="box-item"
                  effect="dark"
                  content="清除选择"
                  placement="top"
              >
                <el-button type="primary" :icon="Remove" circle @click="ClearTagList"/>
              </el-tooltip>
            </div>
          </div>
        </template>
        <el-scrollbar class="tag-scrollbar">
<!--          <template>-->
            <el-checkbox
              v-for="(label, index) in buttonLabels"
              :key="index"
              v-model="buttonStates[index]"
              @change="ChangeTagList(index)"
            >
              {{label}}
            </el-checkbox>
<!--          </template>-->
<!--          <el-button class="tag-button"-->
<!--              v-for="(label, index) in buttonLabels"-->
<!--              :key="index"-->
<!--              :type="buttonTypes[index % buttonTypes.length]"-->
<!--              plain-->
<!--          >-->
<!--            {{ label }}-->
<!--          </el-button>-->
        </el-scrollbar>
        <template #footer>

        </template>
      </el-card>
    </div>
  </el-container>
</template>

<style scoped>
@import "@/css/user.css";
.card-tag{
  margin-left: 30px;
  flex: 0 0 auto;
  height: 246px;
}
.el-table__body tr:last-child {
  border-bottom: none !important;
}
.favorite-title {
  font-weight: 500;
  font-size: 16px;
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
.card-tag-header{
  height: 20px;
  display: flex; /* 启用 Flexbox */
  align-items: center; /* 垂直居中 */
}
.card-tag-header span {
  margin-right: 10px; /* 在 span 和 div 之间添加一些间隙 */
}
.button-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-start;
  align-items: flex-start;
}
.tag-scrollbar {
  height: 100%;
  width: 100%;
  overflow-y: auto;
}
.tag-button{
  margin: 3px;
}
</style>