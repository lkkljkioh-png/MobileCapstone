package com.example.teamproject5;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * 자격증 데이터 Repository
 *
 * 즐겨찾기 저장 방식: SharedPreferences
 *   - key: "fav_" + certificate.getId()
 *   - value: boolean (true = 즐겨찾기)
 *
 * 백엔드 연동 포인트:
 *   - getCertificates(): API 응답으로 교체
 *   - toggleFavorite(): 서버 즐겨찾기 저장 API 호출 추가
 */
public class CertificateRepository {

    private static final String PREF_NAME = "favorite_prefs";
    private static final String KEY_PREFIX = "fav_";

    private final SharedPreferences prefs;

    public CertificateRepository(Context context) {
        // Application Context 사용 → 메모리 누수 방지
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 자격증 목록 반환.
     * SharedPreferences 에 저장된 즐겨찾기 상태를 반영합니다.
     *
     * 백엔드 연동 시: DummyData 대신 API 응답 파싱 결과를 사용하세요.
     */
    public List<Certificate> getCertificates() {
        List<Certificate> list = new ArrayList<>(DummyData.getDummyCertificates());

        // 저장된 즐겨찾기 상태를 각 자격증에 반영
        for (Certificate cert : list) {
            boolean savedFav = prefs.getBoolean(KEY_PREFIX + cert.getId(), cert.isFavorite());
            cert.setFavorite(savedFav);
        }

        return list;
    }

    /**
     * 즐겨찾기 토글 후 SharedPreferences 에 저장합니다.
     *
     * @return 변경된 즐겨찾기 상태 (true = 추가됨)
     */
    public boolean toggleFavorite(Certificate certificate) {
        boolean newState = !certificate.isFavorite();
        certificate.setFavorite(newState);

        // 로컬 저장
        prefs.edit()
                .putBoolean(KEY_PREFIX + certificate.getId(), newState)
                .apply();

        // 백엔드 연동 시 여기에 서버 API 호출 추가
        // ApiClient.updateFavorite(certificate.getId(), newState);

        return newState;
    }
}