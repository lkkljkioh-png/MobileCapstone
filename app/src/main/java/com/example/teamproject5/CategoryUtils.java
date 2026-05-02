package com.example.teamproject5;

/**
 * 카테고리 ID → 한글 라벨 변환 유틸
 *
 * CertificateAdapter, MyActivity, CalendarFragment 등
 * 카테고리를 표시하는 모든 곳에서 이 메서드를 사용합니다.
 * 새 카테고리 추가 시 이 파일만 수정하면 됩니다.
 */
public final class CategoryUtils {

    private CategoryUtils() {
        // 인스턴스화 방지
    }

    public static String toLabel(String categoryId) {
        if (categoryId == null) return "";
        switch (categoryId) {
            case "category_tech":     return "IT·기술";
            case "category_industry": return "건설·산업·안전";
            case "category_business": return "경영·사무·금융";
            case "category_primary":  return "농림·해양·식품";
            case "category_creative": return "예술·미디어";
            case "category_service":  return "생활·서비스·교육";
            default:                  return categoryId;
        }
    }
}
