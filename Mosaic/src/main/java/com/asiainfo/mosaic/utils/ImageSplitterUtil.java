package com.asiainfo.mosaic.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:小木箱 邮箱:yangzy3@asiainfo.com 创建时间:2017年02月11日14点58分 描述:
 */
public class ImageSplitterUtil {

    /**
     * 描述:传入Bitmap,切成Piece*piece快,返回List
     * 创建时间:2/11/17/14:59 作者:小木箱 邮箱:yangzy3@asiainfo.com
     * @param bitmap 图
     * @param piece 块数
     */

    public static List<ImagePiece> splitImage(Bitmap bitmap,int piece){

        List<ImagePiece> imgPieces = new ArrayList<>();

        //获取Bitmap的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //得到每一块的宽度
        int piecewidth = Math.min(width, height)/piece;

        //切图
        for (int i = 0; i < piece; i++) {

            for (int j = 0; j < piece; j++) {

                ImagePiece imgp = new ImagePiece();

                imgp.setIndex(j + i*piece);

                //获取需要截图的图片坐标
                int x = j * piecewidth;
                int y = i * piecewidth;

                //创建每一个小图
                imgp.setBitmap(Bitmap.createBitmap(bitmap,x,y,piecewidth,piecewidth));

                //加入到整个集合
                imgPieces.add(imgp);

            }

        }

        return imgPieces;

    }

}
