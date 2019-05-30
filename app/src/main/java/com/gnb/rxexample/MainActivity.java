package com.gnb.rxexample;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);
        textView = findViewById(R.id.counter_number);

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

        /*resetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = 0;
                        counterSubject.onNext(count);
                    }
                }
        );*/
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
