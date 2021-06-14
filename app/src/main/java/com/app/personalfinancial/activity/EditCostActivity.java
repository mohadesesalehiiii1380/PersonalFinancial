package com.app.personalfinancial.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.personalfinancial.databinding.ActivityEditCostBinding;
import com.app.personalfinancial.model.Cost;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditCostActivity extends AppCompatActivity {

    private ActivityEditCostBinding mBinding;
    private MyClickHandlers handlers;
    private Cost mCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEditCostBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        handlers = new MyClickHandlers(this);
        mBinding.setHandlers(handlers);
        String costId = getIntent().getStringExtra("COST_ID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mBinding.progressBar2.setVisibility(View.VISIBLE);
        db.collection("cost").document(costId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    mCost = new Cost(documentSnapshot.getId(), documentSnapshot.getData());
                    mBinding.textInputEditTextAmount.setText(mCost.amount);
                    mBinding.textInputEditTextCategory.setText(mCost.category);
                    mBinding.textInputEditTextTitle.setText(mCost.title);
                    mBinding.textInputEditTextDate.setText(mCost.date);
                    mBinding.textInputEditTextDescription.setText(mCost.description);
                    mBinding.progressBar2.setVisibility(View.GONE);

                })
                .addOnFailureListener(e -> {
                    mBinding.progressBar2.setVisibility(View.GONE);
                });

        mBinding.textInputEditTextDate.setInputType(InputType.TYPE_NULL);
        mBinding.textInputEditTextDate.setKeyListener(null);
        mBinding.textInputEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

    }

    private void openDatePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mBinding.textInputEditTextDate.setText(year + " / " + (monthOfYear + 1) + " / " + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public class MyClickHandlers {
        Context context;

        public MyClickHandlers(Context context) {
            this.context = context;
        }

        public void btnSave(View view) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", mBinding.textInputEditTextTitle.getText().toString());
            map.put("amount", mBinding.textInputEditTextAmount.getText().toString());
            map.put("date", mBinding.textInputEditTextDate.getText().toString());
            map.put("description", mBinding.textInputEditTextDescription.getText().toString());
            map.put("category", mBinding.textInputEditTextCategory.getText().toString());

            mBinding.progressBar.setVisibility(View.VISIBLE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("cost").document(mCost.id).update(map)
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditCostActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        mBinding.progressBar.setVisibility(View.GONE);
                    })
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(EditCostActivity.this, "ویرایش با موفقیت انجام شد.", Toast.LENGTH_LONG).show();
                        mBinding.progressBar.setVisibility(View.GONE);
                        finish();
                    });
        }

        public void btnBack(View view) {
            finish();
        }
    }
}