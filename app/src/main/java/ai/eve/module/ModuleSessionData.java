package ai.eve.module;

import android.content.Context;


/**
*
* @ClassName  com.ailk.mobile.eve.comp.CompSessionData
* @Description  组件之间的传递消息的封装
* @author wyong
* @date 2012-10-12
*
*/
public class ModuleSessionData<T> {
   public Context context;
   public T bundle;
   public ModuleSessionData(Context context, T bundle) {
       super();
       this.context = context;
       this.bundle = bundle;
   }
}