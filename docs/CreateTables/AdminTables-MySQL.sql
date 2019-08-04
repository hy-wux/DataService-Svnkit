drop table if exists sys_dept;

drop table if exists sys_dict_code;

drop table if exists sys_dict_type;

drop table if exists sys_menu;

drop table if exists sys_params;

drop table if exists sys_relation;

drop table if exists sys_role;

drop table if exists sys_user;

/*==============================================================*/
/* Table: sys_dept                                              */
/*==============================================================*/
create table sys_dept
(
   pid                  varchar(32) comment '上级节点',
   dept_name            varchar(128) comment '部门名称',
   sort_number          int comment '排序序号',
   leader               varchar(128) comment '负责人',
   phone                varchar(32) comment '联系电话',
   email                varchar(128) comment '电子邮箱',
   address              varchar(512) comment '联系地址',
   dept_status          varchar(32) comment '部门状态',
   dept_remark          varchar(512) comment '部门备注',
   module_name          varchar(32) comment '模块名称',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
);

alter table sys_dept comment '系统部门表';

/*==============================================================*/
/* Table: sys_dict_code                                         */
/*==============================================================*/
create table sys_dict_code
(
   code_value           varchar(32) comment '代码值',
   code_text            varchar(128) comment '代码文本',
   code_comment         varchar(512) comment '代码描述',
   dict_type            varchar(32) comment '代码类型',
   sort_number          int comment '排序序号',
   css_style            varchar(512) comment '展现样式',
   list_style           varchar(128) comment '列表样式',
   is_default           varchar(32) comment '是否默认',
   code_status          varchar(32) comment '代码状态',
   module_name          varchar(32) comment '模块名称',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
);

alter table sys_dict_code comment '字典码值';

/*==============================================================*/
/* Table: sys_dict_type                                         */
/*==============================================================*/
create table sys_dict_type
(
   type_code            varchar(32) comment '类型代码',
   type_name            varchar(128) comment '类型名称',
   type_comment         varchar(512) comment '类型描述',
   type_status          varchar(32) comment '类型状态',
   type_remark          varchar(512) comment '类型备注',
   module_name          varchar(32) comment '模块名称',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
);

alter table sys_dict_type comment '字典类型';

/*==============================================================*/
/* Table: sys_menu                                              */
/*==============================================================*/
create table sys_menu
(
   pid                  varchar(32) comment '上级节点',
   menu_type            varchar(32) comment '菜单类型',
   menu_name            varchar(128) comment '菜单名称',
   url                  varchar(512) comment 'URL地址',
   permission           varchar(128) comment '权限',
   sort_number          int comment '排序序号',
   icon                 varchar(128) comment '图标',
   menu_status          varchar(32) comment '菜单状态',
   levels               int comment '层级',
   menu_remark          varchar(512) comment '菜单备注',
   module_name          varchar(32) comment '模块名称',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
);

alter table sys_menu comment '系统菜单表';

/*==============================================================*/
/* Table: sys_params                                            */
/*==============================================================*/
create table sys_params
(
   param_code           varchar(128) comment '参数代码',
   param_name           varchar(128) comment '参数名称',
   param_value          varchar(128) comment '参数值',
   param_status         varchar(32) comment '参数状态',
   module_name          varchar(32) comment '模块名称',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
);

alter table sys_params comment '参数值';

/*==============================================================*/
/* Table: sys_relation                                          */
/*==============================================================*/
create table sys_relation
(
   module_name          varchar(32) comment '模块名称',
   roleid               varchar(32) comment '角色ID',
   menuid               varchar(32) comment '菜单ID'
);

alter table sys_relation comment '角色菜单表';

/*==============================================================*/
/* Table: sys_role                                              */
/*==============================================================*/
create table sys_role
(
   role_code            varchar(32) comment '角色代码',
   role_name            varchar(128) comment '角色名称',
   role_comment         varchar(512) comment '角色描述',
   role_status          varchar(32) comment '角色状态',
   role_remark          varchar(512) comment '角色备注',
   module_name          varchar(32) comment '模块名称',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
);

alter table sys_role comment '系统角色表';

/*==============================================================*/
/* Table: sys_user                                              */
/*==============================================================*/
create table sys_user
(
   avatar               varchar(512) comment '用户头像',
   user_account         varchar(32) comment '用户账号',
   user_pass            varchar(32) comment '用户密码',
   user_salt            varchar(32) comment '盐',
   user_name            varchar(128) comment '用户名称',
   birthday             date comment '生日',
   sex                  varchar(32) comment '性别',
   email                varchar(128) comment '电子邮箱',
   phone                varchar(32) comment '联系电话',
   roleids              varchar(512) comment '角色列表',
   deptid               varchar(32) comment '部门ID',
   user_status          varchar(32) comment '用户状态',
   module_name          varchar(32) comment '模块名称',
   row_key              varchar(32) not null comment '自然主键',
   create_person        varchar(32) comment '创建人',
   create_datetime      datetime comment '创建时间',
   update_person        varchar(32) comment '修改人',
   update_datetime      datetime comment '修改时间',
   primary key (row_key)
);

alter table sys_user comment '系统用户表';
