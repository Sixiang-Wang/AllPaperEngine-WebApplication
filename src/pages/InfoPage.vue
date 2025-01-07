<template>
  <div class="dashboard-container">
    <el-row :gutter="20" style="margin-bottom:25px">
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ userCount }}</div>
              <div>用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ authCount }}</div>
              <div>学者申请数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ claimCount }}</div>
              <div>论文申领数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ complaintCount }}</div>
              <div>投诉总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" class="mgb20"></el-row>
    <el-row :gutter="20" class="mgb20">
      <el-col :span="12">
        <h3 class="mgb20">用户专业领域</h3>
        <div class="chart-container">
          <ve-pie :data="userField"></ve-pie>
        </div>
      </el-col>
      <el-col :span="12">
        <h3 class="mgb20">用户职位</h3>
        <div class="chart-container">
          <ve-pie :data="userProfession"></ve-pie>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {allAuthentication, allClaimUnavailable, allComplaint, getAllUser, getUserCount} from '../api/index'

export default {
  data () {
    return {
      activities: [
        {
          content: 'Event start',
          timestamp: '2018-04-15'
        },
        {
          content: 'Approved',
          timestamp: '2018-04-13'
        },
        {
          content: 'Success',
          timestamp: '2018-04-11'
        }
      ],
      userCount: 0, // 用户总数
      complaintCount: 0, // 歌曲总数
      authCount: 0, // 歌手数量
      claimCount: 0, // 歌单数量
      user: [], // 所有用户
      options: {
        color: ['#87cefa', '#ffc0cb']
      },
      options1: {
        color: ['yellow']
      },
      userField: { // 按性别分类的歌手数
        columns: ['领域', '总数'],
        rows: [
        ]
      },
      userProfession: { // 按性别分类的歌手数
        columns: ['职位', '总数'],
        rows: [
        ]
      }
    }
  },
  created () {

  },
  mounted () {
    this.getUser()
    this.getAuthentication()
    this.getClaim()
    this.getComplaint()
  },
  methods: {
    getUser () {
      getAllUser().then(res => {
        res = res.userList
        console.log(res)
        let fieldCount = {}
        let professionCount = {}
        res.forEach((res) => {
          const field = res.academicField || '不明' // 默认将空值归类为“不明”
          if (fieldCount[field]) {
            fieldCount[field]++
          } else {
            fieldCount[field] = 1
          }
          const profession = res.profession || '不明' // 默认将空值归类为“不明”
          if (professionCount[profession]) {
            professionCount[profession]++
          } else {
            professionCount[profession] = 1
          }
        })

        this.userField.rows = Object.keys(fieldCount).map((field) => ({
          领域: field,
          总数: fieldCount[field]
        }))
        this.userProfession.rows = Object.keys(professionCount).map((profession) => ({
          职位: profession,
          总数: professionCount[profession]
        }))
      })

      getUserCount().then(res => {
        this.userCount = res.count
      })
    },
    getAuthentication () {
      allAuthentication().then(res => {
        res = res.authenticationList
        this.authCount = res.length
      })
    },
    getClaim () {
      allClaimUnavailable().then(res => {
        res = res.claimList
        this.claimCount = res.length
      })
    },
    getComplaint () {
      allComplaint().then(res => {
        res = res.complaints
        this.complaintCount = res.length
      })
    }
  }
}

</script>

<style scoped>
/* 确保页面容器充满视口，避免滚动条 */
.dashboard-container {
  min-height: 100vh; /* 占满视口高度 */
  overflow: hidden; /* 隐藏溢出的滚动条 */
  box-sizing: border-box;
  padding: 20px; /* 内边距，避免内容贴边 */
}

.chart-container {
  background-color: white;
  padding: 10px;
  height: 370px; /* 确保固定的高度，避免内容溢出 */
}

.grid-content {
  display: flex;
  align-items: center;
  height: 60px;
}

.grid-cont-center {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: darkgray;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.mgb20 {
  margin-bottom: 20px;
}

</style>
