<script setup>
import { onMounted, onBeforeUnmount, ref,reactive } from "vue";
import { ArrowRight,Picture,Camera } from "@element-plus/icons-vue";
import defaultAvatar from "@/assets/image/user.gif";

const avatar = ref({
  defaultAvatar: defaultAvatar,
  url: defaultAvatar,
});

const dialogVisible = ref(false);

const openDialog = () => {
  dialogVisible.value = true;
};

const tableData = ref([
  {
    feature: "姓名",
    value: "Albert Einstein",
    editable: false,
  },
  {
    feature: "生日",
    value: "1897-03-14",
    editable: false,
  },
  {
    feature: "性别",
    value: "男",
    editable: false,
  },
]);

const tableData2 = ref([
  {
    feature: "学术领域",
    value: "理论物理",
    editable: false,
  },
  {
    feature: "在职单位",
    value: "普林斯顿高等研究院",
    editable: false,
  },
  {
    feature: "职业",
    value: "教授",
    editable: false,
  },
]);

const tableData3 = ref([
  {
    feature: "电子邮件",
    value: "0d000721@163.com",
    editable: false,
  },
  {
    feature: "电话",
    value: "12345678910",
    editable: false,
  },
]);

const activeRow = ref(null); // 用于跟踪当前活动行
const allTables = [tableData, tableData2, tableData3]; // 方便遍历所有表格数据
const saveConfirmVisible = ref(false);
let originalData = allTables.map(table => JSON.parse(JSON.stringify(table.value)));
let changedData = [...allTables].map(table => [...table.value]);
const dataChanged = ref(false);

const updateEditable = (row) => {
  if (activeRow.value === row) {
    row.editable = !row.editable;
  } else {
    if (activeRow.value) activeRow.value.editable = false; // 关闭上一个活动行
    row.editable = true; // 激活当前行
    activeRow.value = row; // 更新活动行
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

const handleSaveConfirm = (action) => {
  saveConfirmVisible.value = false;
  if (action === 'confirm') {
    // 用户选择保存
    allTables.forEach((table, index) => {
      table.value = changedData[index].map(rowData => allTables[index].value.find(row => row.feature === rowData.feature) || { ...rowData, editable: false });
    });
  } else {
    // 用户选择取消
    allTables.forEach((table, index) => {
      table.value = originalData[index].map(row => ({ ...row, editable: false }));
    });
    activeRow.value = null;
    dataChanged.value = false;
  }
};

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});
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
          :src="avatar.url"
          :size="70"
          shape="square"
          fit="cover"
          class="user-avatar"
      />
      <arrow-right @click="openDialog" style="width: 25px; height: 25px; margin-left: 10px;margin-right: 3%;" />
    </div>
    <el-dialog v-model="dialogVisible" title="> 修改头像" style="width:520px">
      <el-row>
        <el-col span="12">
          <el-button type="text" class="alter-head-button">
            <el-icon><Picture /></el-icon>
            <span class = "alter-head-text">选择本地图片</span>
          </el-button>
          <el-button type="text" class="alter-head-button">
            <el-icon><Camera /></el-icon>
            <span class = "alter-head-text">拍摄照片</span>
          </el-button>
        </el-col>
        <el-col span="1">
          <el-divider direction="vertical" class="vertical-divider" />
        </el-col>
        <el-col span="11">
          <el-avatar
            :src="avatar.url"
            :size="100"
            shape="square"
            fit="cover"
            class="user-avatar"
            style="margin:50px"
          />
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer" style="margin-left: 42%;margin-top: 40px;">
        <el-button @click="dialogVisible = false" style="width:100px">更 新</el-button>
      </span>
    </el-dialog>

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
