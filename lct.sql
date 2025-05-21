create table b_bid_info
(
    id         int auto_increment comment '投标记录ID'
        primary key,
    prod_id    int                         not null comment '产品ID',
    uid        int                         not null comment '用户ID',
    bid_money  decimal(11, 2) default 0.00 not null comment '投标金额',
    bid_time   datetime                    not null comment '投标时间',
    bid_status int                         not null comment '投标状态'
)
    comment '投资记录表' charset = utf8mb3;

create table b_income_record
(
    id            int auto_increment
        primary key,
    uid           int                         not null comment '用户ID',
    prod_id       int                         not null comment '产品ID',
    bid_id        int                         not null comment '投标记录ID',
    bid_money     decimal(11, 2) default 0.00 not null comment '投资金额',
    income_date   date                        not null comment '到期时间',
    income_money  decimal(11, 2)              not null comment '收益金额',
    income_status int                         not null comment '收益状态（0未返，1已返）'
)
    comment '收益记录表' charset = utf8mb3;

create table b_product_record
(
    id                 int auto_increment
        primary key,
    product_name       varchar(50)                 not null comment '产品名称',
    rate               decimal(11, 2)              not null comment '产品利率',
    cycle              int                         not null comment '产品期限',
    release_time       date                        not null comment '产品发布时间',
    product_type       int                         not null comment '产品类型 0新手宝，1优选产品，2散标产品',
    product_no         varchar(50)                 not null comment '产品编号',
    product_money      decimal(11, 2) default 0.00 not null comment '产品金额',
    left_product_money decimal(11, 2)              not null comment '产品剩余可投金额',
    bid_min_limit      decimal(11, 2)              not null comment '最低投资金额，即起投金额',
    bid_max_limit      decimal(11, 2)              not null comment '最高投资金额，即最多能投多少金额',
    product_status     int                         not null comment '产品状态（0未满标，1已满标，2满标已生成收益计划）',
    product_full_time  datetime                    null comment '产品投资满标时间',
    product_desc       varchar(50)                 not null comment '产品描述'
)
    comment '产品信息表' charset = utf8mb3;

create table b_recharge_record
(
    id              int auto_increment
        primary key,
    uid             int                         not null comment '用户id',
    recharge_no     varchar(50)                 not null comment '充值订单号',
    recharge_status int                         not null comment '充值订单状态（0充值中，1充值成功，2充值失败）',
    recharge_money  decimal(11, 2) default 0.00 not null comment '充值金额',
    recharge_time   datetime                    not null comment '充值时间',
    recharge_desc   varchar(50)                 not null comment '充值描述',
    channel         varchar(50)    default ''   null
)
    comment '充值记录表' charset = utf8mb3;

create table u_finance_account
(
    id              int auto_increment
        primary key,
    uid             int                         not null comment '用户ID',
    available_money decimal(11, 2) default 0.00 not null comment '用户可用资金'
)
    comment '用户财务资金账户表' charset = utf8mb3;

create table u_user
(
    id              int auto_increment comment '用户ID，主键'
        primary key,
    phone           varchar(11) not null comment '注册手机号码',
    login_password  varchar(32) not null comment '登录密码，密码长度最大16位',
    name            varchar(16) null comment '用户姓名',
    id_card         varchar(18) null comment '用户身份证号码',
    add_time        datetime    null comment '注册时间',
    last_login_time datetime    null comment '最近登录时间',
    header_image    varchar(50) null comment '用户头像文件路径',
    constraint user_phone
        unique (phone)
)
    comment '用户表' charset = utf8mb3;


