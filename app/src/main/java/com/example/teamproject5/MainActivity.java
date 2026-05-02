package com.example.teamproject5;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰 연결
        drawerLayout = findViewById(R.id.drawerLayout);
        btnMenu      = findViewById(R.id.btnMenu);
        btnAllCert   = findViewById(R.id.btn_all_cert);
        btnFavorite  = findViewById(R.id.btn_favorite);
        btnCalendar  = findViewById(R.id.btn_calendar);

        sideMenu     = findViewById(R.id.sideMenu);
        btnCloseMenu = findViewById(R.id.btnCloseMenu);
        menuHome     = findViewById(R.id.menuHome);
        menuList     = findViewById(R.id.menuList);
        menuMy       = findViewById(R.id.menuMy);

        btnCategoryTech     = findViewById(R.id.btnCategoryTech);
        btnCategoryIndustry = findViewById(R.id.btnCategoryIndustry);
        btnCategoryBusiness = findViewById(R.id.btnCategoryBusiness);
        btnCategoryPrimary  = findViewById(R.id.btnCategoryPrimary);
        btnCategoryCreative = findViewById(R.id.btnCategoryCreative);
        btnCategoryService  = findViewById(R.id.btnCategoryService);

        setSideMenuWidthHalfScreen();
        setupListeners();
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
        setupCategoryButton(btnCategoryTech,     "category_tech");
        setupCategoryButton(btnCategoryIndustry, "category_industry");
        setupCategoryButton(btnCategoryBusiness, "category_business");
        setupCategoryButton(btnCategoryPrimary,  "category_primary");
        setupCategoryButton(btnCategoryCreative, "category_creative");
        setupCategoryButton(btnCategoryService,  "category_service");

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
}