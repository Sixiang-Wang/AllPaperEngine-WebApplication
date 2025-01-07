<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-input v-model="select_word" size="mini" placeholder="搜索举报对象" class="handle-input"></el-input>
      </div>
    </div>
    <el-table size="mini"  border style="width:100%" height="680px" :data="data">
      <el-table-column prop="username" label="举报人" width="110" align="center">
      </el-table-column>
      <el-table-column prop="nameReal" label="举报人实名" width="110" align="center">
      </el-table-column>
      <el-table-column prop="title" label="举报论文" min-width="100" align="center">
      </el-table-column>
      <el-table-column prop="authList" label="论文作者" min-width="130" align="center">
        <template #default="scope">
          <div>
            <!-- 用逗号分隔作者列表 -->
            {{ scope.row.authList.join(", ") }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="举报原因" align="center" width="150">
      </el-table-column>
      <el-table-column  prop="addition" label="举报具体说明"  align="center" min-width="130">
      </el-table-column>

      <el-table-column label="操作" width="100" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleRow(scope.row)">同意</el-button>
          <br>
          <el-button size="mini"  @click="deleteRow(scope.row.id)">忽略</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        background
        layout="total,prev,pager,next"
        :current-page.sync="currentPage"
        :page-size="pageSize"
        :total="tableData.length"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
    <el-dialog title="同意举报" :visible.sync="delVisible" width="300px" center>
      <div align="center">同意举报将会解除论文申领，是否确定？</div>
      <span slot="footer">
                <el-button size="mini" @click="delVisible = false">取消</el-button>
                <el-button size="mini" @click="deleteRow">确定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import {mixin} from '../mixins/index'
import '@/assets/js/iconfont.js'
import {
  allComplaint, dealComplaint
} from '../api/index'

export default {
  mixins: [mixin],
  data () {
    return {
      singerId: '', // 歌手id
      singerName: '', // 歌手名
      centerDialogVisible: false, // 添加弹窗是否显示
      editVisible: false, // 编辑弹窗是否显示
      delVisible: false, // 删除弹窗是否显示
      complaintId: '',
      userId: '',
      form: { // 编辑框
        id: '',
        name: '',
        introduction: '',
        lyric: '',
        style: ''
      },
      tableData: [],
      tempData: [],
      select_word: '',
      pageSize: 7, // 分页每页大小
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
          if (item.username.includes(this.select_word) || item.nameReal.includes(this.select_word) || item.title.includes(this.select_word)) {
            this.tableData.push(item)
          }
        }
      }
    }
  },
  created () {
    this.getData()
  },
  destroyed () {
    this.$store.commit('setIsPlay', false)
  },
  methods: {
    /* 获取当前页 */
    handleCurrentChange (val) {
      this.currentPage = val
    },
    /* 查询 */
    getData (cur) {
      if (!cur) {
        cur = 1
      }
      this.tempData = []
      this.tableData = []
      allComplaint().then(async res => {
        res = res.complaints
        this.tempData = res
        this.tableData = res
        let len = res.length
        this.currentPage = cur
        if (len / this.pageSize <= cur - 1) {
          this.currentPage = 1
        }
      })
    },
    handleRow (row) {
      dealComplaint(row.id, 1).then(tmp => {
        if (tmp) {
          this.getData(this.currentPage)
          this.notify('同意举报成功')
        } else {
          this.notify('同意举报失败')
        }
      })
    },
    deleteRow (id) {
      this.idx = id
      dealComplaint(id, 0)
        .then(res => {
          if (res) {
            this.getData(this.currentPage)
            this.notify('已忽略', 'success')
          } else {
            this.notify('此举报无法忽略', 'error')
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
