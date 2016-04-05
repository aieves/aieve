package ai.eve.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * <p>
 * 图片处理工具类
 * </p>
 *
 * @author Eve-cuixy
 * @version 2015-09-10 上午11:03:06
 * @Copyright 2015 EVE. All rights reserved.
 */
public class EImage {

    private final static String TAG = "EImageUtils";
    // 目前最大宽度支持596px, 超过则同比缩小
    // 最大高度为1192px, 超过从中截取
    public static final int IMAGE_MAX_WIDTH = 596;
    public static final int IMAGE_MAX_HEIGHT = 1192;

    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap ToGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * 去色同时加圆角
     *
     * @param bmpOriginal 原图
     * @param pixels      圆角弧度
     * @return 修改后的图片
     */
    public static Bitmap ToGrayscale(Bitmap bmpOriginal, int pixels) {
        return ToRoundCorner(ToGrayscale(bmpOriginal), pixels);
    }

    /**
     * 把图片变成圆角
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap ToRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
//
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 图片添加水印
     *
     * @param sourceBmp 源图
     * @param watermark 水印图
     * @return 水印图片
     */
    public Bitmap Watermark(Bitmap sourceBmp, Bitmap watermark) {
        if (sourceBmp == null) {
            return null;
        }
        int w = sourceBmp.getWidth();
        int h = sourceBmp.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap, 创建一个新的和SRC长度宽度一样的位图
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        // draw src into,在 0，0坐标开始画入src
        cv.drawBitmap(sourceBmp, 0, 0, null);
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return 圆形图片
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 保存图片为PNG
     * @param bitmap
     * @param file
     * @return 是否保存成功
     */
    public static boolean SaveToPNG(Bitmap bitmap, File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {

                } else {
                    ELog.E(TAG, "请检查是否有SD卡，或者有SD卡写入权限");
                    return false;
                }
            }
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }else{
                ELog.E(TAG, "保存为png失败");
                return false;
            }
            return true;
        } catch (Exception e) {
        	ELog.E(e.getMessage());
            ELog.E(TAG, "保存为png失败");
            return false;
        }
    }


    /**
     * 保存图片为JPEG
     *
     * @param bitmap
     * @param file
     *  @return 是否保存成功
     */
    public static boolean SaveToJPEG(Bitmap bitmap, File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {

                } else {
                    ELog.E(TAG, "请检查是否有SD卡，或者有SD卡写入权限");
                    return false;
                }
            }
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }else {
                ELog.E(TAG, "保存为JPEG失败");
                return false;
            }
            return true;
        } catch (Exception e) {
            ELog.E(TAG, "保存为JPEG失败");
            ELog.E(e.getMessage());
            return false;
        }
    }

    /**
     * 得到bitmap的byte数组
     *
     * @param bitmap
     * @return
     */
    public static byte[] GetBytesByBitmap(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
        	ELog.E(e.getMessage());
        }
        return out.toByteArray();
    }

    /**
     * byte数组转换为bitmap对象
     *
     * @param bytes
     * @return
     */
    public static Bitmap GetBitmapByBytes(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    /**
     * @param drawable
     * @return drawable图片转为bitmap
     */
    public static Bitmap GetBitmapByDrawable(Drawable drawable) {
        int width = drawable.getIntrinsicWidth(); // drawable
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565; //
        Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
        Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas); // 把drawable内容画到画布中
        return bitmap;
    }

    /**
     * 读取路径中的图片，然后将其转化为缩放后的bitmap
     *
     * @param file
     * @param scaleSize
     * @return
     */
    public static Bitmap GetZoomBitmap(File file, int scaleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleSize; // 图片长宽各缩小scaleSize
        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        ELog.D(TAG, "图片缩放:" + scaleSize + ". W=" + w + " H=" + h);
        return bitmap;
    }

    /**
     * 保持长宽比缩小Bitmap
     *
     * @param bitmap
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap ZoomBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {

        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        // no need to resize
        if (originWidth < maxWidth && originHeight < maxHeight) {
            return bitmap;
        }

        int newWidth = originWidth;
        int newHeight = originHeight;

        // 若图片过宽, 则保持长宽比缩放图片
        if (originWidth > maxWidth) {
            newWidth = maxWidth;

            double i = originWidth * 1.0 / maxWidth;
            newHeight = (int) Math.floor(originHeight / i);

            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight,
                    true);
        }

        // 若图片过长, 则从中部截取
        if (newHeight > maxHeight) {
            newHeight = maxHeight;

            int half_diff = (int) ((originHeight - maxHeight) / 2.0);
            bitmap = Bitmap.createBitmap(bitmap, 0, half_diff, newWidth,
                    newHeight);
        }
        return bitmap;
    }


    /**
     * 从assets文件下解析图片
     * @param resName 资源文件名称
     * @return  bitmap
     */
    public static Bitmap DecodeBitmapFromAssets(Context context,String resName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;
        options.inInputShareable = true;
        InputStream in = null;
        try {
            //in = AssetsResourcesUtil.openResource(resName);
            in =context.getAssets().open(resName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(in, null, options);
    }


    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";

    /** 请求相册 */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /** 请求相机 */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
    /** 请求裁剪 */
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;


    /**
     * 写图片文件到SD卡
     * @param ctx
     * @param file
     * @param bitmap
     * @throws IOException
     */
    public boolean SaveImageToSD(Context ctx, File file,
                                     Bitmap bitmap, int quality) throws IOException {
        try {
            if (bitmap != null) {
                if (!file.exists()) {
                    file.mkdirs();
                }
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(file.getAbsolutePath()));
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                bos.flush();
                bos.close();
                return true;
            }
            return false;
        }catch (Exception e){
            Log.e(TAG, "保存图片失败");
            ELog.E(e.getMessage());
            return false;
        }
    }

    /**
     * 浏览图片
     * @param ctx
     * @param file
     */
    public static void ScanPhoto(Context ctx, File file) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }


    /**
     * 获取bitmap
     *
     * @param file
     * @return
     */
    public static Bitmap GetBitmapByFile(File file,BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis,null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 使用当前时间戳拼接一个唯一的文件名
     *
     * @param
     * @return
     */

    public static String GetTempFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
        String fileName = format.format(new Timestamp(System
                .currentTimeMillis()));
        return fileName;
    }

