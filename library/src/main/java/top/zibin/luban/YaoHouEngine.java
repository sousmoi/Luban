package top.zibin.luban;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */
class YaoHouEngine {
    private ExifInterface srcExif;
    private String srcImg;
    private File tagImg;
    private int srcWidth;
    private int srcHeight;
    private int tagWidth = 720;
    private int tagHeight = 960;
    private Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;

    YaoHouEngine(String srcImg, File tagImg) throws IOException {
        if (Checker.isJPG(srcImg)) {
            this.srcExif = new ExifInterface(srcImg);
        }
        this.tagImg = tagImg;
        this.srcImg = srcImg;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeFile(srcImg, options);
        this.srcWidth = options.outWidth;
        this.srcHeight = options.outHeight;
    }

    YaoHouEngine(String srcImg, File tagImg, int tagWidth, int tagHeight) throws IOException {
        this(srcImg, tagImg);
        this.tagWidth = tagWidth;
        this.tagHeight = tagHeight;

    }

    YaoHouEngine(String srcImg, File tagImg, int tagWidth, int tagHeight, Bitmap.CompressFormat format) throws IOException {
        this(srcImg, tagImg, tagWidth, tagHeight);
        this.format = format;
    }

    private Bitmap rotatingImage(Bitmap bitmap) {
        if (srcExif == null) return bitmap;
        Matrix matrix = new Matrix();
        int angle = 0;
        int orientation = srcExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = 270;
                break;
        }
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    File compress() throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inDensity = srcWidth;
        options.inTargetDensity = tagWidth >= tagHeight ? tagWidth : srcWidth * tagHeight / srcHeight;

        Bitmap tagBitmap = BitmapFactory.decodeFile(srcImg, options);
        tagBitmap = rotatingImage(tagBitmap);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        tagBitmap.compress(format, 70, stream);
        tagBitmap.recycle();

        final FileOutputStream fos = new FileOutputStream(tagImg);
        fos.write(stream.toByteArray());
        fos.flush();
        fos.close();
        stream.close();

        return tagImg;
    }

}