<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-input v-model="select_word" size="mini" placeholder="筛选相关用户" class="handle-input"></el-input>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width:100%" height="680px" :data="data"
    >
      <el-table-column label="用户图片" width="110" align="center">
        <template slot-scope="scope">
          <div class="user-img">
            <img :src="getUrl(scope.row.avatar)" style="width:100%"/>
          </div>
          <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatorUpload"
                     :on-success="handleAvatorSuccess">
            <el-button size="mini">更新图片</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="平台名称" min-width="150" align="center"></el-table-column>
      <el-table-column prop="nameReal" label="真实名字" min-width="150" align="center"></el-table-column>
      <el-table-column prop="mail" label="电子邮箱" width="250" align="center"></el-table-column>
      <el-table-column prop="phone" label="手机号" width="150" align="center"></el-table-column>
      <el-table-column prop="academicField" label="学术领域" width="220" align="center"></el-table-column>
      <el-table-column prop="company" label="就职公司/学校" width="130" align="center"></el-table-column>
      <el-table-column prop="role" label="用户权限" width="120" align="center">
        <template #default="scope">
          <span>
            {{ scope.row.role === 1 ? "论文作者" : "普通用户" }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template slot-scope="scope">
          <el-button size="mini"  @click="handleMail(scope.row)">骚扰用户</el-button>
          <br>
          <el-button size="mini" @click="handleEdit(scope.row)">重置密码</el-button>
          <br>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除用户</el-button>
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

    <el-dialog title="更改密码" :visible.sync="editVisible" width="400px" center>
      <el-form :model="form" ref="form" label-width="80px" :rules="rules">
        <el-form-item style="margin-left: 15px;margin-right: 15px" label="平台名称:" size="mini">
          {{form.name}}
        </el-form-item>
        <el-form-item prop="password" label="密码" size="mini">
          <el-input type="password" v-model="form.password" placeholder="请输入新密码"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
                <el-button size="mini" @click="editVisible = false">取消</el-button>
                <el-button size="mini" @click="editPassword">确定</el-button>
            </span>
    </el-dialog>

    <el-dialog title="删除用户" :visible.sync="delVisible" width="300px" center>
      <div align="center">删除不可恢复，是否确定删除？</div>
      <span slot="footer">
                <el-button size="mini" @click="delVisible = false">取消</el-button>
                <el-button size="mini" @click="deleteRow">确定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import {getAllUser, setUser, delUser, sendMail, updatePassword} from '../api/index'
import {mixin} from '../mixins/index'

export default {
  mixins: [mixin],
  data () {
    return {
      centerDialogVisible: false, // 添加弹窗是否显示
      editVisible: false, // 编辑弹窗是否显示
      delVisible: false, // 删除弹窗是否显示
      form: { // 编辑框
        id: '',
        name: '',
        password: ''
      },
      tableData: [],
      tempData: [],
      select_word: '',
      pageSize: 5, // 分页每页大小
      currentPage: 1, // 当前页
      idx: -1, // 当前选择项
      multipleSelection: [], // 哪些项已经打勾
      rules: {
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入昵称', trigger: 'blur'}
        ]
      }
    }
  },
  computed: {
    // 计算当前搜索结果表里的数据
    data () {
      return this.tableData.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  watch: {
    // 搜索框里面的内容发生变化的时候，搜索结果table列表的内容跟着它的内容发生变化
    select_word: function () {
      if (this.select_word === '') {
        this.tableData = this.tempData
      } else {
        this.tableData = []
        for (let item of this.tempData) {
          if (item.username.includes(this.select_word)) {
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
    // 获取当前页
    handleCurrentChange (val) {
      this.currentPage = val
    },
    getData (tmp) {
      if (!tmp) {
        tmp = 1
      }
      this.tempData = []
      this.tableData = []
      getAllUser().then(res => {
        res = res.userList.map(user => ({
          ...user,
          id: user.userid,
          userid: undefined
        }))

        this.tempData = res
        this.tableData = res
        let len = res.length
        this.currentPage = tmp
        if (len / this.pageSize <= tmp - 1) {
          this.currentPage = 1
        }
      })
    },
    // 添加用户
    addUser () {
      this.$refs['registerForm'].validate(valid => {
        if (valid) {
          let d = this.registerForm.birth
          let datetime
          if (d) {
            datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
          }
          let params = new URLSearchParams()
          params.append('username', this.registerForm.username)
          params.append('password', this.registerForm.password)
          params.append('name', this.registerForm.name)
          params.append('sex', this.registerForm.sex)
          params.append('phoneNum', this.registerForm.phoneNum)
          params.append('email', this.registerForm.email)
          params.append('birth', datetime)
          params.append('introduction', this.registerForm.introduction)
          params.append('location', this.registerForm.location)
          params.append('profilePicture', '/img/Pic/default_avatar.jpg')
          setUser(params)
            .then(res => {
              if (res.code === 1) {
                this.getData(this.currentPage)
                this.notify('添加成功', 'success')
              } else {
                this.notify(`添加失败:${res.msg}`, 'error')
              }
            })
            .catch(err => {
              console.log(err)
            })
          this.centerDialogVisible = false
        }
      })
    },
    handleMail (row) {
      sendMail(row.mail)
        .then(res => {
          this.notify(`向${row.mail}发送了验证码${res.verifyCode}`)
        })
    },
    // 弹出编辑页面
    handleEdit (row) {
      this.editVisible = true
      this.form = {
        id: row.userid,
        name: row.name
      }
    },
    editPassword () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          updatePassword(this.form.id, this.form.password)
            .then(res => {
              if (res.code === 200) {
                this.getData(this.currentPage)
                this.notify('修改成功', 'success')
              } else {
                this.notify(`修改失败:${res.msg}`, 'error')
              }
            })
            .catch(err => {
              console.log(err)
            })
          this.editVisible = false
        }
      })
    },
    // 更新图片
    uploadUrl (id) {
      return `${this.$store.state.HOST}/user/updateUserAvatar?userid=${id}`
    },
    // 删除一名用户
    deleteRow () {
      delUser(this.idx)
        .then(res => {
          if (res.code === 200) {
            this.getData(this.currentPage)
            this.notify('删除成功', 'success')
          } else {
            this.notify(`删除失败:${res.msg}`, 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      this.delVisible = false
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 5px;
}

.user-img {
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
</style>
