
package util.file;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtil {

	public static String HOMEPATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	public static String chatImagePath = HOMEPATH;

	public static void createFile() {
		File destDir = new File(chatImagePath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
	}
	
	public static int dip2px(Context context, float dpValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (dpValue * scale + 0.5f); 
    }  
}
