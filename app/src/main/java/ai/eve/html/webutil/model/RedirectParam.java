package ai.eve.html.webutil.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @Description: TODO
 * @author WYONG  
 * @date 2014-3-21 下午4:18:53 
 * @version V1.0
 *
 */
public class RedirectParam {
	/**
	 * 跳转目的，需要告知前端人员
	 */
	public String action;
	/**
	 * 跳转标记，需要统一
	 */
	public int flag;
	/**
	 * 跳转携带参数
	 */
	public HashMap<String, Serializable> data;

}
