package geyunpei.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geyunpei.action.base.BaseController;
import geyunpei.model.Bookmark;
import geyunpei.model.User;
import geyunpei.service.LevelService;
import geyunpei.service.ReturnService;
import geyunpei.utils.Const;
import geyunpei.utils.DateUtils;
import geyunpei.utils.JsonUtil;

public class BookmarkAction extends BaseController<BookmarkAction>{
	
	public ReturnService returnService;
	
	public LevelService levelService;
	
	public void myBookmark() {
		User user = (User) getSession().getAttribute("user");
		List<Bookmark> bookmarkList = Bookmark.dao.getMyBookmark(user);
		setAttr("bookmarkList", bookmarkList);
		render("bookmark.html");
	}
	
	public void addBookmark() {
		Integer start = getParaToInt("start");
		Integer bookId = getParaToInt("bookId");
		Integer textNumber = getParaToInt("textNumber");
		
		if(isParaBlank("start")||isParaBlank("bookId")||isParaBlank("textNumber")){
			renderJson(returnService.result(Const.FAIL, "参数不完整！"));
			return;
		}
		User user = (User) getSession().getAttribute("user");
		Bookmark bookmark = new Bookmark();
		bookmark.setBookId(bookId);
		bookmark.setUserId(user.getId());
		bookmark.setPosition(start);
		bookmark.setTextNumber(textNumber);
		bookmark.setCreateTime(DateUtils.getNowDateShort());
		
		if(bookmark.save()){
			levelService.addExperience(user, "添加书签", "addBookmark", 50);
			Map<String, Object> map = new HashMap<>();
			map.put("resultCode", Const.SUCCESS);
			map.put("message", "添加书签成功");
			map.put("bookmarkId", bookmark.getId());
			renderJson(JsonUtil.ObjectToJsonString(map));
		}else{
			renderJson(returnService.result(Const.FAIL, "添加书签失败！"));
		}
	}
	
	public void delBookmark() {
		Integer bookmarkId = getParaToInt("bookmarkId");
		
		if(isParaBlank("bookmarkId")){
			renderJson(returnService.result(Const.FAIL, "参数不完整！"));
			return;
		}
		
		if(Bookmark.dao.deleteById(bookmarkId)){
			renderJson(returnService.result(Const.SUCCESS, "删除书签成功！"));
		}else{
			renderJson(returnService.result(Const.FAIL, "删除书签失败！"));
		}
	}
}
