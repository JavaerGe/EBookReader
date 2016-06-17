package geyunpei.model;

import geyunpei.model.base.BaseExpLog;

public class ExpLog extends BaseExpLog<ExpLog>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2423554406318621320L;
	
	public static ExpLog dao = new ExpLog();
	
	public ExpLog getLast(String method, Integer userId) {
		StringBuffer sql = new StringBuffer("select * from exp_log where ");
		sql.append("method = ? and userId = ? order by dateTime desc LIMIT 1 ");
		
		return findFirst(sql.toString(), method, userId);
	}

}
