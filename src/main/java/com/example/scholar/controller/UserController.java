package com.example.scholar.controller;
import com.example.scholar.config.annotation.TokenToUser;
import com.example.scholar.dao.UserMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.constant.CheckResult;
import com.example.scholar.domain.constant.R;
import com.example.scholar.domain.myenum.AcademicFieldType;
import com.example.scholar.dto.AddUserFavoriteDto;
import com.example.scholar.dto.LoginDto;
import com.example.scholar.dto.RegistDto;
import com.example.scholar.dto.TagDto;
import com.example.scholar.service.MessageService;
import com.example.scholar.service.UserService;
import com.example.scholar.service.UserTokenService;
import com.example.scholar.service.impl.FileService;
import com.example.scholar.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value ="/user")
public class UserController {
        @Resource
        private UserService userService;
        @Resource
        private UserTokenService userTokenService;
        @Resource
        private UserMapper userMapper;
        @Resource
        private MessageService messageService;
        @Resource
        private FileService fileService;
        @PostMapping(value = "/login")
        @ApiOperation("登录接口")
        public R login(@ApiParam(value="登录表单") @RequestBody LoginDto loginDto){
            try {
                HashMap<String, Object> res = userService.login(loginDto.getMail(), loginDto.getPassword());
                if("no such user".equals(res.get("msg"))|| "wrong password".equals(res.get("msg"))){
                    return R.ok((String) res.get("msg"));
                }else {
                    return Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(R.ok("login success").put("token", res.get("token"))).put("username", res.get("username"))).put("userId", res.get("userId"))).put("avatar",res.get("avatar"));
                }
            }catch (Exception e){
                return R.error(e.toString());
            }
        }

        @GetMapping(value="/preLogin")
        @ApiOperation("自动登录接口")
        public R preLogin(@TokenToUser User user){
            try{
                return Objects.requireNonNull(Objects.requireNonNull(R.ok("login success").
                                        put("username", user.getName()))
                                .put("userId", user.getUserid()))
                        .put("avatar",user.getAvatar());
            }catch (Exception e){
                return R.error(e.toString());
            }
        }

        @GetMapping(value="/validation")
        @ApiOperation("验证token与id是否相等（评论用）")
        public R tokenEqualId(@TokenToUser User user, @RequestParam("userId")int userId){
            try{
                int id = user.getUserid();
                return user.getUserid() == userId ? R.ok("equal") : R.ok("not equal");
            }catch (Exception e){
                return R.error(e.toString());
            }
        }

        @PostMapping(value = "/register")
        @ApiOperation("注册接口")
        public R register(@ApiParam(value="登录表单") @RequestBody RegistDto registDto) {
            String name = registDto.getName();
            String password = registDto.getPassword();
            String mail = registDto.getMail();
            try {
                HashMap<String, Object> res = userService.register(name, password, mail);
                if ("account already exists".equals(res.get("msg"))) {
                    return R.error("account already exists");
                } else if ("注册成功".equals(res.get("msg"))) {
                    messageService.welcomeMessage((Integer)res.get("userid"));
                    return R.ok("register success").put("userid", res.get("userid"));
                } else {
                    return R.error("register failed");
                }
            } catch (Exception e) {
                return R.error(e.toString());
            }
        }

    @GetMapping(value="/ifScholar")
    @ApiOperation("判断是否为scholar")
    public R ifScholar(@RequestParam("userId")int userId){
        try{
            User user1 = userMapper.selectUserById(userId);
            if(user1.getRole() == 1){
                return R.ok().put("judge",1);
            }else{
                return R.ok().put("judge",0);
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @PostMapping(value = "/setUserDetails")
    @ApiOperation("设置用户详细信息接口")
    public R setUserDetails(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String avatar,
            @RequestParam(required = false) LocalDate birthTime,
            @RequestParam(required = false) String biography,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) AcademicFieldType academicField,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) String phone) {
        try {
            // 验证 token 并获取 userId
            CheckResult checkResult = JwtUtils.validateJWT(token);
            if (!checkResult.isSuccess()) {
                return R.error("Token无效或已过期");
            }

            Claims claims = checkResult.getClaims();
            int userId = Integer.parseInt(claims.getId());  // 从 token 中获取用户ID

            // 使用 userService 更新用户详细信息
            HashMap<String, Object> resultMap = userService.setUserDetails(
                    userId, avatar, birthTime, biography, company, academicField, profession, phone);

            if ("详细信息更新成功".equals(resultMap.get("msg"))) {
                return R.ok("详细信息更新成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

        @PostMapping(value = "/updateUserName")
        @ApiOperation("修改用户名接口")
        public R updateUsername(
                @RequestHeader("Authorization") String token,  // 从请求头中获取JWT
                @RequestParam(required = false) String name)
        {
            try {
                // 解析 token 获取 userId
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }

                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId());  // 从 token 中获取用户ID

                HashMap<String, Object> resultMap = userService.updateUserName(userId, name);
                if ("用户名更新成功".equals(resultMap.get("msg"))) {
                    return R.ok("用户名更新成功");
                } else {
                    return R.error((String) resultMap.get("msg"));
                }
            } catch (Exception e) {
                return R.error(e.toString());
            }
        }

        @ApiOperation("修改用户头像头像接口")
        @RequestMapping(value = "/updateUserAvatar", method = RequestMethod.POST)
        public R updateUserAvatar(@RequestParam("file") MultipartFile profilePictureFile, @RequestParam("userid") int userid){
            if (profilePictureFile.isEmpty()) {
                return R.error("图片上传失败");
            }
            String oldAvatar = userMapper.getUserAvatar(String.valueOf(userid));

            String filePath = fileService.updateFile(profilePictureFile);
            userService.updateUserAvatar(userid,filePath);

            fileService.deleteFile(oldAvatar);
            return R.ok("成功上传");
        }

        @PostMapping(value = "/updateUserBirthTime")
        @ApiOperation("修改用户生日接口")
        public R updateUserBirthTime(
                @RequestHeader("Authorization") String token,  // 从请求头获取 JWT token
                @RequestParam(required = false) String birthTime) {

            try {
                // 验证 token 并获取 userId
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }

                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId


                LocalDate date = LocalDate.parse(birthTime);
                HashMap<String, Object> resultMap = userService.updateUserBirthTime(userId,date);
                if ("用户生日更新成功".equals(resultMap.get("msg"))) {
                    return R.ok("用户生日更新成功");
                } else {
                    return R.error((String) resultMap.get("msg"));  // 返回错误信息
                }
            } catch (Exception e) {
                return R.error(e.toString());
            }
        }

        @PostMapping(value = "/updateUserCompany")
        @ApiOperation("修改在职单位接口")
        public R updateUsercompany(@RequestHeader("Authorization") String token,
                                   @RequestParam(required = false) String company)
        {
            try {
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }
                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId

                HashMap<String, Object> resultMap = userService.updateUserCompany(userId, company);

                if ("在职单位更新成功".equals(resultMap.get("msg"))) {
                    return R.ok("在职单位更新成功");
                } else {
                    return R.error((String) resultMap.get("msg"));  // 返回错误信息
                }
            } catch (Exception e) {
                return R.error(e.toString());
            }
        }

        @PostMapping(value = "/updateUserAcademicField")
        @ApiOperation("修改学术领域接口")
        public R updateUserAcademicField(@RequestHeader("Authorization") String token,
                                         @RequestParam(required = false) AcademicFieldType academicField)
        {
            try {
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }
                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId
                HashMap<String, Object> resultMap = userService.updateUserAcademicField(userId, academicField);
                if ("学术领域更新成功".equals(resultMap.get("msg"))) {
                    return R.ok("学术领域更新成功");
                } else {
                    return R.error((String) resultMap.get("msg"));  // 返回错误信息
                }
            } catch (Exception e) {
                return R.error(e.toString());
            }
        }

        @PostMapping(value = "/updateUserProfession")
        @ApiOperation("修改用户职业接口")
        public R updateUserProfession(@RequestHeader("Authorization") String token,
                                      @RequestParam(required = false) String profession)
        {
            try {
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }
                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId
                HashMap<String, Object> resultMap = userService.updateUserProfession(userId, profession);
                if ("用户职业更新成功".equals(resultMap.get("msg"))) {
                    return R.ok("用户职业更新成功");
                } else {
                    return R.error((String) resultMap.get("msg"));  // 返回错误信息
                }
            } catch (Exception e) {
                return R.error(e.toString());
            }
        }

        @PostMapping(value = "/updateUserPhone")
        @ApiOperation("修改用户电话接口")
        public R updateUserPhone(@RequestHeader("Authorization") String token,
                                 @RequestParam(required = false) String phone)
        {
            try {
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }
                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId
                HashMap<String, Object> resultMap = userService.updateUserPhone(userId, phone);
                if ("用户电话更新成功".equals(resultMap.get("msg"))) {
                    return R.ok("用户电话更新成功");
                } else {
                    return R.error((String) resultMap.get("msg"));  // 返回错误信息
                }
            } catch (Exception e) {
                return R.error(e.toString());
            }
        }
        @PostMapping(value = "/changePassword")
        @ApiOperation("修改密码接口")
        public R changePassword(
                @RequestHeader("Authorization") String token,
                @RequestParam String oldPassword,
                @RequestParam String newPassword) {
            try {
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }
                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId
                HashMap<String, Object> res = userService.changePassword(userId, oldPassword, newPassword);
                if ("密码修改成功".equals(res.get("msg"))) {
                    return R.ok("Password changed successfully");
                } else {
                    return R.error((String) res.get("msg"));
                }
            } catch (Exception e) {
                return R.error(e.toString());
            }
        }

    @PostMapping(value = "/updateUser")
    @ApiOperation("修改User接口")
    public R updateUser(
            @RequestHeader("Authorization") String token,
            @RequestBody User user) {
        try {
            CheckResult checkResult = JwtUtils.validateJWT(token);
            if (!checkResult.isSuccess()) {
                return R.error("Token无效或已过期");
            }
            Claims claims = checkResult.getClaims();
            int userId = Integer.parseInt(claims.getId());
            user.setUserid(userId);

            System.out.println(user);
            userMapper.updateUser(user);
            return R.ok("用户修改成功");
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @RequestMapping(value = "/updatePassword")
    @ApiOperation("后台强制修改密码接口")
    public R updatePassword(
            @RequestParam int userId,
            @RequestParam String newPassword) {
        try {
            HashMap<String, Object> res = userService.updatePassword(userId, newPassword);
            if ("密码修改成功".equals(res.get("msg"))) {
                return R.ok("Password changed successfully");
            } else {
                return R.error((String) res.get("msg"));
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

        @PostMapping(value = "/logout")
        @ApiOperation("退出登录接口")
        public R logout(@RequestParam int userId) {
            try {
                HashMap<String, Object> resultMap = userService.logout(userId);
                return R.ok((String) resultMap.get("msg"));
            } catch (Exception e) {
                return R.error(e.toString());
            }
        }

    @GetMapping(value = "/delete")
    @ApiOperation("删除用户")
    public R delete(@RequestParam int userId) {
        try {
            HashMap<String, Object> resultMap = userService.delete(userId);
            return R.ok((String) resultMap.get("msg"));
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }


    // 查看所有标签
    @GetMapping("/viewAllTags")
    @ApiOperation("查看所有标签接口")
    public R viewAllTags(@RequestParam int userId) {
        try {
            List<HashMap<String, Object>> tags = userService.viewAllTags(userId);
            if (tags.isEmpty()) {
                return R.error("No tags found for this user");
            } else {
                return R.ok().put("tags", tags);
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    // 创建新标签
    @PostMapping(value = "/createNewTag")
    @ApiOperation("创建新标签接口")
    public R createNewTag(@RequestParam int userId,
                             @RequestParam String tag) {
        try {
            HashMap<String, Object> resultMap = userService.createFavoriteTag(userId, tag);
            if ("标签创建成功".equals(resultMap.get("msg"))) {
                return R.ok("Tag created successfully");
            } else {
                return R.error((String) resultMap.get("msg"));
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    // 删除标签
    @GetMapping("/deleteTag")
    @ApiOperation("删除标签接口")
    public R deleteTag(@RequestParam int userId, @RequestParam String tag) {
        try {
            HashMap<String, Object> resultMap = userService.deleteFavoriteTag(userId, tag);
            if ("标签删除成功".equals(resultMap.get("msg"))) {
                return R.ok("Tag deleted successfully");
            } else {
                return R.error((String) resultMap.get("msg"));
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    // 查看用户所有收藏
    @GetMapping("/viewAllFavoritesByUser")
    @ApiOperation("查看用户所有收藏接口")
    public R viewAllFavoritesByUser(@RequestParam int userId) {
        try {
            List<HashMap<String, Object>> favoriteList = userService.viewAllFavoritesByUser(userId);
            if (favoriteList.isEmpty()) {
                return R.error("No favorites found for this user");
            } else {
                return R.ok().put("favoriteList", favoriteList);
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    // 查用户带有指定标签的收藏(多选，并集，标签列表)
    @PostMapping("/viewAllFavoritesWithTags")
    @ApiOperation("查用户带有指定标签的收藏接口")
    public R viewAllFavoritesWithTags(@RequestBody TagDto tagDto) {
            List<String> tags = tagDto.getTags();
            int userId = tagDto.getUserId();
        try {
            List<HashMap<String, Object>> favoriteList = userService.viewAllFavoritesWithTags(userId, tags);
            if (favoriteList.isEmpty()) {
                return R.error("No favorites found with these tags");
            } else {
                return R.ok().put("favoriteList", favoriteList);
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    // 查询用户标签及其标记文章的数量
    @GetMapping("/viewAllTagsAndCounts")
    @ApiOperation("查询用户标签及其标记文章的数量接口")
    public R viewAllTagsAndCounts(@RequestParam int userId) {
        try {
            List<HashMap<String, Object>> tagsAndCounts = userService.viewAllTagsAndCounts(userId);
            if (tagsAndCounts.isEmpty()) {
                return R.error("No tags found for this user");
            } else {
                return R.ok().put("tagsAndCounts", tagsAndCounts);
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    // 添加用户收藏(标签可多选)
//    @PostMapping(value = "/addUserFavorite")
//    @ApiOperation("添加用户收藏接口")
//    public R addUserFavorite(
//            @RequestParam int userId,
//            @RequestParam String publicationId,
//            @RequestParam List<String> tags) {
//        try {
//            HashMap<String, Object> resultMap = userService.addUserFavorite(userId, publicationId, tags);
//            if ("收藏添加成功".equals(resultMap.get("msg"))) {
//                return R.ok("Favorite added successfully");
//            } else {
//                return R.error((String) resultMap.get("msg"));
//            }
//        } catch (Exception e) {
//            return R.error(e.toString());
//        }
//    }

    @PostMapping(value = "/addUserFavorite")
    @ApiOperation("添加用户收藏接口")
    public R addUserFavorite(
            @RequestBody AddUserFavoriteDto addUserFavoriteDto) {  // 修改为 @RequestBody
        try {
            HashMap<String, Object> resultMap = userService.addUserFavorite(addUserFavoriteDto.getUserId(), addUserFavoriteDto.getPublicationId(), addUserFavoriteDto.getTags());

            if ("收藏添加成功".equals(resultMap.get("msg"))) {
                return R.ok("Favorite added successfully");
            } else {
                return R.error((String) resultMap.get("msg"));
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }


    @GetMapping(value = "/haveFavorite")
    @ApiOperation("用户是否收藏")
    public R haveUserFavorite(
            @RequestParam int userId,
            @RequestParam String publicationId
    ){
        try {
            int res = userMapper.haveFavorite(userId, publicationId);
            return R.ok("get num").put("haveFavorite",res);
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/workFavoriteNum")
    @ApiOperation("收藏数量")
    public R workFavoriteNum(
            @RequestParam String publicationId
    ){
        try {
            int res = userMapper.getWorkFavoriteNum(publicationId);
            return R.ok("get num").put("favoriteNum",res);
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    // 删除用户收藏
    @GetMapping("/deleteUserFavoriteOld")
    @ApiOperation("删除某个标签下单一收藏接口（旧版）")
    public R deleteUserFavoriteOld(@RequestParam int userId,
                                @RequestParam String publicationId,
                                @RequestParam String tag) {
        try {
            HashMap<String, Object> resultMap = userService.deleteUserFavoriteOld(userId, publicationId, tag);
            if ("收藏删除成功".equals(resultMap.get("msg"))) {
                return R.ok("Favorite deleted successfully");
            } else {
                return R.error((String) resultMap.get("msg"));
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    // 删除用户收藏（新），即从所有标签下移除某篇文章
    @GetMapping("/deleteUserFavorite")
    @ApiOperation("删除单条收藏接口（新版）")
    public R deleteUserFavorite(@RequestParam int userId,
                            @RequestParam String publicationId) {
        try {
            HashMap<String, Object> resultMap = userService.deleteUserFavorite(userId, publicationId);
            if ("收藏删除成功".equals(resultMap.get("msg"))) {
                return R.ok("Favorite deleted successfully");
            } else {
                return R.error((String) resultMap.get("msg"));
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }


    // 添加一条浏览历史
    @PostMapping("/addHistory")
    @ApiOperation("添加一条浏览历史接口")
    @DateTimeFormat
    public R addHistory(@RequestParam int userId,
                        @RequestParam String publicationId,
                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp) {
        try {
            HashMap<String, Object> resultMap = userService.addHistory(userId, publicationId, timestamp);
            if ("浏览历史添加成功".equals(resultMap.get("msg"))) {
                return R.ok("History added successfully");
            } else {
                return R.error("Failed to add history");
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    // 查看所有浏览历史
    @GetMapping("/viewAllHistory")
    @ApiOperation("查看所有浏览历史接口")
    public R viewAllHistory(@RequestParam int userId) {
        try {
            List<HashMap<String, Object>> historyList = userService.viewAllHistory(userId);
            if (historyList.isEmpty()) {
                return R.error("No history found for this user");
            } else {
                return R.ok().put("historyList", historyList);
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/getScholars")
    public R getScholars(@RequestParam("name")String name){
            try{
                return R.ok().put("scholars",userService.getScholarsByName(name));
            }catch (Exception e){
                return R.error(e.toString());
            }
    }

    // 删除某条浏览历史
    @GetMapping("/deleteHistory/{id}")
    @ApiOperation("删除某条浏览历史接口")
    public R deleteHistory(@RequestParam int userId,
                           @RequestParam String publicationId) {
        try {
            HashMap<String, Object> resultMap = userService.deleteHistory(userId, publicationId);
            if ("浏览历史删除成功".equals(resultMap.get("msg"))) {
                return R.ok("History deleted successfully");
            } else {
                return R.error((String) resultMap.get("msg"));
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    // 清空所有浏览历史
    @GetMapping("/clearAllHistory")
    @ApiOperation("清空所有浏览历史接口")
    public R clearAllHistory(@RequestParam int userId) {
        try {
            HashMap<String, Object> resultMap = userService.clearAllHistory(userId);
            if ("浏览历史清空成功".equals(resultMap.get("msg"))) {
                return R.ok("History cleared successfully");
            } else {
                return R.error((String) resultMap.get("msg"));
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getUserInfo")
    @ApiOperation("获取用户详细信息")
    public R getUserInfo(@TokenToUser User user){
        try{
            List<List<HashMap<String,Object>>> res = userService.getUserInfo(user.getUserid());
            return R.ok("success").put("tables",res);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getUserInfo/id")
    @ApiOperation("获取用户详细信息(方便测试版)")
    public R getUserInfo(@RequestParam("userId")int userId){
        try{
            List<List<HashMap<String,Object>>> res = userService.getUserInfo(userId);
            return R.ok("success").put("tables",res);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping("/getById")
    @ApiOperation("获取用户")
    public R getUserById(@RequestParam int userId){
        try {
            User user = userMapper.selectUserById(userId);
            if(user!=null){
                return R.ok("success").put("user",user);
            }else {
                return R.error("get user failed");
            }
        }catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @PostMapping("/setRole")
    @ApiOperation("设置用户权限")
    public R setUserRole(@RequestParam int userId,@RequestParam int role){
        try {
            User user = userMapper.selectUserById(userId);
            if(user!=null){
                user.setRole(role);
                userMapper.updateUserRole(user);
                DateTimeFormatter formatter = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

                messageService.createMessage(1,userId,"恭喜，您于"+new DateTime(System.currentTimeMillis()).toString(formatter) +
                        "通过了审核，已成为科研人员！");
                return R.ok("success").put("user",user);
            }else {
                return R.error("get user failed");
            }
        }catch (Exception e) {
            return R.error(e.toString());
        }
    }
    @PostMapping("/setRole2")
    @ApiOperation("设置用户权限，并将用户与author绑定")
    public R setUserRole(@RequestParam int userId,@RequestParam int role, @RequestParam String authorId){
        try {
            User user = userMapper.selectUserById(userId);
            if(user!=null){
                user.setRole(role);
                userMapper.updateUserRole(user);
                userMapper.updateUserAuthorRelation(authorId, userId);
                DateTimeFormatter formatter = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

                messageService.createMessage(1,userId,"恭喜，您于"+new DateTime(System.currentTimeMillis()).toString(formatter) +
                        "通过了审核，已成为科研人员！");
                return R.ok("success").put("user",user);
            }else {
                return R.error("get user failed");
            }
        }catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @GetMapping("/setNameReal")
    @ApiOperation("设置用户真名")
    public R setUserNameReal(@RequestParam("userId")int userId,@RequestParam("nameReal")String nameReal,@RequestParam("authorId")String authorId){
        try {
            User user = userMapper.selectUserById(userId);
            if(user!=null){
                user.setAuthorId(authorId);
                user.setNameReal(nameReal);
                userMapper.updateUserNameReal(user);
                userMapper.updateUserAuthor(user);
                return R.ok("success").put("user",user);
            }else {
                return R.error("get user failed");
            }
        }catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @GetMapping("/getAll")
    @ApiOperation("获取全部用户")
    public R getAll(){
        try {
            List<User> userList = userMapper.getAll();
            if(userList!=null){
                return R.ok("success").put("userList",userList);
            }else {
                return R.error("get user failed");
            }
        }catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @GetMapping("/getCount")
    @ApiOperation("获取用户总数")
    public R getCount(){
        try {
            int count = userMapper.getCount();
            if(count>=0){
                return R.ok("success").put("count",count);
            }else {
                return R.error("get user failed");
            }
        }catch (Exception e) {
            return R.error(e.toString());
        }
    }


}

