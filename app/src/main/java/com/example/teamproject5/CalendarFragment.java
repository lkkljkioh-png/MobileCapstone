package com.example.teamproject5;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 캘린더 탭 Fragment
 *
 * 역할:
 *  - 월 단위 달력 표시 (좌우 화살표로 월 이동)
 *  - 즐겨찾기한 자격증의 examDate가 있는 날짜에 점(dot) 표시
 *  - 날짜 셀 클릭 → 해당 날의 자격증 목록 표시
 *  - 자격증 카드 클릭 → DetailActivity로 이동
 *
 * 백엔드 연동 포인트:
 *  - loadFavoriteCertificates(): repository 호출을 API 응답으로 교체
 */
public class CalendarFragment extends Fragment {

    // ── Views ──────────────────────────────────────────────────────────────
    private TextView tvYearMonth;
    private ImageButton btnPrevMonth;
    private ImageButton btnNextMonth;
    private GridLayout gridCalendar;
    private LinearLayout layoutCertList;  // 선택된 날짜의 자격증 목록 컨테이너
    private TextView tvSelectedDate;

    // ── Data ───────────────────────────────────────────────────────────────
    private Calendar currentCalendar;

    /**
     * key: "yyyy-MM-dd"  value: 즐겨찾기 자격증 리스트
     * 즐겨찾기 + examDate 가 있는 자격증만 담습니다.
     */
    private Map<String, List<Certificate>> favoriteDateMap;

    private CertificateRepository repository;

