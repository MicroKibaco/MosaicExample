package com.asiainfo.mosaic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.asiainfo.mosaic.R;
import com.asiainfo.mosaic.utils.ImagePiece;
import com.asiainfo.mosaic.utils.ImageSplitterUtil;

import java.util.Collections;
import java.util.List;

/**
 * 作者:小木箱 邮箱:yangzy3@asiainfo.com 创建时间:2017年02月11日15点19分 描述:自定义拼图
 */
public class KiniroMosaicLayout extends RelativeLayout implements View.OnClickListener {

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


    @Override
    public void onClick(View v) {

    }
}