//
//    /**
//     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
//     *
//     * @param
//     * @return
//     */
//    public static String GetAbsolutePathFromNoStandardUri(Uri mUri) {
//        String filePath = null;
//
//        String mUriString = mUri.toString();
//        mUriString = Uri.decode(mUriString);
//
//        String pre1 = "file://" + SDCARD + File.separator;
//        String pre2 = "file://" + SDCARD_MNT + File.separator;
//
//        if (mUriString.startsWith(pre1)) {
//            filePath = Environment.getExternalStorageDirectory().getPath()
//                    + File.separator + mUriString.substring(pre1.length());
//        } else if (mUriString.startsWith(pre2)) {
//            filePath = Environment.getExternalStorageDirectory().getPath()
//                    + File.separator + mUriString.substring(pre2.length());
//        }
//        return filePath;
//    }
//
//    /**
//     * 通过uri获取文件的绝对路径
//     *
//     * @param uri
//     * @return
//     */
//    public static String GetAbsoluteImagePath(Activity context, Uri uri) {
//        String imagePath = "";
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = context.managedQuery(uri, proj, // Which columns to
//                // return
//                null, // WHERE clause; which rows to return (all rows)
//                null, // WHERE clause selection arguments (none)
//                null); // Order-by clause (ascending by name)
//
//        if (cursor != null) {
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
//                imagePath = cursor.getString(column_index);
//            }
//        }
//
//        return imagePath;
//    }

    /**
     * 获取图片缩略图 只有Android2.1以上版本支持
     *
     * @param
     * @param kind
     *            MediaStore.Images.Thumbnails.MICRO_KIND
     * @return
     */
    public static Bitmap LoadImgThumbnail(Activity context, File file,
                                          int kind) {
        Bitmap bitmap = null;

        String[] proj = { MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME };

        Cursor cursor = context.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
                MediaStore.Images.Media.DISPLAY_NAME + "='" + file.getName() + "'",
                null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            ContentResolver crThumb = context.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), cursor.getInt(0), MediaStore.Images.Thumbnails.MICRO_KIND, null);
