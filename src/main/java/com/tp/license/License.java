package com.tp.license;

//자격증 번호와 자격증 이름
public class License {
    private Long id;
    private String name;
    private String category;
    private Long exdate;

    public License(Long id, String name, String category,Long exdate) {
        this.id = id;
        this.name  = name;
        this.category = category;
        this.exdate = exdate;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Long getExdate() { return exdate; }
}
