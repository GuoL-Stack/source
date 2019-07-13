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

#### 2.1.1.1 安装

二进制 pip 容器 三种，常见的 平台 使用 二进制，ARM (树莓派) 使用 pip

安装 

```shell
# 下载 二进制文件
sudo curl -L https://github.com/docker/compose/releases/download/1.17.1/coker-compose-'uname -s'-'uname -m' > /usr/local/bin/docker-compose
chmod -x /usr/loacl/bin/docker-compose
```

```shell
sudo pip install -U docker-compose
```

```shell
# 容器执行
curl -L	https://github.com/docker/compose/releases/download/1. 8.0/run.sh > /usr/local/bin/docker-compose 
chmod -x /usr/loacl/bin/docker-compose
```



卸载

```shell
rm /usr/local/bin/doker-compose
```



## 2.2 Machine

## 2.3 Docker Swarm (Swarm mode)

# 三、Doker 其他项目