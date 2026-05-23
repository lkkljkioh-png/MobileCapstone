package com.example.teamproject5.frontend;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teamproject5.R;
import com.example.teamproject5.database.AppDatabase;
import com.example.teamproject5.database.entity.ScheduleEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarFragment extends Fragment {

    // ── View ──
    private TextView tvYearMonth;
    private ImageButton btnPrevMonth, btnNextMonth;
    private GridLayout gridCalendar;
    private LinearLayout layoutCertList;
    private TextView tvSelectedDate;

    // ── Data ──
    private Calendar currentCalendar;
    private Map<String, List<Certificate>> favoriteDateMap;

    private CertificateRepository repository;
    private AppDatabase db;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        initViews(view);
        initData();
        setupListeners();
        renderCalendar();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadFavoriteCertificates();
        renderCalendar();
    }

    private void initViews(View view) {

        tvYearMonth = view.findViewById(R.id.tv_year_month);

        btnPrevMonth = view.findViewById(R.id.btn_prev_month);

        btnNextMonth = view.findViewById(R.id.btn_next_month);

        gridCalendar = view.findViewById(R.id.grid_calendar);

        layoutCertList = view.findViewById(R.id.layout_cert_list);

        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
    }

    private void initData() {

        repository = new CertificateRepository(requireContext());

        currentCalendar = Calendar.getInstance();

        db = AppDatabase.getDB(requireContext());

        loadFavoriteCertificates();
    }

    private void loadFavoriteCertificates() {

        favoriteDateMap = new HashMap<>();

        for (Certificate cert : repository.getCertificates()) {

            if (cert.isFavorite() && cert.getExamDate() != null) {

                favoriteDateMap
                        .computeIfAbsent(cert.getExamDate(),
                                k -> new ArrayList<>())
                        .add(cert);
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

    // ── 달력 렌더링 ──

    private void renderCalendar() {

        int year = currentCalendar.get(Calendar.YEAR);

        int month = currentCalendar.get(Calendar.MONTH) + 1;

        tvYearMonth.setText(year + "년 " + month + "월");

        removeCalendarDayCells();

        Calendar firstDay = (Calendar) currentCalendar.clone();

        firstDay.set(Calendar.DAY_OF_MONTH, 1);

        int offset = firstDay.get(Calendar.DAY_OF_WEEK) - 1;

        int maxDay =
                currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < offset; i++) {

            addEmptyCell();
        }

        for (int day = 1; day <= maxDay; day++) {

            String dateKey =
                    String.format("%04d-%02d-%02d",
                            year, month, day);

            List<Certificate> certs =
                    favoriteDateMap.get(dateKey);

            List<ScheduleEntity> schedules =
                    db.scheduleDAO().getByDate(dateKey);

            boolean hasAny =
                    (certs != null && !certs.isEmpty()) ||
                            (schedules != null && !schedules.isEmpty());

            addDayCell(day,
                    hasAny,
                    dateKey,
                    certs,
                    schedules);
        }
    }

    private void removeCalendarDayCells() {

        int header = 7;

        int total = gridCalendar.getChildCount();

        if (total > header) {

            gridCalendar.removeViews(header,
                    total - header);
        }
    }

    private void addEmptyCell() {

        View v = new View(requireContext());

        v.setLayoutParams(makeParams());

        gridCalendar.addView(v);
    }

    private void addDayCell(int day,
                            boolean hasAny,
                            String dateKey,
                            List<Certificate> certs,
                            List<ScheduleEntity> schedules) {

        View cell =
                LayoutInflater.from(requireContext())
                        .inflate(R.layout.item_calendar_day,
                                gridCalendar,
                                false);

        TextView tvDay = cell.findViewById(R.id.tv_day);

        View dot = cell.findViewById(R.id.view_dot);

        tvDay.setText(String.valueOf(day));

        dot.setVisibility(hasAny
                ? View.VISIBLE
                : View.GONE);

        // 클릭 → 일정 목록 보기
        cell.setOnClickListener(v -> {

            if (tvSelectedDate != null) {

                tvSelectedDate.setText(dateKey);
            }

            showAllForDate(dateKey);
        });

        // 길게 클릭 → 일정 추가
        cell.setOnLongClickListener(v -> {

            showAddDialog(dateKey);

            return true;
        });

        cell.setLayoutParams(makeParams());

        gridCalendar.addView(cell);
    }

    private GridLayout.LayoutParams makeParams() {

        GridLayout.LayoutParams p =
                new GridLayout.LayoutParams();

        p.columnSpec =
                GridLayout.spec(GridLayout.UNDEFINED, 1f);

        p.width = 0;

        p.height = dpToPx(48);

        return p;
    }

    // ── 일정/자격증 목록 표시 ──

    private void showAllForDate(String dateKey) {

        List<Certificate> certs =
                favoriteDateMap.get(dateKey);

        List<ScheduleEntity> schedules =
                db.scheduleDAO().getByDate(dateKey);

        layoutCertList.setVisibility(View.VISIBLE);

        layoutCertList.removeAllViews();

        // 날짜 제목
        TextView tvHeader = new TextView(requireContext());

        tvHeader.setText("📅 " + dateKey);

        tvHeader.setTextSize(16f);

        layoutCertList.addView(tvHeader);

        // 자격증 목록
        if (certs != null && !certs.isEmpty()) {

            for (Certificate c : certs) {

                TextView tv = new TextView(requireContext());

                tv.setText("📘 " + c.getName());

                tv.setPadding(0, 8, 0, 8);

                layoutCertList.addView(tv);
            }
        }

        // 일정 목록
        if (schedules != null && !schedules.isEmpty()) {

            for (ScheduleEntity s : schedules) {

                TextView tv = new TextView(requireContext());

                tv.setText("📌 " + s.scheduleTitle);

                tv.setPadding(0, 8, 0, 8);

                // 클릭 → 수정
                tv.setOnClickListener(v -> {

                    showEditDialog(dateKey, s);
                });

                // 길게 클릭 → 삭제
                tv.setOnLongClickListener(v -> {

                    showDeleteDialog(dateKey, s);

                    return true;
                });

                layoutCertList.addView(tv);
            }
        }

        // 아무것도 없을 때
        if ((certs == null || certs.isEmpty()) &&
                (schedules == null || schedules.isEmpty())) {

            TextView tv = new TextView(requireContext());

            tv.setText("등록된 일정이 없습니다.");

            tv.setPadding(0, 8, 0, 8);

            layoutCertList.addView(tv);
        }
    }

    private void clearCertList() {

        layoutCertList.setVisibility(View.GONE);

        layoutCertList.removeAllViews();
    }

    // ── 일정 추가 ──

    private void showAddDialog(String dateKey) {

        EditText et = new EditText(requireContext());

        new AlertDialog.Builder(requireContext())
                .setTitle("일정 추가 (" + dateKey + ")")

                .setView(et)

                .setPositiveButton("추가", (d, w) -> {

                    String text =
                            et.getText().toString().trim();

                    if (!text.isEmpty()) {

                        addSchedule(dateKey, text);
                    }
                })

                .setNegativeButton("취소", null)

                .show();
    }

    private void addSchedule(String dateKey,
                             String text) {

        ScheduleEntity schedule =
                new ScheduleEntity();

        schedule.userID = "default";

        schedule.scheduleDate = dateKey;

        schedule.scheduleTitle = text;

        db.scheduleDAO().insert(schedule);

        renderCalendar();

        showAllForDate(dateKey);
    }

    // ── 일정 삭제 ──

    private void showDeleteDialog(String dateKey,
                                  ScheduleEntity s) {

        new AlertDialog.Builder(requireContext())
                .setTitle("일정 삭제")

                .setMessage("\"" +
                        s.scheduleTitle +
                        "\" 를 삭제할까요?")

                .setPositiveButton("삭제", (d, w) -> {

                    deleteSchedule(dateKey, s);
                })

                .setNegativeButton("취소", null)

                .show();
    }

    private void deleteSchedule(String dateKey,
                                ScheduleEntity s) {

        db.scheduleDAO().delete(s);

        renderCalendar();

        showAllForDate(dateKey);
    }

    // ── 일정 수정 ──

    private void showEditDialog(String dateKey,
                                ScheduleEntity s) {

        EditText et = new EditText(requireContext());

        et.setText(s.scheduleTitle);

        et.setSelection(s.scheduleTitle.length());

        new AlertDialog.Builder(requireContext())
                .setTitle("일정 수정")

                .setView(et)

                .setPositiveButton("수정", (d, w) -> {

                    String newText =
                            et.getText()
                                    .toString()
                                    .trim();

                    if (!newText.isEmpty()) {

                        updateSchedule(dateKey,
                                s,
                                newText);
                    }
                })

                .setNegativeButton("취소", null)

                .show();
    }

    private void updateSchedule(String dateKey,
                                ScheduleEntity s,
                                String newText) {

        s.scheduleTitle = newText;

        db.scheduleDAO().update(s);

        renderCalendar();

        showAllForDate(dateKey);
    }

    // ── util ──

    private int dpToPx(int dp) {
        return Math.round(
                dp *
                        requireContext()
                                .getResources()
                                .getDisplayMetrics()
                                .density
        );
    }
}