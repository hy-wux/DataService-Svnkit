# Subversion整合Apache

本文档是基于前文[Subversion软件安装](Subversion软件安装.md)的基础上进行的。

## 软件安装

整合Apache，需要安装 Apache服务 和 mod_dav_svn 模块

```
yum install -y httpd mod_dav_svn
```

安装完成后，默认安装目录在`/etc/httpd`。

```
[root@localhost ~]# cd /etc/httpd
[root@localhost httpd]# ll
total 0
drwxr-xr-x 2 root root  59 Aug  5 09:27 conf
drwxr-xr-x 2 root root  82 Aug  5 08:43 conf.d
drwxr-xr-x 2 root root 172 Aug  5 08:43 conf.modules.d
lrwxrwxrwx 1 root root  19 Aug  5 08:43 logs -> ../../var/log/httpd
lrwxrwxrwx 1 root root  29 Aug  5 08:43 modules -> ../../usr/lib64/httpd/modules
lrwxrwxrwx 1 root root  10 Aug  5 08:43 run -> /run/httpd
[root@localhost httpd]# 
```

同时，在`modules`目录，即`/usr/lib64/httpd/modules`目录下，会有两个svn相关的类库：

```
[root@localhost httpd]# ll modules/*svn*
-rwxr-xr-x 1 root root  19504 Apr 11  2018 modules/mod_authz_svn.so
-rwxr-xr-x 1 root root 181344 Apr 11  2018 modules/mod_dav_svn.so
[root@localhost httpd]# ll /usr/lib64/httpd/modules/*svn*
-rwxr-xr-x 1 root root  19504 Apr 11  2018 /usr/lib64/httpd/modules/mod_authz_svn.so
-rwxr-xr-x 1 root root 181344 Apr 11  2018 /usr/lib64/httpd/modules/mod_dav_svn.so
[root@localhost httpd]# 
```

可以查看版本：

```
[root@localhost httpd]# apachectl -v
Server version: Apache/2.4.6 (CentOS)
Server built:   Jul 29 2019 17:18:49
[root@localhost httpd]#
```

## 软件配置

### Apache配置

为了使Apache能够与SVN整合，需要在其配置文件中添加一些信息。

在安装目录`/etc/httpd`下的`conf/httpd.conf`文件中需要添加以下内容：

```
LoadModule dav_svn_module     modules/mod_dav_svn.so
LoadModule authz_svn_module   modules/mod_authz_svn.so
```

实际上`conf/httpd.conf`文件中已经有以下默认配置：

```
Include conf.modules.d/*.conf
```

所以，以上两条配置信息是可以不需要进行配置的。

但是，下面的配置必须要有。在`conf/httpd.conf`中添加：

```
<Location /svn>
    DAV svn
    SVNParentPath /data/subversion
    AuthzSVNAccessFile /data/subversion/svnauthz
    AuthType Basic
    AuthName "Subversion Repository"
    AuthUserFile /data/subversion/svnpasswd
    Require valid-user
</Location>
```

上述配置，解释如下：

```
<Location /svn> # 指定了http的路径，即最终访问为：http://service.hy-wux.com/svn/repository
    DAV svn
    SVNParentPath /data/subversion # 指定SVN存储库的根目录
    AuthzSVNAccessFile /data/subversion/svnauthz # 指定SVN授权权限配置文件
    AuthType Basic
    AuthName "Subversion Repository"
    AuthUserFile /data/subversion/svnpasswd # 指定SVN用户密码配置文件
    Require valid-user
</Location>
```

### SVN配置

SVN配置比较简单，在保留前文[Subversion软件安装](Subversion软件安装.md)默认配置的基础上，创建密码文件即可（由于HTTP协议与SVN协议两种方式的用户密码规则不同，所以需要先删除SVN协议的密码文件，重新创建）：

```
rm -rf /data/subverion/svnpasswd
htpasswd -c svnpasswd wux

New password: 
Re-type new password: 
Adding password for user wux
```

## 软件启动

### 启动SVN

```
systemctl start svnserve
```

### 启动Apache

```
systemctl start httpd
```

### 仓库访问

```
http://service.hy-wux.com/svn/repo1
```

## 常见的问题

* 问题1：svn: E204900: Can't open file '/data/subversion/repo1/format': Permission denied.

SELinux权限问题，暂时先关闭selinux 

* 问题2：checkout代码和update代码都是可以成功的，但是commit就是不行，换了别人的账号也不行。 错误提示是：commit failed:could not begin a transaction。

在svnadmin create创建仓库目录时是root身份，所以，对httpd而言mod_dav_svn就没有write权限，修改目录增加写的权限即可。

或者，对SVN存储库根路径添加读写权限，用apache用户启动生产者。
