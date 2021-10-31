import Vue from 'vue'
import Vuex from 'vuex';  //导入状态管理vuex模块

Vue.use(Vuex) //显式声明vuex

export default new Vuex.Store({ //创建Store组件
  state: {  //定义存储的数据
    jwt:'',  //存储jwt
    userInfo:JSON.parse(sessionStorage.getItem("userInfo"))  //存储用户信息，由登录控制器方法返回
  },
  //相当于setter
  mutations: {
      SET_JWT:(state,token)=>{  //state就是上面存储数据的state属性，token就是传递过来的数据
        state.jwt = token;  //将传递过来的数据token传递给state存储的jwt
        //将JWT保存到localStorage中，浏览器关闭时也不会消失
        localStorage.setItem("jwt",token); //键是jwt，值是传递过来的数据
      },
      SET_USERINFO:(state,userMessage)=>{  //state就是上面存储数据的state属性，userMessage就是传递过来的数据
        state.userInfo = userMessage;  //将传递过来的数据userMessage传递给state存储的userInfo
        //将用户信息保存到sessionStorage中，一次会话有效
        sessionStorage.setItem("userInfo",JSON.stringify(userMessage)); //键是userInfo，值是传递过来的数据
      },
      REMOVEALL:(state)=>{  //state就是上面存储数据的state属性
       state.jwt = '';
       state.userInfo = {};
       localStorage.removeItem("jwt",'');
       sessionStorage.removeItem("userInfo")
      }
  },
  //相当于getter
  getters:{
      getUserInfo:state=>{
          return state.userInfo;
      },
      getJwt: state=>{
        return state.jwt;
      }
  },
  actions: { },
  modules: { }
})
