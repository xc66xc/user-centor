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

-- team
create table team
(
    id           bigint auto_increment comment 'id' primary key,
    name     varchar(256)                    not   null comment '队伍名称',
    description    varchar(1024)                      null comment '描述',
    maxNum  int   default 1                 not   null comment '最大人数',
    expireTime       datetime                            null comment '过期时间',
    userId bigint                      comment '用户id',
    status        int           default 0 not null          comment '0 - 公开，1 - 私有，2 - 加密',
    password        varchar(512)                       null comment '密码',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP  null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 not null comment '是否删除'
)comment '队伍';

-- 用户关联表
-- team
create table user_team
(
    id           bigint auto_increment comment 'id' primary key,
    userId bigint                      comment '用户id',
    teamId bigint                      comment '队伍id',
    joinTime datetime null comment '加入时间',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP  null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 not null comment '是否删除'
)comment '用户队伍关联表';