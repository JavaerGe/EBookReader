package geyunpei.model.base;


import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

public abstract class BaseLevel<M extends BaseLevel<M>> extends Model<M> implements IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7353067552918727329L;
	
	public void setLevel(Integer level) {
		set("level", level);
	}
	
	public Integer getLevel() {
		return get("level");
	}
	
	public void setExperience(Integer experience) {
		set("experience", experience);
	}
	
	public Integer getExperience() {
		return get("experience");
	}
	
}
