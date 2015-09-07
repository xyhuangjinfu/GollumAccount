package cn.hjf.gollumaccount.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;

/**
 * 主题相关的工具类
 * 
 * @author xfujohn
 * 
 */
public final class BitmapUtil {

    /**
     * 获得从某一个drawable文件得到的跟主题颜色相同的图标Bitmap
     * @param context 上下文
     * @param resId drawable文件的id
     * @param color 希望生成的颜色
     * @return
     */
    public static Bitmap getColorfulBitmap(Context context, int resId, int color) {
        //获得源Bitmap
        Bitmap srcBitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        //从源Bitmap复制一个Mutable的Bitmap
        Bitmap destBitmap = srcBitmap.copy(Config.ARGB_8888, true);
        //把非透明的像素点转换成需要的颜色
        int width = destBitmap.getWidth();
        int height = destBitmap.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (destBitmap.getPixel(i, j) != 0) {
                    destBitmap.setPixel(i, j, context.getResources().getColor(color));
                }
            }
        }
        //释放源Bitmap
        srcBitmap.recycle();
        return destBitmap;
    }
}
