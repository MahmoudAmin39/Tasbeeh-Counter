package com.gnb.rxexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Locale;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button plusButton;
    Button resetButton;

    AppCompatRadioButton noResetRadioButton;
    AppCompatRadioButton reset33RadioButton;
    AppCompatRadioButton reset100RadioButton;

    Integer count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.counter_number);
        plusButton = findViewById(R.id.plus_btn);
        resetButton = findViewById(R.id.reset_btn);
        noResetRadioButton = findViewById(R.id.no_reset_btn);
        noResetRadioButton.setChecked(true);
        reset33RadioButton = findViewById(R.id.reset_33_btn);
        reset100RadioButton = findViewById(R.id.reset_100_btn);

        // Counter observation logic
        final BehaviorSubject<Integer> counterSubject = BehaviorSubject.createDefault(count);
        counterSubject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer integer) {
                textView.setText(String.format(Locale.getDefault(), "%d", integer));
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

        plusButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((reset33RadioButton.isChecked() && count == 33) || (reset100RadioButton.isChecked() && count == 100)) {
                            count = 0;
                        } else {
                            count += 1;
                        }
                        counterSubject.onNext(count);
                    }
                }
        );

        resetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = 0;
                        counterSubject.onNext(count);
                    }
                }
        );

        noResetRadioButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            reset33RadioButton.setChecked(false);
                            reset100RadioButton.setChecked(false);
                        }
                    }
                }
        );
        reset33RadioButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            noResetRadioButton.setChecked(false);
                            reset100RadioButton.setChecked(false);
                        }
                    }
                }
        );
        reset100RadioButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            noResetRadioButton.setChecked(false);
                            reset33RadioButton.setChecked(false);
                        }
                    }
                }
        );
    }
}
