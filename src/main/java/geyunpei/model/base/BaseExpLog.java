package geyunpei.model.base;

import java.util.Date;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

public abstract class BaseExpLog<M extends BaseExpLog<M>> extends Model<M> implements IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3172205434617337282L;

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId() {
		return get("id");
	}
	
	public Integer getUserId() {
		return get("userId");
	}
	
	public void setUserId(Integer userId) {
		set("userId", userId);
	}
	
	public String getAction() {
		return get("action");
	}
	
	public void setAction(String action) {
		set("action", action);
	}
	
	public String getMethod() {
		return get("method");
	}
	
	public void setMethod(String method) {
		set("method", method);
	}
	
	public int getExperience() {
		return get("experience");
	}
	
	public void setExperience(Integer experience){
		set("experience", experience);
	}
	
	public Date getDateTime() {
		return get("dateTime");
	}
	
	public void setDateTime(Date dateTime) {
		set("dateTime", dateTime);
	}
}
