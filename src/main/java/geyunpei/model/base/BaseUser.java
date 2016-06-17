package geyunpei.model.base;


import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3764684143617263081L;

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId() {
		return get("id");
	}
	
	public String getAccount() {
		return get("account");
	}
	
	public void setAccount(String account) {
		set("account", account);
	}
	
	public String getPassword() {
		return get("password");
	}
	
	public void setPassword(String password) {
		set("password", password);
	}
	
	public String getUserName() {
		return get("userName");
	}
	
	public void setUserName(String userName) {
		set("userName", userName);
	}
	
	public String getLogo() {
		return get("logo");
	}
	
	public void setlogo(String logo) {
		set("logo", logo);
	}
	
	public String getEmail() {
		return get("email");
	}
	
	public void setEmail(String email) {
		set("email", email);
	}
	
	public String getQQ() {
		return get("QQ");
	}
	
	public void setQQ(String QQ) {
		set("QQ", QQ);
	}
	
	public String getPhone() {
		return get("phone");
	}
	
	public void setPhone(String phone) {
		set("phone", phone);
	}
	
	public String getSummary() {
		return get("summary");
	}
	
	public void setSummary(String summary) {
		set("summary", summary);
	}
	
	public Integer getTextNumber() {
		return get("textNumber");
	}
	
	public void setTextNumber(Integer textNumber) {
		set("textNumber", textNumber);
	}
	
	public int getLevel() {
		return get("level");
	}
	
	public void setLevel(Integer level){
		set("level", level);
	}
	
	public int getExperience() {
		return get("experience");
	}
	
	public void setExperience(Integer experience){
		set("experience", experience);
	}
	
	public int getVIP() {
		return get("VIP");
	}
	
	public void setVIP(Integer VIP){
		set("VIP", VIP);
	}
}
