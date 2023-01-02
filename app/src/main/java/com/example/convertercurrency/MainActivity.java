package com.example.convertercurrency;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    private EditText editCurrency;
    private ProgressBar loadingPB;

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
        loadingPB = findViewById(R.id.idPBLoading);
        editCurrency = findViewById(R.id.idEdtCurrency);

        ListView listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new RecyclerAdapter(this, R.layout.list_item, arrayList, getLayoutInflater());
        listView.setAdapter(adapter);
        Runnable runnable = this::getPage;
        search();
        Thread secondTh = new Thread(runnable);
        secondTh.start();
    }

    private void search() {
        editCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String filter) {
        ArrayList<ListCurrency> filteredlist = new ArrayList<>();
        for (ListCurrency item : arrayList) {
            if (item.getNameCode().toLowerCase().contains(filter.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "Не нашли...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, filteredlist.get(filteredlist.size() - 1).getNameCode() + " "
                    + filteredlist.get(filteredlist.size() - 1).getUnits() + " в рублях " +
                    filteredlist.get(filteredlist.size() - 1).getCourse(), Toast.LENGTH_LONG).show();
        }
    }

    private void getPage() {
        try {
            loadingPB.setVisibility(View.GONE);
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