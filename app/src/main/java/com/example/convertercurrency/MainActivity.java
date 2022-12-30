package com.example.convertercurrency;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.convertercurrency.View.RecyclerAdapter;
import com.example.convertercurrency.model.ListCurrency;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerAdapter adapter;
    private List<ListCurrency> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (internetChecker()) {
            init();
        } else dialogPage();

    }

    private void dialogPage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("У вас интернет отключен, подключайтесь!")
                .setCancelable(false)
                .setPositiveButton("Хорошо", (dialog, id) -> {
                    finish();
                    startActivity(getIntent());
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean internetChecker() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    private void init() {
        ListView listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new RecyclerAdapter(this, R.layout.list_item, arrayList, getLayoutInflater());
        listView.setAdapter(adapter);
        Runnable runnable = this::getPage;
        Thread secondTh = new Thread(runnable);
        secondTh.start();
    }

    private void getPage() {
        try {
            Document document = Jsoup.connect("http://www.cbr.ru/currency_base/daily/").get();
            Elements currencyTables = document.getElementsByTag("tbody");
            Element table = currencyTables.get(0);

            for (int i = 0; i < table.childrenSize(); i++) {
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