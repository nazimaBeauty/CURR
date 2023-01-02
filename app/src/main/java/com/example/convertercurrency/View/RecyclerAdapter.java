package com.example.convertercurrency.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.convertercurrency.CalculatePage;
import com.example.convertercurrency.R;
import com.example.convertercurrency.model.ListCurrency;

import java.util.List;

public class RecyclerAdapter extends ArrayAdapter<ListCurrency> {
    private final LayoutInflater inflater;
    private List<ListCurrency> list;

    public RecyclerAdapter(@NonNull Context context, int resource, List<ListCurrency> list, LayoutInflater inflater) {
        super(context, resource, list);
        this.inflater = inflater;
        this.list = list;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.first = convertView.findViewById(R.id.one);
            viewHolder.second = convertView.findViewById(R.id.two);
            viewHolder.third = convertView.findViewById(R.id.three);
            viewHolder.fourth = convertView.findViewById(R.id.four);
            viewHolder.fifth = convertView.findViewById(R.id.five);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.first.setText(list.get(position).getNumberCode());
        viewHolder.second.setText(list.get(position).getNameCode());
        viewHolder.third.setText(list.get(position).getUnits());
        viewHolder.fourth.setText(list.get(position).getCurrencyName());
        viewHolder.fifth.setText(list.get(position).getCourse());

        convertView.setOnClickListener(view -> {
            if (isString(viewHolder.first.getText().toString())) {
                Intent calculate = new Intent(getContext(), CalculatePage.class);
                calculate.putExtra("NUMCODE", viewHolder.first.getText().toString());
                calculate.putExtra("ALPHCODE", viewHolder.second.getText().toString());
                calculate.putExtra("UNIT", viewHolder.third.getText().toString());
                calculate.putExtra("NAMECUR", viewHolder.fourth.getText().toString());
                calculate.putExtra("COURSE", viewHolder.fifth.getText().toString());
                calculate.putExtra("TYPE", "NAN");
                getContext().startActivity(calculate);
            } else {
                Toast.makeText(getContext(), "Вы не можете найти баг :)", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private boolean isString(String s) {
        boolean checker = false;
        for (int i = 0; i < s.length(); i++) {
            if ((int) s.charAt(i) >= 48 && (int) s.charAt(i) <= 57) {
                checker = true;
            }
        }
        return checker;
    }

    public void filterList(List<ListCurrency> filteredlist) {
        this.list = filteredlist;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView first, second, third, fourth, fifth;
    }
}
