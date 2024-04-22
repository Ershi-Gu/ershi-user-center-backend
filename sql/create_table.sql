-- auto-generated definition
create table user
(
    id            bigint auto_increment comment 'id'
        primary key,
    user_name     varchar(256)                       null comment '用户昵称',
    user_account  varchar(256)                       null comment '账号',
    user_password varchar(512)                       not null comment '密码',
    avatar_url    varchar(1024)                      null comment '用户头像',
    gender        tinyint                            null comment '性别',
    phone_num     varchar(128)                       null comment '电话',
    email         varchar(512)                       null comment '邮箱',
    user_status   int      default 0                 not null comment '状态 0 - 正常',
    create_time   datetime default CURRENT_TIMESTAMP null comment '数据创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '数据更新时间',
    is_delete     tinyint  default 0                 not null comment '该条数据是否删除',
    user_role     int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    constraint user_account
        unique (user_account)
)
    comment '用户';

