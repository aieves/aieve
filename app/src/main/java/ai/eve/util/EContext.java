package ai.eve.util;

import java.lang.reflect.Field;

/**
 * 反射工具类
 * 
 * @Description: 反射工具类
 * @author xuhj
 * @date 2015年1月20日 下午4:04:06
 * @version V1.0
 */
public class EContext {
	/**
	 * @Title: getResourceId
	 * @Description: 根据field名称找到对应的ResourceId
	 * @param @param code 资源名称
	 * @param @param c 为R资源类型 如 R.string.class
	 * @param @return
	 * @return int 返回-1则表示找不到对应的组件
	 * @throws
	 */
	public static int getResourceId(String code, Class c) {
		Field field;
		int value = -1;
		try {
			field = c.getDeclaredField(code);
			value = field.getInt(null);
		} catch (Exception e) {
			ELog.E(e.getMessage());
		}
		return value;
	}
}
