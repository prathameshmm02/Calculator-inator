package com.example.calculator.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.calculator.Model.History;
import com.example.calculator.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<History> historyItems;

    public HistoryAdapter(Context context2, ArrayList<History> historyItems2) {
        this.context = context2;
        this.historyItems = historyItems2;
    }

    @NotNull
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.history_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        History history = this.historyItems.get(position);
        holder.expression.setText(history.getExpr());
        holder.answer.setText(history.getAnswer());
    }

    public int getItemCount() {
        return this.historyItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView answer;
        TextView expression;

        public ViewHolder(View itemView) {
            super(itemView);
            this.expression = itemView.findViewById(R.id.expression);
            this.answer = itemView.findViewById(R.id.answer);
        }
    }
}
