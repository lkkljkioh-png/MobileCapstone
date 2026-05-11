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
    private ImageView imgBtnMenu;
    private TextView tvBtnAllCert;
    private TextView tvBtnFavorite;
    private TextView tvBtnCalendar;

    private LinearLayout layoutSideMenu;
    private ImageView imgBtnCloseMenu;
    private LinearLayout layoutMenuHome;
    private LinearLayout layoutMenuList;
    private LinearLayout layoutMenuMy;

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
        drawerLayout   = findViewById(R.id.drawerLayout);
        imgBtnMenu     = findViewById(R.id.img_btn_menu);
        tvBtnAllCert   = findViewById(R.id.tv_btn_all_cert);
        tvBtnFavorite  = findViewById(R.id.tv_btn_favorite);
        tvBtnCalendar  = findViewById(R.id.tv_btn_calendar);

        layoutSideMenu    = findViewById(R.id.layout_side_menu);
        imgBtnCloseMenu   = findViewById(R.id.img_btn_close_menu);
        layoutMenuHome    = findViewById(R.id.layout_menu_home);
        layoutMenuList    = findViewById(R.id.layout_menu_list);
        layoutMenuMy      = findViewById(R.id.layout_menu_my);

        btnCategoryTech     = findViewById(R.id.btn_category_tech);
        btnCategoryIndustry = findViewById(R.id.btn_category_industry);
        btnCategoryBusiness = findViewById(R.id.btn_category_business);
        btnCategoryPrimary  = findViewById(R.id.btn_category_primary);
        btnCategoryCreative = findViewById(R.id.btn_category_creative);
        btnCategoryService  = findViewById(R.id.btn_category_service);

        setSideMenuWidthHalfScreen();
        setupListeners();
    }

    private void setupListeners() {

        // 햄버거 버튼
        if (imgBtnMenu != null && drawerLayout != null) {
            imgBtnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));
        }

        // 메뉴 닫기
        if (imgBtnCloseMenu != null && drawerLayout != null) {
            imgBtnCloseMenu.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.END));
        }

        // 전체 자격증
        if (tvBtnAllCert != null) {
            tvBtnAllCert.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, CertificateListActivity.class)));
        }

        // 즐겨찾기
        if (tvBtnFavorite != null) {
            tvBtnFavorite.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, FavoriteListActivity.class)));
        }

        // 캘린더 이동
        if (tvBtnCalendar != null) {
            tvBtnCalendar.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, CalendarActivity.class)));
        }

        // 사이드 메뉴
        if (layoutMenuHome != null && drawerLayout != null) {
            layoutMenuHome.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.END));
        }

        if (layoutMenuList != null && drawerLayout != null) {
            layoutMenuList.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, CertificateListActivity.class));
                drawerLayout.closeDrawer(GravityCompat.END);
            });
        }

        if (layoutMenuMy != null && drawerLayout != null) {
            layoutMenuMy.setOnClickListener(v -> {
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
        if (layoutSideMenu == null) return;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int menuWidth = (int) (screenWidth * 0.5f);

        ViewGroup.LayoutParams params = layoutSideMenu.getLayoutParams();
        params.width = menuWidth;
        layoutSideMenu.setLayoutParams(params);
    }
}
