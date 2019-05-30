package com.gnb.rxexample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

    BehaviorSubject<Integer> counterSubject = BehaviorSubject.createDefault(count);
    Disposable disposable;

    private String[] counterResetOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);
        textView = findViewById(R.id.counter_number);

        counterResetOptions = getResources().getStringArray(R.array.counter_options_items);

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
