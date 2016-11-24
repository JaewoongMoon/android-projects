package org.example.translate.translate;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public class Translate extends Activity {

    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText origText;
    private TextView transText;
    private TextView retransText;
    private TextWatcher textWatcher;
    private AdapterView.OnItemSelectedListener itemListener;
    private Handler guiThread;
    private ExecutorService transThread;
    private Runnable updateTask;
    private Future transPending;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        initThreading();
        findViews();
        setAdapters();
        setListeners();

    }

    private void findViews() {
        fromSpinner = (Spinner) findViewById(R.id.from_language);
        toSpinner = (Spinner) findViewById(R.id.to_language);
        origText = (EditText) findViewById(R.id.original_text);
        transText = (TextView) findViewById(R.id.translate_text);
        retransText = (TextView) findViewById(R.id.retranslated_text);
    }

    private void setAdapters(){
        // リスースからスピなのリストを構築する
        //スピナーユーザインタペェースは標準レイアウトを使う
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.languages,
                android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        // Automatically select two spinner items
        fromSpinner.setSelection(1);
        toSpinner.setSelection(5);
    }

    private void setListeners(){
        // event listener
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                queueUpdate(1000 /* milli second */);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        itemListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                queueUpdate(200);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        // listener setup
        origText.addTextChangedListener(textWatcher);
        fromSpinner.setOnItemSelectedListener(itemListener);
        toSpinner.setOnItemSelectedListener(itemListener);
    }

    private void initThreading(){
        guiThread = new Handler();
        transThread = Executors.newSingleThreadExecutor();

        // このタスクが翻訳すると画面変更を行う
        updateTask = new Runnable(){
            @Override
            public void run() {
                String original = origText.getText().toString().trim();

                // すでに翻訳がある場合、それをキャンセルする
                if (transPending != null)
                    transPending.cancel(true);

                // 簡単な条件の処理
                if (original.length() == 0){
                    transText.setText(R.string.empty);
                    retransText.setText(R.string.translating);
                } else {
                    // ユーザに処理をしていることを知らせる
                    transText.setText(R.string.translating);
                    retransText.setText(R.string.translating);

                    //　翻訳を開始するが終了を持たない
                    try{
                        TranslateTask translateTask = new TranslateTask(
                                Translate.this,
                                original,
                                getLang(fromSpinner),
                                getLang(toSpinner)
                        );
                        transPending = transThread.submit(translateTask);
                    } catch (RejectedExecutionException e){
                        transText.setText(R.string.translation_error);
                        retransText.setText(R.string.translation_error);
                    }
                }

            }
        };
    }

    private String getLang(Spinner spinner){
        String result = spinner.getSelectedItem().toString();
        int lparen = result.indexOf('(');
        int rparen = result.indexOf(')');
        result= result.substring(lparen + 1, rparen);
        return result;
    }

    private void queueUpdate(long delayMillis){
        guiThread.removeCallbacks(updateTask);
        guiThread.postDelayed(updateTask, delayMillis);
    }

    public void setTranslated(String text){
        guiSetText(transText, text);
    }

    public void setRetranslated(String text){
        guiSetText(retransText, text);
    }

    private void guiSetText(final TextView view, final String text){
        guiThread.post(new Runnable(){
            public void run(){
                view.setText(text);
            }
        });
    }


}
