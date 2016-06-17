package geyunpei.model;

import geyunpei.model.base.BaseUser;

public class User extends BaseUser<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9040956279710821717L;
	
	public static User dao = new User();
	
	public User getUserByAccount(String account) {
		StringBuffer sql = new StringBuffer("select * from user where ");
		sql.append("account = ? ");
		return findFirst(sql.toString(), account);
	}
	
	public Boolean isExsit(String account) {
		StringBuffer sql = new StringBuffer("select account from user where ");
		sql.append("account = ? ");
		
		return findFirst(sql.toString(), account) != null;
	}

}
