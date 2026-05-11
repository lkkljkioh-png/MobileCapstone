package com.example.teamproject5;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etName;
    private EditText etPassword;
    private EditText etPasswordCheck;
    private TextView tvPwCondition;
    private TextView tvPwMatch;
    private TextView tvBtnLogin;
    private CheckBox checkBox;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail   = findViewById(R.id.user_id);
        etName    = findViewById(R.id.user_name);
        etPassword      = findViewById(R.id.user_pw);
        etPasswordCheck = findViewById(R.id.user_pw_check);

        tvPwCondition = findViewById(R.id.tv_pw_condition);
        tvPwMatch     = findViewById(R.id.tv_pw_match_correct);
        tvBtnLogin       = findViewById(R.id.tv_login);

        checkBox  = findViewById(R.id.checkbox_id);
        btnSignup = findViewById(R.id.btn_signup);

        // 비밀번호 조건 체크
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String pw = s.toString();
                if (pw.length() >= 6) {
                    tvPwCondition.setText("사용 가능한 비밀번호입니다");
                    tvPwCondition.setTextColor(0xFF4CAF50);
                } else {
                    tvPwCondition.setText("비밀번호는 6자 이상이어야 합니다");
                    tvPwCondition.setTextColor(0xFFFF5252);
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // 비밀번호 일치 확인
        etPasswordCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (etPassword.getText().toString().equals(s.toString())) {
                    tvPwMatch.setText("비밀번호가 일치합니다");
                    tvPwMatch.setTextColor(0xFF4CAF50);
                } else {
                    tvPwMatch.setText("비밀번호가 일치하지 않습니다");
                    tvPwMatch.setTextColor(0xFFFF5252);
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // 회원가입 버튼
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email   = etEmail.getText().toString();
                String name    = etName.getText().toString();
                String pw      = etPassword.getText().toString();
                String pwCheck = etPasswordCheck.getText().toString();

                if (email.isEmpty() || name.isEmpty() || pw.isEmpty() || pwCheck.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "모든 항목을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pw.equals(pwCheck)) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkBox.isChecked()) {
                    Toast.makeText(SignupActivity.this, "개인정보 동의가 필요합니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 회원가입 완료 → 이름을 SharedPreferences에 임시 저장
                // 백엔드 연동 시 서버 응답값으로 교체
                getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("signup_name", name)
                        .apply();

                Toast.makeText(SignupActivity.this, "회원가입 완료! 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // 로그인으로 돌아가기
        tvBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
