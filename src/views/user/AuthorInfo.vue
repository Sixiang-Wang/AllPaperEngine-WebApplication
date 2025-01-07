<template>
  <el-card class="author-details" :body-style="{ padding: '20px' }">
    <div class="author-header" style="display: flex; justify-content: space-between; align-items: center;">
    <div class="author-left" style="display: flex; align-items: center;">
<!--      <el-avatar :src="avatar" :size="120" />-->
      <el-avatar :size="120" class="avatarClass">
        <span>{{ author.name.charAt(0).toUpperCase() }}</span>
      </el-avatar>
      <div class="author-info" style="margin-left: 20px;">
        <h2>{{ author.name }}</h2>
        <p class="description">{{ author.description }}</p>
        <el-button type="primary" @click="followAuthor" class="follow-button">关注</el-button>
      </div>
    </div>

    <div class="author-right" style="flex: 1; text-align: left; padding-left: 35%;">
      <p><strong>引用次数：{{ author.citedByCount }}</strong></p>
      <p><strong>成果总数：{{ author.worksCount }}</strong></p>
      <p><strong>H指数：{{ author.hnumber }}</strong></p>
      <p><strong>高影响力发文数：{{ author.highQualityWorkCount }}</strong></p>
      <p><strong>第一作者发文数：{{ author.firstPublishCount }}</strong></p>
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
            <el-table-column prop="title" style="min-width: 70%">
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
import router from "@/router/index.js";
import {ref, onMounted, computed} from 'vue';
import avatar from '@/assets/image/scholarHead.jpg';

export default {

  setup() {
    const route = useRoute();
    const publications = ref([]);

    const author = ref({
      id: '',
      name: '',
      description: 'IScholar官方认证学者',
      // avatar: '',
      citedByCount: 0,
      worksCount: 0,
      publications: [
        { id: 1, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
        { id: 2, title: 'Ciallo～(∠・ω< )⌒☆', description: '柚子厨蒸鹅心', date: '2024-03-22', cited: 20 },
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

        const response2 = await httpUtil.get(`/author/getFirstPublishWorkCountByAuthorId?authorId=${id}`);
        author.value.firstPublishCount = response2.data.getFirstPublishWorkCountByAuthorId;

        const response3 = await httpUtil.get(`/author/getHighQualityWorksCountByAuthorId?authorId=${id}`);
        author.value.highQualityWorkCount = response3.data.getHighQualityWorksCountByAuthorId;

        const response4 = await httpUtil.get(`/author/getHNumberByAuthorId?authorId=${id}`);
        console.log(response4)
        author.value.hnumber = response4.data.getHNumberByAuthorId;
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


    const viewPublication = (id) => {
      console.log(`Viewing publication with ID: ${id}`);
      router.push({path: '/paper', query: {id: id}})
    };

    return {
      author,
      publications,
      viewPublication,
      avatar
    };
  }
};
</script>

<style scoped>
.avatarClass {
  color: rgba(255, 255, 255, 0.86);
  background-color: #476ec8;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 60px; /* 正确的字体大小 */
  font-weight: bold; /* 加粗字体 */
  text-transform: uppercase; /* 转换为大写 */
}



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