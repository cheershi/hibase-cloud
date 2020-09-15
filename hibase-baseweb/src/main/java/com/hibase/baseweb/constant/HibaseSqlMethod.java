package com.hibase.baseweb.constant;

public enum HibaseSqlMethod {

    /**
     * 修改
     */
    UPDATE_NULL_BY_ID("updateByNullId", "根据ID 选择修改数据", "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>"),
    UPDATE_NULL("updateNull", "根据 whereEntity 条件，更新记录", "<script>\nUPDATE %s %s %s\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

    HibaseSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
