<script setup>
import {onMounted, onBeforeUnmount, ref, reactive, computed, nextTick} from "vue";
import { ArrowRight,Picture,Camera } from "@element-plus/icons-vue";
import defaultAvatar from "@/assets/image/user.gif";
import * as httpUtil from "@/api/http.js";
import * as cookieUtil from "@/utils/cookie.js";
import http from "@/api/http.js";
import {ElMessage} from "element-plus";

const avatarUrl = computed(()=>{
  const avatar = localStorage.getItem('avatar') || '/hahashenmedoumeiyou';
  return http.getUrlWithoutSlash() + avatar;
});



const tableData = ref([
  {
    feature: "姓名",
    english: "name",
    value: "",
    editable: false,
  },
  {
    feature: "生日",
    english: 'birthTime',
    value: "",
    editable: false,
  }
]);
const birthTime = ref("");

const tableData2 = ref([
  {
    feature: "学术领域",
    english: 'academicField',
    value: "",
    editable: false,
  },
  {
    feature: "在职单位",
    english: 'company',
    value: "",
    editable: false,
  },
  {
    feature: "职业",
    english: 'profession',
    value: "",
    editable: false,
  },
]);

const tableData3 = ref([
  {
    feature: "电子邮件",
    english: 'mail',
    value: "",
    editable: false,
  },
  {
    feature: "电话",
    english: 'phone',
    value: "",
    editable: false,
  },
]);
onMounted(async()=>{
  try{
    await getUserData()


  }catch (e){
    console.error(e);
  }
})
const activeRow = ref(null); // 用于跟踪当前活动行
const allTables = [tableData, tableData2, tableData3]; // 方便遍历所有表格数据
const saveConfirmVisible = ref(false);
let originalData = allTables.map(table => JSON.parse(JSON.stringify(table.value)));
let changedData = [...allTables].map(table => [...table.value]);
const dataChanged = ref(false);
const editBirth = ref(false);

const updateEditable = (row) => {
  console.log(tableData.value);

  if (activeRow.value === row) {
    if(row.english!=='birthTime'){
      row.editable = !row.editable;
    }

  } else {
    if (activeRow.value) activeRow.value.editable = false; // 关闭上一个活动行
    if(row.english!=='birthTime'){
      row.editable = true; // 激活当前行
    }
    activeRow.value = row; // 更新活动行
  }

  if(row.english==='birthTime'){
    editBirth.value = true;
    birthTime.value = row.value
    return
  }

  // 检查当前行的数据是否已被修改
  const originalRow = originalData.flat().find(origRow => origRow.feature === row.feature);
  if (originalRow && originalRow.value !== row.value) {
    // 如果找到原始行且值已更改，则更新 changedData
    const tableIndex = allTables.findIndex(tableRef => tableRef.value.some(r => r.feature === row.feature));
    if (tableIndex !== -1) {
      const rowIndex = allTables[tableIndex].value.findIndex(r => r.feature === row.feature);
      if (rowIndex !== -1) {
        dataChanged.value = true;
        // 更新 changedData 中对应行的数据
        changedData[tableIndex][rowIndex] = { ...row }; // 复制当前行的数据
      }
    }
  }
};

const handleClickOutside = (event) => {
  if(activeRow.value.english==='birthTime'){
    return
  }
  const target = event.target;
  const isOutside = !target.closest(".el-table"); // 检查是否在表格外部点击
  if (isOutside && activeRow.value) {
    activeRow.value.editable = false; // 关闭编辑模式
    activeRow.value = null; // 重置活动行
    console.log("在表格外点击了。");
    if (dataChanged) {
      // 有数据被修改，弹出保存确认弹窗
      saveConfirmVisible.value = true;
      console.log(originalData);
      console.log(changedData);
    } 
  }
};

const handleSaveConfirm = async (action) => {
  saveConfirmVisible.value = false;
  if (action === 'confirm') {
    // 用户选择保存
    allTables.forEach((table, index) => {
      table.value = changedData[index].map(rowData => allTables[index].value.find(row => row.feature === rowData.feature) || { ...rowData, editable: false });
    });
    const allData = allTables.flatMap(table => table.value);

    const user = {};
    allData.forEach(row => {
      user[row.english] = row.value; // 动态生成 User 对象属性
    });


    console.log("发送到后端的用户数据:", user);

// 发起请求
    const res = await httpUtil.postWithHeader('/user/updateUser', user, {
      Authorization: cookieUtil.getCookie("token")
    });
    if(res.code === 200) {
      ElMessage.success("修改成功!")
    }

  } else {
    // 用户选择取消
    await getUserData()

    activeRow.value = null;
    dataChanged.value = false;
  }
};



const getUserData = async () =>{
  const res = await httpUtil.get('/user/getUserInfo',{},{
    Authorization: cookieUtil.getCookie("token")
  })

  tableData.value = tableData.value.map((row, index) => ({
    ...row,
    value: res.data.tables[0][index]?.value || row.value, // 更新 value
  }));


  tableData2.value = tableData2.value.map((row, index) => ({
    ...row,
    value: res.data.tables[1][index]?.value || row.value,
  }));

  tableData3.value = tableData3.value.map((row, index) => ({
    ...row,
    value: res.data.tables[2][index]?.value || row.value,
  }));
}

const updateDate = async () => {
  // 等待 DOM 更新
  await nextTick();
  // 打印选择的日期
  const date = new Date(birthTime.value);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 补足两位
  const day = String(date.getDate()).padStart(2, '0');       // 补足两位
  const formattedDate = `${year}-${month}-${day}`;
  ElMessage.success(`选择的日期是：${formattedDate}`);
  const res = await httpUtil.post2WithHeader('/user/updateUserBirthTime',
      { birthTime: formattedDate },  // 这里使用对象传递
      { Authorization: cookieUtil.getCookie("token") }
  );

  console.log(res);

  await getUserData()

  // 可选：关闭弹窗
  editBirth.value = false;
};



