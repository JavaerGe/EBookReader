package geyunpei.action.base;

import geyunpei.service.base.BaseService;

import java.lang.reflect.Field;

import com.jfinal.core.Controller;

public class BaseController<T> extends Controller {

	   public BaseController(){
	       Field[] fields = this.getClass().getDeclaredFields();
	        for (int i=0;i < fields.length; i++){
	            Field field = fields[i];
	            Class clazz = field.getType();
	            if(BaseService.class.isAssignableFrom(clazz) && clazz != BaseService.class){
	                try {
	                    field.set(this, BaseService.getInstance(clazz, this));
	                } catch (IllegalAccessException e) {
	                	e.printStackTrace();
	                }
	            }
	        }
	    }
}
