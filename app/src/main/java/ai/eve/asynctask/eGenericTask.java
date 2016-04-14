package ai.eve.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Observable;
import java.util.Observer;

import ai.eve.activity.EActivity;
import ai.eve.activity.EToolBarActivity;
import ai.eve.util.ELog;

/**
*异步任务封装，提供默认的UI界面处理。
*<p>默认无参构造不提供默认的UI界面处理，有参构造提供默认的UI界面处理</p>
* @ClassName  com.ailk.mobile.eve.task.GenericTask
* @Description 异步任务封装，提供默认的UI界面处理
* @author wy
* @date 2012-10-12
*
*/
public abstract class eGenericTask extends AsyncTask<eTaskParams, Object, eTaskResult> implements Observer{

   private boolean isCancelable = true;

   public String T_Name = "GenericTask";

   private Context context;

   public eGenericTask(Context context) {
       this.context = context;
   }

    /**
     * 异步任务执行体，需重写
    * @param params
    * @return
    */
   abstract protected eTaskResult _doInBackground(eTaskParams... params);

   public void doPublishProgress(Object... values) {
       super.publishProgress(values);
   }

   @Override
   protected void onCancelled() {
       super.onCancelled();
       ELog.D("异步任务："+T_Name+"取消");
       if(context==null)return;
       if(context instanceof  EActivity){
           if(((EActivity)context).getDialog()!=null){
               ((EActivity)context).getDialog().dismiss();
           }
       }else if(context instanceof EToolBarActivity){
           if(((EToolBarActivity)context).getDialog()!=null){
               ((EToolBarActivity)context).getDialog().dismiss();
           }
       }
   }

   @Override
   protected void onPreExecute() {
       super.onPreExecute();
       ELog.D(Thread.currentThread().getName()+"开启异步任务-----开始执行");
       if(context==null)return;
       if(context instanceof  EActivity){
           if(((EActivity)context).getDialog()!=null){
               ((EActivity)context).getDialog().show();
           }
       }else if(context instanceof EToolBarActivity){
           if(((EToolBarActivity)context).getDialog()!=null){
               ((EToolBarActivity)context).getDialog().show();
           }
       }
   }

   @Override
   protected void onPostExecute(eTaskResult result) {
       super.onPostExecute(result);
       ELog.D("异步任务：" + T_Name + "完成");
       if(context==null)return;
       if(context instanceof  EActivity){
           if(((EActivity)context).getDialog()!=null){
               ((EActivity)context).getDialog().dismiss();
           }
       }else if(context instanceof EToolBarActivity){
           if(((EToolBarActivity)context).getDialog()!=null){
               ((EToolBarActivity)context).getDialog().dismiss();
           }
       }
   }


   @Override
   protected void onProgressUpdate(Object... values) {
       super.onProgressUpdate(values);
   }

   @Override
   protected eTaskResult doInBackground(eTaskParams... params) {
       T_Name = Thread.currentThread().getName();
       eTaskResult result = _doInBackground(params);
       return result;
   }

   public void setCancelable(boolean flag) {
       isCancelable = flag;
   }

   @Override
   public void update(Observable o, Object arg) {
       if (eTaskManager.CANCEL_ALL == ((Integer) arg).intValue() && isCancelable) {
           if (getStatus() == Status.RUNNING) {
               cancel(true);
           }
       }
   }

}
