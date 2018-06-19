# File Observer

方向：Web开发工具

功能：监控文件夹，自动刷新页面



### 设置目标文件夹

默认监控目标：

主程序所在目录下的 `dev`



更改监控目标—方式一：

```shell
java -jar fileobserver-0.1.0.jar -Dfolder="c:/dev"
```

通过  `System.getProperty("aaa","1"); `方式获取。作为环境变量

更改监控目标—方式二：

```shell
java -jar fileobserver-0.1.0.jar --folder="c:/dev"
```

是Spring Boot 的写法，可以通过 `@Value("${a1}"）` 获取



### 细节

默认端口：8193

文件映射：`http://localhost:8193/file/{filepath}`



