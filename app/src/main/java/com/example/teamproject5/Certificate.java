package com.example.teamproject5;

/**
 * 자격증 데이터 모델
 * - examDate: "yyyy-MM-dd" 형식의 시험 날짜 (null 허용)
 * - 백엔드 연동 시 examDate 필드를 API 응답에 맞게 파싱하면 됩니다.
 */
public class Certificate {

    private final int id;
    private final String name;
    private final String description;
    private final String category;
    private boolean favorite;
    private final String examDate; // "yyyy-MM-dd" 형식, 없으면 null

    public Certificate(int id, String name, String description, String category, boolean favorite) {
        this(id, name, description, category, favorite, null);
    }

    public Certificate(int id, String name, String description, String category, boolean favorite, String examDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.favorite = favorite;
        this.examDate = examDate;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getCategory() { return category; }

    public boolean isFavorite() { return favorite; }

    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    /** 시험 날짜 반환 */
    public String getExamDate() { return examDate; }
}