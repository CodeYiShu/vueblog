<template>
  <div>
    <el-container>

      <el-header>
        <img src="/static/img.jpg" id="headImg" />
      </el-header>

      <el-main>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="ruleForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input v-model="ruleForm.password"></el-input>
            </el-form-item>          
            <el-form-item>
                <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
        </el-form>
      </el-main>

    </el-container>
  </div>
</template>

<script>
    export default {
    data() {
        return {
        ruleForm: {
            username: "zhangsan",
            password:"123456"
        },
        rules: {
            username: [
                { required: true, message: "请输入用户名", trigger: "blur" },
                { min: 3, max: 15, message: "长度在 3 到 15 个字符", trigger: "blur" },
            ],
            password: [
                { required: true, message: "请输入密码", trigger: "blur" },
                { min: 3, max: 15, message: "长度在 3 到 15 个字符", trigger: "blur" },
            ]
           },
        };
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                  //保存全局this
                    const _this = this;
                    alert("submit!");
                    //发送登录请求，携带表单数据到请求体
                    this.$axios.post("http://localhost:8081/login",this.ruleForm)
                    .then(response =>{
                      const token = response.headers['authorization'];
                      const userMessage = response.data.data;
                      _this.$store.commit("SET_JWT",token); //将JWT保存到store中
                      _this.$store.commit("SET_USERINFO",userMessage); //将用户信息保存到store中
                      console.log(_this.$store.getters.getJwt); //获取store中保存的JWT
                      console.log(_this.$store.getters.getUserInfo); //获取store中保存的userInfo
                      this.$router.push("/blogs");
                    })
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
        resetForm(formName) {
        this.$refs[formName].resetFields();
        },
    },
    };
</script>

<style>
.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
#headImg {
  height: 60%;
  margin-top: 10px;
}
.demo-ruleForm{
    max-width: 500px;
    margin:0 auto;
}
</style>