package com.app.personalfinancial.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.personalfinancial.R;
import com.app.personalfinancial.databinding.ListitemReportBinding;
import com.app.personalfinancial.model.Cost;
import com.app.personalfinancial.model.Income;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionVH> {
    Context context;
    List<Object> incomeList;

    public TransactionAdapter(Context context, List<Object> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }

    @NotNull
    @Override
    public TransactionVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new TransactionVH(ListitemReportBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TransactionVH holder, int position) {

        if (incomeList.get(position) instanceof Income) {
            Income income = (Income) incomeList.get(position);
            holder.binding.textViewAmount.setTextColor(context.getResources().getColor(R.color.green));
            holder.binding.textViewAmount.setText(income.amount + " تومان");
            holder.binding.textViewTitle.setText(income.title);
            holder.binding.textViewCategory.setText(income.category);

            holder.binding.getRoot().setOnClickListener(v -> {
//                Intent intent = new Intent(context, IncomeDetailsActivity.class);
//                intent.putExtra("income", new Gson().toJson(income));
//                context.startActivity(intent);
            });

        }

        if (incomeList.get(position) instanceof Cost) {
            Cost cost = (Cost) incomeList.get(position);
            holder.binding.textViewAmount.setTextColor(context.getResources().getColor(R.color.red));
            holder.binding.textViewAmount.setText(cost.amount + " تومان");
            holder.binding.textViewTitle.setText(cost.title);
            holder.binding.textViewCategory.setText(cost.category);
        }


    }

    @Override
    public int getItemCount() {
        return incomeList == null ? 0 : incomeList.size();
    }

    public static class TransactionVH extends RecyclerView.ViewHolder {
        ListitemReportBinding binding;

        public TransactionVH(ListitemReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
