package com.example.teamproject5.frontend;

import android.content.Context;

import com.example.teamproject5.database.AppDatabase;
import com.example.teamproject5.database.LicenseDatabase;
import com.example.teamproject5.database.entity.CertificateEntity;
import com.example.teamproject5.database.entity.FavoriteEntity;

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

    private final LicenseDatabase licenseDb;
    private final AppDatabase appDb;



    public CertificateRepository(Context context) {
        licenseDb = LicenseDatabase.getInstance(context);
        appDb = AppDatabase.getInstance(context);
    }
    public List<CertificateEntity> getFavoriteCertificates(int userId){

        List<Integer> ids = appDb.favoriteDao().getFavoriteIds(userId);

        List<CertificateEntity> result = new ArrayList<>();

        for (int id : ids) {
            CertificateEntity cert =
                    licenseDb.certificateDao().getById(id); //
            if (cert != null) result.add(cert);
        }

        return result;
    }

    public List<CertificateEntity> getCertificates() {
        return licenseDb.certificateDao().getAll();
    }

    // 검색
    public List<CertificateEntity> search(String query, int w, int p) {
        return licenseDb.certificateDao()
                .searchCertificate(query,w,p);
    }

    public boolean toggleFavorite(int userId,int certId){

        FavoriteEntity favorite =
                appDb.favoriteDao().getFavorite(userId, certId);

        if(favorite==null){

            FavoriteEntity newFavorite =
                    new FavoriteEntity();

            newFavorite.userId=userId;
            newFavorite.certId=certId;

            appDb.favoriteDao().insert(newFavorite);

            return true;
        }

        appDb.favoriteDao().delete(favorite);

        return false;
    }

    public boolean isFavorite(int userId, int certId){

        FavoriteEntity favorite =
                appDb.favoriteDao().getFavorite(userId, certId);

        return favorite != null;
    }
}