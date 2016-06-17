package geyunpei.model;

import java.util.List;

import geyunpei.model.base.BaseBookmark;

public class Bookmark extends BaseBookmark<Bookmark> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4151918679747127955L;
	
	public static Bookmark dao = new Bookmark();
	
	public List<Bookmark> getMyBookmark(User user) {
		StringBuffer sql = new StringBuffer("select b.*, e.bookName from bookmark b ");
		sql.append("left join ebook e on b.bookId = e.id ");
		sql.append("where b.userId = ? order by b.position desc ");
		
		return find(sql.toString(), user.getId());
	}
	
	public Bookmark getDetail(Integer bookmarkId) {
		StringBuffer sql = new StringBuffer("select b.*, e.path from bookmark b ");
		sql.append("left join ebook e on b.bookId = e.id ");
		sql.append("where b.id = ? ");
		return findFirst(sql.toString(), bookmarkId);
	}
	
	public Bookmark getBookmark(Integer bookId, Integer start) {
		StringBuffer sql = new StringBuffer("select * from bookmark ");
		sql.append("where bookId = ? and position = ? ");
		
		return findFirst(sql.toString(), bookId, start);
	}
}
