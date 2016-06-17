package geyunpei.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;

import geyunpei.model.Book;
import geyunpei.model.User;
import geyunpei.service.base.BaseService;
import geyunpei.utils.Const;
import geyunpei.utils.DateUtils;
import geyunpei.utils.StringUtil;

public class FileService extends BaseService{
	
	public static Prop prop = PropKit.use("sysParameter.properties");
	
	public void createDoc(User user) {
		String doc = prop.getProperties().getProperty("filePath") + user.getAccount();
		File file = new File(doc);
		if(!file.exists()){
			file.mkdir();
		}
	}
	
	public String uploadLogo(User user, UploadFile logo) {
		String type = "";
		
		List<String> typeList = Const.getLogoType();
		
		try {
			type = logo.getFileName().substring(logo.getFileName().lastIndexOf(".")).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(!typeList.contains(type)){
			return null;
		}
		String newFileName = prop.getProperties().getProperty("filePath") + user.getAccount() + "/" + DateUtils.getStrDate() + StringUtil.random(6) + type;
		logo.getFile().renameTo(new File(newFileName));
		
		String path = newFileName.replace(prop.getProperties().getProperty("filePath"), prop.getProperties().getProperty("url"));
		
		return path;
	}
	
	public String uploadBook(User user, UploadFile book) {
		String type = "";
		
		List<String> typeList = Const.getBookType();
		try {
			type = book.getFileName().substring(book.getFileName().lastIndexOf(".")).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!typeList.contains(type)){
			return null;
		}
		String newFileName = prop.getProperties().getProperty("filePath") + user.getAccount() + "/" + DateUtils.getStrDate() + StringUtil.random(6) + type;
		book.getFile().renameTo(new File(newFileName));
		
		String path = newFileName.replace(prop.getProperties().getProperty("filePath"), prop.getProperties().getProperty("url"));
		
		return path;
	}
	
	public Map<String, Object> getBookText(Integer start, Book book, Integer oldTextNumber, Integer newTextNumber) {
		
		Map<String, Object> map = new HashMap<>();
		String path = book.getPath().replace(prop.getProperties().getProperty("url"), prop.getProperties().getProperty("filePath"));

		File ebook = new File(path);
		if(!ebook.exists()){
			return null;
		}
		String text = null;
		try {
			text = FileUtils.readFileToString(ebook, "GB2312").replace("\n", "<br>");
			text = text.substring(oldTextNumber*(start-1), oldTextNumber*(start-1)+newTextNumber);
			map.put("isEnd", false);
		}catch(Exception e){
			text =  text.substring(oldTextNumber*(start-1));
			map.put("isEnd", true);
		}
		if(start.equals(1)){
			map.put("isStart", true);
		}else{
			map.put("isStart", false);
		}
		map.put("start", start);
		map.put("content", text);
		return map;
	}
	
}
