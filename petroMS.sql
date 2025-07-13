create schema petroMS collate utf8mb4_0900_ai_ci;

use petroMS;
-- 生产管理模块
CREATE TABLE plan_schedule (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '计划调度表主键，唯一标识每条生产计划记录',
    plan_name VARCHAR(100) NOT NULL COMMENT '生产计划的名称',
    begin_date DATE NOT NULL COMMENT '生产计划的开始日期',
    end_date DATE NOT NULL COMMENT '生产计划的结束日期',
    status smallint NOT NULL COMMENT '生产计划的状态，如未开始0、1进行中、2已完成等',
    description VARCHAR(255) COMMENT '生产计划的描述信息',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录生产计划创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录生产计划更新的时间'
) charset = utf8mb4;

CREATE TABLE process_monitor (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '过程监控表主键，唯一标识每条监控记录',
    plan_schedule_id INT NOT NULL COMMENT '关联 plan_schedule 表的 id，标识该监控数据所属的生产计划',
    process_name VARCHAR(100) NOT NULL COMMENT '生产过程的名称',
    monitor_time DATETIME NOT NULL COMMENT '监控数据发生的时间点',
    status VARCHAR(20) NOT NULL COMMENT '生产过程的状态，如正常、异常等',
    remarks VARCHAR(255) COMMENT '监控数据的备注信息',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录监控数据创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录监控数据更新的时间'
) charset = utf8mb4;

CREATE TABLE quality_manage (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '质量管理表主键，唯一标识每条质量检验记录',
    plan_schedule_id INT NOT NULL COMMENT '关联 plan_schedule 表的 id，标识该质量检验对应的生产计划',
    product_id INT NOT NULL COMMENT '产品的编号',
    inspect_time DATETIME NOT NULL COMMENT '产品的检验日期',
    result VARCHAR(20) NOT NULL COMMENT '产品的检验结果，如合格、不合格等',
    inspector VARCHAR(20) NOT NULL COMMENT '执行检验的人员姓名',
    remarks VARCHAR(255) COMMENT '质量检验的备注信息',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录质量检验数据创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录质量检验数据更新的时间'
) charset = utf8mb4;

CREATE TABLE equipment_manage (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '设备管理表主键，唯一标识每条设备记录',
    equipment_name VARCHAR(50) NOT NULL COMMENT '设备的名称',
    type VARCHAR(20) NOT NULL COMMENT '设备的类型，如反应釜、泵等',
    status VARCHAR(20) NOT NULL COMMENT '设备的状态，如可用、维修中、报废等',
    last_maintenance DATETIME COMMENT '设备上次维护的日期',
    location VARCHAR(100) COMMENT '设备的存放位置',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录设备信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录设备信息更新的时间'
) charset = utf8mb4;

-- 供应链管理模块
CREATE TABLE purchase_manage (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '采购管理表主键，唯一标识每条采购订单记录',
    supplier_id INT NOT NULL COMMENT '供应商的编号',
    item_name VARCHAR(100) NOT NULL COMMENT '采购的物品名称',
    item_num INT NOT NULL COMMENT '采购的物品数量',
    item_price DECIMAL(10, 2) NOT NULL COMMENT '采购物品的单价，精确到小数点后 2 位',
    purchase_date DATETIME NOT NULL COMMENT '采购的日期',
    status VARCHAR(20) NOT NULL COMMENT '采购订单的状态，如已下单、已收货等',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录采购订单信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录采购订单信息更新的时间'
) charset = utf8mb4;

CREATE TABLE inventory_manage (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '库存管理表主键，唯一标识每条库存记录',
    item_name VARCHAR(100) NOT NULL COMMENT '库存物品的名称',
    item_num INT NOT NULL COMMENT '库存物品的数量',
    location VARCHAR(100) COMMENT '库存物品的存放位置',
    last_update DATE COMMENT '库存信息的上次更新日期',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录库存信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录库存信息更新的时间'
) charset = utf8mb4;

CREATE TABLE logistics_manage (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '物流管理表主键，唯一标识每条物流记录',
    purchase_id INT NOT NULL COMMENT '关联采购表的 id，标识该物流记录对应的采购订单',
    origin VARCHAR(100) NOT NULL COMMENT '物流运输的起始地',
    destination VARCHAR(100) NOT NULL COMMENT '物流运输的目的地',
    status VARCHAR(20) NOT NULL COMMENT '物流运输的状态，如运输中、已到达等',
    ship_date DATETIME NOT NULL COMMENT '物流发货的日期',
    arrival_date DATETIME COMMENT '物流预计到达的日期',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录物流信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录物流信息更新的时间'
) charset = utf8mb4;

-- 销售与市场模块
CREATE TABLE customer_manage (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '客户管理表主键，唯一标识每条客户记录',
    name VARCHAR(100) NOT NULL COMMENT '客户的名称',
    phone VARCHAR(11) COMMENT '客户的电话',
    email VARCHAR(50) COMMENT '客户的邮箱',
    address VARCHAR(255) COMMENT '客户的地址',
    industry VARCHAR(50) COMMENT '客户所属的行业',
    remarks VARCHAR(255) COMMENT '关于客户的备注信息',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录客户信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录客户信息更新的时间'
) charset = utf8mb4;

CREATE TABLE order_manage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '销售订单管理表主键，唯一标识每条销售订单记录',
    total_fee INT NOT NULL COMMENT '订单总价格',
    customer_id INT NOT NULL COMMENT '关联 customer_manage 表的 id，标识该订单对应的客户',
    order_address VARCHAR(255) NOT NULL COMMENT '订单收货地址',
    status VARCHAR(20) NOT NULL COMMENT '销售订单的状态，如已下单、已取消、已发货、已完成等',
    pay_type VARCHAR(20) COMMENT '订单支付方式',
    pay_time DATETIME COMMENT '订单支付时间',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录销售订单信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录销售订单信息更新的时间'
) charset = utf8mb4;

