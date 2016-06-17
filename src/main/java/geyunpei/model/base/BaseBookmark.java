package geyunpei.model.base;


import java.util.Date;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

public abstract class BaseBookmark<M extends BaseBookmark<M>> extends Model<M> implements IBean {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3166394724229802451L;

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
	
	public Integer getBookId() {
		return get("bookId");
	}
	
	public void setBookId(Integer bookId) {
		set("bookId", bookId);
	}
	
	public Integer getTextNumber() {
		return get("textNumber");
	}
	
	public void setTextNumber(Integer textNumber) {
		set("textNumber", textNumber);
	}
	
	public Integer getPosition() {
		return get("position");
	}
	
	public void setPosition(Integer position) {
		set("position", position);
	}
	
	public Date getCreateTime() {
		return get("createTime");
	}
	
	public void setCreateTime(Date createTime) {
		set("createTime", createTime);
	}
}
