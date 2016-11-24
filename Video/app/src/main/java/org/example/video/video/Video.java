package org.example.video.video;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class Video extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // リスースをビューに表示する
        setContentView(R.layout.main);
        VideoView video = (VideoView)findViewById(R.id.video);

        // ムービーをロードして開始する
        video.setVideoPath("/data/sampleVideo.mp3");
        video.start();
    }
}
