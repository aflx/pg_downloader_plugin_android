/**
 * aflx - always flexible
 * http://www.aflx.de
 * ak@aflx.de
 * 
 * I have adapted the classname and signature of the method of this plugin for
 * Android and iOS.
 * 
 * Copyright 2011 Alexander Keller
 * All Rights Reserved.
 * 
 * Thanks to Mauro Rocco 
 * http://www.toforge.com
 */

package de.aflx.phonegap.plugins;
 
import org.json.JSONArray;
import org.json.JSONException;
 
import android.util.Log;
 
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
 
public class Downloader extends Plugin{
 
	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		if (action.equals("downloadFile")) {
			try {
				return this.downloadFile(args.getString(0),args.getString(1),args.getString(2));
			} catch (JSONException e) {
				return new PluginResult(PluginResult.Status.ERROR, "Param errrors");
			}
		} else {
			return new PluginResult(PluginResult.Status.INVALID_ACTION);
		}
	}
 
	private PluginResult downloadFile(String fileUrl, String dirName, String fileName){
		try {
			File dir = new File(dirName);
			if(!dir.exists()){
				Log.d("DownloaderPlugin", "directory "+dirName+" created");
				dir.mkdirs();
			}
 
			File file = new File(dirName+fileName);
			URL url = new URL(fileUrl);
			HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
			ucon.setRequestMethod("GET");
			ucon.setDoOutput(true);
			ucon.connect();
 
			Log.d("DownloaderPlugin", "download url:" + url);
 
			InputStream is = ucon.getInputStream();
			byte[] buffer = new byte[1024];
			int len1 = 0;
 
			FileOutputStream fos = new FileOutputStream(file);
 
			while ( (len1 = is.read(buffer)) > 0 ) {
				fos.write(buffer,0, len1);
			}
 
			fos.close();
 
			Log.d("DownloaderPlugin", "Downloaded file " + dirName+fileName);
 
		} catch (IOException e) {
 
			Log.d("DownloaderPlugin", "Error: " + e);
			return new PluginResult(PluginResult.Status.ERROR, "Error: " + e);
 
		}
 
		return new PluginResult(PluginResult.Status.OK, dirName+fileName);
	}
}