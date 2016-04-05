package ai.eve.module;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 组件工厂
 * <p>通过反射得到组件代理对象</p>
 *
 * @ClassName com.ailk.mobile.eve.comp.CompFactory 
 * @Description  组件工厂
 * @author wyong
 * @date 2012-10-12
 *
 */
public class ModuleFactory {
	private static HashMap<String, Module> classMap = new HashMap<String, Module>();

	/**
	 * 通过此方法得到组件的实例
	 * 其中参数className由新工程的常量类提供即可
	 * @param className
	 * @param data
	 * @return
	 */
	public static Module getComp(String className) {
		Module localComp = null;
		if (className == null || className.length() == 0)
			return null;
		localComp = getCompByClassName(className);
		return localComp;
	}

	private static Module getCompByClassName(String className) {
		if (classMap.get(className) != null) {
			return classMap.get(className);
		}
		Module localComp = getCompByReflection(className);
		return localComp;

	}

	private static Module getCompByReflection(String className) {
		Module localComp = null;

		Class localClass;
		try {
			localClass = Class.forName(className);
			Method localMethod = localClass.getDeclaredMethod("getInstance");
			Object localObject = localMethod.invoke(localClass);
			if (localObject instanceof Module) {
				localComp = (Module) localObject;
			}
			classMap.put(className, localComp);
			return localComp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	

}
