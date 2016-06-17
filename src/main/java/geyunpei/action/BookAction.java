package geyunpei.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.upload.UploadFile;

import geyunpei.action.base.BaseController;
import geyunpei.model.Book;
import geyunpei.model.Bookmark;
import geyunpei.model.Collect;
import geyunpei.model.User;
import geyunpei.service.FileService;
import geyunpei.service.LevelService;
import geyunpei.service.ReturnService;
import geyunpei.utils.Const;
import geyunpei.utils.DateUtils;

public class BookAction extends BaseController<BookAction>{
	
	public ReturnService returnService;
	public FileService fileService;
	public LevelService levelService;
	
	public void myBook() {
		User user = (User) getSession().getAttribute("user");
		List<Book> myBookList = Book.dao.getMyBook(user);
		setAttr("bookList", myBookList);
		render("book.html");
	}
	
	public void toUpload() {
		render("upload.html");
	}
	
	public void addBook(){
		UploadFile book = getFile("book", "", 20971520);
		if(book == null){
			renderJson(returnService.result(Const.FAIL, "参数不完整！"));
			return;
		}
		User user = (User) getSession().getAttribute("user");
		String path = fileService.uploadBook(user, book);
		if(path == null){
			renderJson(returnService.result(Const.FAIL, "格式错误！"));
			return;
		}
		String bookName = getPara("bookName");
		String tag = getPara("tag");
		Integer isShare = getParaToInt("isShare");
		String summary = getPara("summary");
		
		if(isParaBlank("bookName")||isParaBlank("tag")){
			renderJson(returnService.result(Const.FAIL, "参数不完整！"));
			return;
		}
		Book ebook = new Book();
		ebook.setBookName(bookName);
		ebook.setUserId(user.getId());
		ebook.setTag(tag);
		ebook.setIsShare(isShare);
		ebook.setPath(path);
		ebook.setSummary(summary);
		ebook.setCreateTime(DateUtils.getNowDateShort());
		
		if(ebook.save()){
			levelService.addExperience(user, "上传", "addBook", 100);
			renderJson(returnService.result(Const.SUCCESS, "保存电子书成功！"));
			return;
		}else{
			renderJson(returnService.result(Const.FAIL, "保存电子书失败！"));
			return;
		}
		
	}
	
	public void myCollect() {
		User user = (User)getSession().getAttribute("user");
		List<Collect> bookList = Collect.dao.getMyCollect(user);
		setAttr("bookList", bookList);
		render("collect.html");
	}
	
	public void detail() {
		User user = (User)getSession().getAttribute("user");
		Integer bookId = getParaToInt("bookId");
		if(isParaBlank("bookId"))
			forwardAction("/");
		if(Book.dao.findById(bookId).getUserId().equals(user.getId())){
			setAttr("flag", 0);
		}else if(Collect.dao.getOneCollect(user, bookId) == null){
			setAttr("flag", 1);
		}else{
			setAttr("flag", 2);
		}
		Book book = Book.dao.getBookDetail(bookId);
		setAttr("book", book);
		render("detail.html");
	}
	
	public void addCollect(){
		Integer bookId = getParaToInt("bookId");
		if(isParaBlank("bookId")){
			renderJson(returnService.result(Const.FAIL, "收藏失败！"));
			return;
		}
		User user = (User)getSession().getAttribute("user");
		if(Collect.dao.getOneCollect(user, bookId) != null){
			renderJson(returnService.result(Const.FAIL, "请不要重复收藏！"));
		}
		Collect collect = new Collect();
		collect.setBookId(bookId);
		collect.setUserId(user.getId());
		collect.setValid(1);
		collect.setCreateTime(DateUtils.getNowDateShort());
		if(collect.save()){
			Book book = Book.dao.findById(bookId);
			book.setHot(book.getHot() + 1);
			book.update();
			levelService.addExperience(user, "收藏", "addCollect", 50);
			renderJson(returnService.result(Const.SUCCESS, "收藏成功！"));
		}else{
			renderJson(returnService.result(Const.FAIL, "收藏失败！"));
		}
	}
	
	public void searchBook() {
		User user = (User)getSession().getAttribute("user");
		String key = getPara("key");
		if(isParaBlank("key")){
			renderJson(returnService.result(Const.FAIL, "请输入关键字！"));
			return;
		}
		levelService.addExperience(user, "搜索", "searchBook", 50);
		List<Book> bookList = Book.dao.getBookByKey(key, user);
		
		setAttr("bookList", bookList);
		setAttr("key", key);
		
		render("list.html");
				
	}
	
	public void toModify() {
		Integer bookId = getParaToInt("bookId");
		Book book = Book.dao.findById(bookId);
		setAttr("book", book);
		render("modifyBook.html");
	}
	
	public void modify() {
		Integer bookId = getParaToInt("bookId");
		String bookName = getPara("bookName");
		String tag = getPara("tag");
		Integer isShare = getParaToInt("isShare");
		String summary = getPara("summary");
		
		Book book = Book.dao.findById(bookId);
		if(book==null){
			renderJson(returnService.result(Const.NO_DATA, "该电子书不存在！"));
			return;
		}
		book.setBookName(bookName);
		book.setTag(tag);
		book.setIsShare(isShare);
		book.setSummary(summary);
		
		if(book.update()){
			renderJson(returnService.result(Const.SUCCESS, "修改成功！"));
		}else{
			renderJson(returnService.result(Const.FAIL, "修改失败！"));
		}
		return;
	}
	
	public void read() {
		User user = (User)getSession().getAttribute("user");
		Integer bookId = getParaToInt("bookId");
		Integer bookmarkId = getParaToInt("bookmarkId");
		Integer start = getParaToInt("start", 1);
		Map<String, Object> map = new HashMap<>();
		if(!isParaBlank("bookId")){
			Book book = Book.dao.findById(bookId);
			if(book.getIsShare() == 0 && !book.getUserId().equals(user.getId())){
				setAttr("book", book);
				render("cannotRead.html");
				return;
			}
			map = fileService.getBookText(start, book, user.getTextNumber(), user.getTextNumber());
			Bookmark bookmark = Bookmark.dao.getBookmark(bookId, start);
			map.put("haveBookmark", bookmark!=null);
			map.put("bookId", book.getId());
			if(bookmark!=null)
				map.put("bookmarkId", bookmark.getId());
		}else if(!isParaBlank("bookmarkId")){
			Bookmark bookmark = Bookmark.dao.findById(bookmarkId);
			Book book = Book.dao.findById(bookmark.getBookId());
			if(book.getIsShare() == 0 && !book.getUserId().equals(user.getId())){
				setAttr("book", book);
				render("cannotRead.html");
				return;
			}
			map = fileService.getBookText(bookmark.getPosition(), book, bookmark.getTextNumber(), user.getTextNumber());
			map.put("bookId", book.getId());
			map.put("haveBookmark", true);
			map.put("bookmarkId", bookmark.getId());
		}
		levelService.addExperience(user, "阅读", "read", 100);
		setAttr("book", map);
		render("read.html");
	}
}
