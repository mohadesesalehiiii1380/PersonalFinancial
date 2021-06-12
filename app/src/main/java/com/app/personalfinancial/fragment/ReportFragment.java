package com.app.personalfinancial.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.app.personalfinancial.adapter.TransactionAdapter;
import com.app.personalfinancial.databinding.FragmentReportBinding;
import com.app.personalfinancial.model.Cost;
import com.app.personalfinancial.model.Income;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {
    private static final String TAG = "ReportFragment";
    private FragmentReportBinding mBinding;
    private MyClickHandlers handlers;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentReportBinding.inflate(getLayoutInflater());
        handlers = new MyClickHandlers(getContext());
        mBinding.setHandlers(handlers);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    int income = 0;
    int cost = 0;

    private void getData() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("income")
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task) -> {
                    if (task.isSuccessful()) {
                        List<Object> incomes = new ArrayList<>();
                        income = 0;
                        cost = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Income income1 = new Income(document.getId(), document.getData());
                            income = income + Integer.parseInt(income1.amount);
                            incomes.add(income1);
                        }
                        db.collection("cost")
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task1.getResult()) {
                                            Cost cost1 = new Cost(document.getId(), document.getData());
                                            cost = cost + Integer.parseInt(cost1.amount);
                                            incomes.add(cost1);
                                        }
                                        mBinding.recyclerView.setAdapter(new TransactionAdapter(getContext(), incomes));
                                        mBinding.textViewCosts.setText(cost + " تومان");
                                        mBinding.textViewIncomes.setText(income + " تومان");
                                        mBinding.textView.setText(" تومان" + (income - cost));
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task1.getException());
                                    }
                                    mBinding.progressBar.setVisibility(View.GONE);
                                });
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        mBinding.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public class MyClickHandlers {
        Context context;

        public MyClickHandlers(Context context) {
            this.context = context;
        }

        public void btnAdd(View view) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

}

