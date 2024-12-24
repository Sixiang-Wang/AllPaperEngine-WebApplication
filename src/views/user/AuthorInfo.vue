<template>
  <el-card class="author-details" :body-style="{ padding: '20px' }">
    <div class="author-header" style="display: flex; justify-content: space-between; align-items: center;">
    <div class="author-left" style="display: flex; align-items: center;">
      <el-avatar :src="avatar" :size="120" />
      <div class="author-info" style="margin-left: 20px;">
        <h2>{{ author.name }}</h2>
        <p class="description">{{ author.description }}</p>
        <el-button type="primary" @click="followAuthor" class="follow-button">关注</el-button>
      </div>
    </div>

    <div class="author-right" style="flex: 1; text-align: center; padding-left: 40px;">
      <p><strong>引用次数：{{ author.citedByCount }}</strong></p>
      <p><strong>成果总数：{{ author.worksCount }}</strong></p>
    </div>
  </div>

    <el-divider />

    <div class="author-timeline">
      <h3>发表文章</h3>
      <el-timeline>
        <el-timeline-item
          v-for="(publication, index) in author.publications"
          :key="publication.id"
          :citedByCount="publication.citedByCount"
          :timestamp="publication.publicationDate"
          :color="index % 2 === 0 ? 'primary' : 'warning'"
          placement="top"
          size="large">
          <el-table :data="[publication]" style="width: 100%" :show-header="false">
            <el-table-column prop="title">
              <template #default="{ row }">
                <a @click="viewPublication(row.id)" class="publication-link">{{ row.title }}</a>
              </template>
            </el-table-column>
            <el-table-column prop="description" />
            <el-table-column>
              <template v-slot="scope">
                <span>引用次数: {{ scope.row.citedByCount }}</span>
              </template>
            </el-table-column>
            
          </el-table>
        </el-timeline-item>
      </el-timeline>
    </div>
  </el-card>
</template>

<script>
import httpUtil from "@/api/http.js";
import { useRoute } from 'vue-router';
import { ref, onMounted } from 'vue';

export default {
  setup() {
    const route = useRoute();
    const publications = ref([]);

    const author = ref({
      id: '114514',
      name: '王思翔',
      description: '全栈工程师',
      avatar: '',
      citedByCount: 10,
      worksCount: 20,
      publications: [
        { id: 1, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
        { id: 2, title: 'Ciallo～(∠・ω< )⌒☆', description: '柚子厨蒸鹅心', date: '2024-03-22', cited: 20 },
        { id: 3, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
        { id: 4, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
        { id: 5, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
        { id: 6, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
        { id: 7, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 20 }
      ],
  });

    const fetchAuthorDetails = async (id) => {
      try {
        const response = await httpUtil.get(`/author/getById?id=${id}`);
        const data = response.data.authors;
        console.log('data: ', data);
        author.value = {
          ...author.value,
          name: data.displayName,
          citedByCount: data.citedByCount,
          worksCount: data.worksCount,
        };
      } catch (error) {
        console.error('获取作者信息错误', error);
      }
    };

    const fetchAuthorPublications = async (id) => {
      try {
        const response = await httpUtil.get(`/author/getWorksByAuthorId?authorId=${id}`);
        author.value = {
          ...author.value,
          publications : response.data.getWorksByAuthorId,
        };
  
      } catch (error) {
        console.error('获取作者作品信息错误', error);
      }
    };

    onMounted(() => {
      const authorId = route.query.id;
      fetchAuthorDetails(authorId);
      fetchAuthorPublications(authorId);
    });

    return {
      author,
      publications,
    };
  }
};
</script>

<style scoped>
.author-details {
  max-width: 80%;
  margin: 20px auto;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
.author-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.author-left {
  display: flex;
  align-items: center;
}
.author-info h2 {
  margin: 0;
  font-size: 20px;
}
.author-info .description {
  color: #888;
}
.author-right p {
  margin: 5px 0;
  color: #333;
}
css
.author-right {
  flex: 1;
  text-align: center;
}
.follow-button {
  margin-top: 10px;
}
.author-publications {
  margin-top: 20px;
}
.publication-link {
  color: #409eff;
  cursor: pointer;
  text-decoration: underline;
}
.publication-link:hover {
  text-decoration: none;
}
.author-timeline {
  margin-top: 20px;
}
</style>