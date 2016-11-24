package org.example.audio.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

public class Audio extends AppCompatActivity {


    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int resId;
        switch(keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                resId = R.raw.lucky;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                resId = R.raw.latin;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                resId = R.raw.rosas;
                break;
            default :
                return super.onKeyDown(keyCode, event);
        }

        // 前のMediaPlayerのリスースをすべて開放する。
        if (mp != null){
            mp.release();
        }

        // このサウンドの再生のために新いMediaPlayerを作る
        mp = MediaPlayer.create(this, resId);
        mp.start();

        // このキーが処理されたことを知らせる
        return true;
    }
}
