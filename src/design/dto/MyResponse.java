package design.dto;

/**
 * 
 * 前端ajax回应
 * @author xuhui
 *
 */
public class MyResponse {
	/**
	 * 状态码，默认20000表示OK
	 */
	public int code = ResponseCode.OK;
	public String error;
	public Object data = new Object();
}
