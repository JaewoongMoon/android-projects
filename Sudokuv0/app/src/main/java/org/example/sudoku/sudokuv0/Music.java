package org.example.sudoku.sudokuv0;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by jwmoon on 2016. 11. 11..
 */

public class Music {
    private static MediaPlayer mp = null;

    public static void play(Context context, int resource){
        stop(context);

        // オプションが無効にされていない時に限り、曲を開始する
        if (Prefs.getMusic(context)) {
            mp = MediaPlayer.create(context, resource);
            mp.setLooping(true);
            mp.start();
        }
    }

    public static void stop(Context context){
        if (mp != null){
            mp.stop();
            mp.release();
            mp = null;
        }
    }


}
