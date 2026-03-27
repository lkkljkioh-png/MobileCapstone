package com.example.teamproject5;

import java.util.ArrayList;
import java.util.List;

public final class DummyData {

    private DummyData() {
        // 인스턴스화 방지
    }

    public static List<Certificate> getDummyCertificates() {
        List<Certificate> list = new ArrayList<>();

        int id = 1;

        list.add(new Certificate(id++, "정보처리기사", "IT 개발 및 시스템 관련 대표 자격증", "기술 및 산업", false));
        list.add(new Certificate(id++, "산업안전기사", "산업 현장의 안전 관리 및 예방 관련 자격증", "기술 및 산업", true));
        list.add(new Certificate(id++, "전산회계 1급", "회계 및 세무 실무 능력을 평가하는 자격증", "경영 및 사무", false));
        list.add(new Certificate(id++, "컴퓨터활용능력 1급", "문서 및 엑셀 활용 능력 자격증", "경영 및 사무", true));
        list.add(new Certificate(id++, "한국사능력검정시험 1급", "한국사 이해도를 평가하는 시험", "교육", false));
        list.add(new Certificate(id++, "국제의료관광코디네이터전문자격검정", "의료 관광 관련 자격", "의료", false));
        list.add(new Certificate(id++, "TOEIC", "영어 능력 평가 시험", "언어", false));
        list.add(new Certificate(id++, "JLPT N1", "일본어 최고 난도 시험", "언어", true));
        list.add(new Certificate(id++, "투자자산운용사", "금융 자산 운용 관련 자격", "금융", false));
        list.add(new Certificate(id++, "AFPK", "재무 설계 관련 자격", "금융", false));

        // 더미 50개
        for (int i = 1; i <= 40; i++) {
            list.add(new Certificate(
                    id++,
                    "더미 자격증 " + i,
                    "RecyclerView 테스트용 설명 " + i,
                    "기타",
                    i % 3 == 0
            ));
        }

        return list;
    }
}