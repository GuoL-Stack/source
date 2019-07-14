# 二、docker三剑客

## 2.1 Compose

`Docker Compose`是 docker 官方的项目值，负责快速部署分布式应用。

### 2.1.1 简介

Compose 定位是 ： 定义和运行多个 docker 容器的应用。

dockerfile 可以让用户 很方便定义 一个单独的应用容器。然而 在实际中，往往需要部署很多程序 来完成一个服务（web服务器，后端，负载均衡等）。

Compose 刚好可以完成。允许用户使用 一个单独的 `docker-compose.yml`模板文件来定义一组关联的应用容器为一个项目。

Compose 两个概念：

+ 服务（service）：一个应用的容器，实际上可以包括若干运行相同镜像的容器实例
+ 项目（project）： 由一组关联的应用容器组成的一个完整业务单元，在 `docker-compose.yml`文件中定义

Compose 默认管理的对象是项目，通过子命令对项目中 的一组容器进行便捷地生命周期管理。

Compose 由 Python 编写，实际 是调用 Docker 服务的接口 对容器进行管理。

 **安装**

二进制 pip 容器 三种，常见的 平台 使用 二进制，ARM (树莓派) 使用 pip

安装 

```shell
# 下载 二进制文件
sudo curl -L https://github.com/docker/compose/releases/download/1.24.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
# https://github.com/docker/compose/releases/ 查看最新
chmod -x /usr/local/bin/docker-compose
```

```shell
sudo pip install -U docker-compose
```

```shell
# 容器执行
curl -L	https://github.com/docker/compose/releases/download/1. 8.0/run.sh > /usr/local/bin/docker-compose 
chmod -x /usr/local/bin/docker-compose
```

**卸载**

```shell
rm /usr/local/bin/doker-compose
sudo pip uninstall docker-compose
```

### 2.1.2 简单 使用

2019年7月13日

可以理解 为 一个 `dockerfile`为一个 服务镜像，而 `docker-compose.yml`就是一个 项目。

app.py

```python
from flask import Flask
from redis import Redis

app = Flask(__name__)
redis = Redis(host='redis' , port=6379)

@app.route('/')
def hello():
    count = redis.incr('hits')
    return 'Hello Dockre-compose! 页面已经被访问{}次。\n'.format(count)

if __name__ == "__main__":
    app.run(host="0.0.0.0",debug=True)

```

Dockrefile

```dockerfile
FROM python:3.6-alpine
ADD . /code
WORKDIR /code
RUN pip install redis flask
CMD ["python","app.py"]
```

docker-compose 模板文件：

```yml
version:'3.3'
services: 

    web:
        build: .
        ports:
            - "5000:5000"
            
    redis:
        image: "redis:alpine"
        
```

` Version in "./docker-compose.yml" is invalid. You might be seeing this error because you're using the wrong Compose file version. Either specify a supported version (e.g "2.2" or "3.3") and place your service definitions under the `services` key, or omit the `version` key and place your service definitions at the root of the file to use version 1.
For more on the Compose file format versions, see https://docs.docker.com/compose/compose-file/`

```shell
docker-compose up  # 执行 compose 项目
```

### 2.1.3 docker-compose 命令

```shell
docker-compose 命令选项 
```

+ `-f, --file FILE` 指定使用的Compose 的模板文件，默认 `docker-compose.yml`可以多次指定
+ `-p,--project-name Name` 指定项目 名称，默认 使用 所在目录作为项目名称
+ `--x-networking` 使用 Docker 的可拔插网络后端特性
+ `--x-network-driver DRIVER` 指定 后端 网络驱动，默认为 `bridge`
+ `--verbose`输出更多调试信息
+ `-v,--version` 答应 版本 并退出

#### **子命令 build **

`build` 构建 (重新构建) 项目中的服务容器，可以 随时 在项目目录下运行，进行重新构建服务。

选项 ： 

+ `--force-rm`   删除 构建过程中的临时容器
+ `--no-cache` 构建镜像过程中不使用 cache （将 增加 构建过程）
+ `--pull` 始终尝试通过 pull 获取 最新的镜像

