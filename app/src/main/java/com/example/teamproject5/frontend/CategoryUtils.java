package com.example.teamproject5.frontend;

import java.util.ArrayList;
import java.util.List;

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
            case "category_primary":    return "농림·해양·식품";

            case "category_tech":       return "IT·기술";

            case "category_business":   return "경영·사무·금융";

            case "category_industry":   return "건설·산업·안전";

            case "category_creative":   return "예술·미디어";

            case "category_service":    return "생활·서비스·교육";

            default: return categoryId;
        }
    }

    public static List<String> getDbCategories(String categoryId) {
        List<String> list = new ArrayList<>();

        switch (categoryId) {
            case "category_primary":
                list.add("농림/축산");
                list.add("식품");
                list.add("해양");
                break;

            case "category_tech":
                list.add("IT");
                list.add("기술");
                break;

            case "category_business":
                list.add("사무");
                list.add("금융");
                break;

            case "category_industry":
                list.add("건설");
                list.add("소방");
                break;

            case "category_creative":
                list.add("교양");
                list.add("디자인");
                list.add("문화/예술");
                list.add("미디어/언론");
                break;

            case "category_service":
                list.add("미용");
                list.add("보건/복지");
                list.add("보건의료");
                list.add("어학");
                list.add("항공");
                list.add("환경");
                break;
        }

        return list;
    }
}