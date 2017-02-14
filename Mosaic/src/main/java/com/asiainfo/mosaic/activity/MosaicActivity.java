package com.asiainfo.mosaic.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.asiainfo.mosaic.R;
import com.asiainfo.mosaic.view.KiniroMosaicLayout;

/***
 * TODO 实现游戏时间逻辑 TODO GameOver与暂停
 */
public class MosaicActivity extends Activity implements KiniroMosaicLayout.GameMosaicListener {

    private KiniroMosaicLayout mGameMosaicLayout;

    private TextView mCurrentLevel;
    private TextView mCountTime;

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
        mCurrentLevel = (TextView) findViewById(R.id.id_level);
        mCountTime = (TextView) findViewById(R.id.id_time);
        mGameMosaicLayout.setTimeEnable(true);

    }

    @Override
    public void nextLevel(final int nextLevel) {

        new AlertDialog.Builder(this).setTitle("黄金拼图")
                .setMessage("恭喜你,过关了!").setPositiveButton("下一关", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mGameMosaicLayout.nextLevel();
                mCurrentLevel.setText("" + nextLevel);
            }
        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        }).show();
    }

    @Override
    public void timeChange(int currentTime) {

        mCountTime.setText("" + currentTime);


    }

    @Override
    public void gameOver() {

        new AlertDialog.Builder(this).setTitle("黄金拼图")
                .setMessage("游戏失败,再接再厉!!").setPositiveButton("重新游戏", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mGameMosaicLayout.restartGame();


            }
        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        }).show();

    }

    @Override
    protected void onPause() {

        super.onPause();
        mGameMosaicLayout.pauseGame();

    }

    @Override
    protected void onResume() {

        super.onResume();
        mGameMosaicLayout.resumeGame();

    }
}
