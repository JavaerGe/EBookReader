package geyunpei.action;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.upload.UploadFile;

import geyunpei.action.base.BaseController;
import geyunpei.model.ExpLog;
import geyunpei.model.Level;
import geyunpei.model.User;
import geyunpei.service.FileService;
import geyunpei.service.LevelService;
import geyunpei.service.ReturnService;
import geyunpei.utils.Const;
import geyunpei.utils.JsonUtil;
import geyunpei.utils.MD5Util;
import geyunpei.utils.StringUtil;

public class UserAction extends BaseController<UserAction>{
	
	public FileService fileService;
	public ReturnService returnService;
	public LevelService levelService;
	
	public void myImf() {
		User user = (User) getSession().getAttribute("user");
		Level level = Level.dao.findById(user.getLevel());
		String expPercent = null;
		Integer restExp = null;
		if(level.getExperience().equals(0)){
			expPercent = "100%";
			restExp = 0;
		}
		expPercent = StringUtil.getPercent(new Double((double)user.getExperience()/(double)level.getExperience()), 2);
		String VIPPercent = StringUtil.getPercent(new Double((double)user.getVIP()/10D), 2);
		restExp = level.getExperience() - user.getExperience();
		setAttr("expPercent", expPercent);
		setAttr("restExp", restExp);
		setAttr("VIPPercent", VIPPercent);
		render("user.html");
	}
	
	public void toModifyImf() {
		Integer flag = getParaToInt(0);
		User user = (User) getSession().getAttribute("user");
		Level level = Level.dao.findById(user.getLevel());
		String expPercent = null;
		Integer restExp = null;
		if(level.getExperience().equals(0)){
			expPercent = "100%";
			restExp = 0;
		}
		expPercent = StringUtil.getPercent(new Double((double)user.getExperience()/(double)level.getExperience()), 2);
		String VIPPercent = StringUtil.getPercent(new Double((double)user.getVIP()/10D), 2);
		restExp = level.getExperience() - user.getExperience();
		setAttr("expPercent", expPercent);
		setAttr("restExp", restExp);
		setAttr("VIPPercent", VIPPercent);
		if(flag!=null && StringUtil.isNotEmpty(flag)){
			setAttr("flag", flag);
			setAttr("message", "修改失败！");
		}
		render("modifyUser.html");
	}
	
	public void logo() {
		
		User user = (User) getSession().getAttribute("user");
		UploadFile logo = null;
		try{
			logo = getFile("logoUpload", "", 1048576);
		}catch(Exception e){
			renderJson(returnService.result(Const.FAIL, "大小超过限制！"));
			return;
		}
		String path = fileService.uploadLogo(user, logo);
		
		if(path==null){
			renderJson(returnService.result(Const.FAIL, "头像格式错误"));
			return;
		}
		
		user.setlogo(path);
		if(user.update()){
			getSession().removeAttribute("user");
			getSession().setAttribute("user", user);
			getSession().setMaxInactiveInterval(-1);
			Map<String, Object> result = new HashMap<>();
			result.put("resultCode", Const.SUCCESS);
			result.put("message", "头像上传成功");
			result.put("path", path);
			renderJson(JsonUtil.ObjectToJsonString(result));
		}else{
			renderJson(returnService.result(Const.FAIL, "头像上传失败"));
		}
	}
	
	public void modifyImf() {
		String userName = getPara("userName");
		String email = getPara("email");
		String QQ = getPara("QQ");
		String phone = getPara("phone");
		Integer textNum = getParaToInt("textNumber");
		String summary = getPara("summary");
		
		User user = (User) getSession().getAttribute("user");
		if(!isParaBlank("userName"))
			user.setUserName(userName);
		if(!isParaBlank("email"))
			user.setEmail(email);
		if(!isParaBlank("QQ"))
			user.setQQ(QQ);
		if(!isParaBlank("phone"))
			user.setPhone(phone);
		if(!isParaBlank("textNumber"))
			user.setTextNumber(textNum);
		if(!isParaBlank("summary"))
			user.setSummary(summary);
		try{
			if(user.update()){
				getSession().removeAttribute("user");
				getSession().setAttribute("user", user);
				getSession().setMaxInactiveInterval(-1);
				
				if(ExpLog.dao.getLast("modifyImf", user.getId())==null){
					levelService.addExperience(user, "完善個人信息", "modifyImf", 100);
				}
				forwardAction("/user/myImf");
			}else{
				forwardAction("/user/toModifyImf/2");
			}
		}catch(Exception e){
			forwardAction("/user/toModifyImf/2");
		}
	}
	
	public void toModifyPwd() {
		render("modifyPwd.html");
	}
	
	public void modifyPwd(){
		String account = getPara("account");
		String oldPwd = getPara("oldPwd");
		String newPwd = getPara("newPwd");
		String confirmPwd = getPara("confirmPwd");
		
		User user = (User) getSession().getAttribute("user");
		if(!account.equals(user.getAccount())){
			renderJson(returnService.result(Const.FAIL, "你无法修改别人的密码！"));
			return;
		}
		
		if(!newPwd.equals(confirmPwd)){
			renderJson(returnService.result(Const.FAIL, "确认密码错误！"));
			return;
		}
		
		user = User.dao.getUserByAccount(account);
		oldPwd = MD5Util.getMD5Str(MD5Util.getMD5Str(oldPwd)).substring(4, 20);
		
		if(!oldPwd.equals(user.getPassword())){
			renderJson(returnService.result(Const.FAIL, "原密码错误！"));
			return;
		}
		
		newPwd = MD5Util.getMD5Str(MD5Util.getMD5Str(newPwd)).substring(4, 20);
		
		user.setPassword(newPwd);
		
		if(user.update()){
			getSession().removeAttribute("user");
			getSession().setAttribute("user", user);
			getSession().setMaxInactiveInterval(-1);
			renderJson(returnService.result(Const.SUCCESS, "修改成功！"));
		}else{
			renderJson(returnService.result(Const.FAIL, "修改失败！"));
		}
		
	}
}
