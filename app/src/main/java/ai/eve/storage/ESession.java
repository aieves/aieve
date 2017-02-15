package ai.eve.storage;

import java.util.HashMap;

/**
 * Created by wyong on 2016/1/14.
 */
public class ESession {


    private HashMap<String,Object> data;
    private static ESession session;

    private ESession(){
        data = new HashMap<>();
    }

    public static ESession load(){
        if(session==null){
            session = new ESession();
        }
        return session;
    }

    public void setObject(String key,Object object){
        data.put(key,object);
    }

    public Object getObject(String key){
        return data.get(key);
    }
}
