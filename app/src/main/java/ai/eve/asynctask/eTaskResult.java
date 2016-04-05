package ai.eve.asynctask;

/**
 * 
 * @ClassName com.ailk.mobile.eve.task.TaskResult
 * @Description 异步任务执行结果
 * @author wy
 * @date 2012-10-12
 * 
 */
public class eTaskResult {
	public Object obj;
	public int code;
	private Exception exception;

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}