package com.example.teamproject5;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.example.teamproject5.database.AppDatabase;
import com.example.teamproject5.database.entity.ScheduleEntity;
import com.example.teamproject5.frontend.CalendarActivity;
import com.example.teamproject5.frontend.CategoryListActivity;
import com.example.teamproject5.frontend.CertificateListActivity;
import com.example.teamproject5.frontend.FavoriteListActivity;
import com.example.teamproject5.frontend.MyActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.teamproject5.frontend.RecentCertificateAdapter;
import com.example.teamproject5.database.entity.RecentCertificateEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView btnMenu;
    private TextView btnAllCert;
    private TextView btnFavorite;
    private TextView btnCalendar;

    private LinearLayout sideMenu;
    private ImageView btnCloseMenu;
    private LinearLayout menuHome;
    private LinearLayout menuList;
    private LinearLayout menuMy;

    private LinearLayout btnCategoryTech;
    private LinearLayout btnCategoryIndustry;
    private LinearLayout btnCategoryBusiness;
    private LinearLayout btnCategoryPrimary;
    private LinearLayout btnCategoryCreative;
    private LinearLayout btnCategoryService;
    private RecyclerView recentRecyclerView;
    private RecentCertificateAdapter recentAdapter;
    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰 연결
        drawerLayout = findViewById(R.id.drawerLayout);
        btnMenu = findViewById(R.id.btnMenu);
        btnAllCert = findViewById(R.id.btn_all_cert);
        btnFavorite = findViewById(R.id.btn_favorite);
        btnCalendar = findViewById(R.id.btn_calendar);

        sideMenu = findViewById(R.id.sideMenu);
        btnCloseMenu = findViewById(R.id.btnCloseMenu);
        menuHome = findViewById(R.id.menuHome);
        menuList = findViewById(R.id.menuList);
        menuMy = findViewById(R.id.menuMy);

        btnCategoryTech = findViewById(R.id.btnCategoryTech);
        btnCategoryIndustry = findViewById(R.id.btnCategoryIndustry);
        btnCategoryBusiness = findViewById(R.id.btnCategoryBusiness);
        btnCategoryPrimary = findViewById(R.id.btnCategoryPrimary);
        btnCategoryCreative = findViewById(R.id.btnCategoryCreative);
        btnCategoryService = findViewById(R.id.btnCategoryService);

        setSideMenuWidthHalfScreen();
        setupListeners();

        /* DB 정상작동 확인
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.userID="yoon";
        schedule.scheduleDate="2026-05-11";
        schedule.scheduleTitle="b-day";

        db.scheduleDAO().insert(schedule);

        List<ScheduleEntity> list=db.scheduleDAO().getALL();

        for(ScheduleEntity s:list) {
            Log.d("ROOM_TEST", s.scheduleDate + "/" + s.scheduleTitle); */

        db = AppDatabase.getDB(this);

        recentRecyclerView = findViewById(R.id.recentRecyclerView);

        recentAdapter = new RecentCertificateAdapter();

        recentRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recentRecyclerView.setAdapter(recentAdapter);

        List<RecentCertificateEntity> list =
                db.recentCertificateDAO().getRecentCertificates();

        recentAdapter.setList(list);

        /* 최근 자격증 저장 확인
        RecentCertificateEntity entity =

                new RecentCertificateEntity();

        entity.certificateName = "정보처리기사";
        entity.viewTime = System.currentTimeMillis();

        db.recentCertificateDAO().insert(entity); */
    }



    private void setupListeners() {

        // 햄버거 버튼
        if (btnMenu != null && drawerLayout != null) {
            btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));
        }

        // 메뉴 닫기
        if (btnCloseMenu != null && drawerLayout != null) {
            btnCloseMenu.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.END));
        }

        // 전체 자격증
        if (btnAllCert != null) {
            btnAllCert.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, CertificateListActivity.class));
            });
        }

        // 즐겨찾기
        if (btnFavorite != null) {
            btnFavorite.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, FavoriteListActivity.class));
            });
        }

        // 캘린더 이동
        if (btnCalendar != null) {
            btnCalendar.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
            });
        }

        // 사이드 메뉴
        if (menuHome != null && drawerLayout != null) {
            menuHome.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.END));
        }

        if (menuList != null && drawerLayout != null) {
            menuList.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, CertificateListActivity.class));
                drawerLayout.closeDrawer(GravityCompat.END);
            });
        }

        if (menuMy != null && drawerLayout != null) {
            menuMy.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, MyActivity.class));
                drawerLayout.closeDrawer(GravityCompat.END);
            });
        }

        // 카테고리
        setupCategoryButton(btnCategoryTech, "category_tech");
        setupCategoryButton(btnCategoryIndustry, "category_industry");
        setupCategoryButton(btnCategoryBusiness, "category_business");
        setupCategoryButton(btnCategoryPrimary, "category_primary");
        setupCategoryButton(btnCategoryCreative, "category_creative");
        setupCategoryButton(btnCategoryService, "category_service");

        // 뒤로가기
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void setupCategoryButton(LinearLayout button, String categoryId) {
        if (button == null) return;

        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoryListActivity.class);
            intent.putExtra(CategoryListActivity.EXTRA_CATEGORY_ID, categoryId);
            startActivity(intent);
        });
    }

    private void setSideMenuWidthHalfScreen() {
        if (sideMenu == null) return;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int menuWidth = (int) (screenWidth * 0.5f);

        ViewGroup.LayoutParams params = sideMenu.getLayoutParams();
        params.width = menuWidth;
        sideMenu.setLayoutParams(params);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<RecentCertificateEntity> list =
                db.recentCertificateDAO().getRecentCertificates();

        recentAdapter.setList(list);
    }
}

