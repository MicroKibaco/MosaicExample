package com.asiainfo.mosaic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asiainfo.mosaic.R;
import com.asiainfo.mosaic.bean.ImagePiece;
import com.asiainfo.mosaic.utils.ImageSplitterUtil;

import java.util.Collections;
import java.util.List;

/**
 * 作者:小木箱 邮箱:yangzy3@asiainfo.com 创建时间:2017年02月11日15点19分 描述:自定义拼图
 */
public class KiniroMosaicLayout extends RelativeLayout implements View.OnClickListener {

    private static final int TIME_CHANGED = 0x110;
    private static final int NEXT_LEVEL = 0x111;
    //设置九宫格的样式3X3格式
    private int mColumn = 3;
    //容器的内边距
    private int mPadding;
    //每张小图之间的距离(横,纵)
    private int mMagin = 3;
    private ImageView[] mMosaicItems;
    private int mItemWidth;
    /***
     * 游戏的图片
     */
    private Bitmap mBitmap;
    private List<ImagePiece> mItemBitmaps;
    private boolean once;
    /***
     * 游戏面板宽度
     *
     * @param context
     */
    private int mWidth;
    /***
     * 动画层
     */
    private RelativeLayout mAnimLayout;
    private ImageView mFirst;
    private ImageView mSecond;
    private boolean IsAniming;
    private GameMosaicListener mListener;
    private int mLevel;
    private boolean isTimeEnable = false;
    private boolean isGameSucess;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case TIME_CHANGED:
                    break;

                case NEXT_LEVEL:

                    mLevel = mLevel + 1;

                    if (mListener != null) {
                        mListener.nextLevel(mLevel);

                    } else {

                        nextLevel();

                    }

                    break;

