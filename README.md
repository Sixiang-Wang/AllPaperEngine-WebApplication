# 所有人目光向我看齐！

# 看我看我！

# 我要宣布个事！



> [!NOTE]
>
> 有重要事项注意更新README并通知大家



## node版本

### ***<u>20.13.1</u>***



## 端口

后端端口：1145

前端端口：2221



## 项目结构

### api-http.js

已经封装好了get和post方法

对接端口时可方便调用

```javascript
get(url,params={})

post(url,data={})
```



### assets

用于存放静态资源

img中存放图片或动图

svg中存放svg

video存放视频（暂定）

audio存放音频（暂定）



### components

存放重要全局组件，如顶部栏、侧边栏



### css

放样式



### router

导航



### views

页面。分支功能可以建点文件夹啥的



## 更好用的全局变量
现在我有了更好用的
比如要使用userId，
可以直接
localStorage.getItem("userId");
即可！（我已经自动存好了，可以直接用）

## 全局变量的使用

在store/store.js中

定义示例：用户名和token

```
export const useUserStore = defineStore('user', {
    state: () => ({
        username: '', // 初始用户名为空
    }),
    actions: {
        setUsername(name) {
            this.username = name;
        },
    },
});

export const useTokenStore = defineStore('token', {
    state: () => ({
        token: '',
    }),
    actions: {
        setToken(token) {
            this.token = token;
        },
    },
});
```



使用示例：

```
文件A:
const userStore = useUserStore();
const user_name = computed(() => userStore.username);

文件B:
const userStore = useUserStore();
```

效果：

文件B中调用userStore.setUserName后

文件A中user_name会改变



实际样例：

详情请见Header、Login中的使用方法。
