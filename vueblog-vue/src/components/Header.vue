<template>
    <div class="m-content">
        <h3>欢迎来到CodeShu的博客</h3>
        <!-- 头像，链接来自user.avatar数据 -->
        <div class="block"><el-avatar :size="50" :src="user.avatar"></el-avatar></div>
        <!-- 用户名，来自user.username数据 -->
        <div>{{user.username}}</div>
        <div class="m-action">
            <el-link href="/blogs">主页</el-link>
            <el-divider direction="vertical"></el-divider>
            <el-link type="success" href="/blog/add">发表博客</el-link>
            <el-divider direction="vertical"></el-divider>
            <el-link type="danger" @click="logout" v-show="hasLogin">退出</el-link>
            <el-link type="info" href="/login" v-show="!hasLogin">登录</el-link>
        </div>
    </div>
</template> 

<script>
export default {
    data(){
        return {
            user:{
                username:'',
                avatar:''
            },
            hasLogin:false
        }
    },
    methods:{
        //退出操作
        logout(){
            const _this = this;
             /*发送/logout请求到后台，会自动添加上前缀（在axios.js配置了）
               后台的/logout控制器方法需要认证通过才可以访问，所以需要携带token
               控制器方法会调用主体对象的SecurityUtils.getSubject().logout()进行退出登录
            */
            _this.$axios.get("/logout",{
                headers:{  //请求头中携带保存jwt
                    //可以直接去localStorage中获取
                    "Authorization":localStorage.getItem("jwt")
                }
            }).then(response=>{
                //清除store和localStorage保存的jwt和用户信息
                _this.$store.commit("REMOVEALL");
                //路由到登录页面
                _this.$router.push("/login")    
                
            })
        }
    },
    created(){
        //如果sessionStorage中保存的用户信息不为空（他在登录时被保存）
        if(sessionStorage.getItem("userInfo")){  
            console.log("userInfo" + JSON.parse(sessionStorage.getItem("userInfo")))
            //将用户信息中的用户名设置给数据user.username
            this.user.username = JSON.parse(sessionStorage.getItem("userInfo")).username;
            //将用户信息中的头像地址设置给数据user.avatar
            this.user.avatar = JSON.parse(sessionStorage.getItem("userInfo")).avatar;
            //sessionStorage有用户信息表示已经登录（退出后会在上面被清除），此时设置hasLogin为true，显示退出按钮
            this.hasLogin = true;
        }
    }
}
</script>

<style scoped>
    .m-content{
        max-width: 960px;
        margin:0 auto;
        text-align: center;
    }
    .m-action{
        margin:10px 0;
    }
</style>