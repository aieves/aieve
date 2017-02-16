package ai.eve.html.webutil.model;

import java.util.HashMap;
/**
 *
 * @Description: TODO
 * @author WYONG  
 * @date 2014-3-21 下午4:17:08 
 * @version V1.0
 *
 */
public class HttpParam {
	/**
	 * 请求相对地址
	 */
	public String url;
	/**
	 * 请求参数,
	 */
	public HashMap<String, String> data;
	/**
	 * 成功回调
	 */
	public String success;
	/**
	 * 失败回调
	 */
	public String fail;
}
