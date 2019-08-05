# Subversion软件安装

## 软件安装

### 通过yum命令安装svnserve

```
yum -y install subversion
```

此命令会全自动安装svn服务器相关服务和依赖，安装完成会自动停止命令运行。若需查看svn安装位置，可以用以下命令：

```
rpm -ql subversion
```

```
[root@localhost ~]# whereis svnserve
svnserve: /usr/bin/svnserve /usr/share/man/man8/svnserve.8.gz
[root@localhost ~]# which svnserve
/usr/bin/svnserve
[root@localhost ~]# svnserve --version
svnserve, version 1.7.14 (r1542130)
   compiled Apr 11 2018, 02:40:28

Copyright (C) 2013 The Apache Software Foundation.
This software consists of contributions made by many people; see the NOTICE
file for more information.
Subversion is open source software, see http://subversion.apache.org/

The following repository back-end (FS) modules are available:

* fs_base : Module for working with a Berkeley DB repository.
* fs_fs : Module for working with a plain file (FSFS) repository.

Cyrus SASL authentication is available.
```

## 软件配置

### 修改根目录

subversion默认以/var/svn作为数据根目录，可以通过/etc/sysconfig/svnserve修改这个默认位置。

```
[root@localhost ~]# cat /etc/sysconfig/svnserve
# OPTIONS is used to pass command-line arguments to svnserve.
# 
# Specify the repository location in -r parameter:
OPTIONS="-r /var/svn"
```

```
[root@localhost ~]# vi /etc/sysconfig/svnserve
[root@localhost ~]# cat /etc/sysconfig/svnserve
# OPTIONS is used to pass command-line arguments to svnserve.
# 
# Specify the repository location in -r parameter:
#OPTIONS="-r /var/svn"
OPTIONS="-r /data/subversion"
[root@localhost ~]# 
```

### 创建根目录

```
mkdir -p /data/subversion
```

### 创建仓库

```
svnadmin create /data/subversion/repo1
```

为了能够统一管理SVN的用户、组、权限等，这里设置采用统一文件进行管理的方式。

复制仓库`repo1`中的`conf`目录下的`authz`和`passwd`文件到仓库根目录下，并重命名：
```bash
cp /data/subversion/repo1/conf/authz /data/subversion/svnauthz
cp /data/subversion/repo1/conf/passwd /data/subversion/svnpasswd
```
并且，修改仓库配置文件，将用户、权限控制配置项指向统一配置文件：
```bash
[root@localhost ~]# vi /data/subversion/repo1/conf/svnserve.conf

[general]
anon-access = read
auth-access = write
password-db = /data/subversion/svnpasswd
authz-db = /data/subversion/svnauthz
```

## 软件使用

### 服务启动

```
systemctl start svnserve.service
```

### 仓库访问

```
svn://service.hy-wux.com/repo1
```