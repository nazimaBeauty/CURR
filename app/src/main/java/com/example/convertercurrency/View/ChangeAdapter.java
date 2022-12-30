package com.example.convertercurrency.View;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.convertercurrency.R;
import com.example.convertercurrency.model.ListCurrency;

import java.util.List;

public class ChangeAdapter extends RecyclerView.Adapter<ChangeAdapter.ViewHolder> {
    List<ListCurrency> arrayList;
    ItemClickListener itemClickListener;
    int selectedPosition = -1;
    double value, course;

    public ChangeAdapter(List<ListCurrency> arrayList, ItemClickListener itemClickListener, String value) {
        this.arrayList = arrayList;
        this.itemClickListener = itemClickListener;
        String[] temp = value.split(",");
        this.value = Double.parseDouble(temp[1]);
        this.course = Double.parseDouble(temp[2]);
    }

    @NonNull
    @Override
    public ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_change, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.radioButton.setText(arrayList.get(position).getNameCode() + ": " + arrayList.get(position).getCurrencyName());
        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setOnCheckedChangeListener(
                (compoundButton, b) -> {
                    if (b) {
                        selectedPosition = holder.getAdapterPosition();
                        if (value == 0) {
                            itemClickListener.onClick(arrayList.get(position).getNameCode() + "," +
                                    arrayList.get(position).getUnits() + "," +
                                    arrayList.get(position).getCourse());
                        } else {
                            itemClickListener.onClick(arrayList.get(position).getNameCode() + "," +
                                    arrayList.get(position).getUnits() + "," +
                                    countingCurrency(Double.parseDouble(arrayList.get(position).getUnits()),
                                            Double.parseDouble(arrayList.get(position).getCourse().replace(',','.'))));
                        }
                    }
                });
    }

    private String countingCurrency(double a, double b) {
        double ans = (b * value) / course;
        return String.valueOf(ans);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_button);
        }
    }
}
