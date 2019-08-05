# SVN资源权限管理系统

## 项目简介
公司的项目使用的是SVN进行版本管理。但是SVN服务并没有提供友好的远程管理工具，每次对SVN进行操作还需要登录到服务器进行本地命令行方式操作，比较麻烦；
并且SVN服务本身记录的信息较少，如果不使用其他诸如文件、数据库等方式记录详细信息，单从SVN服务器本身查看仓库、用户等，很难知道谁是谁。

所以一直想做一个具有友好界面的管理工具。

之前做过一个基于Excel的进行SVN管理的工具[SVN管理插件](https://gitee.com/hy-wux/SVN-Management)，可以基于Excel进行详细信息的记录，方便的实现仓库、组、用户等的创建、删除以及权限管理，
但是，仍然存在一些弊端：只能基于Windows系统、必须登录远程服务器、难以控制插件权限等。

因此一个基于Web的远程SVN资源权限管理系统诞生了。

他基于SpringBoot 2.1.x开发，整合了MyBatis数据操作、Shiro权限框架、Thymeleaf模板引擎、Svnkit仓库访问，可作为单独的应用独立运行，也可以作为一个模块集成到其他项目中。
可作为单体应用进行部署，也可以基于SpringCloud、SOFABoot、Motan、EDAS、Dubbo等分布式RPC架构进行部署，支持使用Zookeeper、Nacos、AliCloud ANS、Eureka、Consul等作为服务注册发现中心。
并且提供了用于在各种分布式架构下快速集成到自己的应用的Spring-Boot-Starter。

他让枯燥的SVN管理工作变得方便、简单、高效。

## 项目架构

```text
DataDataService-Svnkit
├── dataservice-svnkit-framework                  -- 脚手架
|   ├── service-core                              -- 基础核心，主要是一些公共类库、Scala语言等
|   ├── service-web                               -- WEB功能，主要用于集成数据库和WEB相关的功能
|   └── service-web-admin                         -- 前端界面，主要提供一个简单通用的权限管理框架
├── dataservice-svnkit-module                     -- 核心模块
|   ├── service-integrates-svnkit-admin           -- SVN资源权限管理系统 - 管理系统，通过注册中心调用生产者提供的功能
|   ├── service-integrates-svnkit-api             -- SVN资源权限管理系统 - 仓库、组、用户的管理接口
|   └── service-integrates-svnkit-provider        -- SVN资源权限管理系统 - 仓库、组、用户的管理接口的具体实现类
├── dataservice-svnkit-registry                   -- 服务注册与发现中心
|   └── dataservice-svnkit-eureka                 -- SVN资源权限管理系统 - 基于Eureka的服务注册于发现中心
├── dataservice-svnkit-springcloud                -- 基于SpringCloud的部署方案
|   ├── dataservice-svnkit-dispatcher-feign       -- SVN资源权限管理系统 - 基于FeignClient的生产者调度器
|   ├── dataservice-svnkit-springcloud-consumer   -- SVN资源权限管理系统 - 消费者
|   └── dataservice-svnkit-springcloud-producer   -- SVN资源权限管理系统 - 生产者
└── docs                                          -- 文档
```

## 内置功能
* 实现多个SVN服务器集中管理
* 创建SVN仓库、组、用户等，并通过Email的方式通知相关人员
* 仓库条目浏览。以树形展示条目的层级关系；以列表的形式展示仓库条目，包括条目名称、版本、最后提交人、最后提交时间
* 仓库条目权限。可以展示仓库访问权限、维护仓库访问权限，访问权限能够精确到每一个目录，并且在权限发生变动时通过Email的方式通知相关人员
* 能够为仓库设置组和用户、能够为组设置用户，并且通过Email的方式通知相关人员
* 能够修改SVN用户的密码，并且通过Email的方式通知相关人员
* 能够通过Excel导入的方式，批量创建仓库、用户；能够将仓库、用户的信息导出到Excel
* 可管理基于Windows的VisualSVN；可以管理基于Linux的Subversion，同时实现了基于svn://协议的管理，以及整合Apache后基于http://协议的管理

## 软件文档

* [VisualSVN管理文档](docs/管理文档VisualSVN/VisualSVN管理文档.md)
* [Subversion管理文档](docs/管理文档Subversion/Subversion管理文档.md)
* [Subversion整合Apache管理文档](docs/管理文档Subversion整合Apache/Subversion整合Apache管理文档.md)
* [软件部署及运行效果](docs/软件部署运行/软件部署及运行效果.md)
* [软件操作手册](docs/软件操作手册/软件操作手册.md)

## 代码开源

### GIT共享
```text
https://gitee.com/hy-wux/DataService-Svnkit.git
```
* 仓库设置
```bash
DataService-Svnkit> git init
DataService-Svnkit> git config user.name 伍鲜
DataService-Svnkit> git config user.email hy_wux@outlook.com
```

* 项目共享
```bash
DataService-Svnkit> git remote add origin https://gitee.com/hy-wux/DataService-Svnkit.git
DataService-Svnkit> git pull origin master
DataService-Svnkit> git add dataservice-svnkit-*
DataService-Svnkit> git add pom.xml
DataService-Svnkit> git add README.md
DataService-Svnkit> git commit -m SVN资源权限管理系统
DataService-Svnkit> git push origin master
```

* 项目提交
```bash
DataService-Svnkit> git pull origin master
DataService-Svnkit> git add *
DataService-Svnkit> git commit -m SVN资源权限管理系统
DataService-Svnkit> git push origin master
```
