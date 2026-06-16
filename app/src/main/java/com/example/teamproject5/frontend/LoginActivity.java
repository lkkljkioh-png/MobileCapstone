package com.example.teamproject5.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teamproject5.MainActivity;
import com.example.teamproject5.R;
import com.example.teamproject5.database.AppDatabase;
import com.example.teamproject5.database.entity.UserEntity;

public class LoginActivity extends AppCompatActivity {

    EditText etId, etPw;
    Button btnLogin;
    TextView tvSignup;

    private AppDatabase appDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appDb = AppDatabase.getInstance(LoginActivity.this);

        etId     = findViewById(R.id.et_id);
        etPw     = findViewById(R.id.et_pw);
        btnLogin = findViewById(R.id.btn_login);
        tvSignup = findViewById(R.id.tv_signup);

        // 로그인 버튼 클릭
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etId.getText().toString();
                String pw = etPw.getText().toString();

                if (id.isEmpty() || pw.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserEntity user = appDb.userDao().login(id, pw);

                if (user == null) {
                    Toast.makeText(
                            LoginActivity.this,
                            "아이디 또는 비밀번호가 틀렸습니다.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .edit()
                        .putInt("user_id", user.userID)
                        .putBoolean("is_logged_in", true)
                        .apply();

                // 홈 화면으로 이동 (뒤로가기로 로그인 화면 못 돌아오게)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        // 회원가입 이동
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}