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
 *  2. 즐겨찾기 자격증 목록 (최대 3개)
 *  3. 미니 캘린더 - 즐겨찾기 자격증 시험일에 원형 표시
 */
public class MyActivity extends AppCompatActivity {

    // ── 상수 ───────────────────────────────────────────────────────────────
    private static final int MAX_FAVORITE_DISPLAY = 3;

    // ── Views ──────────────────────────────────────────────────────────────
    private TextView tvBtnBack;
    private TextView tvUserName;
    private LinearLayout layoutFavoriteList;
    private ImageButton imgBtnGoFavorite;
    private ImageButton imgBtnGoCalendar;
    private TextView tvMiniYearMonth;
    private GridLayout gridMiniCalendar;

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

    @Override
    protected void onResume() {
        super.onResume();
        buildFavoriteDateMap();
        renderAll();
    }

    // ── 초기화 ─────────────────────────────────────────────────────────────

    private void initViews() {
        tvBtnBack         = findViewById(R.id.tv_btn_back);
        tvUserName        = findViewById(R.id.tv_user_name);
        layoutFavoriteList = findViewById(R.id.layout_favorite_list);
        imgBtnGoFavorite  = findViewById(R.id.img_btn_go_favorite);
        imgBtnGoCalendar  = findViewById(R.id.img_btn_go_calendar);
        tvMiniYearMonth   = findViewById(R.id.tv_mini_year_month);
        gridMiniCalendar  = findViewById(R.id.grid_mini_calendar);
    }

    private void initData() {
        repository = new CertificateRepository(this);
        buildFavoriteDateMap();
    }

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
        // 뒤로가기
        if (tvBtnBack != null) {
            tvBtnBack.setOnClickListener(v -> finish());
        }

        // 즐겨찾기 상세 목록으로 이동
        imgBtnGoFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(MyActivity.this, FavoriteListActivity.class);
            startActivity(intent);
        });

        // 캘린더 탭으로 이동
        imgBtnGoCalendar.setOnClickListener(v -> {
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

    private void renderUserName() {
        String userName = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("user_name", "사용자");
        tvUserName.setText(userName + " 님");
    }

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

    private void renderMiniCalendar() {
        Calendar cal = Calendar.getInstance();
        int year  = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        tvMiniYearMonth.setText(year + "년 " + month + "월");

        int headerCount = 7;
        int total = gridMiniCalendar.getChildCount();
        if (total > headerCount) {
            gridMiniCalendar.removeViews(headerCount, total - headerCount);
        }

        Calendar firstDay = (Calendar) cal.clone();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        int offset = firstDay.get(Calendar.DAY_OF_WEEK) - 1;

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int todayDay = cal.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < offset; i++) {
            addMiniEmptyCell();
        }

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
