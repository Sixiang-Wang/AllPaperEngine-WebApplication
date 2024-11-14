<template>
    <div class="institution-info">
      <el-card class="institution-card">
        <h2>{{ institution.name }}</h2>
        <p>{{ institution.description }}</p>
      </el-card>
  
      <div class="scholars-list">
        <el-card
          v-for="(scholar, index) in scholars"
          :key="scholar.name"
          :class="['scholar-item']"
          shadow="hover"
        >
          <div class="scholar-header">
            <router-link :to="'/scholar/' + scholar.name" class="scholar-name">
              {{ scholar.name }}
            </router-link>
            <el-tag :type="getTagType(index)" size="small">{{ scholar.fields }}</el-tag>
          </div>
          <p><strong>引用次数：</strong>{{ scholar.citations }}</p>
          <el-divider></el-divider>
        </el-card>
      </div>
    </div>
  </template>

<script>
import { ref, onMounted } from 'vue';
import { ElCard, ElTag, ElDivider } from 'element-plus';

export default {
name: 'InstitutionInfo',
components: {
    ElCard,
    ElTag,
    ElDivider
},

setup() {
    const institution = ref({
        name: '北京航空航天大学',
        description: '好'
      });
  
      const scholars = ref([
        { name: '张三', fields: '计算机科学', citations: 1500 },
        { name: '李四', fields: '人工智能', citations: 2000 },
        { name: '王五', fields: '生物医学', citations: 950 },
        { name: '赵六', fields: '物理学', citations: 1200 },
        { name: '钱七', fields: '数据科学', citations: 1800 },
        { name: 'aaa', fields: '计算机科学', citations: 1500 },
        { name: 'bbb', fields: '人工智能', citations: 2000 },
        { name: 'ccc', fields: '生物医学', citations: 950 },
        { name: 'ddd', fields: '物理学', citations: 1200 },
        { name: 'eee', fields: '数据科学', citations: 1800 },
        { name: 'aaa', fields: '计算机科学', citations: 1500 },
        { name: 'bbb', fields: '人工智能', citations: 2000 },
        { name: 'ccc', fields: '生物医学', citations: 950 },
        { name: 'ddd', fields: '物理学', citations: 1200 },
        { name: 'eee', fields: '数据科学', citations: 1800 }
      ]);

    const getTagType = (index) => {
      const fieldColors = [
        'success',
        'warning',
        'info',
        'primary',
        'danger'
      ];

      return fieldColors[index % 5];
    };

    const fetchInstitutionData = async () => {
      try {
        // const res = await axios.get('/api/institution');
        // institution.value = res.data.institution;
        // scholars.value = res.data.scholars;
      } catch (error) {
        console.error('获取数据失败', error);
      }
    };

    // onMounted(fetchInstitutionData);

    return {
      institution,
      scholars,
      getTagType
    };
  }
};
</script>

<style scoped>
.institution-info {
padding: 20px;
}

.institution-card {
margin-bottom: 20px;
padding: 20px;
}

.institution-card h2 {
font-size: 24px;
margin-bottom: 10px;
}

.institution-card p {
color: #666;
}

.scholars-list {
display: grid;
grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
gap: 20px;
}

.scholar-item {
padding: 20px;
}

.scholar-header {
display: flex;
justify-content: space-between;
align-items: center;
}

.scholar-item h3 {
margin: 0;
font-size: 20px;
}

.scholar-item p {
margin-top: 10px;
}

.el-tag {
font-size: 14px;
padding: 3px 8px;
}

.el-divider {
margin-top: 10px;
margin-bottom: 10px;
}

.scholar-name {
font-size: 18px;
font-weight: bold;
text-decoration: none;
color: #409eff;
}

.scholar-name:hover {
text-decoration: underline;
}

.even-column {
background-color: #f9f9f9; /* 偶数列背景颜色 */
}

.odd-column {
background-color: #e6f7ff; /* 奇数列背景颜色 */
}
</style>
