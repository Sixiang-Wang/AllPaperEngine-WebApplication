export const mixin = {
  methods: {
    // 提示信息
    notify (title, type) {
      this.$notify({
        title: title,
        type: type
      })
    },
    // 根据相对地址获取绝对地址
    getUrl (url) {
      return `${this.$store.state.HOST}${url}`
    },
    // 上传图片之前的校验
    beforeAvatorUpload (file) {
      const isJPG = (file.type === 'image/jpg') || (file.type === 'image/jpeg') || (file.type === 'image/png')
      if (!isJPG) {
        this.$message.error('上传图片只能是jpg或png格式')
        return false
      }
      const isLt5M = (file.size / 1024 / 1024) < 5
      if (!isLt5M) {
        this.$message.error('上传图片大小不能超过5MB')
        return false
      }
      return true
    },
    // 上传图片成功之后要做的工作
    handleAvatorSuccess (res) {
      let _this = this
      console.log(res)
      if (res.code === 200) {
        if (this.currentPage) {
          _this.getData(this.currentPage)
        } else {
          _this.getData()
        }
        _this.$notify({
          title: '上传成功',
          type: 'success'
        })
      } else {
        _this.$notify({
          title: `上传失败: ${res.msg}`,
          type: 'error'
        })
      }
    },
    // 弹出删除窗口
    handleDelete (id) {
      this.idx = id
      this.delVisible = true
    },
    // 把已经选择的项赋值给multipleSelection
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    // 批量删除已经选择的项
    delAll () {
      for (let item of this.multipleSelection) {
        this.handleDelete(item.id)
        this.deleteRow(item.id)
      }
      this.multipleSelection = []
    }
  }
}
