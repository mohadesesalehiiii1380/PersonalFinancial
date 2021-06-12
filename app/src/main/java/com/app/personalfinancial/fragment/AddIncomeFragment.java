package com.app.personalfinancial.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.personalfinancial.databinding.FragmentAddIncomeBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AddIncomeFragment extends Fragment {

    public AddIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddIncomeBinding mBinding;
    private MyClickHandlers handlers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddIncomeBinding.inflate(getLayoutInflater());
        handlers = new MyClickHandlers(getContext());
        mBinding.setHandlers(handlers);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public class MyClickHandlers {
        Context context;

        public MyClickHandlers(Context context) {
            this.context = context;
        }

        public void btnAdd(View view) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("title", mBinding.textInputEditTextTitle.getText().toString());
            user.put("amount", mBinding.textInputEditTextAmount.getText().toString());
            user.put("date", mBinding.textInputEditTextDate.getText().toString());
            user.put("description", mBinding.textInputEditTextDescription.getText().toString());
            user.put("category", mBinding.textInputEditTextCategory.getText().toString());
            mBinding.progressBar.setVisibility(View.VISIBLE);
            mBinding.btnLogin.setVisibility(View.GONE);
            db.collection("income")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "درآمد با موفقیت ثبت شد", Toast.LENGTH_LONG).show();
                        mBinding.textInputEditTextTitle.setText("");
                        mBinding.textInputEditTextAmount.setText("");
                        mBinding.textInputEditTextDate.setText("");
                        mBinding.textInputEditTextDescription.setText("");
                        mBinding.textInputEditTextCategory.setText("");
                        mBinding.progressBar.setVisibility(View.GONE);
                        mBinding.btnLogin.setVisibility(View.VISIBLE);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        mBinding.progressBar.setVisibility(View.GONE);
                        mBinding.btnLogin.setVisibility(View.VISIBLE);
                    });
        }
    }

}