package com.tp.licenseList;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "licenseList") // DB Browser에서 만든 테이블 이름과 똑같아야 함
public class License {
    @PrimaryKey
    public int id;

    public String category;
    public String name;
    public String exdate;
    public String qualification;
    public String form;
    public String note;
}
