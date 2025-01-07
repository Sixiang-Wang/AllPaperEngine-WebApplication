<template >
  <div class="login-wrap">
    <div v-if="showLogin">
      <div class="ms-title">AllPaper Engine后台管理登录</div>
      <div  class="ms-login">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
          <el-form-item prop="admin">
            <el-input v-model="ruleForm.admin" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input type="password" v-model="ruleForm.password" placeholder="密码"></el-input>
          </el-form-item>
          <div class="login-btn">
            <el-button type="primary" @click="submitForm">登录</el-button>
          </div>
        </el-form>
        <p class="register" @click="showRegister = true;showLogin = false">
          申请成为管理员?
        </p>
      </div>
    </div>
    <div v-if="showRegister">
      <div class="ms-title">申请成为AllPaper Engine后台管理员</div>
      <div class="ms-register">
        <el-form :model="registerForm" :rules="rules" ref="ruleForm">
          <el-form-item prop="admin">
            <el-input v-model="registerForm.admin" placeholder="注册用户名"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input type="password" v-model="registerForm.password" placeholder="密码"></el-input>
          </el-form-item>
          <div class="register-btn">
            <el-button type="primary" @click="submitRegister">申请注册</el-button>
            <el-button type="danger" @click="showRegister=false;showLogin=true">退出</el-button>
          </div>
        </el-form>
      </div>

    </div>
  </div>

</template>

<script>
import {mixin} from '../mixins/index'
import {getPassword, login, register} from '../api/index'
import { Md5Utils } from '@/api/md5Util'

export default {
  mixins: [mixin],
  data: function () {
    return {
      ruleForm: {
        admin: '', // 最后调成空
        password: ''
      },
      registerForm: {
        admin: '', // 最后调成空
        password: ''
      },
      rules: {
        admin: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ]
      },
      showRegister: false,
      showLogin: true
    }
  },
  methods: {
    submitForm () {
      getPassword(this.ruleForm.admin)
        .then((res) => {
          if (res.code === -2) {
            this.notify('用户名不存在', 'error')
            return
          }
          let encryptedPassword = res.hashedPassword
          const isValid = Md5Utils.verify(this.ruleForm.password, encryptedPassword)
          if (isValid) {
            localStorage.setItem('admin', this.ruleForm.admin)
            this.$router.push('/Info')
            this.notify('登录成功', 'success')
          } else {
            this.notify('密码错误', 'error')
          }
        })
      // login(this.ruleForm.admin, this.ruleForm.password)
      //   .then((res) => {
      //     if (res.code === 1) {
      //       localStorage.setItem('admin', this.ruleForm.admin)
      //       this.$router.push('/Info')
      //       this.notify('登录成功', 'success')
      //     } else if (res.code === -2) {
      //       this.notify('用户名不存在', 'error')
      //     } else if (res.code === -1) {
      //       this.notify('密码错误', 'error')
      //     } else {
      //       this.notify('登录失败', 'error')
      //     }
      //   })
    },
    submitRegister () {
      register(this.registerForm.admin, this.registerForm.password)
        .then((res) => {
          if (res.code === 1) {
            this.notify('已申请,等待审批', 'success')
          } else if (res.code === -2) {
            this.notify('用户名已存在', 'error')
          } else {
            this.notify('注册失败,网络错误', 'error')
          }
        })
    }
  }
}
</script>

<style scoped>

.register-btn {
  text-align: center;
}

.ms-register {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 300px;
  height: 150px;
  margin-left: -190px;
  margin-top: -150px;
  padding: 40px;
  border-radius: 5px;
  background: #fff;
}

.register {
  color: #003fb8;
  margin-top: 12px;
  text-align: right; /* 右对齐 */
  text-decoration: underline; /* 下划线 */
  cursor: pointer; /* 鼠标悬停时显示为手形 */
  transition: color 0.3s ease, background-color 0.3s ease; /* 平滑过渡效果 */
}

.register:hover {
  color: rgba(0, 89, 255, 0.89); /* 悬停时文字高亮颜色 */
}

.login-wrap {
  position: relative;
  background: url("../assets/img/background.jpg");
  background-attachment: fixed;
  background-position: center;
  background-size: cover;
  width: 100%;
  height: 100%;
}

.ms-title {
  position: absolute;
  top: 50%;
  width: 100%;
  margin-top: -230px;
  text-align: center;
  font-size: 30px;
  font-weight: 600;
  color: #ffffff;
}

.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 300px;
  height: 170px;
  margin-left: -190px;
  margin-top: -150px;
  padding: 40px;
  border-radius: 5px;
  background: #fff;
}

.login-btn {
  text-align: center;
}

.login-btn button {
  width: 100%;
  height: 36px;
}
</style>
