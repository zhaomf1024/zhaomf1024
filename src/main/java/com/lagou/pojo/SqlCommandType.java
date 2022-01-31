package com.lagou.pojo;

/**
 * SQL 操作类型
 */
public enum SqlCommandType {
    /**
     * 插入
     */
    INSERT,
    /**
     *
     */
    DELETE,

    /**
     * 更新
     */
    UPDATE,
    /**
     * 查询
     */
    SELECT,

    /**
     * 未知
     */
    UNKNOWN;



    SqlCommandType() {
    }
}
