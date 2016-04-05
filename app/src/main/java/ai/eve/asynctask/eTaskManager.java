package ai.eve.asynctask;

import java.util.Observable;
import java.util.Observer;

import ai.eve.util.ELog;


/**
*
* @ClassName  com.ailk.mobile.eve.task.TaskManager
* @Description 多异步任务管理类
* @author wy
* @date 2012-10-12
*
*/
public class eTaskManager extends Observable {
   public static final Integer CANCEL_ALL = 1;

   public void cancelAll() {
       ELog.D("All task ready to Cancel.");
       setChanged();
       notifyObservers(CANCEL_ALL);
   }

   public  void deleteTask(Observer task) {
       super.deleteObserver(task);
   }

   public void addTask(Observer task) {
       super.addObserver(task);
   }
}