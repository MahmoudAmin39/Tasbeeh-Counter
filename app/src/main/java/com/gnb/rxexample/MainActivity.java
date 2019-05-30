package com.gnb.rxexample;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ConstraintLayout container;

    Integer count = 0;
    private Boolean is33Reset = false;
    private Boolean is100Reset = false;
    private String languageCode = "";

    BehaviorSubject<Integer> counterSubject = BehaviorSubject.createDefault(count);
    Disposable disposable;

    private String[] counterResetOptions;
    private String[] languageOptions;

    static String LANGUAGE = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get selected language
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        languageCode = preferences.getString(LANGUAGE, "ar");
        changeLanguage();
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);
        textView = findViewById(R.id.counter_number);

        counterResetOptions = getResources().getStringArray(R.array.counter_options_items);
        languageOptions = getResources().getStringArray(R.array.language_options_items);

        // Counter observation logic
        disposable = counterSubject.subscribe(
                new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        textView.setText(String.format(Locale.getDefault(), "%d", integer));
                    }
                }
        );

        container.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((is33Reset && count == 33) || (is100Reset && count == 100)) {
                            count = 0;
                        } else {
                            count += 1;
                        }
                        counterSubject.onNext(count);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                count = 0;
                counterSubject.onNext(count);
                return true;
            case R.id.menu_settings:
                int checkedItem = 0;
                if (is33Reset) {
                    checkedItem = 1;
                } else if (is100Reset) {
                    checkedItem = 2;
                }

                new AlertDialog.Builder(this).setSingleChoiceItems(counterResetOptions, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            is33Reset = false;
                            is100Reset = false;
                        } else if (which == 1) {
                            is33Reset = true;
                            is100Reset = false;
                        } else {
                            is33Reset = false;
                            is100Reset = true;
                        }
                        dialog.dismiss();
                    }
                }).show();
                return true;
            case R.id.menu_language:
                int checkedLanguage = 0;
                if (languageCode.equals("en")) {
                    checkedLanguage = 1;
                }
                new AlertDialog.Builder(this).setSingleChoiceItems(languageOptions, checkedLanguage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            languageCode = "ar";
                            changeLanguage();
                        } else {
                            languageCode = "en";
                            changeLanguage();
                        }
                        dialog.dismiss();
                    }
                }).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeLanguage() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        android.content.res.Configuration conf = getResources().getConfiguration();
        conf.setLocale(new Locale(languageCode));
        getResources().updateConfiguration(conf, dm);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        counterResetOptions = getResources().getStringArray(R.array.counter_options_items);
        getPreferences(MODE_PRIVATE).edit().putString(LANGUAGE, languageCode).apply();
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}