package com.example.scholar.service.impl;


import com.example.scholar.dao.UserMapper;
import com.example.scholar.dao.UserTokenMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.myenum.AcademicFieldType;
import com.example.scholar.service.UserService;
import com.example.scholar.util.JwtUtils;
import com.example.scholar.util.Md5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.example.scholar.config.SystemConst.tokenValidTime;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserTokenMapper userTokenMapper;
    @Override
    public HashMap<String, Object> login(String mail, String password) {
        User user = userMapper.selectUserByMail(mail);
        HashMap<String,Object> map = new HashMap<>();
        if(user == null){
            map.put("msg", "no such user");
            return map;
        }else{
            if(Md5Utils.verify(password, user.getPassword())){
                //添加token
                String jwtToken = JwtUtils.createJWT(String.valueOf(user.getUserid()),user.getMail(),tokenValidTime);
                if(userTokenMapper.ifUserToken(user.getUserid())==1){
                    userTokenMapper.updateUserToken(jwtToken, user.getUserid());
                }else{
                    userTokenMapper.insertUserToken(jwtToken,user.getUserid());
                }
                map.put("token",jwtToken);
                map.put("username",user.getName());
                return map;
            }else{
                map.put("msg","wrong password");
                return map;
            }
        }
    }

    @Override
    public HashMap<String, Object> register(String name, String password, String mail) {
        // 检查账号是否已存在
        User existingUser = userMapper.selectUserByMail(mail);
        HashMap<String,Object> resultMap = new HashMap<>();
        if (existingUser != null) {
            resultMap.put("msg", "account already exists");
            return resultMap;
        }

        // 密码加密
        String hashedPassword = Md5Utils.agenerate(password);

        // 创建新用户对象并赋值
        User newUser = new User();
        newUser.setPassword(hashedPassword);  // 存储加密后的密码
        newUser.setName(name);
        newUser.setMail(mail);
        newUser.setAvatar(""); // 可以设置默认头像

        // 插入新用户到数据库
        int result = userMapper.insertUser(newUser);
        if (result > 0) {
            resultMap.put("msg", "注册成功");
            resultMap.put("userid", newUser.getUserid());
        } else {
            resultMap.put("msg", "注册失败");
        }

        return resultMap;
    }

    @Override
    public HashMap<String, Object> setUserDetails(int userId, String avatar, LocalDate birthTime, String biography,
                                                  String company, AcademicFieldType academicField, String profession,
                                                  String phone) {
        HashMap<String, Object> resultMap = new HashMap<>();

        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "用户不存在");
            return resultMap;
        }

        if (avatar != null && !avatar.isEmpty()) {
            existingUser.setAvatar(avatar);
        }
        if (birthTime != null) {
            existingUser.setBirthTime(birthTime);
        }
        if (biography != null && !biography.isEmpty()) {
            existingUser.setBiography(biography);
        }
        if (company != null && !company.isEmpty()) {
            existingUser.setCompany(company);
        }
        if (academicField != null) {
            existingUser.setAcademicField(academicField);
        }
        if (profession != null && !profession.isEmpty()) {
            existingUser.setProfession(profession);
        }
        if (phone != null && !phone.isEmpty()) {
            existingUser.setPhone(phone);
        }

        // 更新用户信息
        int result = userMapper.updateUser(existingUser);
        if (result > 0) {
            resultMap.put("msg", "用户详细信息更新成功");
        } else {
            resultMap.put("msg", "用户详细信息更新失败");
        }

        return resultMap;
    }

    @Override
    public HashMap<String, Object> updateUserName(int userId, String username) {
        User existingUser = userMapper.selectUserById(userId);
        HashMap<String, Object> resultMap = new HashMap<>();
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
        }
        if (username != null && !username.isEmpty()) {
            existingUser.setName(username);
        }
        int result = userMapper.updateUserName(existingUser);
        if (result > 0) {
            resultMap.put("msg", "用户名更新成功");
        }
        else{
            resultMap.put("msg", "用户名更新失败");
        }

        return resultMap;
    }


    @Override
    public Boolean updateUserAvatar(Integer userid, String avatar){
        return userMapper.updateUserAvatar(userid.toString(),avatar)>0;
    }

    @Override
    public HashMap<String, Object> updateUserBirthTime(int userId, LocalDate birthTime) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
        }
        if(birthTime != null){
            existingUser.setBirthTime(birthTime);
        }
        int result = userMapper.updateUser(existingUser);
        if (result > 0) {
            resultMap.put("msg", "用户生日更新成功");
        }
        else{
            resultMap.put("msg", "用户生日更新失败");
        }
        return resultMap;
    }

    @Override
    public HashMap<String, Object> updateUserCompany(int userId, String company) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
        }
        if(company != null && !company.isEmpty()){
            existingUser.setCompany(company);
        }
        int result = userMapper.updateUser(existingUser);
        if (result > 0) {
            resultMap.put("msg", "在职单位更新成功");
        }
        else{
            resultMap.put("msg", "在职单位更新失败");
        }
        return resultMap;
    }

    @Override
    public HashMap<String, Object> updateUserAcademicField(int userId, AcademicFieldType academicField) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
        }
        if(academicField != null){
            existingUser.setAcademicField(academicField);
        }
        int result = userMapper.updateUser(existingUser);
        if (result > 0) {
            resultMap.put("msg", "学术领域更新成功");
        }
        else {
            resultMap.put("msg", "学术领域更新失败");
        }
        return resultMap;
    }

    @Override
    public HashMap<String, Object> updateUserProfession(int userId, String profession) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
        }
        if(profession != null && !profession.isEmpty()){
            existingUser.setProfession(profession);
        }
        int result = userMapper.updateUser(existingUser);
        if (result > 0) {
            resultMap.put("msg", "用户职业更新成功");
        }
        else {
            resultMap.put("msg", "用户职业更新失败");
        }
        return resultMap;
    }

    @Override
    public HashMap<String, Object> updateUserPhone(int userId, String phone) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
        }
        if(phone != null && !phone.isEmpty()){
            existingUser.setPhone(phone);
        }
        int result = userMapper.updateUser(existingUser);
        if (result > 0) {
            resultMap.put("msg", "用户电话更新成功");
        }
        else {
            resultMap.put("msg", "用户电话更新失败");
        }
        return resultMap;
    }


    @Override
    public HashMap<String, Object> changePassword(int userId, String oldPassword, String newPassword) {
        User existingUser = userMapper.selectUserById(userId);
        HashMap<String, Object> resultMap = new HashMap<>();

        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }
        if(oldPassword != null && !oldPassword.isEmpty() && Md5Utils.verify(oldPassword, existingUser.getPassword())){
            if(newPassword != null && !newPassword.isEmpty())
            {
                existingUser.setPassword(Md5Utils.agenerate(newPassword));
                int result = userMapper.updateUser(existingUser);
                if (result > 0) {
                    resultMap.put("msg", "密码修改成功");
                } else {
                    resultMap.put("msg", "密码修改失败");
                }
            }else {
                resultMap.put("msg", "新密码不能为空");
            }
        }else {
            resultMap.put("msg", "旧密码不正确");
        }

        return resultMap;
    }

    @Override
    public HashMap<String, Object> logout(int userId) {

        User existingUser = userMapper.selectUserById(userId);
        HashMap<String, Object> resultMap = new HashMap<>();
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }

        userTokenMapper.deleteUserToken(userId);
        resultMap.put("msg", "退出登录成功");

        return resultMap;
    }

    @Override
    public HashMap<String, Object> createFavoriteFolder(int userId, String folder) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }
        if (userMapper.checkFavoriteFolder(userId, folder) > 0) {// 如果收藏夹已存在，则返回
            resultMap.put("msg", "收藏夹已存在");
            return resultMap;
        }

        int result = userMapper.createFavoriteFolder(userId, folder);
        if (result > 0) {
            resultMap.put("msg", "收藏夹创建成功");
        } else {
            resultMap.put("msg", "收藏夹创建失败");
        }
        return resultMap;
    }

    @Override
    public List<HashMap<String, Object>> viewAllFolders(int userId) {
        List<HashMap<String, Object>> resultList = userMapper.selectUserFavoriteFolder(userId);
        return resultList;
    }

    @Override
    public HashMap<String, Object> deleteFavoriteFolder(int userId, String folder) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }
        if (userMapper.checkFolderNotEmpty(userId, folder) > 0) { // 如果收藏夹非空，要执行级联删除
            if (userMapper.deleteAllFavoritesInFolder(userId, folder) <= 0) {
                resultMap.put("msg", "收藏文章级联删除失败");
            }
        }
        // 随后删除收藏夹
        if (userMapper.deleteFavoriteFolder(userId, folder) > 0) {
            resultMap.put("msg", "收藏夹删除成功");
        } else {
            resultMap.put("msg", "收藏夹删除失败");
        }
        return resultMap;
    }

    @Override
    public HashMap<String, Object> addUserFavorite(int userId, String publicationId, LocalDateTime timestamp, String folder) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }
        int result = userMapper.addUserFavorite(userId, publicationId, timestamp, folder);
        if (result > 0) {
            resultMap.put("msg", "收藏添加成功");
        }
        else {
            resultMap.put("msg", "收藏添加失败");
        }
        return resultMap;
    }

    @Override
    public HashMap<String, Object> deleteUserFavorite(int userId, String publicationId, String folder) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }
        int result = userMapper.deleteUserFavorite(userId, publicationId, folder);
        if (result > 0) {
            resultMap.put("msg", "收藏删除成功");
        }
        else {
            resultMap.put("msg", "收藏删除失败");
        }
        return resultMap;
    }

    @Override
    public List<HashMap<String, Object>> viewAllFavorites(int userId, String folder) {
        List<HashMap<String, Object>> resultList = userMapper.selectUserFavorite(userId, folder);
        for (HashMap<String, Object> favorite : resultList) {
            favorite.put("title", userMapper.selectPublicationTitle(favorite.get("publicationid").toString()));
        }
        return resultList;
    }

    @Override
    public HashMap<String, Object> addHistory(int userId, String publicationId, LocalDateTime timestamp) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        int result;
        System.out.println(timestamp);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }
        int isExist = userMapper.checkUserBrowserHistory(userId, publicationId);
        if (isExist > 0) {// 如果已有相同浏览记录，就只更新时间戳
            result = userMapper.updateUserBrowserHistoryTimestamp(userId, publicationId, timestamp);
        } else {// 没有的话就直接添加
            result = userMapper.addUserBrowserHistory(userId, publicationId, timestamp);
        }
        if (result > 0) {
            resultMap.put("msg", "浏览历史添加成功");
        }
        else {
            resultMap.put("msg", "浏览历史添加失败");
        }
        return resultMap;
    }

    @Override
    public List<HashMap<String, Object>> viewAllHistory(int userId) {
        List<HashMap<String, Object>> resultList = userMapper.selectUserBrowserHistory(userId);
        return resultList;
    }

    @Override
    public HashMap<String, Object> deleteHistory(int userId, String publicationId) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }
        int result = userMapper.deleteUserBrowserHistory(userId, publicationId);
        if (result > 0) {
            resultMap.put("msg", "浏览历史删除成功");
        }
        else {
            resultMap.put("msg", "浏览历史删除失败");
        }
        return resultMap;
    }

    @Override
    public HashMap<String, Object> clearAllHistory(int userId) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }
        int result = userMapper.clearUserBrowserHistory(userId);
        if (result > 0) {
            resultMap.put("msg", "清空浏览历史成功");
        } else {
            resultMap.put("msg", "清空浏览历史失败");
        }
        return resultMap;
    }

}