CREATE TABLE order_detail (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '订单详情管理表主键，唯一标识每条记录',
    order_no BIGINT NOT NULL COMMENT '销售订单的单号',
    product_id INT NOT NULL COMMENT '产品的编号',
    quantity INT NOT NULL COMMENT '销售的产品数量',
    price DECIMAL(10, 2) NOT NULL COMMENT '销售产品的单价，精确到小数点后 2 位',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录销售订单信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录销售订单信息更新的时间'
) charset = utf8mb4;

CREATE TABLE market_analysis (
    product_id INT NOT NULL COMMENT '产品的编号',
    month DATE NOT NULL COMMENT '月份',
    sales_volume INT NOT NULL COMMENT '产品的销售数量',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录市场分析信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录市场分析信息更新的时间'
) charset = utf8mb4;

-- 财务管理模块
CREATE TABLE cost_accounting (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '成本核算表主键，唯一标识每条成本核算记录',
    item_name VARCHAR(100) NOT NULL COMMENT '成本核算的项目名称',
    electricity_cost INT DEFAULT 0 COMMENT '电费成本，以分为单位',
    labor_cost INT DEFAULT 0 COMMENT '人力费用成本，以分为单位',
    material_cost INT DEFAULT 0 COMMENT '原材料成本，以分为单位',
    equipment_cost INT DEFAULT 0 COMMENT '设备维护成本，以分为单位',
    transportation_cost INT DEFAULT 0 COMMENT '运输成本，以分为单位',
    total_cost INT DEFAULT 0 COMMENT '总成本，以分为单位',
    remarks VARCHAR(255) COMMENT '成本核算的备注信息',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录成本核算信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录成本核算信息更新的时间'
) charset = utf8mb4;

CREATE TABLE budget_manage (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '预算管理表主键，唯一标识每条预算记录',
    month DATE NOT NULL COMMENT '预算对应的月份',
    department VARCHAR(50) NOT NULL COMMENT '预算所属的部门',
    budget_amount INT NOT NULL COMMENT '部门的预算金额，以分为单位',
    used_amount INT DEFAULT 0 COMMENT '部门已经使用的预算金额，以分为单位',
    remaining_amount INT COMMENT '剩余预算金额，以分为单位，通过计算得出',
    status VARCHAR(20) DEFAULT '待批准' COMMENT '预算状态，如已批准、待批准、已取消',
    remarks VARCHAR(255) COMMENT '预算管理的备注信息',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录预算信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录预算信息更新的时间'
) charset = utf8mb4;

CREATE TABLE finance_management (
    transaction_no INT PRIMARY KEY COMMENT '财务管理表主键，唯一标识每条财务交易记录',
    type VARCHAR(20) NOT NULL COMMENT '财务交易的类型，限定为收入、支出',
    amount INT NOT NULL COMMENT '财务交易的金额，以分为单位',
    transaction_date DATE NOT NULL COMMENT '财务交易的日期',
    description VARCHAR(255) COMMENT '财务交易的描述信息',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录财务交易信息创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录财务交易信息更新的时间'
) charset = utf8mb4;

-- 系统用户权限管理模块
CREATE TABLE user (
    id          BIGINT                 PRIMARY KEY AUTO_INCREMENT COMMENT '用户表主键，唯一标识每个用户',
    username    VARCHAR(45)            NOT NULL COMMENT '用户登录名',
    password    VARCHAR(96)            NOT NULL COMMENT '用户密码，加密存储',
    salt        VARCHAR(45)            NULL COMMENT '密码加密盐值',
    
    name        VARCHAR(45)            NULL COMMENT '用户真实姓名',
    sex         CHAR                   NULL COMMENT '用户性别',
    email       VARCHAR(20)            NULL COMMENT '用户邮箱',
    cellphone   VARCHAR(11)            NULL COMMENT '用户手机号',
    qq          VARCHAR(32)            NULL COMMENT '用户QQ号',
    
    status      VARCHAR(32)            NOT NULL COMMENT '用户状态，如启用、禁用等',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户更新的时间'
) charset = utf8mb4;

CREATE TABLE role (
    id          INT  PRIMARY KEY AUTO_INCREMENT COMMENT '角色表主键，唯一标识每个角色',
    role_name   VARCHAR(100) NOT NULL COMMENT '角色名称',
    role_code   VARCHAR(100) NOT NULL COMMENT '角色代码',
    description VARCHAR(255) NULL COMMENT '角色描述',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户角色创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户角色更新的时间'
) charset = utf8mb4;

CREATE TABLE user_role (
    id          INT      PRIMARY KEY AUTO_INCREMENT COMMENT '用户角色关联表主键',
    user_id     BIGINT   NOT NULL COMMENT '关联用户表的id',
    role_id     INT      NOT NULL COMMENT '关联角色表的id',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户角色之间关系创建的时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户角色之间关系更新的时间',
    creator     VARCHAR(255) NULL COMMENT '创建人'
) charset = utf8mb4;

CREATE TABLE permission (
    id          INT   PRIMARY KEY AUTO_INCREMENT COMMENT '权限表主键',
    role_id     INT   NOT NULL COMMENT '关联角色表的id',
    permit      VARCHAR(255) NOT NULL COMMENT '权限标识',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '权限创建的时间'
) charset = utf8mb4;    