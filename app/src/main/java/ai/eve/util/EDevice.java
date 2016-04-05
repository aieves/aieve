package ai.eve.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * <p>
 * 设备工具类
 * </p>
 * @author Eve-huoyaning
 * @version 2015-09-10 上午10:12:14
 * @Copyright 2015 EVE. All rights reserved.
 */
public class EDevice {

    /**
     * 获取当前客户端版本号
     * @param context
     * @return
     */
    public static int getCurrentVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionCode;
    }
    /**
     * 获取当前客户端版本名字
     * @param context
     * @return
     */
    public static String getCurrentVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;
    }


    /**
     * 获取内网IP地址
     * @param context
     * @return
     */
    public static String GetIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                return intToIp(wifiInfo.getIpAddress());
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * int型Ip转化成String
     * @param ipAddress
     * @return
     */
    private static String intToIp(int ipAddress) {
        return (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF) + "." + ((ipAddress >> 24) & 0xFF);
    }

    /**
     * 获取外网IP地址
     * @param context
     * @return
     */
    public String GetNetIpAddress(Context context) {
        if (IsNetAvailable(context)) {
            return "";
        }
        URL infoUrl;
        InputStream inputStream;
        try {
            infoUrl = new URL("http://www.baidu.com");
            URLConnection urlConnection = infoUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                inputStream.close();
                int start = stringBuilder.indexOf("[") + 1;
                int end = stringBuilder.indexOf("]", start);
                line = stringBuilder.substring(start, end);
                return line;
            }
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return "";
    }

    /**
     * 获取设备MAC地址
     * @param context
     * @return
     */
    public static String GetMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getMacAddress();
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * 检测设备GPS是否打开
     * @param context
     * @return
     */
    public static boolean IsGPSOpen(Context context) {
        LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        return false;
    }

    /**
     * 检测是否有网络
     * @param context
     * @return
     */
    public static boolean IsNetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return networkInfo.getState() == NetworkInfo.State.CONNECTED;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断网络是否连接WIFI
     * @param context
     * @return
     */
    public static boolean IsWIFIConnected(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo != null ? wifiInfo.getIpAddress() : 0;
            return wifiManager.isWifiEnabled() && ipAddress != 0;
        }
        return false;
    }

    /**
     * 获取设备联网类型
     * @param context
     * @return -1代表无连接,1代表wifi,0代表连接gprs
     */
    public static int GetConnectionType(Context context) {
        if (IsWIFIConnected(context)) {
            return ConnectivityManager.TYPE_WIFI;
        }
        if (IsNetAvailable(context)) {
            return ConnectivityManager.TYPE_MOBILE;
        }
        return -1;
    }

    /**
     * 获取设备IMEI
     * @param context
     * @return
     */
    public static String GetIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Activity.TELEPHONY_SERVICE);
        if (telephonyManager != null && PackageManager.PERMISSION_GRANTED == context.getApplicationContext().getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE, context.getApplicationContext().getPackageName())) {
            return telephonyManager.getDeviceId();
        }
        return "";
    }

    /**
     * 获取设备类型 （PHONE_TYPE_NONE PHONE_TYPE_GSM PHONE_TYPE_CDMA PHONE_TYPE_SIP）
     * @param context
     * @return
     */
    public static int GetPhoneType(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Activity.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getPhoneType();
        }
        return  TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取设备系统SDK版本
     * @return
     */
    public static String GetSysVersion() {
        return Build.VERSION.SDK;
    }

    /**
     * 获得注册的网络运营商的国家代码
     * @param context
     * @return
     */
    public static String GetNetWorkCountryIso(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkCountryIso();
    }

    /**
     * 获得移动网络运营商的名字
     * @param context
     * @return
     */
    public static String GetNetWorkOperator(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkOperator();
    }

    /**
     * 获得注册的网络运营商的名字
     * @param context
     * @return
     */
    public static String GetNetWorkOperatorName(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }

    /**
     * 获取网络类型
     * @param context
     * @return
     */
    public static int GetNetworkType(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkType();
    }

    /**
     * 获取当前连接的网络名称 (Mobile WIFI OFFLINE)
     * @param context
     * @return
     */
    public static String GetConnectTypeName(Context context) {
        if (!IsNetAvailable(context)) {
            return "OFFLINE";
        }
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.getTypeName();
        } else {
            return "OFFLINE";
        }
    }

    /**
     * 获取系统可用内存
     * @param context
     * @return
     */
    public static long GetFreeMem(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        long free = info.availMem / 1024 / 1024;
        return free;
    }

    /**
     * 获取系统内存容量
     * @return
     */
    public static long GetTotalMem() {
        try {
            FileReader fr = new FileReader("/proc/meminfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split("\\s+");
            Log.w("EPhoneInfo", text);
            return Long.valueOf(array[1]) / 1024;
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return -1;
    }

    /**
     * 获取设备CPU信息
     * @return
     */
    public static String GetCpuInfo() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
                Log.w("EPhoneInfo", " .....  " + array[i]);
            }
            Log.w("EPhoneInfo", text);
            return array[1];
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return null;
    }

    /**
     * 获取设备制造商名称
     * @return
     */
    public static String GetProductName() {
        return Build.PRODUCT;
    }

    /**
     * 获取设备型号
     * @return
     */
    public static String GetModelName() {
        return Build.MODEL;
    }

    /**
     * 获取设备硬件制造商
     * @return
     */
    public static String GetManufacturerName() {
        return Build.MANUFACTURER;
    }

    public static String GetSingInfo(Context context){
        try{
            ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(componentInfo.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            return parseSignature(sign.toByteArray());
        }catch (Exception e){
            ELog.E(e.getMessage());
            return null;
        }
    }

    private static String parseSignature(byte[] signature) throws Exception {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
        byte[] bs = cert.getEncoded();
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] mdbs = md.digest(bs);
        return bytesToHexString(mdbs);
    }

    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}