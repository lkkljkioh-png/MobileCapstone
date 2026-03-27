package com.example.teamproject5;

import java.util.ArrayList;
import java.util.List;

public class CertificateRepository {

    public List<Certificate> getCertificates() {
        // 지금은 더미 데이터 반환
        // 나중에는 API 호출 결과로 교체하면 됨
        return new ArrayList<>(DummyData.getDummyCertificates());
    }

    public boolean toggleFavorite(Certificate certificate) {
        boolean newFavoriteState = !certificate.isFavorite();
        certificate.setFavorite(newFavoriteState);

        // 나중에는 여기서 서버에 즐겨찾기 저장 요청 가능
        return newFavoriteState;
    }
}