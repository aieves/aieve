package ai.eve.module;

/**
 * <p>组件抽象</p> 
 * @ClassName: com.ailk.mobile.eve.comp.Comp 
 * @Description: 组件抽象
 * @author wyong
 * @date 2012-10-12
 *
 */
public abstract class Module implements Runnable {
	/**
	 * 线程，子类需在execute方法中创建对象并且运行此线程，从而执行子类中重写的run方法。
	 * 子类在run方法中根据参数进行跳转
	 */
	public  Thread compThread = null;
	
	/**
	 * 跳转到组件的某一默认的界面
	 */
	public abstract void run();
	
	/**
	 * 子类需重写此方法用于组建工厂创建对象
	 * @return
	 */
	public abstract Module Load();
	/**
	 * 子类需重写此方法用于组建工厂创建对象
	 * @return
	 */
	public abstract Module Load(String bundleKey);
}