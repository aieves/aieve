package ai.eve.asynctask;

import android.content.Context;

import java.util.concurrent.Executor;

import ai.eve.util.ELog;

/**
 * 
 * 
 * @Description: 异步任务(包含异常处理)
 * @author WYONG
 * @date 2013-8-27 下午8:39:23
 * @version V1.0
 * 
 */
public abstract class ETask {

	abstract protected Object doInBackground() throws Exception;

	abstract protected void onPreExecute();

	abstract protected void onPostExecute(Object data);

	protected void onCancelled() {

	}

	private eGenericTask task;

	/**
	 * 参数为Context时表示使用默认progressDialog，如果为null则不使用默认progressDialog，需开发者自行定义。
	 * 
	 * @param context
	 */
	public ETask(Context context) {
		initTask(context);
	}

	public void execute(eTaskParams... params) {
		if (task != null) {
			task.execute(params);
		}
	}

	public void execute(Executor exc, eTaskParams... params) {
		if (task != null) {
			task.executeOnExecutor(exc, params);
		}
	}

	public boolean isActive() {
		if (task != null && task.getStatus() == eGenericTask.Status.RUNNING) {
			return true;
		}
		return false;
	}

	public void cancel() {
		if (task != null && task.getStatus() == eGenericTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	public boolean isCancelled() {
		if (task.isCancelled()) {
			return true;
		}
		return false;
	}

	long start;

	private void initTask(Context context) {

		task = new eGenericTask(context) {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				ETask.this.onPreExecute();
				start = System.currentTimeMillis();
			}

			@Override
			protected eTaskResult _doInBackground(eTaskParams... params) {
				eTaskResult result = new eTaskResult();
				try {
					Object data = ETask.this.doInBackground();
					result.setObj(data);
				} catch (Exception e) {
					result.setException(e);
				}
				return result;
			}

			@Override
			protected void onPostExecute(eTaskResult result) {
				super.onPostExecute(result);
				long end = System.currentTimeMillis();
				ELog.D(end - start + "ms");
				ETask.this.onPostExecute(result.obj);
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
				ETask.this.onCancelled();
			}
		};

	}

}
