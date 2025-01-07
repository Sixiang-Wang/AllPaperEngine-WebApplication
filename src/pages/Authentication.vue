<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="mini" @click="delAll">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="搜索认证信息" class="handle-input"></el-input>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width:100%" height="680px" :data="data"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"></el-table-column>

      <el-table-column  prop="name" label="用户名" width="120" align="center"></el-table-column>
      <el-table-column prop="nameReal" label="真实姓名" width="180" align="center"></el-table-column>
      <el-table-column prop="workplace" label="工作单位" width="180" align="center"></el-table-column>
      <el-table-column prop="authorId" label="学者ID" width="180" align="center"></el-table-column>
      <el-table-column prop="authorName" label="学者名字" width="180" align="center"></el-table-column>
      <el-table-column prop="field" label="研究领域" width="180" align="center"></el-table-column>
      <el-table-column prop="mail" label="工作邮箱" min-width="130" align="center"></el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">拒绝</el-button>
          <br>
          <el-button size="mini" type="primary" @click="agreeRow(scope.row.id,scope.row.userid,scope.row.nameReal,scope.row.authorId,scope.row.authorName)">同意</el-button>
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
    <el-dialog title="拒绝学者认证" :visible.sync="delVisible" width="300px" center>
      <div align="center">你真的要拒绝他吗？</div>
      <span slot="footer">
        <el-button size="mini" type="danger" @click="deleteRow">拒绝!</el-button>
        <el-button size="mini" @click="delVisible = false">再想想</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {mixin} from '../mixins/index'
import '@/assets/js/iconfont.js'
import {
  allAuthentication,
  deleteAuthentication,
  getUserById, setUserNameReal,
  setUserRole
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
          if (item.name.includes(this.select_word) || item.nameReal.includes(this.select_word) || item.workplace.includes(this.select_word) || item.field.includes(this.select_word)) {
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
      allAuthentication().then(res => {
        res = res.authenticationList
        Promise.all(
          res.map(async item => {
            try {
              const userInfo = await getUserById(item.userid)
              item.name = userInfo.user.name
            } catch (error) {
              console.error(`Error fetching user data for userId ${item.userId}:`, error)
              item.name = 'Unknown'
            }
            return item
          })
        ).then(updatedRes => {
          this.tempData = updatedRes
          this.tableData = updatedRes

          const len = updatedRes.length
          this.currentPage = cur
          if (len / this.pageSize <= cur - 1) {
            this.currentPage = 1
          }
        }).catch(error => {
          console.error('Error processing authentication list:', error)
        })
      })
    },
    deleteRow (id) {
      this.idx = id
      deleteAuthentication(this.idx)
        .then(res => {
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
    agreeRow (id, userid, nameReal, authorId, authorName) {
      setUserRole(userid, 1, authorId).then(res => {
        if (res) {
          setUserNameReal(userid, nameReal, authorId, authorName)
          this.getData(this.currentPage)
          this.notify('已同意', 'success')
          deleteAuthentication(id).then(res => {
            if (res) {
              this.getData(this.currentPage)
            } else {
              this.notify('删除失败', 'error')
            }
          })
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

.handle-input {
  width: 300px;
  display: inline-block;
}

.pagination {
  display: flex;
  justify-content: center;
}
</style>