onMounted(() => {
  if(localStorage.getItem("avatarReload")==="yes"){
    ElMessage.success("头像更新中...")
    setTimeout(() => {
      window.location.reload();
      localStorage.setItem("avatarReload", "no");
    }, 1000); // 延迟 1 秒
  }
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});


const updateAvatar = () => {
  return `${http.baseUrl}user/updateUserAvatar?userid=${localStorage.getItem("userId")}`;
}

const afterUpdateAvatar = () => {
  avatarUrl.value="/hahashenmedoumeiyou"
  localStorage.setItem("avatarReload","yes")
  window.location.reload()
}


</script>

<template>
  <el-card style="max-width: 800px">
    <div class="card-header">
      <span>基本信息</span>
    </div>
    <br />

    <div style="display: flex; align-items: center;">
      <p class="text-item">个人资料照片</p>
      <el-avatar
          :src="avatarUrl"
          :size="70"
          shape="square"
          fit="cover"
          class="user-avatar"
      />
      <el-upload :action="updateAvatar()" :on-success="afterUpdateAvatar">
        <arrow-right style="width: 25px; height: 25px; margin-left: 10px;margin-right: 3%;" />
      </el-upload>

    </div>

    <el-divider style="margin: 0" />
    <el-table :data="tableData" style="width: 100%" :show-header="false">
      <el-table-column prop="feature" label="Feature" width="100" />
      <el-table-column label="Value" width="600">
        <template #default="scope">
          <el-input
              v-if="scope.row.editable"
              v-model="scope.row.value"
              placeholder="请输入"
          />
          <span v-else @dblclick="updateEditable(scope.row)">{{ scope.row.value }}</span>
        </template>
      </el-table-column>
      <el-table-column width="50px">
        <template #default="scope">
          <arrow-right @click="updateEditable(scope.row)" style="width: 25px; height: 25px;" />
        </template>
      </el-table-column>
    </el-table>

  </el-card>
  <el-card style="max-width: 800px; margin-top: 45px; user-select: none">
    <div class="card-header">
      <span>学术信息</span>
    </div>
    <br />
    <el-table :data="tableData2" style="width: 100%" :show-header="false">
      <el-table-column prop="feature" label="Feature" width="100" />
      <el-table-column label="Value" width="600">
        <template #default="scope">
          <el-input
              v-if="scope.row.editable"
              v-model="scope.row.value"
              placeholder="请输入"
          />
          <span v-else @dblclick="updateEditable(scope.row)">{{ scope.row.value }}</span>
        </template>
      </el-table-column>
      <el-table-column width="50px">
        <template #default="scope">
          <arrow-right style="width: 25px; height: 25px;" @click="updateEditable(scope.row)" />
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <el-card style="max-width: 800px; margin-top: 45px">
    <div class="card-header">
      <span>联系信息</span>
    </div>
    <br />
    <el-table :data="tableData3" style="width: 100%" :show-header="false">
      <el-table-column prop="feature" label="Feature" width="100" />
      <el-table-column label="Value" width="600">
        <template #default="scope">
          <el-input
              v-if="scope.row.editable"
              v-model="scope.row.value"
              placeholder="请输入"
          />
          <span v-else>{{ scope.row.value }}</span>
        </template>
      </el-table-column>
      <el-table-column width="50px">
        <template #default="scope">
          <arrow-right style="width: 25px; height: 25px;" />
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  
  <el-dialog
    title="保存更改"
    v-model="saveConfirmVisible"
    width="20%"
  >
    <el-row class="save-text">
      <span>您确定要保存这些更改吗？</span>
    </el-row>
    <span slot="footer" style="margin-left: 25%">
      <el-button @click="handleSaveConfirm('cancel')">取消</el-button>
      <el-button type="primary" @click="handleSaveConfirm('confirm')">保存</el-button>
    </span>
  </el-dialog>


  <el-dialog
      title="选择日期"
      v-model="editBirth"
      width="300px"
  >
    <div style="display: flex;margin-top:15px;flex-direction: column;align-items: center;margin-bottom: 30px;">
      <el-date-picker
          v-model="birthTime"
          type="date"
          placeholder="Pick a day"
          style="width: 100%;"
      />
    </div>

    <span slot="footer" style="display: flex;justify-content: center;gap: 20px;margin-top: 20px;">
      <el-button @click="editBirth = false">取消</el-button>
      <el-button type="primary" @click="updateDate">确定</el-button>
    </span>
  </el-dialog>

</template>

<style scoped>
@import "@/css/user.css";
.el-table__body tr:last-child {
  border-bottom: none !important;
}
.alter-head-button {
  height:80px;
  width:200px;
  margin:20px;
  display: flex;
  align-items: center;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
  background-color:rgb(245, 242, 242);
}
.alter-head-button:hover {
  background-color:rgb(183, 181, 181);
}
.alter-head-button .el-icon {
  font-size: 18px;
  color: rgb(96, 95, 95);
}
.alter-head-text {
  color: rgb(84, 83, 83);
  margin-left: 8px;
}
.vertical-divider {
  height: 80%; /* 使分割线高度与按钮容器相同 */
  margin: 20px; 
}
::v-deep .el-dialog__title {
  font-size: 15px; 
}
.head-line{
  width:100%;
  height:0.5px;
  background-color: rgb(154, 152, 152);
  border: none;
}
.save-text{
  margin-top:20px;
  margin-bottom: 40px;
  font-size: 16px;
  color: rgb(96, 95, 95);
}

</style>
