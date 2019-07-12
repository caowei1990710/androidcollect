package com.codeboy.qianghongbao.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Zero on 6/4/2016.
 */
public class NetworkUtil {

    public static String Cookie = null;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = false;
        if (activeNetwork != null) {
            isConnected = activeNetwork.isConnectedOrConnecting();
        }

        return isConnected;
    }

    public static String getStringFromURL(String url) {
        return getStringFromURL(url, false);
    }

    public static String getStringFromURL(String url, boolean resetCookie) {
        String result = "";

        URL urlObj = null;
        HttpURLConnection con = null;
        BufferedReader reader = null;
        try {
//            HttpCookie cookie = new HttpCookie("sessionid", sessionId);
//            CookieManager cookieManager = new CookieManager(CookiePolicy.ACCEPT_ORIGINAL_SERVER);

            urlObj = new URL(url);
            Log.i("urlObj toString() : ", urlObj.toString());
            con = (HttpURLConnection) urlObj.openConnection();
            if (resetCookie) {
                Cookie = con.getHeaderField("Set-Cookie");
            } else {
                if (Cookie != null && !Cookie.isEmpty()) {
                    con.setRequestProperty("Cookie", Cookie);
                }
                Log.i("xyz loggingHeaderFields", "Cookie: " + Cookie + "");
            }
//            con.setConnectTimeout(5000);
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line + "\n");
            }
            result = strBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
        return result;
    }

    public static String getHeaderFromURL(String url) {
        String lastModifiedDate = "";

        URL urlObj = null;
        HttpURLConnection con = null;
        BufferedReader reader = null;
        try {
//            HttpCookie cookie = new HttpCookie("sessionid", sessionId);
//            CookieManager cookieManager = new CookieManager(CookiePolicy.ACCEPT_ORIGINAL_SERVER);

            urlObj = new URL(url);
            Log.i("urlObj toString() : ", urlObj.toString());
            con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("HEAD");

            lastModifiedDate = con.getHeaderField("Last-Modified");
            Log.i("xyz loggingHeaderFields", "lastModifiedDate: " + lastModifiedDate + "");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
        return lastModifiedDate;
    }

    public static void loggingHeaderFields(Map<String, List<String>> headerFields) {
        Set<String> headerFieldsSet = headerFields.keySet();
        Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();

        while (hearerFieldsIter.hasNext()) {
            String headerFieldKey = hearerFieldsIter.next();
            Log.i("xyz loggingHeaderFields", "headerFieldKey: " + headerFieldKey + "");
            if ("Set-Cookie".equalsIgnoreCase(headerFieldKey)) {
                List<String> headerFieldValue = headerFields.get(headerFieldKey);
                for (String headerValue : headerFieldValue) {
                    Log.i("xyz loggingHeaderFields", "headerValue: " + headerValue + "");
                }
            }
        }
    }

    public static Bitmap getBitmapFromURL(String url) {
        Bitmap bitmap = null;

        URL urlObj = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        try {
            urlObj = new URL(url);
            con = (HttpURLConnection) urlObj.openConnection();
            inputStream = con.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
        return bitmap;
    }

    public static String uploadBitmapFromURL(String url, Bitmap bitmap) {
        URL urlObj = null;

        String response = "";

        String attachmentName = "photo";
        String attachmentFileName = "photo.png";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        HttpURLConnection conn = null;
        InputStream responseStream = null;
        try {
            urlObj = new URL(url);

            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            if (Cookie != null && !Cookie.isEmpty()) {
                conn.setRequestProperty("Cookie", Cookie);
                Log.i("zzz uploadBitmapFromURL", "Cookie: " + Cookie + "");
            }

            DataOutputStream request = new DataOutputStream(conn.getOutputStream());
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" + attachmentName + "\";filename=\"" + attachmentFileName + "\"" + crlf);
            request.writeBytes(crlf);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);

            byte[] byteArray = byteStream.toByteArray();
            byteStream.close();
            byteStream = null;

            request.write(byteArray);

            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

            request.flush();
            request.close();

            responseStream = new BufferedInputStream(conn.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            response = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response;
    }
}