//            bitmap = MethodsCompat.getThumbnail(crThumb, cursor.getInt(0),
//                    kind, options);
        }
        return bitmap;
    }

    public static Bitmap loadImgThumbnail(File file, int w, int h) {
        Bitmap bitmap = GetBitmapByFile(file, null);
        return ZoomBitmap(bitmap, w, h);
    }


    /**
     * 计算缩放图片的宽高
     *
     * @param img_size
     * @param square_size
     * @return
     */
    public static int[] ScaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size)
            return img_size;
        double ratio = square_size
                / (double) Math.max(img_size[0], img_size[1]);
        return new int[] { (int) (img_size[0] * ratio),
                (int) (img_size[1] * ratio) };
    }

    /**
     * 创建缩略图
     *
     * @param context
     * @param file
     *            原始大图路径
     * @param thumbfilePath
     *            输出缩略图路径
     * @param square_size
     *            输出图片宽度
     * @param quality
     *            输出图片质量
     * @throws IOException
     */
    public static Bitmap createImageThumbnail(Context context,
                                            File file, String thumbfilePath, int square_size,
                                            int quality) throws IOException {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        // 原始图片bitmap
        Bitmap cur_bitmap = GetBitmapByFile(file, opts);

        if (cur_bitmap == null)
            return null;

        try {
            // 原始图片的高宽
            int[] cur_img_size = new int[] { cur_bitmap.getWidth(),
                    cur_bitmap.getHeight() };
            // 计算原始图片缩放后的宽高
            int[] new_img_size = ScaleImageSize(cur_img_size, square_size);
            // 生成缩放后的bitmap
            Bitmap thb_bitmap = ZoomBitmap(cur_bitmap, new_img_size[0],
                    new_img_size[1]);
            return thb_bitmap;
        }catch (Exception e){
        	ELog.E(e.getMessage());
            Log.e(TAG, "创建缩略图失败");
            return  null;
        }

    }



//    //读取图片使用
//    public static final String[] STORE_IMAGES={
//            MediaStore.Images.Media._ID,
//            MediaStore.Images.Media.DATA,
//            MediaStore.Images.Media.DISPLAY_NAME,
//            MediaStore.Images.Media.LATITUDE,
//            MediaStore.Images.Media.LONGITUDE,
//            MediaStore.Images.Media.BUCKET_ID,
//            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
//    };
//
//    //读取缩略图
//    public static final String[] THUMB_IMAGES={
//            MediaStore.Images.Media.DISPLAY_NAME,
//            MediaStore.Images.Media.LATITUDE,
//            MediaStore.Images.Media.LONGITUDE,
//            MediaStore.Images.Media._ID,
//            MediaStore.Images.Media.DATA,
//            MediaStore.Images.Media.BUCKET_ID,
//            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//    };
//
//    /**
//     * 根据图片Uri获取图片地址
//     * @param uri
//     * @return
//     */
//    public static String GetRealFilePath(Context mContext,Uri uri) {
//        if (null == uri)
//            return null;
//        final String scheme = uri.getScheme();
//        String data = null;
//        if (scheme == null)
//            data = uri.getPath();
//        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
//            data = uri.getPath();
//        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
//            Cursor cursor = mContext.getContentResolver().query(uri,
//                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
//            if (null != cursor) {
//                if (cursor.moveToFirst()) {
//                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                    if (index > -1) {
//                        data = cursor.getString(index);
//                    }
//                }
//                cursor.close();
//            }
//        }
//        return data;
//    }
//
//
//    /**
//     * 获取相册中的所有图片（缩略图）
//     * @return map--string：原图对应的image_id Bitmap---缩略图
//     */
//    public static List<String> GetThumbnailPhotos(Context mContext){
//        List<String> list = new ArrayList<String>();
//        Cursor cursor = MediaStore.Images.Media.query(mContext.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, THUMB_IMAGES, null, null, null);
//        while (cursor.moveToNext()) {
//            String id = cursor.getString(3);
//            String url = cursor.getString(4);
//            list.add(url);
//        }
//        cursor.close();
//        return list;
//    }
//
//    /**
//     * 获取相册中的所有图片（缩略图）
//     * @return map--string：原图对应的image_id Bitmap---缩略图
//     */
//    public static List<Bitmap> GetThumbnailBitmaps(Context mContext){
//        List<Bitmap> list = new ArrayList<Bitmap>();
//        Cursor cursor = MediaStore.Images.Media.query(mContext.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, THUMB_IMAGES,null,null,null);
//        Bitmap photo =null;
//        while (cursor.moveToNext()) {
//            String id = cursor.getString(3);
//            photo = MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(), Integer.parseInt(id), MediaStore.Images.Thumbnails.MICRO_KIND, null);
//            list.add(photo);
//        }
//        cursor.close();
//        return list;
//    }
//
//    /**
//     * 根据缩略图获取原图信息
//     * 表thumbnails和images通过thumbnails.image_id与images._id关联的，通过images的_id，
//     * 就可以找出来thumbnails表中的图片和images表中图片的映射关系了。
//     * 原始图片的位置就是images表中的_data字段的值。
//     * @return 原始图片url
//     */
//    public static String GetPhotoByThumbnail(Context mContext,String image_id){
//        Cursor cursor = MediaStore.Images.Media.query(mContext.getContentResolver(),MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,MediaStore.Images.Media._ID + "=" + image_id, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            cursor.close();
//            return path;
//        }else{
//            return "";
//        }
//    }


}
