package geyunpei.common;

import geyunpei.model.User;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class UserInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		User user = (User) inv.getController().getSession().getAttribute("user");
		inv.getController().setAttr("user", user);
		inv.invoke();
	}

}