    // ── Fragment 생성 ──────────────────────────────────────────────────────

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    // ── Lifecycle ──────────────────────────────────────────────────────────

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initData();
        setupListeners();
    }

    /**
     * onResume: 다른 화면에서 즐겨찾기 변경 후 돌아올 때 자동 갱신
     * (예: 자격증 목록에서 즐겨찾기 변경 → 뒤로가기 → 캘린더 최신 상태 반영)
     */
    @Override
    public void onResume() {
        super.onResume();
        if (repository != null) {
            loadFavoriteCertificates();
            renderCalendar();
        }
    }

    // ── 초기화 ─────────────────────────────────────────────────────────────

    private void initViews(View view) {
        tvYearMonth    = view.findViewById(R.id.tv_year_month);
        btnPrevMonth   = view.findViewById(R.id.btn_prev_month);
        btnNextMonth   = view.findViewById(R.id.btn_next_month);
        gridCalendar   = view.findViewById(R.id.grid_calendar);
        layoutCertList = view.findViewById(R.id.layout_cert_list);
        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
    }

    private void initData() {
        repository = new CertificateRepository(requireContext());
        currentCalendar = Calendar.getInstance(); // 오늘 날짜로 초기화
        loadFavoriteCertificates();
    }

    /**
     * 즐겨찾기 자격증 중 examDate 가 있는 항목만 날짜별 Map 으로 구성합니다.
     * 백엔드 연동 시 이 메서드만 수정하면 됩니다.
     */
    private void loadFavoriteCertificates() {
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
        btnPrevMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            renderCalendar();
            clearCertList();
        });

        btnNextMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            renderCalendar();
            clearCertList();
        });
    }

    // ── 달력 렌더링 ────────────────────────────────────────────────────────

    private void renderCalendar() {
        int year  = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH) + 1; // 0-based → 1-based
        tvYearMonth.setText(year + "년 " + month + "월");

        // 요일 헤더(첫 7개)는 XML 고정, 날짜 셀만 제거 후 재생성
        removeCalendarDayCells();

        // 이번 달 1일의 요일 오프셋 (0=일 ~ 6=토)
        Calendar firstDay = (Calendar) currentCalendar.clone();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        int offset = firstDay.get(Calendar.DAY_OF_WEEK) - 1;

        int maxDay = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 오늘 날짜 (하이라이트용)
        Calendar today = Calendar.getInstance();
        int todayYear  = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay   = today.get(Calendar.DAY_OF_MONTH);

        // 1일 이전 빈 셀
        for (int i = 0; i < offset; i++) {
            addEmptyCell();
        }

        // 날짜 셀
        for (int day = 1; day <= maxDay; day++) {
            String dateKey = String.format("%04d-%02d-%02d", year, month, day);
            List<Certificate> certsOnDay = favoriteDateMap.get(dateKey);
            boolean hasCert = certsOnDay != null && !certsOnDay.isEmpty();
            boolean isToday = (year == todayYear)
                    && (month - 1 == todayMonth)
                    && (day == todayDay);

            addDayCell(day, hasCert, isToday, dateKey, certsOnDay);
        }
    }

    /**
     * GridLayout에서 요일 헤더(첫 7개) 이후의 날짜 셀만 제거합니다.
     */
    private void removeCalendarDayCells() {
        int headerCount = 7;
        int totalChildren = gridCalendar.getChildCount();
        if (totalChildren > headerCount) {
            gridCalendar.removeViews(headerCount, totalChildren - headerCount);
        }
    }

    private void addEmptyCell() {
        View empty = new View(requireContext());
        empty.setLayoutParams(makeCellParams());
        gridCalendar.addView(empty);
    }

    private void addDayCell(int day,
                            boolean hasCert,
                            boolean isToday,
                            String dateKey,
                            @Nullable List<Certificate> certsOnDay) {

        View cellView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_calendar_day, gridCalendar, false);

        TextView tvDay = cellView.findViewById(R.id.tv_day);
        View dotView   = cellView.findViewById(R.id.view_dot);

        tvDay.setText(String.valueOf(day));
        dotView.setVisibility(hasCert ? View.VISIBLE : View.GONE);

        // 오늘 날짜 배경 강조
        if (isToday) {
            tvDay.setBackgroundResource(R.drawable.bg_calendar_today);
            tvDay.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        }

        // 날짜 클릭 → 자격증 목록 표시 (dot 없는 날은 목록 닫기)
        cellView.setOnClickListener(v -> {
            if (hasCert) {
                showCertListForDate(dateKey, certsOnDay);
            } else {
                clearCertList();
            }
        });

        cellView.setLayoutParams(makeCellParams());
        gridCalendar.addView(cellView);
    }

    private GridLayout.LayoutParams makeCellParams() {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec    = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.width      = 0;
        params.height     = dpToPx(48);
        return params;
    }

    // ── 자격증 목록 표시 ───────────────────────────────────────────────────

    private void showCertListForDate(String dateKey, List<Certificate> certs) {
        layoutCertList.setVisibility(View.VISIBLE);

        // "5월 17일 시험" 형식으로 표시
        String[] parts = dateKey.split("-");
        String label = parts[1].replaceFirst("^0", "") + "월 "
                + parts[2].replaceFirst("^0", "") + "일 시험";
        tvSelectedDate.setText(label);

        // 기존 자격증 카드 제거 (헤더 TextView + 구분선 2개는 유지)
        int keepCount = 2;
        int total = layoutCertList.getChildCount();
        if (total > keepCount) {
            layoutCertList.removeViews(keepCount, total - keepCount);
        }

        // 자격증 카드 동적 추가
        for (Certificate cert : certs) {
            View itemView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_calendar_cert, layoutCertList, false);

            TextView tvName     = itemView.findViewById(R.id.tv_cert_name);
            TextView tvCategory = itemView.findViewById(R.id.tv_cert_category);

            tvName.setText(cert.getName());
            tvCategory.setText(cert.getCategory());

            // 자격증 카드 클릭 → DetailActivity로 이동
            itemView.setOnClickListener(v -> navigateToDetail(cert));

            layoutCertList.addView(itemView);
        }
    }

    private void clearCertList() {
        layoutCertList.setVisibility(View.GONE);
        int keepCount = 2;
        int total = layoutCertList.getChildCount();
        if (total > keepCount) {
            layoutCertList.removeViews(keepCount, total - keepCount);
        }
    }

    /**
     * 자격증 카드 클릭 시 DetailActivity로 이동합니다.
     * cert_name을 Intent로 전달합니다.
     */
    private void navigateToDetail(Certificate cert) {
        Intent intent = new Intent(requireContext(), DetailActivity.class);
        intent.putExtra("cert_name", cert.getName());
        startActivity(intent);
    }

    // ── 유틸 ───────────────────────────────────────────────────────────────

    private int dpToPx(int dp) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}