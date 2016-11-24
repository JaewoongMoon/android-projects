package org.example.translate.translate;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jwmoon on 2016. 11. 11..
 */

public class TranslateTask implements Runnable {

    private static final String TAG = "TranslateTask";
    private final Translate translate;
    private final String original, from , to ;

    TranslateTask(Translate translate, String original, String from , String to){
        this.translate = translate;
        this.original = original;
        this.from = from;
        this.to = to;
    }

    public void run(){
        String trans = doTranslate(original, from, to );
        translate.setTranslated(trans);

        String retrans = doTranslate(trans, to, from);
        translate.setRetranslated(retrans);
    }

    private String doTranslate(String original, String from, String to){
        String result = translate.getResources().getString(R.string.translation_error);
        HttpURLConnection con = null;
        Log.d(TAG, "doTranslate(" + original + ", " + from + ", " + to + ")");

        try {
            if (Thread.interrupted()){
                throw new InterruptedException();
            }

            // Google API に送るRESTFulクエリを構築
            String q = URLEncoder.encode(original, "UTF-8");
            URL url = new URL(
                    "http://www.googleapis.com/language/translate/v2?parameters"
                    + "&q=" + q + "&source=" + from + "&target=" + to
                    + "&key=2567bbf2e8a48f5140ad24d31c80f1421385a7ef"
            );

            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000 /* milli */);
            con.setConnectTimeout(15000);
            con.setRequestMethod("GET");
            con.addRequestProperty("Referer", "http://www.pragprog.com/titles/eband3/hello-android");
            con.setDoInput(true);

            // クエリ開始
            con.connect();

            // タスクが割り込まれているかどうかをチェック
            if (Thread.interrupted())
                throw new InterruptedException();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));
            String payload = reader.readLine();
            reader.close();

            //
            JSONObject jsonObject = new JSONObject(payload);
            result = jsonObject.getJSONObject("responseData")
                    .getString("translateText")
                    .replace("&#39", "'")
                    .replace("&amp;", "&");

            if (Thread.interrupted())
                throw new InterruptedException();


        } catch(IOException e){
            Log.e(TAG, "IOException", e);
        } catch (JSONException e){
            Log.e(TAG, "JSONException", e);
        } catch (InterruptedException e){
            Log.d(TAG, "InterruptedException", e);
            result = translate.getResources().getString(R.string.translation_interrupted);
        } finally {
            if (con != null){
                con.disconnect();
            }
        }

        //
        Log.d(TAG, " -> returned " + result);
        return result;
    }

}
