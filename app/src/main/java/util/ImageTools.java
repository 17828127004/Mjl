package util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Tools for handler picture
 *
 * @author Duanhao
 *
 */
public final class ImageTools {

	/**
	 * Transfer drawable to bitmap
	 *
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap to drawable
	 *
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * Input stream to bitmap
	 *
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static Bitmap inputStreamToBitmap(InputStream inputStream)
			throws Exception {
		return BitmapFactory.decodeStream(inputStream);
	}

	/**
	 * Byte transfer to bitmap
	 *
	 * @param byteArray
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory
					.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	/**
	 * Byte transfer to drawable
	 *
	 * @param byteArray
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = null;
		if (byteArray != null) {
			ins = new ByteArrayInputStream(byteArray);
		}
		return Drawable.createFromStream(ins, null);
	}

	/**
	 * Bitmap transfer to bytes
	 *
	 * @param
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		byte[] bytes = null;
		if (bm != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			bytes = baos.toByteArray();
		}
		return bytes;
	}

	/**
	 * Drawable transfer to bytes
	 *
	 * @param drawable
	 * @return
	 */
	public static byte[] drawableToBytes(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		byte[] bytes = bitmapToBytes(bitmap);
		;
		return bytes;
	}

	/**
	 * Base64 to byte[] //
	 */
	// public static byte[] base64ToBytes(String base64) throws IOException {
	// byte[] bytes = Base64.decode(base64);
	// return bytes;
	// }
	//
	// /**
	// * Byte[] to base64
	// */
	// public static String bytesTobase64(byte[] bytes) {
	// String base64 = Base64.encode(bytes);
	// return base64;
	// }

	/**
	 * Create reflection images
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
				h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * Get rounded corner images
	 *
	 * @param bitmap
	 * @param roundPx
	 *            5 10
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Resize the bitmap
	 *
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/**
	 * Resize the drawable
	 *
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);
		Matrix matrix = new Matrix();
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		matrix.postScale(sx, sy);
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true);
		return new BitmapDrawable(newbmp);
	}

	/**
	 * Get images from SD card by path and the name of image
	 *
	 * @param photoName
	 * @return
	 */
	public static Bitmap getPhotoFromSDCard(String path, String photoName) {
		Bitmap photoBitmap = BitmapFactory.decodeFile(path + "/" + photoName);
		if (photoBitmap == null) {
			return null;
		} else {
			return photoBitmap;
		}
	}

	/**
	 * Check the SD card
	 *
	 * @return
	 */
	public static boolean checkSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * Get image from SD card by path and the name of image
	 *
	 * @param
	 * @return
	 */
	public static boolean findPhotoFromSDCard(String path, String photoName) {
		boolean flag = false;

		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (dir.exists()) {
				File folders = new File(path);
				File photoFile[] = folders.listFiles();
				for (int i = 0; i < photoFile.length; i++) {
					String fileName = photoFile[i].getName().split("\\.")[0];
					if (fileName.equals(photoName)) {
						flag = true;
					}
				}
			} else {
				flag = false;
			}
			// File file = new File(path + "/" + photoName + ".jpg" );
			// if (file.exists()) {
			// flag = true;
			// }else {
			// flag = false;
			// }

		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * Save image to the SD card
	 *
	 * @param photoBitmap
	 * @param
	 * @param path
	 */
	public static File savePhotoToSDCard(Bitmap photoBitmap, String path) {
		if (checkSDCardAvailable()) {
			File photoFile = new File(path);
			FileOutputStream fileOutputStream = null;
			try {

				if (!photoFile.exists()) photoFile.createNewFile();
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 45,
							fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return photoFile;
		} else {
			return null;
		}
	}

	/**
	 * Delete the image from SD card
	 *
	 * @param
	 * @param path
	 *            file:///sdcard/temp.jpg
	 */
	public static void deleteAllPhoto(String path) {
		if (checkSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
	}

	public static void deletePhotoAtPathAndName(String path, String fileName) {
		if (checkSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().split("\\.")[0].equals(fileName)) {
					files[i].delete();
				}
			}
		}
	}

	@SuppressLint("NewApi")
	public static Bitmap getLoacalBitmap(String draw_name) {
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize = 1;
			opt.inPreferredConfig = Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true; // ��ȡ��ԴͼƬ
			FileInputStream fis = new FileInputStream(draw_name);
			return BitmapFactory.decodeStream(fis, null, opt);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 图片按比例大小压缩方法（根据Bitmap图片压缩＄1ķ
	 *
	 * @param image
	 * @return
	 */
	public static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos

			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos丄1ķ

		}

		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

		BitmapFactory.Options newOpts = new BitmapFactory.Options();

		// 弄1ķ始读入图片，此时把options.inJustDecodeBounds 设回true亄1ķ

		newOpts.inJustDecodeBounds = true;

		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

		newOpts.inJustDecodeBounds = false;

		int w = newOpts.outWidth;

		int h = newOpts.outHeight;

		// 现在主流手机比较多是800*480分辨率，扄1ķ以高和宽我们设置丄1ķ

		float hh = 800f;// 这里设置高度丄1ķ0f

		float ww = 480f;// 这里设置宽度丄1ķ0f

		// 缩放比ㄱķçԱ于是固定比例缩放，只用高或脱ķƥν其中丄1ķ个数据进行计算即叄1ķ

		int be = 1;// be=1表示不缩攄1ķ

		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩攄1ķ

			be = (int) (newOpts.outWidth / ww);

		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩攄1ķ

			be = (int) (newOpts.outHeight / hh);

		}

