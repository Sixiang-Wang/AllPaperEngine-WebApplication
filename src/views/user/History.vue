<script setup>
import { onMounted, onBeforeUnmount, ref } from "vue";
import {ArrowRight, Delete, StarFilled} from "@element-plus/icons-vue";
import httpUtil from "@/api/http.js";

// 导入 Element UI 的图标
import { ElButton } from 'element-plus';
import router from "@/router/index.js";
import {useRoute} from "vue-router";

const userId = ref(localStorage.getItem("userId"));
// const userId = ref(1);
const activeName = ref('1')
const route = useRoute();
const padZero = (num) => {
  return num < 10 ? '0' + num : num;
}
const getCurrentDateTime = () =>{
  const now = new Date();
  const year = now.getFullYear();
  const month = padZero(now.getMonth() + 1); // 月份是从0开始的
  const day = padZero(now.getDate());
  const hours = padZero(now.getHours());
  const minutes = padZero(now.getMinutes());
  const seconds = padZero(now.getSeconds());
  // 构建 $date-time 格式的字符串
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}


const ResetDateFormat = (date) => {
  const dateArray = date.split('T');
  const datePart = dateArray[0];
  const timePart = dateArray[1];
  const timeArray = timePart.split(':');
  const hour = timeArray[0];
  const minute = timeArray[1];
  return `${datePart}`;
}
const formatDate = (date) => {
  const year = date.getFullYear(); // 获取年份
  const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 获取月份，+1是因为getMonth()返回的月份是从0开始的
  const day = date.getDate().toString().padStart(2, '0'); // 获取日期

  return `${year}-${month}-${day}`; // 拼接成YYYY-MM-DD格式
}
const OnDeleteButtonClicked = async (id) => {
  console.log(id)
  await httpUtil.get('user/deleteHistory', {
    userId: userId.value,
    publicationId: id
  });
  await UpdateHistory();
}
const goToPaper = async (id)=> {
  console.log(id)
  console.log(userId.value)
  await httpUtil.post2('user/addHistory', {
    publicationId: id,
    tstring : getCurrentDateTime(),
    userId: userId.value
  });
  await router.push({path: '/paper', query: {id: id, input: route.query.input}})
}
const tableData = ref([])
const tableDataToday = ref([])
const tableDataWeek = ref([])
const tableDataMonth = ref([])
const tableDataYear = ref([])
const tableDataEarlier = ref([])

const CategorizeDates = (dates) => {
  tableDataToday.value = [];
  tableDataWeek.value = [];
  tableDataMonth.value = [];
  tableDataYear.value = [];
  tableDataEarlier.value = [];
  const now = new Date();
  const today = formatDate(new Date(now.getFullYear(), now.getMonth(), now.getDate()));
  const startOfWeek = formatDate(new Date(now.setDate(now.getDate() - now.getDay())));
  const startOfMonth = formatDate(new Date(now.setMonth(now.getMonth(), 1)));
  const startOfYear = formatDate(new Date(now.setFullYear(now.getFullYear(), 0, 1)));
  tableData.value.forEach(function (item) {
    if(compareDate(item.timestamp, today) === 0) {
      tableDataToday.value.push(item);
    } else if(compareDate(item.timestamp, startOfWeek) === 1) {
      tableDataWeek.value.push(item);
    } else if(compareDate(item.timestamp, startOfMonth) === 1) {
      tableDataMonth.value.push(item);
    } else if(compareDate(item.timestamp, startOfYear) === 1) {
      tableDataYear.value.push(item);
    } else {
      tableDataEarlier.value.push(item);
    }
  });
}
const compareDate = (date1, date2) => {
  for(let i = 0; i < date1.length; i++) {
    if (date1[i] > date2[i]) {
      return 1;
    } else if (date1[i] < date2[i]) {
      return -1;
    }
  }
  return 0;
}
const UpdateHistory = async () => {
  const res = await httpUtil.get('user/viewAllHistory', {
    userId: userId.value
  });
  console.log(res.data.historyList);
  tableData.value = res.data.historyList;
  tableData.value.forEach((item) => {
    item.timestamp = ResetDateFormat(item.timestamp);
  });
  tableData.value.reverse();
  CategorizeDates(tableData.value);
};
onMounted(async () => {
  await UpdateHistory();
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
          <el-table :data="tableDataToday" style="width: 100%" max-height="800">
            <el-table-column label="标题" width="500">
              <template #default="scope">
                <span class="history-title" @click="goToPaper(scope.row.publicationid)">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column label="收藏日期" width="180" >
              <template #default="scope">
                <span class="date-range">{{ scope.row.timestamp }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" #default="scope">
              <el-button circle class="collect-button" :icon="StarFilled" @click="OnDeleteButtonClicked(scope.row.publicationid)"/>
            </el-table-column>

          </el-table>
        </div>
      </el-collapse-item>
      <el-collapse-item>
        <template #title>
          <span class="custom-title">本周</span>
        </template>
        <div>
          <el-table :data="tableDataWeek" style="width: 100%" max-height="800">
            <el-table-column label="标题" width="500">
              <template #default="scope">
                <span class="history-title" @click="goToPaper(scope.row.publicationid)">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column label="收藏日期" width="180" >
              <template #default="scope">
                <span class="date-range">{{ scope.row.timestamp }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" #default="scope">
              <el-button circle class="collect-button" :icon="StarFilled" @click="OnDeleteButtonClicked(scope.row.id)"/>
            </el-table-column>

          </el-table>
        </div>
      </el-collapse-item>
      <el-collapse-item>
        <template #title>
          <span class="custom-title">本月</span>
        </template>
        <div>
          <el-table :data="tableDataMonth" style="width: 100%" max-height="800">
            <el-table-column label="标题" width="500">
              <template #default="scope">
                <span class="history-title" @click="goToPaper(scope.row.publicationid)">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column label="收藏日期" width="180" >
              <template #default="scope">
                <span class="date-range">{{ scope.row.timestamp }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" #default="scope">
              <el-button circle class="collect-button" :icon="StarFilled" @click="OnDeleteButtonClicked(scope.row.id)"/>
            </el-table-column>

          </el-table>
        </div>
      </el-collapse-item>
      <el-collapse-item>
        <template #title>
          <span class="custom-title">今年</span>
        </template>
        <div>
          <el-table :data="tableDataYear" style="width: 100%" max-height="800">
            <el-table-column label="标题" width="500">
              <template #default="scope">
                <span class="history-title" @click="goToPaper(scope.row.publicationid)">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column label="收藏日期" width="180" >
              <template #default="scope">
                <span class="date-range">{{ scope.row.timestamp }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" #default="scope">
              <el-button circle class="collect-button" :icon="StarFilled" @click="OnDeleteButtonClicked(scope.row.id)"/>
            </el-table-column>

          </el-table>
        </div>
      </el-collapse-item>
      <el-collapse-item>
        <template #title>
          <span class="custom-title">更早以前</span>
        </template>
        <div>
          <el-table :data="tableDataEarlier" style="width: 100%" max-height="800">
            <el-table-column label="标题" width="500">
              <template #default="scope">
                <span class="history-title" @click="goToPaper(scope.row.publicationid)">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column label="收藏日期" width="180" >
              <template #default="scope">
                <span class="date-range">{{ scope.row.timestamp }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" #default="scope">
              <el-button circle class="collect-button" :icon="StarFilled" @click="OnDeleteButtonClicked(scope.row.id)"/>
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
  font-size: 12px;
}
.el-table__body tr:last-child {
  border-bottom: none !important;
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