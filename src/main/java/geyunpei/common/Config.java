package geyunpei.common;

import geyunpei.action.Action;
import geyunpei.action.BookAction;
import geyunpei.action.BookmarkAction;
import geyunpei.action.UserAction;
import geyunpei.model.Book;
import geyunpei.model.Bookmark;
import geyunpei.model.Collect;
import geyunpei.model.ExpLog;
import geyunpei.model.Level;
import geyunpei.model.User;

import com.alibaba.druid.filter.stat.MergeStatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;

public class Config extends JFinalConfig {

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		loadPropertyFile("sysParameter.properties");
		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setEncoding("utf-8");
		me.setError404View("/error/404.html");
		me.setError500View("/error/500.html");
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", Action.class);
		me.add("/user", UserAction.class);
		me.add("/book", BookAction.class);
		me.add("/bookmark", BookmarkAction.class);

	}

	/**
	 * 配置数据库插件 最大连接数默认 100 最小连接数 默认 10
	 */
	public void configPlugin(Plugins me) {

		// 增加Druid连接池插件配置
		DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcUrl"),
				getProperty("user"), getProperty("password").trim());
		druidPlugin.addFilter(new MergeStatFilter());
		druidPlugin.setMaxActive(50);
		WallFilter wall = new WallFilter();
		wall.setDbType(getProperty("dbType"));
		druidPlugin.addFilter(wall);
		me.add(druidPlugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		me.add(arp);
		
		// 添加映射关系
		arp.addMapping("user", User.class);
		arp.addMapping("ebook", Book.class);
		arp.addMapping("bookmark", Bookmark.class);
		arp.addMapping("level", "level", Level.class);
		arp.addMapping("collect", Collect.class);
		arp.addMapping("exp_log", ExpLog.class);
	}

	public void configInterceptor(Interceptors me) {
		me.add(new LoginInterceptor());
		me.add(new UserInterceptor());
	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("path"));
	}

}