		if (be <= 0)

			be = 1;

		newOpts.inSampleSize = be;// 设置缩放比例

		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false亄1ķ

		isBm = new ByteArrayInputStream(baos.toByteArray());

		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缄1ķ

	}

	private static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这釄1ķ0表示不压缩，把压缩后的数据存放到baos丄1ķ

		int options = 100;

		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大亄1ķ0kb,大于继续压缩
			baos.reset();// 重置baos即清空baos

			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos丄1ķ

			options -= 10;// 每次都减射1ķ

		}

		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream丄1ķ

		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

		return bitmap;

	}

	// @SuppressLint("NewApi")
	// public static Bitmap getVideoThumbnail(String videoPath, Context con) {
	// Bitmap bitmapSrc = ThumbnailUtils.createVideoThumbnail(videoPath,
	// MediaStore.Images.Thumbnails.MICRO_KIND);
	// // System.out.println("w"+bitmap.getWidth());
	// // System.out.println("h"+bitmap.getHeight());
	// // bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
	// // ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	// Bitmap b1 = BitmapFactory.decodeResource(con.getResources(),
	// R.drawable.video_icon);
	// Bitmap bitmap = null;
	// if (bitmapSrc != null) {
	// bitmap = Bitmap.createBitmap(bitmapSrc.getWidth(),
	// bitmapSrc.getHeight(), Config.ARGB_8888);
	// } else {
	// bitmap = Bitmap.createBitmap(100, 100, Config.ARGB_8888);
	// }
	// Canvas cv = new Canvas(bitmap);
	// if (bitmapSrc != null) {
	// cv.drawBitmap(bitmapSrc, 0, 0, null);
	// }
	// cv.drawBitmap(b1, 25, 25, null);
	// cv.save(Canvas.ALL_SAVE_FLAG);
	// cv.restore();
	// return bitmap;
	// }
	/**
	 * 根据丄1ķ个网络连掄1ķ(String)获取bitmap图像
	 *
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmap(String imageUri) {
		// 显示网络上的图片
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			Log.i("imagetools:", "image download finished." + imageUri);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	 public static Bitmap compressImageFromFile(String srcPath) {
	 BitmapFactory.Options newOpts = new BitmapFactory.Options();
	 newOpts.inJustDecodeBounds = true;//ֻׁҟ,һׁŚɝ
	 Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

	 newOpts.inJustDecodeBounds = false;
	 int w = newOpts.outWidth;
	 int h = newOpts.outHeight;
	 float hh = 800f;//
	 float ww = 480f;//
	 int be = 1;
	 if (w > h && w > ww) {
	 be = (int) (newOpts.outWidth / ww);
	 } else if (w < h && h > hh) {
	 be = (int) (newOpts.outHeight / hh);
	 }
	 if (be <= 0)
	 be = 1;
	 newOpts.inSampleSize = be;//ʨ׃ӉҹÊ

	 newOpts.inPreferredConfig = Config.ARGB_8888;//كģʽˇĬɏք,ࠉһʨ
	 newOpts.inPurgeable = true;// ͬʱʨ׃ӅܡԐЧ
	 newOpts.inInputShareable = true;//cձϵͳŚզһٻʱ۲ͼƬؔ֯ѻܘ˕

	 bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
	 // return compressBmpFromBmp(bitmap);//ԭ4ք׽רַԃ֢ٶ׽רǳͼ޸ѐ׾Վѹ̵
	 //ǤʵˇϞЧք,ճݒޡڜӢ˔
	 return bitmap;
	 }

	public static Bitmap rotateBitMap(Bitmap bp) {
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.postRotate(90);
		Bitmap nowBp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(),
				bp.getHeight(), matrix, true);
		if (bp.isRecycled()) {
			bp.recycle();
		}
		return nowBp;
	}

}
