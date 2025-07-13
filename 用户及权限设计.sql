create table user
(
    id          BIGINT                 primary key AUTO_INCREMENT,
    username    varchar(45)            not null,
    password    varchar(96)            not null,
    salt        varchar(45)            null,
    
    name        varchar(45)            null,
    sex         char                   null,
    email       varchar(20)            null,
    cellphone   varchar(11)            null,
    qq          varchar(32)            null,
    
    status      varchar(32)            not null comment '用户状态',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户更新的时间'
)
    charset = utf8mb4;

create table role
(
    id          int  primary key AUTO_INCREMENT,
    role_name   varchar(100) not null,
    role_code   varchar(100) not null,
    description varchar(255) null,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户角色创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户角色更新的时间'
)
    charset = utf8mb4;


create table user_role
(
    id          int      primary key AUTO_INCREMENT,
    user_id     BIGINT   not null,
    role_id     int      not null,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户角色之间关系创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT  '用户角色之间关系更新的时间',
    creator     varchar(255) null
)
    charset = utf8mb4;

create table permission
(
    id          int   primary key AUTO_INCREMENT,
    role_id     int   not null,
    permit      varchar(255) not null,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '权限创建的时间'
)
    charset = utf8mb4
