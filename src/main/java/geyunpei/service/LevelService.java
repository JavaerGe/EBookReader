package geyunpei.service;

import java.util.Date;

import geyunpei.model.ExpLog;
import geyunpei.model.Level;
import geyunpei.model.User;
import geyunpei.service.base.BaseService;
import geyunpei.utils.DateUtils;

public class LevelService extends BaseService{
	public boolean addExperience(User user, String action, String method, Integer exp) {
		
		ExpLog last = ExpLog.dao.getLast(method,user.getId());
		Date now = DateUtils.getNowDate();
		
		if(last==null){
			
		}else if(DateUtils.isSameDate(now, last.getDateTime())){
			return false;
		}
		Integer userLevel = user.getLevel();
		Integer userExp = user.getExperience();
		
		Level level = Level.dao.findById(userLevel);
		if(level.getExperience().equals(0)){
			return true;
		}else{
			ExpLog expLog = new ExpLog();
			expLog.setAction(action);
			expLog.setMethod(method);
			expLog.setUserId(user.getId());
			expLog.setExperience(exp);
			expLog.setDateTime(now);
			
			expLog.save();
			
			userExp += exp;
			if(userExp > level.getExperience()){
				userLevel += 1;
				if(Level.dao.findById(userLevel).getExperience().equals(0)){
					userExp = 0;
				}else{
					userExp = userExp - level.getExperience();
				}
			}
			user.setLevel(userLevel);
			user.setExperience(userExp);
			return user.update();
		}
	}
	
}
