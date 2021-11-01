package design.dto;

public class ResponseCode
{
	/**
	 * 业务成功 
	 */
	public static final int OK = 20000;
	/**
	 * 业务失败
	 */
	public static final int Fail = 30000;
	/**
	 * 非法Token
	 */
	public static final int IllegalToken = 50008;
	
	/**
	 * 其他客户端登陆
	 */
	public static final int OtherLogin = 50012;
	
	/**
	 * Token过期
	 */
	public static final int TokenExpired = 50014;
	
	
}
