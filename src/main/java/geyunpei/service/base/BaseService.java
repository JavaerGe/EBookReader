package geyunpei.service.base;

import geyunpei.action.base.BaseController;
import geyunpei.common.TxProxy;

import java.util.HashMap;
import java.util.Map;



public abstract class BaseService {
	
	    protected BaseController controller;
	    private static Map<Class<? extends BaseService>, BaseService> INSTANCE_MAP = new HashMap<Class<? extends BaseService>, BaseService>();
	 
	    public static <Ser extends BaseService> Ser getInstance(Class<Ser> clazz, BaseController controller){
	        Ser service = (Ser) INSTANCE_MAP.get(clazz);
	        if (service == null){
	            try {
	                service = TxProxy.newProxy(clazz);
	                INSTANCE_MAP.put(clazz, service);
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	        }
	        service.controller = controller;
	        return service;
	    }
}
