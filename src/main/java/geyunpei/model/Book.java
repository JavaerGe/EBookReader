package geyunpei.model;

import java.util.ArrayList;
import java.util.List;

import geyunpei.model.base.BaseBook;

public class Book extends BaseBook<Book> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -637285259248118504L;
	
	public static Book dao = new Book();
	
	public List<Book> getMyBook(User user) {
		StringBuffer sql = new StringBuffer("select * from ebook where ");
		sql.append("userId = ? ");
		return find(sql.toString(), user.getId());
	}
	
	public Book getBookDetail(Integer bookId) {
		StringBuffer sql = new StringBuffer("select e.*, u.userName, u.account from ebook e ");
		sql.append("left join user u on e.userId = u.id ");
		sql.append("where e.id = ? ");
		return findFirst(sql.toString(), bookId);
	}
	
	public List<Book> getHotBook(User user) {
		StringBuffer sql = new StringBuffer("select e.*, u.userName, u.account from ebook e ");
		sql.append("left join user u on e.userId = u.id ");
		sql.append("where e.isShare = 1 or e.userId = ? ");
		sql.append("order by e.hot desc LIMIT 5 ");
		
		return find(sql.toString(), user.getId());
	}
	
	public List<Book> getNewBook(User user) {
		StringBuffer sql = new StringBuffer("select e.*, u.userName, u.account from ebook e ");
		sql.append("left join user u on e.userId = u.id ");
		sql.append("where e.isShare = 1 or e.userId = ? ");
		sql.append("order by e.createTime desc LIMIT 5 ");
		
		return find(sql.toString(), user.getId());
	}
	
	public List<Book> getBookByKey(String key, User user) {
		List<Object> para = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select e.*, u.userName, u.account from ebook e ");
		sql.append("left join user u on e.userId = u.id ");
		sql.append("where (e.isShare = 1 or e.userId = ?) ");
		para.add("%" + user.getId() + "%");
		sql.append("and (e.bookName like ? ");
		para.add("%" + key + "%");
		sql.append("or e.tag like ? ");
		para.add("%" + key + "%");
		sql.append("or u.userName like ?) ");
		para.add("%" + key + "%");
		
		return find(sql.toString(), para.toArray());
	}

}
