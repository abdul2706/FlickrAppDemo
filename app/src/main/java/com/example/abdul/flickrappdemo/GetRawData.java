package com.example.abdul.flickrappdemo;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INSTALLED, FAILED_OR_EMPTY, OK}

class GetRawData extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetRawData";
    private DownloadStatus downloadStatus;
    private final OnDownloadComplete callback;

    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    GetRawData(OnDownloadComplete callback) {
        downloadStatus = DownloadStatus.IDLE;
        this.callback = callback;
    }

    public void runOnSameThread(String s) {
        if(callback != null) {
            callback.onDownloadComplete(doInBackground(s), downloadStatus);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i(TAG, "onPostExecute: parameter -> " + s);
        if (callback != null) {
            callback.onDownloadComplete(s, downloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: start");
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            downloadStatus = DownloadStatus.NOT_INSTALLED;
            Log.d(TAG, "doInBackground: end");
            return null;
        }

        try {
            downloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.i(TAG, "doInBackground: responseCode -> " + responseCode);

            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append('\n');
            }

            downloadStatus = DownloadStatus.OK;
            Log.d(TAG, "doInBackground: end");
            return result.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: " + e.getMessage());
            e.printStackTrace();
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error Closing Stream: " + e.getMessage());
                }
            }
        }

        downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        Log.d(TAG, "doInBackground: end");
        return null;
    }

}
