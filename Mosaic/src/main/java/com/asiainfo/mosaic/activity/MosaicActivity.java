package com.asiainfo.mosaic.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.asiainfo.mosaic.R;
import com.asiainfo.mosaic.view.KiniroMosaicLayout;

/***
 * TODO 实现游戏时间逻辑 TODO GameOver与暂停
 */
public class MosaicActivity extends Activity implements KiniroMosaicLayout.GameMosaicListener {

    private KiniroMosaicLayout mGameMosaicLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic);

        initView();
        initListener();

    }

    private void initListener() {

        mGameMosaicLayout.setOnGameMosaicListener(this);

    }

    private void initView() {

        mGameMosaicLayout = (KiniroMosaicLayout) findViewById(R.id.game_mosaic);

    }

    @Override
    public void nextLevel(int nextLevel) {

        new AlertDialog.Builder(this).setTitle("Game Info")
                .setMessage("LEVEL UP!!!").setPositiveButton("NEXT LEVEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mGameMosaicLayout.nextLevel();

            }
        }).show();
    }

    @Override
    public void timeChange(int currentTime) {

    }

    @Override
    public void gameOver() {

    }
}
