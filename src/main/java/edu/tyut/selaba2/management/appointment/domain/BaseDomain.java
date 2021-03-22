package edu.tyut.selaba2.management.appointment.domain;

import java.sql.Timestamp;

/**
 * 基本的数据库映射对象
 *
 * @author KlenKiven
 */
public class BaseDomain {
    private Long id;
    private Timestamp createTime;
    private Timestamp lastModifiedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Timestamp lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
