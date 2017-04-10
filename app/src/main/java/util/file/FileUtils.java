package util.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtils {

	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/kxhl/formats/";
	public static String SDPATH1 = Environment.getExternalStorageDirectory()
			+ "/Kxhl/mylogimages/";

	public static void saveBitmap(String sdpath, Bitmap bm, String picName) {
		Log.e("", "保存图片");
		try {
			if (!isFileExist(sdpath, "")) {
				File tempf = createSDDir(sdpath, "");
			}
			File f = new File(sdpath, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			//bm.compress(Bitmap.CompressFormat.PNG, 90, out);//压缩PNG，质量90，输出流
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			Log.e("", "已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String sdpath, String dirName)
			throws IOException {
		File dir = new File(sdpath + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String sdpath, String fileName) {
		File file = new File(sdpath + fileName);
		file.isFile();
		return file.exists();
	}

	// 删除某文件路径下某文件
	public static void delFile(String sdpath, String fileName) {
		File file = new File(sdpath + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(path); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}
	
}
