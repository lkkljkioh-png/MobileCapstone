package com.example.teamproject5;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * 자격증 데이터 및 즐겨찾기 관리 Repository
 *
 * 역할:
 *   - 전체 자격증 목록 제공 (현재 DummyData 기반)
 *   - 즐겨찾기 상태를 SharedPreferences에 저장/조회/토글
 *   - 앱 전체에서 동일한 즐겨찾기 저장소를 공유하는 단일 창구
 *
 * 즐겨찾기 저장 방식:
 *   - 저장소 : SharedPreferences ("favorite_prefs")
 *   - 키 형식 : "fav_{자격증 id}"  (예: "fav_1", "fav_2")
 *   - 값      : boolean (true = 즐겨찾기됨)
 *
 * 주요 메서드:
 *   - getCertificates()        : 전체 자격증 목록 반환 (저장된 즐겨찾기 상태 반영)
 *   - toggleFavorite(cert)     : Certificate 객체 기반 즐겨찾기 토글 — 리스트 화면에서 사용
 *   - toggleFavoriteById(id)   : id 기반 즐겨찾기 토글 — DetailActivity에서 사용
 *   - isFavoriteById(id)       : id 기반 즐겨찾기 상태 조회 — DetailActivity 하트 UI 갱신에 사용
 *
 * 백엔드 연동 포인트:
 *   - getCertificates()      : DummyData 대신 API 응답 파싱 결과로 교체
 *   - toggleFavorite()       : SharedPreferences 저장 후 서버 즐겨찾기 API 호출 추가
 *   - toggleFavoriteById()   : 동일하게 서버 API 호출 추가 (주석 처리된 ApiClient 참고)
 */

public class CertificateRepository {

    private static final String PREF_NAME = "favorite_prefs";
    private static final String KEY_PREFIX = "fav_";

    private final SharedPreferences prefs;

    public CertificateRepository(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public List<Certificate> getCertificates() {
        List<Certificate> list = new ArrayList<>(DummyData.getDummyCertificates());

        for (Certificate cert : list) {
            boolean savedFav = prefs.getBoolean(KEY_PREFIX + cert.getId(), cert.isFavorite());
            cert.setFavorite(savedFav);
        }

        return list;
    }

    public boolean toggleFavorite(Certificate certificate) {
        boolean newState = !certificate.isFavorite();
        certificate.setFavorite(newState);

        prefs.edit()
                .putBoolean(KEY_PREFIX + certificate.getId(), newState)
                .apply();

        return newState;
    }

    /**
     * certId만으로 즐겨찾기 토글.
     * DetailActivity처럼 Certificate 객체 없이 id만 아는 경우 사용.
     * 백엔드 연동 시 여기에 서버 API 호출 추가.
     *
     * @return 변경된 즐겨찾기 상태 (true = 추가됨)
     */
    public boolean toggleFavoriteById(int id) {
        boolean current = prefs.getBoolean(KEY_PREFIX + id, false);
        boolean newState = !current;

        prefs.edit()
                .putBoolean(KEY_PREFIX + id, newState)
                .apply();

        // 백엔드 연동 시 여기에 서버 API 호출 추가
        // ApiClient.updateFavorite(id, newState);

        return newState;
    }

    /**
     * certId로 현재 즐겨찾기 상태 조회.
     */
    public boolean isFavoriteById(int id) {
        return prefs.getBoolean(KEY_PREFIX + id, false);
    }
}