package ai.eve.http;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import ai.eve.EApplication;
import ai.eve.volley.Listener;
import ai.eve.volley.request.JsonObjectRequest;

/**
 * Created by wyong on 2016/1/21.
 */
public class EJSONObjectRequest extends JsonObjectRequest {

    public EJSONObjectRequest(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener) {
        super(method, url, jsonRequest, listener);
    }

    public EJSONObjectRequest(String url, JSONObject jsonRequest, Listener<JSONObject> listener) {
        super(url, jsonRequest, listener);
    }
    /**
     * 将当前请求添加到请求队列等待执行.
     */
    public void start(){
        this.setCacheExpireTime(TimeUnit.MINUTES, EApplication.FILE_CACHEEXPIRETIME);
        EApplication.mQueue.add(this);
    }
}
