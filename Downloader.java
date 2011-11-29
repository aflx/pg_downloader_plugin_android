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

package de.aflx.phonegap.plugins.Downloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private static final String LOG_TAG = "Downloader";

    @Override
    public PluginResult execute(String action, JSONArray args, String callbackId) {
        if (action.equals("download")) {
            String file = null;
            String server = null;

            try {
                file = args.getString(0);
                server = args.getString(1);
            }
            catch (JSONException e) {
                Log.d(LOG_TAG, "Missing filename or server name");
                return new PluginResult(PluginResult.Status.JSON_EXCEPTION, "Missing filename or server name");
            }

            try {
                JSONObject r = download(file, server);
                Log.d(LOG_TAG, "****** About to return a result from download");
                return new PluginResult(PluginResult.Status.OK, r, "window.localFileSystem._castEntry");
            } catch (Exception e) {
                return new PluginResult(PluginResult.Status.ERROR, "Param errrors");
            }
        } else {
            return new PluginResult(PluginResult.Status.INVALID_ACTION);
        }
    }

    /**
     * Downloads a file form a given URL and saves it to the specified directory.
     *
     * @param server        URL of the server to receive the file
     * @param file      	Full path of the file on the file system
     * @return JSONObject 	the downloaded file
     */
    public JSONObject download(String filePath, String sourceUrl) throws IOException {
        try {
            File file = new File(filePath);

            // create needed directories
            file.getParentFile().mkdirs();

            // connect to server
            URL url = new URL(sourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();

            Log.d(LOG_TAG, "Download file:" + url);

            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = 0;

            FileOutputStream outputStream = new FileOutputStream(file);

            // write bytes to file
            while ( (bytesRead = inputStream.read(buffer)) > 0 ) {
                outputStream.write(buffer,0, bytesRead);
            }

            outputStream.close();

            Log.d(LOG_TAG, "Saved file: " + filePath);

            // create FileEntry object
            JSONObject entry = new JSONObject();

            entry.put("isFile", file.isFile());
            entry.put("isDirectory", file.isDirectory());
            entry.put("name", file.getName());
            entry.put("fullPath", file.getAbsolutePath());

            return entry;
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage(), e);
            throw new IOException("Error while downloading");
        }
    }
}