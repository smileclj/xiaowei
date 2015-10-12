package com.clj.panda.model.entity.test;

import java.io.Serializable;

/**
 * Created by wana on 2015/10/10.
 */
public class TestCourse implements Serializable{
    private static final long serialVersionUID = -8365948491418654260L;

    private String id;
    private String name;
    private String remark;
    private long creationTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
}
