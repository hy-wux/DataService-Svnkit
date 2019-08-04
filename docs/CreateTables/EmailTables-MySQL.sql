drop table if exists eds_t_email_server;

drop table if exists eds_t_email_account;

/*==============================================================*/
/* Table: eds_t_email_server                                    */
/*==============================================================*/
create table eds_t_email_server
(
     server_name        varchar(128) comment '服务器名称',
     email_protocol     varchar(32) comment '邮件协议名称',
     email_host         varchar(32) comment '邮件服务器主机',
     email_port         int comment '邮件服务器端口',
     row_key            varchar(32) not null comment '自然主键',
     create_person      varchar(32) comment '创建人',
     create_datetime    datetime comment '创建时间',
     update_person      varchar(32) comment '修改人',
     update_datetime    datetime comment '修改时间',
     primary key (row_key)
 );

alter table eds_t_email_server comment '电子邮件服务器';

/*==============================================================*/
/* Table: eds_t_email_account                                   */
/*==============================================================*/
create table eds_t_email_account
(
    server_key           varchar(32) not null comment '服务器主键',
    account_name         varchar(128) not null comment '账户名称',
    username             varchar(128) not null comment '邮箱地址',
    password             varchar(128) comment '邮箱密码',
    row_key              varchar(32) not null comment '自然主键',
    create_person        varchar(32) comment '创建人',
    create_datetime      datetime comment '创建时间',
    update_person        varchar(32) comment '修改人',
    update_datetime      datetime comment '修改时间',
    primary key (row_key)
);

alter table eds_t_email_account comment '电子邮件账号';
