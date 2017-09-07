package top.zibin.luban;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * created by sfx on 2017/9/7.
 */

public class LitNoise {
    public static Bitmap noise(Bitmap bitmap){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);

        int r, g, b, color;
        int[] oldPx = new int[w *h];
        int[] newPx = new int[w *h];
        int num = (int)(0.2f * 32768f);
        bitmap.getPixels(oldPx, 0, w, 0, 0, w, h);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {

                color = oldPx[x * h + y];
                r = Color.red(color);
                g = Color.green(color);
                b = Color.blue(color);

                if (num != 0){
                    int rr = getRandomInt(-255, 0xff) * num;
                    int gg = getRandomInt(-255, 0xff) * num;
                    int bb = getRandomInt(-255, 0xff) * num;
                    int rrr = r + (rr >> 15);
                    int ggg = g + (gg >> 15);
                    int bbb = b + (bb >> 15);
                    r = (rrr > 0xff) ? ((byte)0xff) : ((rrr < 0) ? ((byte)0) : ((byte)rrr));
                    g = (ggg > 0xff) ? ((byte)0xff) : ((ggg < 0) ? ((byte)0) : ((byte)ggg));
                    b = (bbb > 0xff) ? ((byte)0xff) : ((bbb < 0) ? ((byte)0) : ((byte)bbb));
                }
                newPx[x * h + y] = Color.rgb(r, g, b);
            }
        }
        result.setPixels(newPx, 0, w, 0, 0, w, h);
        return result;
    }

    static int getRandomInt(int a, int b) {
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return min + (int)(Math.random() * (max - min + 1));
    }
}
