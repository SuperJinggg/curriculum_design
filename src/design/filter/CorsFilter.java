package design.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns="/*")
public class CorsFilter implements Filter
{
	@Override
	public void destroy()
	{
		
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
	    HttpServletResponse httpResponse = (HttpServletResponse) response;   
	    HttpServletRequest httpRequest = (HttpServletRequest) request; 
	    
        // 不使用*，自动适配跨域域名，避免携带Cookie时失效
        String origin = httpRequest.getHeader("Origin");
        if(!"".equals(origin)) 
        {
        	httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        }
        // 允许跨域请求包含某请求头
        String headers = httpRequest.getHeader("Access-Control-Request-Headers");
        if(!"".equals(headers)) 
        {
        	httpResponse.setHeader("Access-Control-Allow-Headers", headers);
        	httpResponse.setHeader("Access-Control-Expose-Headers", headers);
        }
		// *允许任何域 // 允许的外域请求方式
		httpResponse.setHeader("Access-Control-Allow-Methods", "*");
		// 在999999秒内，不需要再发送预检验请求，可以缓存该结果
		httpResponse.setHeader("Access-Control-Max-Age", "999999");
        // 明确许可客户端发送Cookie，不允许删除字段即可
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

     
	     

	        // 不使用*，自动适配跨域域名，避免携带Cookie时失效
//	        String origin = httpRequest.getHeader("Origin");
//	        if(!CommonUtil.isNullOrEmpty(origin)) 
//	        {
//	        	httpResponse.setHeader("Access-Control-Allow-Origin", origin);
//	        }
//
//	        // 自适应所有自定义头
//	        String headers = httpRequest.getHeader("Access-Control-Request-Headers");
//	        if(!CommonUtil.isNullOrEmpty(headers)) 
//	        {
//	        	httpResponse.setHeader("Access-Control-Allow-Headers", headers);
//	        	httpResponse.setHeader("Access-Control-Expose-Headers", headers);
//	        }
//
//	        // 允许跨域的请求方法类型
//	        httpResponse.setHeader("Access-Control-Allow-Methods", "*");
//	        // 预检命令（OPTIONS）缓存时间，单位：秒
//	        httpResponse.setHeader("Access-Control-Max-Age", "3600");
//	        // 明确许可客户端发送Cookie，不允许删除字段即可
//	        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
	        
	        chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		System.out.println("允许跨域");
	}


	
}
