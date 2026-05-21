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

/**
 * 홈 화면 Activity
 *
 * 역할:
 *   - 앱의 메인 홈 화면 (로그인 후 최초 진입 화면)
 *   - 전체 자격증 목록 / 즐겨찾기 목록 / 카테고리별 목록으로 이동
 *   - 오른쪽에서 슬라이드되는 사이드 메뉴 제공 (홈 / 목록 / MY 탭 이동)
 *   - 뒤로가기 시 사이드 메뉴가 열려 있으면 메뉴를 먼저 닫음
 *
 * 사이드 메뉴 너비:
 *   - 화면 가로 크기의 50%로 고정 (setSideMenuWidthHalfScreen)
 *
 * 카테고리 목록:
 *   - IT·기술 / 건설·산업·안전 / 경영·사무·금융 / 농림·해양·식품 / 예술·미디어 / 생활·서비스·교육
 *   - 각 버튼 클릭 시 CategoryListActivity로 이동하며 category_id를 Intent로 전달
 *   - 카테고리 추가 시 : setupCategoryButton() 호출 1줄 추가 + 레이아웃에 버튼 추가
 */

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout; // 사이드 메뉴를 감싸는 DrawerLayout
    private ImageView btnMenu; // 햄버거 버튼 (사이드 메뉴 열기)
    private TextView btnAllCert; // "전체 자격증" 버튼 → CertificateListActivity
    private TextView btnFavorite; // "즐겨찾기" 버튼 → FavoriteListActivity

    private LinearLayout sideMenu; // 사이드 메뉴 컨테이너 (너비 = 화면의 50%)
    private ImageView btnCloseMenu; // 사이드 메뉴 닫기 버튼
    private LinearLayout menuHome; // 사이드 메뉴 "홈" → 현재 화면 유지
    private LinearLayout menuList; // 사이드 메뉴 "목록" → CertificateListActivity
    private LinearLayout menuMy; // 사이드 메뉴 "MY"  → MyActivity

    // 각 버튼 클릭 시 CategoryListActivity로 이동, EXTRA_CATEGORY_ID로 카테고리 구분
    private LinearLayout btnCategoryTech; // IT·기술
    private LinearLayout btnCategoryIndustry; // 건설·산업·안전
    private LinearLayout btnCategoryBusiness; // 경영·사무·금융
    private LinearLayout btnCategoryPrimary; // 농림·해양·식품
    private LinearLayout btnCategoryCreative; // 예술·미디어
    private LinearLayout btnCategoryService; // 생활·서비스·교육

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 뷰 연결
        drawerLayout = findViewById(R.id.drawerLayout);
        btnMenu      = findViewById(R.id.btnMenu);
        btnAllCert   = findViewById(R.id.btn_all_cert);
        btnFavorite  = findViewById(R.id.btn_favorite);

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

        setSideMenuWidthHalfScreen(); // 사이드 메뉴 너비 화면의 50%로 설정
        setupListeners(); // 버튼 클릭 이벤트 등록
    }

    private void setupListeners() {
        // 햄버거 버튼 → 사이드 메뉴 오른쪽에서 열기
        if (btnMenu != null && drawerLayout != null) {
            btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));
        }
        // 사이드 메뉴 닫기 버튼
        if (btnCloseMenu != null && drawerLayout != null) {
            btnCloseMenu.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.END));
        }
        // "전체 자격증" 버튼 → CertificateListActivity
        if (btnAllCert != null) {
            btnAllCert.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CertificateListActivity.class);
                startActivity(intent);
            });
        }
        // "즐겨찾기" 버튼 → FavoriteListActivity
        if (btnFavorite != null) {  // 추가
            btnFavorite.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, FavoriteListActivity.class);
                startActivity(intent);
            });
        }
        // 사이드 메뉴 "홈" → 메뉴만 닫기 (이미 홈 화면이므로 이동 없음)
        if (menuHome != null && drawerLayout != null) {
            menuHome.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.END));
        }
        // 사이드 메뉴 "목록" → CertificateListActivity 이동 후 메뉴 닫기
        if (menuList != null && drawerLayout != null) {
            menuList.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CertificateListActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.END);
            });
        }
        // 사이드 메뉴 "MY" → MyActivity 이동 후 메뉴 닫기
        if (menuMy != null && drawerLayout != null) {
            menuMy.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.END);
            });
        }

        // 카테고리 버튼 등록
        // 카테고리 추가 시 setupCategoryButton() 1줄 추가 + 레이아웃에 버튼 추가

        setupCategoryButton(btnCategoryTech,     "category_tech");
        setupCategoryButton(btnCategoryIndustry, "category_industry");
        setupCategoryButton(btnCategoryBusiness, "category_business");
        setupCategoryButton(btnCategoryPrimary,  "category_primary");
        setupCategoryButton(btnCategoryCreative, "category_creative");
        setupCategoryButton(btnCategoryService,  "category_service");

        // 뒤로가기 처리 : 사이드 메뉴 열려 있으면 메뉴 먼저 닫기, 아니면 일반 뒤로가기
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

    /**
     * 카테고리 버튼 클릭 이벤트 공통 등록 메서드
     * 클릭 시 CategoryListActivity로 이동하며 categoryId를 EXTRA_CATEGORY_ID로 전달
     *
     * @param button     카테고리 버튼 LinearLayout
     * @param categoryId 카테고리 식별자 문자열 (예: "category_tech")
     *                   CategoryListActivity.EXTRA_CATEGORY_ID 키로 전달됨
     */

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