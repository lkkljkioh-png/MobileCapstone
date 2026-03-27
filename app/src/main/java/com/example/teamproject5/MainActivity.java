package com.example.teamproject5;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView btnMenu;
    private TextView btnAllCert;

    private LinearLayout sideMenu;
    private ImageView btnCloseMenu;
    private LinearLayout menuHome;
    private LinearLayout menuList;
    private LinearLayout menuMy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        btnMenu = findViewById(R.id.btnMenu);
        btnAllCert = findViewById(R.id.btn_all_cert);

        sideMenu = findViewById(R.id.sideMenu);
        btnCloseMenu = findViewById(R.id.btnCloseMenu);
        menuHome = findViewById(R.id.menuHome);
        menuList = findViewById(R.id.menuList);
        menuMy = findViewById(R.id.menuMy);

        setSideMenuWidthHalfScreen();

        if (btnMenu != null && drawerLayout != null) {
            btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));
        }

        if (btnCloseMenu != null && drawerLayout != null) {
            btnCloseMenu.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.END));
        }

        if (btnAllCert != null) {
            btnAllCert.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CertificateListActivity.class);
                startActivity(intent);
            });
        }

        if (menuHome != null && drawerLayout != null) {
            menuHome.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.END));
        }

        if (menuList != null && drawerLayout != null) {
            menuList.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CertificateListActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.END);
            });
        }

        if (menuMy != null && drawerLayout != null) {
            menuMy.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "MY 화면은 아직 준비 중입니다.", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.END);
            });
        }

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