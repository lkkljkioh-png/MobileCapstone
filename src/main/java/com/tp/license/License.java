package com.tp.license;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//@Entity를 이용해서 DB와 연결
@Entity
@Table(name = "licensedata")
public class License {

    public License() {}

    @Id //primaryKey 명시
    private Integer id;

    private String category;
    private String name;
    private String exdate;
    private String qualification;
    private String form;
    private String note;

    public void setId(Integer id) { this.id = id; }
    public void setCategory(String category) { this.category = category; }
    public void setName(String name) { this.name = name; }
    public void setExdate(String exdate) { this.exdate = exdate; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public void setForm(String form) { this.form = form; }
    public void setNote(String note) { this.note = note; }

    public Integer getId() { return id; }
    public String getCategory() { return category; }
    public String getName() { return name; }
    public String getExdate() { return exdate; }
    public String getQualification() { return qualification; }
    public String getForm() { return form; }
    public String getNote() { return note; }

}


