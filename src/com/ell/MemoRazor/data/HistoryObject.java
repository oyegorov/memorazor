package com.ell.MemoRazor.data;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public abstract class HistoryObject {
    public static final String CREATED_DATE_COLUMN = "createdDate";
    public static final String MODIFIED_DATE_COLUMN = "modifiedDate";

    protected HistoryObject() {
        createdDate = new Date();
        modifiedDate = new Date();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @DatabaseField(columnName = CREATED_DATE_COLUMN, index = true)
    protected Date createdDate;

    @DatabaseField(columnName = MODIFIED_DATE_COLUMN)
    protected Date modifiedDate;
}
