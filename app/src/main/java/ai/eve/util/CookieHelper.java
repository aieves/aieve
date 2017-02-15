package ai.eve.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by wyong on 2016/1/26.
 */
public class cookieHelper {
    private final static String cookieFileName = "cookie";
    public static String getCookies(Context mContext) {
        try {
            // 打开文件输入流，读取cookie
            FileInputStream fileInput = mContext.openFileInput(cookieFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
            String str = null;
            StringBuilder stb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                stb.append(str);
            }
            if (stb.length() > 1) {
                return stb.toString().substring(0, stb.length() - 2);
            } else {
                return null;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }
    public static synchronized void setCookies(Context mContext,HashMap<String, HttpCookie> c) {
        StringBuilder sb = new StringBuilder();
        // 1 读取本地存的cookie
        String getCookie = getCookies(mContext);
        HashMap<String, HttpCookie> map = new HashMap<String, HttpCookie>();
        if (getCookie != null) {
            String cookie[] = getCookie.split(",");
            for (int i = 0; i < cookie.length; i++) {
                String key[] = cookie[i].split(";");
                String name = key[0].split("=")[1];
                String value = key[1].split("=")[1];
                String domain = key[2].split("=")[1];
                String path = key[3].split("=")[1];
                long maxAge = Long.valueOf(key[4].split("=")[1]).longValue();
                int version = Integer.valueOf(key[5].split("=")[1]).intValue();
                String comment = key[6].split("=")[1];
                String commentURL = key[7].split("=")[1];
                String portlist = key[8].split("=")[1];
                boolean secure = Boolean.getBoolean(key[9].split("=")[1]);
                boolean discard = Boolean.getBoolean(key[10].split("=")[1]);

                HttpCookie cook = new HttpCookie(name, "");
                cook.setCommentURL(commentURL);
                cook.setComment(comment);
                cook.setMaxAge(maxAge);
                cook.setPath(path);
                cook.setPortlist(portlist);
                cook.setSecure(secure);
                cook.setValue(value);
                cook.setDomain(domain);
                cook.setVersion(version);
                cook.setDiscard(discard);
                if (!domain.equals("null") && !path.equals("null")) {
                    map.put(name + domain + path, cook);
                } else if (domain.equals("null") && !path.equals("null")) {
                    map.put(name + path, cook);
                } else if (!domain.equals("null") && path.equals("null")) {
                    map.put(name + domain, cook);
                } else {
                    map.put(name, cook);
                }

                // 2 cookie失效，remove
                // 未失效 两个map中的cookie值合并
                Set<String> keys = map.keySet();
                for (String str : keys) {
                    if (!map.get(str).hasExpired()) {
                        if (c.containsKey(str)) {

                        } else {
                            c.put(str, map.get(str));
                        }
                    } else {
                        c.remove(str);
                    }

                }
            }
        }
        for (String string : c.keySet()) {
            // if (EApplication.cookies.containsKey(string)) {
            // EApplication.cookies.remove(string);
            // }
            // EApplication.cookies.put(string, c.get(string));
            String name = c.get(string).getName();
            String value = c.get(string).getValue();
            String domain = c.get(string).getDomain();
            String path = c.get(string).getPath();
            long maxAge = c.get(string).getMaxAge();
            int version = c.get(string).getVersion();
            String comment = c.get(string).getComment();
            String commentURL = c.get(string).getCommentURL();
            String portlist = c.get(string).getPortlist();
            boolean secure = c.get(string).getSecure();
            boolean discard = c.get(string).getDiscard();
            sb.append("name=" + name + ";" + "value=" + value + ";" + "domain="
                    + domain + ";" + "path=" + path + ";" + "maxAge=" + maxAge
                    + ";" + "version=" + version + ";" + "comment=" + comment
                    + ";" + "commentURL=" + commentURL + ";" + "portlist="
                    + portlist + ";" + "secure=" + secure + ";" + "discard="
                    + discard + ";" + ",");
        }
        if (sb.length() > 1) {
            try {
                // 以追加的方式打开文件输出流
                FileOutputStream fileOut = mContext.openFileOutput(cookieFileName, mContext.MODE_PRIVATE);
                // 写入数据
                fileOut.write(sb.toString().getBytes());
                // 关闭文件输出流
                fileOut.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        } else {

        }

    }

    public static boolean clearCookies(Context mContext) {
        if (mContext.deleteFile(cookieFileName)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getReqCookies(Context mContext) {
        StringBuilder sb = new StringBuilder();
        // 1 读取本地存的cookie
        String getCookie = getCookies(mContext);
        HashMap<String, HttpCookie> map = new HashMap<String, HttpCookie>();
        if (getCookie != null) {
            String cookie[] = getCookie.split(",");
            for (int i = 0; i < cookie.length; i++) {
                String key[] = cookie[i].split(";");
                String name = key[0].split("=")[1];
                String value = key[1].split("=")[1];
                HttpCookie cook = new HttpCookie(name, "");
                cook.setValue(value);
                map.put(name, cook);
            }
        }
        for (String string : map.keySet()) {
            sb.append(map.get(string).getName() + "="
                    + map.get(string).getValue() + ";");
        }
        if (sb.length() > 1) {
            return sb.substring(0, sb.toString().length() - 1);
        } else {
            return null;
        }
    }
}
