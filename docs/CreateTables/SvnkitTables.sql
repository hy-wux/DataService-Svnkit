drop table if exists eds_t_svn_servers;

drop table if exists eds_t_svn_repositories;

drop table if exists eds_t_svn_groups;

drop table if exists eds_t_svn_users;

/*==============================================================*/
/* Table: eds_t_svn_servers                                     */
/*==============================================================*/
create table eds_t_svn_servers (
   server_name          varchar(128) comment '服务器名称',
   service_name         varchar(128) comment '微服务名称',
   server_address       varchar(256) comment '服务器路径',
   admin_name           varchar(128) comment '管理员名称',
   admin_account        varchar(32) comment '管理员账号',
   send_email           varchar(32) comment '发邮件邮箱',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '仓库表';

/*==============================================================*/
/* Table: eds_t_svn_repositories                                */
/*==============================================================*/
create table eds_t_svn_repositories (
   repository_name      varchar(128) comment '仓库名称',
   repository_type      varchar(32) comment '仓库类型',
   repository_desc      varchar(256) comment '仓库描述',
   contacts_person      varchar(128) comment '联系人',
   contacts_email       varchar(128) comment '联系人邮箱',
   server_key           varchar(32) comment '服务器',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '仓库表';

/*==============================================================*/
/* Table: eds_t_svn_groups                                      */
/*==============================================================*/
create table eds_t_svn_groups (
   group_name           varchar(128) comment '组别名称',
   group_desc           varchar(32) comment '组别描述',
   server_key           varchar(32) comment '服务器',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '组别表';

/*==============================================================*/
/* Table: eds_t_svn_users                                       */
/*==============================================================*/
create table eds_t_svn_users (
   username             varchar(128) comment '用户名称',
   password             varchar(128) comment '用户密码',
   staff_name           varchar(128) comment '员工名称',
   staff_num            varchar(32) comment '员工工号',
   staff_email          varchar(32) comment '员工邮箱',
   login_account        varchar(32) comment '登录账号',
   server_key           varchar(32) comment '服务器',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '用户表';
