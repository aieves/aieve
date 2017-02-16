package ai.eve.html.webutil.model;

import com.alibaba.fastjson.JSON;
/**
 * 选择项
 *
 * @Description: TODO
 * @author WYONG  
 * @date 2014-3-21 下午4:20:59 
 * @version V1.0
 *
 */
public class Option{
	/**
	 * 选择项文字
	 */
	public String text;

	/**
	 * 选择项值
	 */
	public String value;
	/**
	 * 是否选中
	 */
	public boolean selected;
	/**
	 * JS回调参数，可修改
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
