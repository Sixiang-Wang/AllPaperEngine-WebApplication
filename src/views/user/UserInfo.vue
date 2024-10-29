<script setup>
import { onMounted, onBeforeUnmount, ref } from "vue";
import { ArrowRight } from "@element-plus/icons-vue";
import defaultAvatar from "@/assets/image/user.gif";

const avatar = ref({
  defaultAvatar: defaultAvatar,
  url: defaultAvatar,
});

const tableData = ref([
  {
    feature: "姓名",
    value: "miaomiao",
    editable: false,
  },
  {
    feature: "生日",
    value: "2007-02-21",
    editable: false,
  },
  {
    feature: "性别",
    value: "保密",
    editable: false,
  },
]);

const tableData2 = ref([
  {
    feature: "学术领域",
    value: "软件工程",
    editable: false,
  },
  {
    feature: "在职单位",
    value: "南京航空航天大学",
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

const updateEditable = (row) => {
  if (activeRow.value === row) {
    row.editable = !row.editable;
  } else {
    if (activeRow.value) activeRow.value.editable = false; // 关闭上一个活动行
    row.editable = true; // 激活当前行
    activeRow.value = row; // 更新活动行
  }
};

const handleClickOutside = (event) => {
  const target = event.target;
  const isOutside = !target.closest(".el-table"); // 检查是否在表格外部点击
  if (isOutside && activeRow.value) {
    activeRow.value.editable = false; // 关闭编辑模式
    activeRow.value = null; // 重置活动行
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
  
</template>

<style scoped>
@import "@/css/user.css";
.el-table__body tr:last-child {
  border-bottom: none !important;
}
</style>
