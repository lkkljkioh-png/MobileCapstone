package com.example.teamproject5;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MY 탭 Activity
 *
 * 구성:
 *  1. 프로필 영역 - 원형 도형 + "user_name 님"
 *  2. 즐겨찾기 자격증 목록 (최대 3개) - 추후 구현 예정
 *  3. 미니 캘린더 - 즐겨찾기 자격증 시험일에 원형 표시
 *                  → 화살표 버튼 클릭 시 CalendarActivity로 이동
 *
 * 백엔드 연동 포인트:
 *  - renderUserName(): 현재는 SharedPreferences에서 로그인 이름 표시
 *                      백엔드 연동 시 사용자 프로필 API 응답으로 교체
 *  - renderFavoriteList(): 서버 즐겨찾기 목록 API로 교체
 */
public class MyActivity extends AppCompatActivity {

    // ── 상수 ───────────────────────────────────────────────────────────────
    private static final int MAX_FAVORITE_DISPLAY = 3; // 즐겨찾기 최대 표시 개수

    // ── Views ──────────────────────────────────────────────────────────────
    private TextView tvUserName; // 상단 프로필 영역 — "OO 님" 형태로 로그인한 사용자 이름 표시
    private LinearLayout layoutFavoriteList; // 즐겨찾기 자격증 카드 목록을 동적으로 추가하는 컨테이너 (최대 3개)
    private ImageButton btnGoFavorite; // 즐겨찾기 전체 목록으로 이동하는 화살표 버튼 → FavoriteListActivity
    private ImageButton btnGoCalendar; // 캘린더 전체 화면으로 이동하는 화살표 버튼 → CalendarActivity
    private TextView tvMiniYearMonth; // 미니 캘린더 상단 — 현재 년/월 표시 (예: "2026년 4월")
    private GridLayout gridMiniCalendar; // 미니 캘린더 본체 — 이번 달 날짜 셀을 동적으로 그리는 7열 그리드

    // ── Data ───────────────────────────────────────────────────────────────
    private CertificateRepository repository;

    /** key: "yyyy-MM-dd", value: 즐겨찾기 자격증 리스트 */
    private Map<String, List<Certificate>> favoriteDateMap;

