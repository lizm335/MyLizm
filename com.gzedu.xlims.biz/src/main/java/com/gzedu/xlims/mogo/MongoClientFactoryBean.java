package com.gzedu.xlims.mogo;

import com.cybermkd.mongo.kit.MongoKit;
import com.cybermkd.mongo.plugin.MongoPlugin;
import com.gzedu.xlims.common.DateUtils;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by llx on 4/13/2017.
 * 更多使用方法请看文档https://t-baby.gitbooks.io/mongodb-plugin/content/
 */
public class MongoClientFactoryBean {

	private  static  Logger logger = LoggerFactory.getLogger(MongoClientFactoryBean.class);

	public MongoClientFactoryBean(){
		logger.debug("["+MongoClientFactoryBean.class.getSimpleName()+"]:"+"init MongoClientFactoryBean "+DateUtils.getNowTime());
	}
	/**
	 * mongodb ip
	 */
	private String mongoHost;

	/**
	 * mongodb 端口
	 */
	private int mongoPort;

	/**
	 * 数据库名称
	 */
	private String database;

	/***/
	private String username;

	/***/
	private String password;


	/**
	 * 初始化MongodClient
	 * @return
	 */
	public MongoClient initMongoClient(){
		MongoClient client = null;
		try {
			MongoPlugin mongoPlugin = new MongoPlugin();
			mongoPlugin.add(mongoHost,mongoPort);
			mongoPlugin.setDatabase(database);
			mongoPlugin.readPreference();
		if(password!=null&&username!=null){
			//mongoPlugin.authCR(username,password);// mongodb v3.0以前用这个方法
			mongoPlugin.auth(username,password);// mongodb v3.0以后用这个方法
		}
		client = mongoPlugin.getMongoClient();
		MongoKit.INSTANCE.init(client, mongoPlugin.getDatabase());
	}catch (Exception e){
		e.printStackTrace();
		logger.error("["+this.getClass().getSimpleName()+"]"+" init MongoClient error... "+ DateUtils.getNowTime());
	}
		return client;
}

	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}

	public void setMongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
