package ai.eve.html.webutil.model;

import java.util.ArrayList;
/**
 * 
	{'title':'请选择',
	'options':[
		{'text':'aaa','value':'1'},
		{'text':'bbb','value':'2','selected':true},
		{'text':'ccc','value':'3'}
	]
	'callback':'success'
	};
 * @Description: TODO
 * @author WYONG  
 * @date 2014-3-21 下午4:20:20 
 * @version V1.0
 *
 */
public class SingleChoiceDialogParam {
	
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 带tip选项集合
	 */
	public ArrayList<Option> options;
	/**
	 * JS回调
	 */
	public OK ok;

    public Cancel cancel;
    
    
}