    // ── Lifecycle ──────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        initViews();
        initData();
        setupListeners();
    }

    /**
     * onResume: 다른 화면에서 즐겨찾기 변경 후 돌아올 때 자동 갱신
     * (예: 자격증 목록에서 즐겨찾기 변경 → 뒤로가기 → MY 화면 최신 상태 반영)
     */
    @Override
    protected void onResume() {
        super.onResume();
        buildFavoriteDateMap();
        renderAll();
    }

    // ── 초기화 ─────────────────────────────────────────────────────────────

    private void initViews() {
        tvUserName        = findViewById(R.id.tv_user_name);
        layoutFavoriteList = findViewById(R.id.layout_favorite_list);
        btnGoFavorite     = findViewById(R.id.btn_go_favorite);
        btnGoCalendar     = findViewById(R.id.btn_go_calendar);
        tvMiniYearMonth   = findViewById(R.id.tv_mini_year_month);
        gridMiniCalendar  = findViewById(R.id.grid_mini_calendar);
    }

    private void initData() {
        repository = new CertificateRepository(this);
        buildFavoriteDateMap();
    }

    /**
     * 즐겨찾기 + examDate 있는 자격증만 날짜별 Map으로 구성합니다.
     * 백엔드 연동 시 이 메서드를 수정하세요.
     */
    private void buildFavoriteDateMap() {
        favoriteDateMap = new HashMap<>();

        List<Certificate> allList = repository.getCertificates();
        for (Certificate cert : allList) {
            if (cert.isFavorite() && cert.getExamDate() != null) {
                List<Certificate> certsOnDate = favoriteDateMap.get(cert.getExamDate());
                if (certsOnDate == null) {
                    certsOnDate = new ArrayList<>();
                }
                certsOnDate.add(cert);
                favoriteDateMap.put(cert.getExamDate(), certsOnDate);
            }
        }
    }

    private void setupListeners() {
        // 즐겨찾기 상세 목록으로 이동
        btnGoFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(MyActivity.this, FavoriteListActivity.class);
            startActivity(intent);
        });

        // 캘린더 탭으로 이동
        btnGoCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(MyActivity.this, CalendarActivity.class);
            startActivity(intent);
        });
    }

    // ── 렌더링 ─────────────────────────────────────────────────────────────

    private void renderAll() {
        renderUserName();
        renderFavoriteList();
        renderMiniCalendar();
    }

    /**
     * 사용자 이름 표시
     * 현재는 LoginActivity에서 저장한 SharedPreferences 값을 읽어 표시
     * 백엔드 연동 시: 사용자 프로필 API 응답값으로 교체하세요.
     */
    private void renderUserName() {
        // 현재는 SharedPreferences에 저장된 로그인 이름 사용
        // 백엔드 연동 시 사용자 프로필 API 응답값으로 교체
        String userName = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("user_name", "사용자");
        tvUserName.setText(userName + " 님");
    }

    /**
     * 즐겨찾기 자격증 목록 표시 (최대 3개)
     * 추후 보완 예정
     */
    private void renderFavoriteList() {
        List<Certificate> allList = repository.getCertificates();
        List<Certificate> favorites = new ArrayList<>();

        for (Certificate cert : allList) {
            if (cert.isFavorite()) {
                favorites.add(cert);
                if (favorites.size() >= MAX_FAVORITE_DISPLAY) break;
            }
        }

        layoutFavoriteList.removeAllViews();

        if (favorites.isEmpty()) {
            // 즐겨찾기가 없을 때 안내 텍스트
            TextView tvEmpty = new TextView(this);
            tvEmpty.setText("즐겨찾기한 자격증이 없어요.");
            tvEmpty.setTextSize(14f);
            tvEmpty.setTextColor(ContextCompat.getColor(this, R.color.maintext_black));
            layoutFavoriteList.addView(tvEmpty);
            return;
        }

        for (Certificate cert : favorites) {
            View itemView = LayoutInflater.from(this)
                    .inflate(R.layout.item_my_favorite, layoutFavoriteList, false);

            TextView tvName     = itemView.findViewById(R.id.tv_fav_name);
            TextView tvCategory = itemView.findViewById(R.id.tv_fav_category);

            tvName.setText(cert.getName());
            tvCategory.setText(CategoryUtils.toLabel(cert.getCategory()));

            // 즐겨찾기 카드 클릭 → DetailActivity로 이동
            itemView.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(MyActivity.this, DetailActivity.class);
                intent.putExtra("cert_name", cert.getName());
                intent.putExtra("cert_id", cert.getId());
                startActivity(intent);
            });

            layoutFavoriteList.addView(itemView);
        }
    }

    // ── 미니 캘린더 렌더링 ─────────────────────────────────────────────────

    /**
     * 이번 달 기준 미니 캘린더를 그림
     * 즐겨찾기 자격증 시험일에만 원형 표시(dot)를 그림
     * 날짜 클릭 등 상세 기능은 CalendarActivity에서 제공
     */
    private void renderMiniCalendar() {
        Calendar cal = Calendar.getInstance();
        int year  = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // 1-based

        tvMiniYearMonth.setText(year + "년 " + month + "월");

        // 요일 헤더 7개 이후 날짜 셀 제거
        int headerCount = 7;
        int total = gridMiniCalendar.getChildCount();
        if (total > headerCount) {
            gridMiniCalendar.removeViews(headerCount, total - headerCount);
        }

        // 1일의 요일 오프셋
        Calendar firstDay = (Calendar) cal.clone();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        int offset = firstDay.get(Calendar.DAY_OF_WEEK) - 1; // 0(일)~6(토)

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 오늘 날짜
        int todayDay = cal.get(Calendar.DAY_OF_MONTH);

        // 빈 셀
        for (int i = 0; i < offset; i++) {
            addMiniEmptyCell();
        }

        // 날짜 셀
        for (int day = 1; day <= maxDay; day++) {
            String dateKey = String.format("%04d-%02d-%02d", year, month, day);
            boolean hasCert = favoriteDateMap.containsKey(dateKey);
            boolean isToday = (day == todayDay);
            addMiniDayCell(day, hasCert, isToday);
        }
    }

    private void addMiniEmptyCell() {
        View empty = new View(this);
        empty.setLayoutParams(makeMiniCellParams());
        gridMiniCalendar.addView(empty);
    }

    private void addMiniDayCell(int day, boolean hasCert, boolean isToday) {
        View cellView = LayoutInflater.from(this)
                .inflate(R.layout.item_mini_calendar_day, gridMiniCalendar, false);

        TextView tvDay = cellView.findViewById(R.id.tv_mini_day);
        View dotView   = cellView.findViewById(R.id.view_mini_dot);

        tvDay.setText(String.valueOf(day));
        dotView.setVisibility(hasCert ? View.VISIBLE : View.GONE);

        // 오늘 강조
        if (isToday) {
            tvDay.setBackgroundResource(R.drawable.bg_calendar_today);
            tvDay.setTextColor(ContextCompat.getColor(this, R.color.white));
        }

        cellView.setLayoutParams(makeMiniCellParams());
        gridMiniCalendar.addView(cellView);
    }

    private GridLayout.LayoutParams makeMiniCellParams() {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec    = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.width      = 0;
        params.height     = dpToPx(36);
        return params;
    }

    // ── 유틸 ───────────────────────────────────────────────────────────────

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}