<template>
  <el-card class="author-details" :body-style="{ padding: '20px' }">
    <div class="author-header" style="display: flex; justify-content: space-between; align-items: center;">
    <div class="author-left" style="display: flex; align-items: center;">
      <el-avatar :src="avatar" :size="120" />
      <div class="author-info" style="margin-left: 20px;">
        <h2>{{ name }}</h2>
        <!-- <p class="description">{{ description }}</p> -->
        <p><span style="font-weight: lighter;">工作单位</span>: <span style="color: grey;">{{ workPlace }}</span></p>
        <el-button type="primary" @click="followAuthor" class="follow-button">关注</el-button>
      </div>
    </div>

    <div class="author-right" style="flex: 1; text-align: center; padding-left: 40px;">
      <p><strong>引用次数：{{ citedByCount }}</strong></p>
      <p><strong>成果总数：{{ worksCount }}</strong></p>
      <p>H指数：{{ H_index }}</p>
      <p>第一作者发文量：{{ firstAuthor }}</p>
      <p>高影响力论文发文量：{{ highInflu }}</p>
    </div>
  </div>

    <el-divider />

    <div class="author-timeline">
      <h3>发表文章</h3>
      <el-timeline>
        <el-timeline-item
          v-for="(publication, index) in author.publications"
          :key="publication.id"
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
            <!-- <el-table-column prop="description" /> -->
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
import axios from 'axios';
import { ref, onMounted } from 'vue';
import router from "@/router/index.js";
import { useRoute } from 'vue-router';

export default {
  created() {
    // 从路由查询参数中获取数据
    this.id = this.$route.query.id;
    this.name = this.$route.query.name;
    // 获取其他参数
    this.workPlace = this.$route.query.workPlace;
    this.area = this.$route.query.area;
    this.avatar = this.$route.query.avatar;
    this.citedByCount = this.$route.query.citedByCount;
    this.worksCount = this.$route.query.worksCount;
    this.H_index = this.$route.query.H_index;
    this.firstAuthor = this.$route.query.firstAuthor;
    this.highInflu = this.$route.query.highInflu;
  },
  setup() {
    const publications = ref([]);
    const route = useRoute();

    const fetchPublications = async () => {
      try {
        const res = await axios.get('/author/getWorksByAuthorId', {
          params: { authorId: route.query.id }
        });
        publications.value = res.data.getWorksByAuthorId || [];
        console.log(publications.value);
      } catch (error) {
        console.error('Error fetching publications:', error);
      }
    };

    onMounted(() => {
      fetchPublications();
    });

    const viewPublication = (id) => {
      console.log('Viewing publication with id:', id);
      // Add your logic to view the publication details
    };

    const followAuthor = () => {
      console.log('Following author:', name);
      // Add your logic to follow the author
    };

    return {
      publications,
      viewPublication,
      followAuthor
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
