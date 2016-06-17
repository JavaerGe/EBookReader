package geyunpei.model;

import java.util.List;

import geyunpei.model.base.BaseCollect;

public class Collect extends BaseCollect<Collect>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4656208183741548268L;
	
	public static Collect dao = new Collect();
	
	public List<Collect> getMyCollect(User user) {
		StringBuffer sql = new StringBuffer("select * from collect c ");
		sql.append("left join ebook e on c.bookId = e.id ");
		sql.append("left join user u on e.userId = u.id ");
		sql.append("where c.userId = ? ");
		
		return find(sql.toString(), user.getId());
	}
	
	public Collect getOneCollect(User user, Integer bookId) {
		StringBuffer sql = new StringBuffer("select * from collect where ");
		sql.append("userId = ? ");
		sql.append("and bookId = ? ");
		
		return findFirst(sql.toString(), user.getId(), bookId);
	}
}
