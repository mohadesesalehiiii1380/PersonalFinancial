package com.app.personalfinancial.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.personalfinancial.databinding.ActivityShowIncomeDetailsBinding;
import com.app.personalfinancial.model.Income;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowIncomeDetailsActivity extends AppCompatActivity {

    private ActivityShowIncomeDetailsBinding mBinding;
    private MyClickHandlers handlers;
    private Income mIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityShowIncomeDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        handlers = new MyClickHandlers(this);
        mBinding.setHandlers(handlers);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String incomeId = getIntent().getStringExtra("INCOME_ID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mBinding.progressBar2.setVisibility(View.VISIBLE);
        db.collection("income").document(incomeId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    mIncome = new Income(documentSnapshot.getId(), documentSnapshot.getData());
                    mBinding.textInputEditTextAmount.setText(mIncome.amount);
                    mBinding.textInputEditTextCategory.setText(mIncome.category);
                    mBinding.textInputEditTextDate.setText(mIncome.date);
                    mBinding.textInputEditTextTitle.setText(mIncome.title);
                    mBinding.textInputEditTextDescription.setText(mIncome.description);
                    mBinding.progressBar2.setVisibility(View.GONE);

                })
                .addOnFailureListener(e -> {
                    mBinding.progressBar2.setVisibility(View.GONE);
                });

    }

    public class MyClickHandlers {
        Context context;

        public MyClickHandlers(Context context) {
            this.context = context;
        }

        public void btnEdit(View view) {
            Intent intent = new Intent(context, EditIncomeActivity.class);
            intent.putExtra("INCOME_ID", mIncome.id);
            context.startActivity(intent);
        }

        public void btnDelete(View view) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("income").document(mIncome.id).delete()
                    .addOnFailureListener(e -> {
                        Toast.makeText(ShowIncomeDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        mBinding.progressBar.setVisibility(View.GONE);
                    })
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(ShowIncomeDetailsActivity.this, "آیتم با موفقیت حذف شد.", Toast.LENGTH_LONG).show();
                        mBinding.progressBar.setVisibility(View.GONE);
                        finish();
                    });
        }

        public void btnBack(View view) {
            finish();
        }
    }
}