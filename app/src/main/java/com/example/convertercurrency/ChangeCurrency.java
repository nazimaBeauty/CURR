package com.example.convertercurrency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.convertercurrency.View.ChangeAdapter;
import com.example.convertercurrency.View.ItemClickListener;
import com.example.convertercurrency.View.RecyclerAdapter;
import com.example.convertercurrency.model.ListCurrency;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangeCurrency extends AppCompatActivity {
    RecyclerView recyclerView;
    ItemClickListener itemClickListener;
    ChangeAdapter adapter;
    List<ListCurrency> arrayList;
    String type, value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_currency);
        init();
        action();
    }

    private void init() {
        Bundle extras = getIntent().getExtras();
        type = extras.getString("TYPE");
        value = extras.getString("VALUE");

        recyclerView = findViewById(R.id.recycler_view);
        arrayList = new ArrayList<>();
        Runnable runnable = this::getPage;
        Thread secondTh = new Thread(runnable);
        secondTh.start();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void action() {
        itemClickListener = s -> {
            recyclerView.post(() -> adapter.notifyDataSetChanged());
            Intent calculatePage = new Intent(this, CalculatePage.class);
            calculatePage.putExtra("TYPE", type);
            if (type.equals("FIRST")) {
                calculatePage.putExtra("VALUE", s);
            } else {
                calculatePage.putExtra("VALUE", value);
                calculatePage.putExtra("VALUE2", s);
                System.out.println(s + "*****************************" + value);
            }
            startActivity(calculatePage);
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChangeAdapter(arrayList, itemClickListener, value);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getPage() {
        try {
            Document document = Jsoup.connect("http://www.cbr.ru/currency_base/daily/").get();
            Elements currencyTables = document.getElementsByTag("tbody");
            Element table = currencyTables.get(0);

            for (int i = 1; i < table.childrenSize(); i++) {
                ListCurrency listCurrency = new ListCurrency(table.children().get(i).child(0).text(),
                        table.children().get(i).child(1).text(),
                        table.children().get(i).child(2).text(),
                        table.children().get(i).child(3).text(),
                        table.children().get(i).child(4).text()
                );
                arrayList.add(listCurrency);
            }
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}