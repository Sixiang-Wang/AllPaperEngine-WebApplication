<script setup>
import {onMounted, ref} from "vue";
import defaultAvatar from "@/assets/image/user.gif";
import {Comment} from "@element-plus/icons-vue";
import * as cookieUtil from "@/utils/cookie.js";
import * as httpUtil from "@/api/http.js";
import {ElMessage} from "element-plus";
const props = defineProps({
  userId: String,
  commentIndex: String,
  userName: String,
  date: String,
  workId: String,
  commentId: String,
  likes: 0
})
const avatar = ref({
  defaultAvatar: defaultAvatar,
  url : defaultAvatar
})
const ifMine = ref(false);
onMounted(async()=>{
  const token = cookieUtil.getCookie("token");
  console.log(props.userId);
  const res = await httpUtil.get('/user/validation',{
    userId: props.userId
  },{ Authorization: token});
  console.log(res.data);
  ifMine.value = res.data.msg === 'equal';

  //处理是否已经点赞
  const res2 = await httpUtil.get('/comment/ifLiked',{
    commentId: props.commentId
  },{Authorization: cookieUtil.getCookie("token")});
  console.log(res2.data.val);
  if(res2.data.val === 1){
    thumbState.value = true;
  }else if(res2.data.val === 0){
    thumbState.value = false;
  }
})
const deleteVisible = ref(false);
const deleteComment = async()=>{
  try{
    const res = await httpUtil.get('/comment/delete',{
      workId: props.workId,
      commentIndex: props.commentIndex
    },{ Authorization: cookieUtil.getCookie("token")});
    if(res.data.msg === 'delete success'){
      ElMessage.success("删除成功！");
      setTimeout(()=>{
        location.reload();
      },500);
    }else{
      ElMessage.error("删除失败；请稍后再试");
    }
  }catch (e){
    console.error(e);
  }
}
const thumbState = ref(false);
const changeDeleteVisible = ()=>{
  deleteVisible.value = !deleteVisible.value;
}
const currentLikes = ref(props.likes);
const handleLike = async()=>{
  try{
    const res = await httpUtil.get('/comment/like',{
      commentId: props.commentId
    },{Authorization: cookieUtil.getCookie("token")})
    console.log(res.data);
    if(res.data.msg === 'cancel success'){
      ElMessage.success("已取消点赞");
      currentLikes.value--;
      thumbState.value = false;
    }else if(res.data.msg === 'like success'){
      ElMessage.success("点赞成功！");
      currentLikes.value++;
      thumbState.value = true;
    }else{
      ElMessage.error("请稍后再试");
    }
  }catch (e){
    console.error(e);
  }
}
</script>

<template>
  <div class="comment-first-line">
    <el-avatar :src="avatar.url" ></el-avatar>
    <div style="display: flex; flex-direction: column;margin-left: 2%">
      <div style="margin-left: 2%;  color: #0f3357;  font-size: 16px;font-weight: normal;">{{ userName }}</div>
      <div style="font-weight: normal;  font-size: 15px;">{{ date }}</div>
    </div>
  </div>
  <div class="comment-second-line">
    {{commentIndex}}
  </div>
  <div class="comment-third-line">
    <img src="@/assets/svg/liked.svg" @click="handleLike" style="width: 20px" v-if="thumbState"/>
    <img src="@/assets/svg/thumb.svg" @click="handleLike" style="width: 20px" v-else/>
    <span style="font-weight: lighter;font-size: 16px;margin-right: 2%">{{ currentLikes }}</span>
    <img src="@/assets/svg/dislike.svg" style="width: 16px;margin-right: 2%" @click="ElMessage.success('踩你嘛呢，奖励你踩提莫的蘑菇')"/>
<!--    <img src="@/assets/svg/comment.svg" style="width: 18px;margin-right: 2%"/>-->
    <img src="@/assets/svg/delete.svg" style="width: 18px;margin-right: 2%" v-if="ifMine" @click="changeDeleteVisible"/>
  </div>
  <el-dialog v-model="deleteVisible" title="提示" width="500" align-center>
    <span style="font-weight: normal">您确定要删除该评论吗？</span>
    <template #footer>
      <el-button @click="changeDeleteVisible">返回</el-button>
      <el-button @click="deleteComment" type="primary">确认删除</el-button>
    </template>
  </el-dialog>
  <el-divider/>
</template>

<style scoped>
.comment-first-line{
  display: flex;
  flex-direction: row;
  margin-left: 3%;
}
.comment-second-line{
  display: flex;
  align-items: center;
  margin-left: 10%;
  font-size: 16px;
  font-weight: normal;
  color: black;
}
.comment-third-line{
  display: flex;
  align-items: center;
  margin-left: 10%;
  margin-top: 1%;
}
</style>