package com.codeboy.qianghongbao.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


import com.codeboy.qianghongbao.QHBApplication;
import com.codeboy.qianghongbao.alipyActivity;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Zero on 6/4/2016.
 */
public class AutoUpdateTask extends AsyncTask<Void, Integer, Boolean> {
    Context context = null;
    ProgressDialog progressDialog = null;
    String appName = null;
    String domainName = null;

    public AutoUpdateTask(Context context) {
        this.context = context;
        this.appName = "midpay";
        if ("1".equals(QHBApplication.platfrom))
            this.appName = "shunfupay";
//        if ("2".equals(QHBApplication.platfrom))
//            this.appName = "shoulu";
//        this.domainName = "http://www.shunfu.me/";
        this.domainName = "http://download.wandapay88.com/";
        if ("1".equals(QHBApplication.platfrom))
            this.domainName = "http://47.75.68.254/images/";
//        this.domainName = "http://10.10.32.153:8081/";
//        this.appName = "record";
//        this.domainName = "http://pms-ftp.neweb.me/";
//        if ("anxeuz".equals(alipyActivity.depart))
//            this.domainName = "http://anxeus.autopay8.me:90/logs/";
//        else if ("ipeak".equals(alipyActivity.depart))
//            this.domainName = "http://10.10.10.202:90/logs/";
    }

    @Override
    protected void onPreExecute() {
        Log.i("M1 " + getClass().getSimpleName() + " " + new Throwable().getStackTrace()[0].getLineNumber(), "onPreExecute Called");
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("发现新版本");
        progressDialog.setCancelable(true);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean result = false;
        if (!NetworkUtil.isNetworkAvailable(context)) {
            return false;
        }
        try {
            String response = NetworkUtil.getStringFromURL(domainName + "version.json").trim();
//            String response = NetworkUtil.getStringFromURL("www.baidu." + "version.json").trim();
            Gson gson = new Gson();
            version person = gson.fromJson(response, version.class);

            if (response != null) {
                response = response.trim();
            }
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;

//            int comparedResult = Integer.parseInt(person.getMidpay()) - version;
            int comparedResult = 0;
            if ("1".equals(QHBApplication.platfrom)) {
                Log.e("getVersion", person.getShunfupay());
                comparedResult = Integer.parseInt(person.getShunfupay()) - version;
                Log.e("getVersion", person.getMidpay());
            } else {
                Log.e("getVersion", person.getMidpay());
                comparedResult = Integer.parseInt(person.getMidpay()) - version;
            }
//            int comparedResult = 10 ;
            Log.i("M1 " + getClass().getSimpleName() + " " + new Throwable().getStackTrace()[0].getLineNumber(), "versionResult : " + comparedResult);
//            comparedResult = 1;
            if (comparedResult > 0) {
                int count = 0;
//                try {
                URL url = new URL(domainName + appName + ".apk");
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                //c.setDoOutput(true);
                c.connect();
                int length = c.getContentLength();
                String PATH = Environment.getExternalStorageDirectory() + "/download/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, appName + "_update.apk");
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                long total = 0;
                while ((count = is.read(buffer)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / length));
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();//till here, it works fine - .apk is download to my sdcard in download file
                result = true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        publishProgress(100);
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setProgress(values[0]);
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.i("M1 " + getClass().getSimpleName() + " " + new Throwable().getStackTrace()[0].getLineNumber(), "onPostExecute Called");
        if (result) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + appName + "_update.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
//            Intent intent = new Intent(context, WebviewActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(intent);
        }
        progressDialog.dismiss();
        super.onPostExecute(result);
    }

    /**
     * Compares two version strings.
     * <p/>
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     * The result is a positive integer if str1 is _numerically_ greater than str2.
     * The result is zero if the strings are _numerically_ equal.
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     */
    public Integer versionCompare(String str1, String str2) {
        str2 = str2.equals("") ? "1.0.0" : str2;
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else {
            return Integer.signum(vals1.length - vals2.length);
        }
    }

    class version {
        private String wehcat_test;
        private String midpay;
        private String shunfupay;

        public String getWehcat_test() {
            return wehcat_test;
        }

        public void setWehcat_test(String wehcat_test) {
            this.wehcat_test = wehcat_test;
        }

        public String getMidpay() {
            return midpay;
        }

        public String getShunfupay() {
            return shunfupay;
        }

        public void setShunfupay(String shunfupay) {
            this.shunfupay = shunfupay;
        }

        public void setMidpay(String midpay) {
            this.midpay = midpay;
        }
    }
}