                default:
                    break;


            }
        }
    };

    public KiniroMosaicLayout(Context context) {
        this(context, null);
    }

    public KiniroMosaicLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public KiniroMosaicLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public GameMosaicListener getListener() {
        return mListener;
    }

    /***
     * 设置接口回调
     */

    public void setOnGameMosaicListener(GameMosaicListener listener) {
        this.mListener = listener;
    }

    /***
     * 设置是否开启时间
     */
    public void setTimeEnable(boolean timeEnable) {
        isTimeEnable = timeEnable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //取宽高的最小值作为容器的最小值
        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
        if (!once) {


            initBitmap();

            initItem();

            once = true;
        }

        //强制调用setMeasuredDimension 让所有的控件占据正方形
        setMeasuredDimension(mWidth, mWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /***
     * 进行切图,以及排序
     */
    private void initBitmap() {

        if (mBitmap == null) {

            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img);

        }

        mItemBitmaps = ImageSplitterUtil.splitImage(mBitmap, mColumn);

        //使用sort完成我们的乱序
        Collections.shuffle(mItemBitmaps);

    }

    /***
     * 设置ImageView(Item)的宽高等属性
     */
    private void initItem() {

        mItemWidth = (mWidth - mPadding * 2 - mMagin * (mColumn - 1)) / mColumn;

        mMosaicItems = new ImageView[mColumn * mColumn];

        //生成我们的Item,设置Rule
        for (int i = 0; i < mMosaicItems.length; i++) {

            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);

            item.setImageBitmap(mItemBitmaps.get(i).getBitmap());

            mMosaicItems[i] = item;

            item.setId(i + 1);

            //在item里面存储了item的Index
            item.setTag(i + "_" + mItemBitmaps.get(i).getIndex());

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);

            //设置item间的横向间距,通过rightMargin
            // 不是最后一列,给第一列设置右边距

            if ((i + 1) % mColumn != 0) {

                lp.rightMargin = mMagin;

            }

            //不是第一列

            if (i % mColumn != 0) {

                lp.addRule(RelativeLayout.RIGHT_OF,
                        mMosaicItems[i - 1].getId());

            }

            //如果不是第一行,设置topMargin和rule
            if ((i + 1) > mColumn) {

                lp.topMargin = mMagin;
                lp.addRule(RelativeLayout.BELOW, mMosaicItems[i - mColumn].getId());


            }

            addView(item, lp);

        }

    }

    /***
     * 初始化操作
     */
    private void initView() {

        mMagin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                getResources().getDisplayMetrics());
        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(), getPaddingBottom());

    }

    /***
     * 获取多个参数的最小值
     */
    private int min(int... params) {

        int min = params[0];

        for (int param : params) {

            if (param < min) {

                min = param;

            }

        }

        return min;
    }

    /***
     * 下一关的操作逻辑
     */
    public void nextLevel() {

        this.removeAllViews();
        mAnimLayout = null;
        mColumn++;
        isGameSucess = false;
        initBitmap();
        initItem();

    }

    @Override
    public void onClick(View v) {

        if (IsAniming) {

            return;

        }
        //两次点击的都是同一张图片则取消高亮
        if (mFirst == v) {

            mFirst.setColorFilter(null);
            mFirst = null;

            return;

        }

        //第一次点击
        if (mFirst == null) {

            mFirst = (ImageView) v;

            //设置透明度
            mFirst.setColorFilter(Color.parseColor("#55F00000"));

        } else {


            mSecond = (ImageView) v;


            //交换图片
            exChangeView();
        }


    }

    /***
     * 交换图片item
     */

    private void exChangeView() {

        //取消第一张图片的高亮状态
        mFirst.setColorFilter(null);

        //准备动画层
        setUpAnimLayout();

        /***
         * 将第一次选择图片复制到动画层
         */
        ImageView first = new ImageView(getContext());
        final String firstTag = (String) mFirst.getTag();
        final Bitmap firstImgBitmap = mItemBitmaps.get(getImageByTag(firstTag)).getBitmap();
        first.setImageBitmap(firstImgBitmap);

        //设置布局属性
        RelativeLayout.LayoutParams lpfirst = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
        lpfirst.leftMargin = mFirst.getLeft() - mPadding;
        lpfirst.topMargin = mFirst.getTop() - mPadding;
        first.setLayoutParams(lpfirst);
        mAnimLayout.addView(first);


        /***
         * 将第二次选择图片复制到动画层
         */
        ImageView second = new ImageView(getContext());
        final String secondTag = (String) mSecond.getTag();
        final Bitmap secondImgBitmap = mItemBitmaps.get(getImageByTag(secondTag)).getBitmap();
        second.setImageBitmap(secondImgBitmap);

        //设置布局属性
        RelativeLayout.LayoutParams lpsecond = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
        lpsecond.leftMargin = mFirst.getLeft() - mPadding;
        lpsecond.topMargin = mFirst.getTop() - mPadding;
        second.setLayoutParams(lpsecond);
        mAnimLayout.addView(second);


        /***
         * 设置动画
         */
        float toFirstXDelta = mSecond.getLeft() - mFirst.getLeft();
        float toFirstYDelta = mSecond.getTop() - mFirst.getTop();
        TranslateAnimation animFirst = new TranslateAnimation(0, toFirstXDelta, 0, toFirstYDelta);
        animFirst.setFillAfter(true);
        animFirst.setDuration(400);
        first.startAnimation(animFirst);

        float toSecondXDelta = -mSecond.getLeft() + mFirst.getLeft();
        float toSecondYDelta = -mSecond.getTop() + mFirst.getTop();
        TranslateAnimation animSecond = new TranslateAnimation(0, toSecondXDelta, 0, toSecondYDelta);
        animSecond.setFillAfter(true);
        animSecond.setDuration(400);
        second.startAnimation(animSecond);

        animFirst.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                //将两张图片隐藏
                mFirst.setVisibility(View.INVISIBLE);
                mSecond.setVisibility(View.INVISIBLE);
                IsAniming = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {


                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);

                mFirst.setImageBitmap(secondImgBitmap);
                mSecond.setImageBitmap(firstImgBitmap);

                mFirst.setVisibility(VISIBLE);
                mSecond.setVisibility(VISIBLE);

                mFirst = mSecond = null;

                mAnimLayout.removeAllViews();

                //判断用户游戏是否成功
                checkSucess();
                IsAniming = false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //第二张图片的监听动画
        animSecond.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /***
     * 判断用户游戏是否成功
     */
    private void checkSucess() {

        boolean isSucess = true;

        for (int i = 0; i < mMosaicItems.length; i++) {

            ImageView imageView = mMosaicItems[i];

            if (getImageIndexByTag((String) imageView.getTag()) != i) {

                isSucess = false;

            }

        }
        if (isSucess) {

            Log.e("TAG", "Sucess!");
            Toast.makeText(getContext(), "Sucess ,level up!!!", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessage(NEXT_LEVEL);

        }
    }

    /***
     * 根据tag获取id
     */

    public int getImageByTag(String tag) {

        String[] imgArraryId = tag.split("_");

        return Integer.parseInt(imgArraryId[0]);

    }

    /***
     * 根据tag获取id
     */

    public int getImageIndexByTag(String tag) {

        String[] imgArraryIndex = tag.split("_");

        return Integer.parseInt(imgArraryIndex[1]);

    }

    /***
     * 构造我们的动画层
     */
    private void setUpAnimLayout() {

        if (mAnimLayout == null) {

            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);

        }

    }

    public interface GameMosaicListener {

        void nextLevel(int nextLevel);

        void timeChange(int currentTime);

        void gameOver();

    }


}
