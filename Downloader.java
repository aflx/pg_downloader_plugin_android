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

    public static int FILE_NOT_FOUND_ERR = 1;
    public static int INVALID_URL_ERR = 2;
    public static int CONNECTION_ERR = 3;

    @Override
    public PluginResult execute(String action, JSONArray args, String callbackId) {
        if (action.equals("download")) {
            String source = null;
            String target = null;

            try {
                source = args.getString(0);
                target = args.getString(1);
            }
            catch (JSONException e) {
                Log.d(LOG_TAG, "Missing filename or server name");
                return new PluginResult(PluginResult.Status.JSON_EXCEPTION, "Missing filename or server name");
            }

            try {
                JSONObject r = download(source, target);
                Log.d(LOG_TAG, "****** About to return a result from download");
                return new PluginResult(PluginResult.Status.OK, r, "window.localFileSystem._castEntry");
            } catch (Exception e) {
                JSONObject error = createFileTransferError(CONNECTION_ERR, source, target);
                return new PluginResult(PluginResult.Status.IO_EXCEPTION, error);
            }
        } else {
            return new PluginResult(PluginResult.Status.INVALID_ACTION);
        }
    }

    /**
     * Downloads a file form a given URL and saves it to the specified directory.
     *
     * @param source        URL of the server to receive the file
     * @param target      	Full path of the file on the file system
     * @return JSONObject 	the downloaded file
     */
    public JSONObject download(String source, String target) throws IOException, JSONException{
        File file = new File(target);

        // create needed directories
        file.getParentFile().mkdirs();

        // connect to server
        URL url = new URL(source);
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

        Log.d(LOG_TAG, "Saved file: " + target);

        // create FileEntry object
        JSONObject entry = new JSONObject();

        entry.put("isFile", file.isFile());
        entry.put("isDirectory", file.isDirectory());
        entry.put("name", file.getName());
        entry.put("fullPath", file.getAbsolutePath());

        return entry;
    }

    /**
     * Create an error object based on the passed in errorCode
     * @param errorCode 	the error
     * @return JSONObject containing the error
     */
    private JSONObject createFileTransferError(int errorCode, String source, String target) {
        JSONObject error = null;
        try {
            error = new JSONObject();
            error.put("code", errorCode);
            error.put("source", source);
            error.put("target", target);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return error;
    }

}