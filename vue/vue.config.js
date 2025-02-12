const { defineConfig } = require('@vue/cli-service')
const server = 'http://localhost:85/workhive';
var path = require("path");

module.exports = defineConfig({
   // outputDir : path.resolve("../server/public"),
   transpileDependencies: true,

   // cors => prosy setting
   devServer : {
      port : 8077,    //원래 8081 변경되서 8099
      proxy : {
         '^/api' : {
            target : server,
            changeOrigin : true,
            pathRewrite : { '^/api' : '/' },
            ws : false //vue가 가지고 있는 웹소켓 (소켓을 설정 안했기 때문에 false)
         }
      }
   }
})