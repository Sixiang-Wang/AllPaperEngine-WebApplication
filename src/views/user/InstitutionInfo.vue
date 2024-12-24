<template>
    <div class="institution-info">
      <el-card class="institution-card">
        <h2>{{ institution.displayName }}</h2>
        <p>{{ institution.description }}</p>
      </el-card>
  
      <div class="scholars-list">
        <el-card
          v-for="(scholar, index) in scholars"
          :key="scholar.author.displayName"
          :class="['scholar-item']"
          shadow="hover"
        >
          <div class="scholar-header">
            <router-link :to="'/scholar/' + scholar.author.displayName" class="scholar-name">
              {{ scholar.author.displayName }}
            </router-link>
            <el-tag :type="getTagType(index)" size="small">{{ scholar.author.fields }}</el-tag>
          </div>
          <p><strong>引用次数：</strong>{{ scholar.author.citedByCount }}</p>
          <el-divider></el-divider>
        </el-card>
      </div>
    </div>
  </template>

<script>
import httpUtil from "@/api/http.js";
import { useRoute } from 'vue-router';
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
    const route = useRoute();
    const institution = ref({
        displayName: '北京航空航天大学',
      });
  
    const scholars = ref([
      {
        author: { displayName: '张三', fields: '计算机科学', citedByCount: 1500 },
        institution: { }
      },
      {
        author: { displayName: '李四', fields: '人工智能', citedByCount: 2000 },
        institution: { }
      },
      {
        author: { displayName: '王五', fields: '生物医学', citedByCount: 950 },
        institution: { }
      },
      {
        author: { displayName: '赵六', fields: '物理学', citedByCount: 1200 },
        institution: { }
      },
      {
        author: { displayName: '钱七', fields: '数据科学', citedByCount: 1800 },
        institution: { }
      },
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

    const fetchInstitutionData = async (id) => {
      console.log('Fetching institution data...', id);
      try {
        const response = await httpUtil.get(`institution/getInstitutionById?id=${id}`);
        institution.value = response.data.getInstitutionById;
        console.log('Institution data:', institution.value);
        const response2 = await httpUtil.get(`institution/getAuthorByInstitutionId?id=${id}`);
        console.log('Response2:', response2);
        scholars.value = response2.data.authorList;
        console.log('Scholars:', scholars.value);
      } catch (error) {
        console.error('Error fetching institution data:', error);
      }
    };

    onMounted(() => {
      const institutionId = route.query.id;
      fetchInstitutionData(institutionId);
    });

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
  background: linear-gradient(45deg, #f3f4f6, #e8e9ec);
  border-radius: 8px;
}

.institution-card {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.institution-card h2 {
  font-size: 26px;
  color: #333;
  font-weight: bold;
  margin-bottom: 10px;
}

.institution-card p {
  color: #666;
}

.scholars-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.scholar-item {
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease-in-out;
}

.scholar-item:hover {
  transform: translateY(-5px);
}

.scholar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.scholar-item h3 {
  font-size: 22px;
  color: #409eff;
  font-weight: bold;
  margin: 0;
}

.scholar-item p {
  margin-top: 10px;
  font-size: 16px;
}

.scholar-name {
  font-size: 18px;
  color: #409eff;
  text-decoration: none;
  font-weight: bold;
}

.scholar-name:hover {
  text-decoration: underline;
}

.scholar-stats {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #666;
}

.citation-icon {
  color: #409eff;
}

.el-tag {
  font-size: 14px;
  padding: 3px 8px;
}

.el-divider {
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>
