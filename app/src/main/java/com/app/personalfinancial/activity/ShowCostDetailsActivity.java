package com.app.personalfinancial.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.personalfinancial.databinding.ActivityShowCostDetailsBinding;
import com.app.personalfinancial.model.Cost;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowCostDetailsActivity extends AppCompatActivity {

    private ActivityShowCostDetailsBinding mBinding;
    private MyClickHandlers handlers;
    private Cost mCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityShowCostDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        handlers = new MyClickHandlers(this);
        mBinding.setHandlers(handlers);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String costId = getIntent().getStringExtra("COST_ID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mBinding.progressBar2.setVisibility(View.VISIBLE);
        db.collection("cost").document(costId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    mCost = new Cost(documentSnapshot.getId(), documentSnapshot.getData());
                    mBinding.textInputEditTextAmount.setText(mCost.amount);
                    mBinding.textInputEditTextCategory.setText(mCost.category);
                    mBinding.textInputEditTextDate.setText(mCost.date);
                    mBinding.textInputEditTextTitle.setText(mCost.title);
                    mBinding.textInputEditTextDescription.setText(mCost.description);
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
            Intent intent = new Intent(context, EditCostActivity.class);
            intent.putExtra("COST_ID", mCost.id);
            context.startActivity(intent);
        }

        public void btnDelete(View view) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("cost").document(mCost.id).delete()
                    .addOnFailureListener(e -> {
                        Toast.makeText(ShowCostDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        mBinding.progressBar.setVisibility(View.GONE);
                    })
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(ShowCostDetailsActivity.this, "آیتم با موفقیت حذف شد.", Toast.LENGTH_LONG).show();
                        mBinding.progressBar.setVisibility(View.GONE);
                        finish();
                    });
        }

        public void btnBack(View view) {
            finish();
        }
    }
}