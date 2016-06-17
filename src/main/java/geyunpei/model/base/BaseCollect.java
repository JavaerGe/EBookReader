package geyunpei.model.base;

import java.util.Date;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

public abstract class BaseCollect<M extends BaseCollect<M>> extends Model<M> implements IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6414251132620493074L;

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId(){
		return get("id");
	}
	
	public void setUserId(Integer userId) {
		set("userId", userId);
	}
	
	public Integer getUserId(){
		return get("userId");
	}
	
	public void setBookId(Integer bookId) {
		set("bookId", bookId);
	}
	
	public Integer getBookId(){
		return get("bookId");
	}
	
	public void setValid(Integer valid) {
		set("valid", valid);
	}
	
	public Integer getValid(){
		return get("valid");
	}
	
	public Date getCreateTime() {
		return get("createTime");
	}
	
	public void setCreateTime(Date createTime) {
		set("createTime", createTime);
	}
}
