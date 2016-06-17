package geyunpei.action;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.jfinal.aop.Clear;
import geyunpei.action.base.BaseController;
import geyunpei.model.Book;
import geyunpei.model.User;
import geyunpei.service.FileService;
import geyunpei.service.LevelService;
import geyunpei.service.ReturnService;
import geyunpei.utils.Const;
import geyunpei.utils.MD5Util;
import geyunpei.utils.StringUtil;

public class Action extends BaseController<Action> {
	
	public ReturnService returnService;
	
	public FileService fileService;
	
	public LevelService levelService;
	
	@Clear
	public void index() {
		HttpSession session = getSession();
		User user = (User)session.getAttribute("user");
		if(user==null){
			forwardAction("/toLogin");
		}else{
			List<Book> hotBookList = Book.dao.getHotBook(user);
			List<Book> newBookList = Book.dao.getNewBook(user);
			setAttr("hotBookList", hotBookList);
			setAttr("newBookList", newBookList);
			setAttr("user", user);
			render("index.html");
		}
	}
	@Clear
	public void toLogin() {
		Integer flag = getParaToInt(0);
		if(flag!=null && StringUtil.isNotEmpty(flag)){
			if(flag.equals(2)){
				setAttr("flag", flag);
				setAttr("message", "帐号或密码错误");
			}
			if(flag.equals(3)){
				setAttr("flag", flag);
				setAttr("message", "登录过期，请重新登录");
			}
		}
		render("login.html");
	}
	@Clear
	public void login() {
		String account = getPara("account");
		String password = getPara("password");
		if(isParaBlank("account") || isParaBlank("password")){
			forwardAction("/toLogin/2");
			return;
		}
		Cookie cookie = new Cookie("account", account);
		cookie.setMaxAge(5*365*24*60*60);
		setCookie(cookie);
		
		password = MD5Util.getMD5Str(MD5Util.getMD5Str(password)).substring(4, 20);
		
		User user = User.dao.getUserByAccount(account);
		
		if(user == null){
			forwardAction("/toLogin/2");
			return;
		}
		
		if(user.getPassword().equals(password)){
			HttpSession session = getSession();
			session.setAttribute("user",user);
			session.setMaxInactiveInterval(-1);
			levelService.addExperience(user, "登录", "login", 100);
			forwardAction("/");
			
		}else{
			forwardAction("/toLogin/2");
		}
	}
	@Clear
	public void logout() {
		removeSessionAttr("user");
		forwardAction("/toLogin");
	}
	@Clear
	public void toRegister() {
		render("register.html");
	}
	@Clear
	public void register() {
		String account = getPara("account");
		String password = getPara("password");
		
		if(isParaBlank("account") || isParaBlank("password"))
			renderJavascript("alert('注册失败');");
		
		password = MD5Util.getMD5Str(MD5Util.getMD5Str(password)).substring(4, 20);
		
		User user = new User();
		
		user.setAccount(account);
		user.setPassword(password);
		user.setLevel(1);
		user.setExperience(0);
		user.setVIP(0);
		user.setTextNumber(2000);
		
		if(user.save()){
			getSession().setAttribute("user", user);
			getSession().setMaxInactiveInterval(-1);
			fileService.createDoc(user);
			levelService.addExperience(user, "注册", "register", 200);
			levelService.addExperience(user, "登录", "login", 100);
			forwardAction("/");
		}else{
			renderJavascript("alert('注册失败');");
		}
	}
	@Clear
	public void isExsitAccount() {
		String account = getPara("account");
		
		if(User.dao.isExsit(account)){
			renderJson(returnService.result(Const.FAIL, "用户名已存在!"));
		}else{
			renderJson(returnService.result(Const.SUCCESS, "成功"));
		}
		
	}
}
