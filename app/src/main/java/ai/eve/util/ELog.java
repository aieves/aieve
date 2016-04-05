package ai.eve.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ai.eve.EApplication;

/**
 * 日志处理工具类
 * Created by Administrator on 2015/9/16.
 */
public class ELog {
    private static boolean IS_SHOW_LOG = EApplication.OPEN_LOG;

    private static final String DEFAULT_MESSAGE = "execute";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int JSON_INDENT = 4;

    private static final int V = 0x1;
    private static final int D = 0x2;
    private static final int I = 0x3;
    private static final int W = 0x4;
    private static final int E = 0x5;
    private static final int A = 0x6;
    private static final int JSON = 0x7;

    public static void Init(boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
    }

    public static void V() {
        printLog(V, null, DEFAULT_MESSAGE);
    }

    public static void V(Object msg) {
        printLog(V, null, msg);
    }

    public static void V(String tag, String msg) {
        printLog(V, tag, msg);
    }

    public static void D() {
        printLog(D, null, DEFAULT_MESSAGE);
    }

    public static void D(Object msg) {
        printLog(D, null, msg);
    }

    public static void D(String tag, Object msg) {
        printLog(D, tag, msg);
    }

    public static void I() {
        printLog(I, null, DEFAULT_MESSAGE);
    }

    public static void I(Object msg) {
        printLog(I, null, msg);
    }

    public static void I(String tag, Object msg) {
        printLog(I, tag, msg);
    }

    public static void W() {
        printLog(W, null, DEFAULT_MESSAGE);
    }

    public static void W(Object msg) {
        printLog(W, null, msg);
    }

    public static void W(String tag, Object msg) {
        printLog(W, tag, msg);
    }

    public static void E() {
        printLog(E, null, DEFAULT_MESSAGE);
    }

    public static void E(Object msg) {
        printLog(E, null, msg);
    }

    public static void E(String tag, Object msg) {
        printLog(E, tag, msg);
    }

    public static void A() {
        printLog(A, null, DEFAULT_MESSAGE);
    }

    public static void A(Object msg) {
        printLog(A, null, msg);
    }

    public static void A(String tag, Object msg) {
        printLog(A, tag, msg);
    }


    public static void Json(String jsonFormat) {
        printLog(JSON, null, jsonFormat);
    }

    public static void Json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }


    private static void printLog(int type, String tagStr, Object objectMsg) {
        String msg;
        if (!IS_SHOW_LOG) {
            return;
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = 4;
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();

        String tag = (tagStr == null ? className : tagStr);
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");

        if (objectMsg == null) {
            msg = "Log with null Object";
        } else {
            msg = objectMsg.toString();
        }
        if (msg != null && type != JSON) {
            stringBuilder.append(msg);
        }

        String logStr = stringBuilder.toString();

        switch (type) {
            case V:
                Log.v(tag, logStr);
                break;
            case D:
                Log.d(tag, logStr);
                break;
            case I:
                Log.i(tag, logStr);
                break;
            case W:
                Log.w(tag, logStr);
                break;
            case E:
                Log.e(tag, logStr);
                break;
            case A:
                Log.wtf(tag, logStr);
                break;
            case JSON: {

                if (TextUtils.isEmpty(msg)) {
                    Log.d(tag, "Empty or Null json content");
                    return;
                }

                String message = null;

                try {
                    if (msg.startsWith("{")) {
                        JSONObject jsonObject = new JSONObject(msg);
                        message = jsonObject.toString(JSON_INDENT);
                    } else if (msg.startsWith("[")) {
                        JSONArray jsonArray = new JSONArray(msg);
                        message = jsonArray.toString(JSON_INDENT);
                    }
                } catch (JSONException e) {
                    E(tag, e.getCause().getMessage() + "\n" + msg);
                    return;
                }

                printLine(tag, true);
                message = logStr + LINE_SEPARATOR + message;
                String[] lines = message.split(LINE_SEPARATOR);
                StringBuilder jsonContent = new StringBuilder();
                for (String line : lines) {
                    jsonContent.append("| ").append(line).append(LINE_SEPARATOR);
                }
                Log.d(tag, jsonContent.toString());
                printLine(tag, false);
            }
            break;
        }

    }

    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

}
