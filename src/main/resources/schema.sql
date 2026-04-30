-- 创建鱼标签数据表
CREATE TABLE IF NOT EXISTS fish_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tag_id BIGINT NOT NULL COMMENT '鱼标签编号（十进制）',
    hex_tag_id VARCHAR(20) NOT NULL COMMENT '鱼标签编号（十六进制）',
    signal_strength INT COMMENT '信号强度',
    timeout INT COMMENT '超时时间',
    receive_time DATETIME NOT NULL COMMENT '数据接收时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    UNIQUE KEY uk_tag_id (tag_id),
    INDEX idx_receive_time (receive_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='鱼标签数据表';
