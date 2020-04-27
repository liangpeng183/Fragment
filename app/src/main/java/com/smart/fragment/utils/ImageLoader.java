package com.smart.fragment.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

/*
 *  自定义 设置圆形头像
 * */
public class ImageLoader {

  public static Bitmap getRoundBitmap(Bitmap bitmap) {
    // 获取传入进来的Bitmap宽度和高度
    int width = bitmap.getWidth();
    int height = bitmap.getHeight();
    int r = 0;// 园的半径
    if (width < height) {
      r = width;
    } else {
      r = height;
    }
    // 新建一个bitmap，相当于传入进来的bitmap的copy
    Bitmap backgroundBitmap = Bitmap.createBitmap(width, height,
            Bitmap.Config.ARGB_8888);
    // 建立画布
    Canvas canvas = new Canvas(backgroundBitmap);
    // 建立画笔
    Paint paint = new Paint();
    // 无锯齿
    paint.setAntiAlias(true);
    // 矩形
    RectF rect = new RectF(0, 0, r, r);
    // 画圆角矩形，当它的宽和高一样时，就是一个圆了
    canvas.drawRoundRect(rect, r / 2, r / 2, paint);
    // 取画布和bitmap相交的部分，即展示的圆
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    // 画出圆形头像
    canvas.drawBitmap(bitmap, null, rect, paint);

    return backgroundBitmap;
  }


}
