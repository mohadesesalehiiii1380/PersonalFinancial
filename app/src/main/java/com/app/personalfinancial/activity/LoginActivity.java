package com.app.personalfinancial.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.personalfinancial.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding mBinding;
    private MyClickHandlers handlers;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        handlers = new MyClickHandlers(this);
        mBinding.setHandlers(handlers);
        mAuth = FirebaseAuth.getInstance();
    }

    private void loginUserAccount() {

        String email, password;
        email = mBinding.textInputEditTextUsername.getText().toString();
        password = mBinding.textInputEditTextPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "لطفا ایمیل را وارد کنید...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "لطفا رمزعبور را وارد کنید...", Toast.LENGTH_LONG).show();
            return;
        }
        mBinding.progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "ورود به حساب کاربری با موفقیت انجام شد!", Toast.LENGTH_LONG).show();
                        mBinding.progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "ورود انجام نشد. لطفا دوباره تلاش کنید", Toast.LENGTH_LONG).show();
                        mBinding.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public class MyClickHandlers {
        Context context;

        public MyClickHandlers(Context context) {
            this.context = context;
        }

        public void btnLogin(View view) {
            loginUserAccount();
        }

        public void btnRegister(View view) {
            startActivity(new Intent(context, RegisterActivity.class));
        }
    }
}