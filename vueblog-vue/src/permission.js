import router from "./router";
// 路由判断登录 根据路由配置文件的参数
router.beforeEach((to, from, next) => {
  // 判断该路由有meta属性，并且是否是requireAuth字段，且为true，如果是则表示此路由需要进行登录才可以进入
  if (to.matched.some(record => record.meta.requireAuth)) { 
    //从localStorage中尝试获取token
    const jwt = localStorage.getItem("jwt")
    console.log("------------" + jwt)
    if (jwt) { // 判断当前的token是否存在(登录存入的token)
      if (to.path === '/login') { //如果存在token且路由/login，则先不管
        next();
      } else {
        next() //如果存在token，则表示已经登录，放行
      }
    } else {  //不存在token
      next({  //路由到登录组件
        path: '/login'
      })
    }
  } else {
    next()
  }
})

