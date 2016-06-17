package geyunpei.common;

import geyunpei.model.User;
import geyunpei.utils.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;


public class LoginInterceptor implements Interceptor {
	
	public void intercept(Invocation ai) {
		User user = (User) ai.getController().getSession().getAttribute("user");

		if(null==user||StringUtil.isEmpty(user.getId())){
			ai.getController().forwardAction("/toLogin/3");
		}else{ 
			ai.invoke(); 
		}
		
	}

}