```shell
docker-compose build 
# 直接构建 出需要的镜像  例如 生成 test_web 的镜像
# 如果直接使用 docker-compose up 也会走这一步，
# 并且会创建 test_web 与相关 服务容器
```



#### **子命令 config**

验证 Compose 文件格式是否正确

#### **子命令 exec**

进入指定容器

#### **子命令 help**

获取帮助

#### **子命令 images**

列出 Compse 文件中 包含的镜像

```shell
docker-compose images  
# 当前 所在路径下的compose项目 用到的镜像
```

#### **子命令 kill**

通过 发送 `SIGKILL`信号强制停止服务容器

```shell
docker-compose kill -s SIGINT
```

#### **子命令 logs**

查看 服务容器的输出。默认情况下：docker-compose 将对不同的服务输出 使用不同的颜色区分， `--no-color` 关闭颜色区分。

#### port

打印某个 容器端口 所 映射的公共端口

```shell
docker-compose port [options] SERVICE-PORT
--protocol=proto指定端口协议，tcp  或者 udp
--index=index 如果同一服务在多个容器中，指定 命令对象容器的序号  默认1
```

#### ps

列出项目中 目前 所有的容器

```shell
docker-compse ps [options] Service
-q 只打印容器id
# 当前 所在路径下的compose项目
```

#### pull

拉取 服务 依赖镜像。

```shell
docker-compose pull [options] Service
--ignore-pull-failures 忽略拉取镜像过程中的错误
```

#### push

推送

#### restart

重启项目中的服务

```shell
docker-compose restart [options] Service
-t,--timeout TIMEOUT 指定重启前停止容器的超时时间 默认10s
```

#### rm

删除所有（停止状态的）服务容器。推荐先执行 `docker-compose stop` 命令来停止容器。

```shell
docker-compse rm [options] Service
-f,--force 强制删除包括 非停止
-v 删除容器所挂载的 数据卷
# 通过 build 构建的 镜像 还是存在
```

#### run

在指定服务上执行 一个命令

```shell
docker-compose run 容器名称 命令
# 如果 容器名称  没有启动 ，会去启动一个对应的容器
```

+ -d 后台运行
+ --name NAME 为容器指定一个名称
+ --entrypoint CMD 覆盖默认的容器启动指令
+ -e KEY=VAL 设置环境变量值，可以i多次使用
+ -u ,--user=""  指定容器运行的用户 或者 uuid
+ --no-deps 不启动互联的服务容器
+ --rm  运行命令后 制动删除 -d 忽略
+ -p,--publish=[] 设置 映射端口
+ --service-ports 配置服务端口并映射到本地主机
+ -T 不分配 tty

#### scale

设置 指定 服务 运行的服务容器 个数

```shel
docker-compose scale web=2 db=3
-t , --timeout TIMEOUT 
```

将启动 2个容器 运行 web ,..

正常 指定多余 当前，创建。反之 停止

#### start

启动 已经停止的  服务容器

#### stop

停止 运行状态的 服务容器

#### 子命令 pause

暂停一个服务容器：

```shell
docker-compose pause Service
```

#### unpause

恢复处于 暂停状态的服务

#### **子命令 down**

停止 up 命令所有启动的容器，并移除 网络

#### 启动运行命令 up

主要命令，功能包括：构建（build）镜像 、（重新）创建服务、启动服务、关联服务相关的容器 一些列操作

其中 与之 关联的服务都会被自动启动。

基本上 都是以此命令 运行一个项目

+ -d 后台运行
+ --no-color 日志颜色不区分
+ --no-deps 不启动服务所链接的容器
+ --force-recreate 强制创建新的容器，与下命令 不能同用
+ --no-recreate  不重建
+ --no-build   不自动 构建缺少服务
+ -t,--timeout TIMEOUT 超时

#### version

版本信息

### 2.1.4 Compose 模板

docker-compose 模板中 的服务 `services`，都必须通过 image 指令 指定镜像 或者 build指令 `Dockerfile `进行构建

```yaml
version:'3.3'
services: 

    web:
        build: .
        ports:
            - "5000:5000"
            
    redis:
        image: "redis:alpine"
```





## 2.2 Machine

## 2.3 Docker Swarm (Swarm mode)

# 三、Doker 其他项目