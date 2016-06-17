package geyunpei.model.base;


import java.util.Date;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

public abstract class BaseBook<M extends BaseBook<M>> extends Model<M> implements IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4824939257087025441L;
	
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
	
	public String getBookName() {
		return get("bookName");
	}
	
	public void setBookName(String bookName) {
		set("bookName", bookName);
	}
	
	public String getSummary() {
		return get("summary");
	}
	
	public void setSummary(String summary) {
		set("summary", summary);
	}
	
	public Integer getIsShare() {
		return get("isShare");
	}
	
	public void setIsShare(Integer isShare) {
		set("isShare", isShare);
	}
	
	public String getPath() {
		return get("path");
	}
	
	public void setPath(String path) {
		set("path", path);
	}
	
	public String getTag() {
		return get("tag");
	}
	
	public void setTag(String tag) {
		set("tag", tag);
	}
	
	public Date getCreateTime() {
		return get("createTime");
	}
	
	public void setCreateTime(Date createTime) {
		set("createTime", createTime);
	}
	
	public Integer getHot() {
		return get("hot");
	}
	
	public void setHot(Integer hot) {
		set("hot", hot);
	}
}
