-- auto-generated definition
create table user
(
    avatarUrl    varchar(1024)                      null comment '用户头像',
    username     varchar(128)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '账号',
    id           bigint auto_increment comment 'id'
        primary key,
    gender       tinyint                            null comment '性别',
    userPassword varchar(520)                       not null comment '用户密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    userStatus   int      default 0                 not null comment '状态 ',
    updateTime   datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色 0-普通用户  1-管理员',
    planetCode   varchar(512)                       null comment '平台专属编号'
)
    comment '用户' engine = InnoDB;

