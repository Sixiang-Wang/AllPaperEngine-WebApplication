<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="mini" @click="delAll">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="搜索认领信息" class="handle-input"></el-input>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width:100%" height="680px" :data="data"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"></el-table-column>

      <el-table-column  prop="name" label="用户名" width="120" align="center"></el-table-column>
      <el-table-column prop="nameReal" label="真实姓名" width="180" align="center"></el-table-column>
      <el-table-column prop="title" label="论文标题" min-width="130" align="center"></el-table-column>
      <el-table-column prop="arther" label="论文作者" min-width="130" align="center">
        <template #default="scope">
          <div>
            <!-- 用逗号分隔作者列表 -->
            {{ scope.row.authorList.join(", ") }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="deleteRow(scope.row.id)">拒绝</el-button>
          <br>
          <el-button size="mini" type="primary" @click="agreeRow(scope.row.id,scope.row.userid)">同意</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
          :key="paginationKey"
          background
          layout="total,prev,pager,next"
          :current-page.sync="currentPage"
          :page-size="pageSize"
          :total="tableData.length"
          @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {mixin} from '../mixins/index'
import '@/assets/js/iconfont.js'
import {
  ableClaim,
  allClaimUnavailable,
  deleteClaim
} from '../api/index'

export default {
  mixins: [mixin],
  data () {
    return {
      userId: '',
      userName: '',
      delVisible: false, // 删除弹窗是否显示
      tableData: [],
      tempData: [],
      select_word: '',
      paginationKey: 0,
      pageSize: 5, // 分页每页大小
      currentPage: 1, // 当前页
      idx: -1, // 当前选择项
      multipleSelection: [] // 哪些项已经打勾
    }
  },
  computed: {
    /* 计算当前搜索结果表里的数据 */
    data () {
      return this.tableData.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  watch: {
    /* 搜索框里面的内容发生变化的时候，搜索结果table列表的内容跟着它的内容发生变化 */
    select_word: function () {
      if (this.select_word === '') {
        this.tableData = this.tempData
      } else {
        this.tableData = []
        for (let item of this.tempData) {
          if (item.name.includes(this.select_word) || item.nameReal.includes(this.select_word) || item.title.includes(this.select_word)) {
            this.tableData.push(item)
          }
        }
      }
    }
  },
  created () {
    this.getData()
  },
  methods: {
    /* 获取当前页 */
    handleCurrentChange (val) {
      this.currentPage = val
    },
    /* 查询 */
    getData (cur) {
      if (!cur) cur = 1
      this.tempData = []
      this.tableData = []
      allClaimUnavailable().then(res => {
        res = res.claimList
        this.tableData = res
        this.tempData = res
      })
    },
    deleteRow (id) {
      this.idx = id
      deleteClaim(this.idx)
        .then(res => {
          this.notify(res)
          if (res) {
            this.getData(this.currentPage)
            this.notify('删除成功', 'success')
          } else {
            this.notify('删除失败', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      this.delVisible = false
    },
    agreeRow (id, userid) {
      ableClaim(id).then(res => {
        if (res) {
          this.getData(this.currentPage)
          this.notify('已同意', 'success')
        } else {
          this.notify('审核失败', 'error')
        }
      })
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 5px;
}

.song-img {
  width: 100%;
  height: 80px;
  border-radius: 5px;
  margin-bottom: 5px;
  overflow: hidden;
}

.handle-input {
  width: 300px;
  display: inline-block;
}

.pagination {
  display: flex;
  justify-content: center;
}

.play {
  position: absolute;
  z-index: 100;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  top: 18px;
  left: 15px;
}

.icon {
  width: 2em;
  height: 2em;
  color: white;
  fill: currentColor;
  overflow: hidden;
}

.flexible-column {
  min-width: 200px; /* 设置最小宽度为 150px */
  width: auto; /* 允许列宽自动调整 */
}
.song-inf-crumbs{
  margin-bottom: 5px;
}
</style>
