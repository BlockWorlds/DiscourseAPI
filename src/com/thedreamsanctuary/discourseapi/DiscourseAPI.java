package com.thedreamsanctuary.discourseapi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.wareninja.opensource.discourse.DiscourseApiClient;
import com.wareninja.opensource.discourse.utils.ResponseListener;
import com.wareninja.opensource.discourse.utils.ResponseMeta;
import com.wareninja.opensource.discourse.utils.ResponseModel;
public class DiscourseAPI extends JavaPlugin{
	public void onEnable(){
		
		try {
            final File[] libs = new File[] {
                    new File(getDataFolder(), "commons-codec-1.6.jar"),
                    new File(getDataFolder(), "commons-logging-1.1.3.jar"),
                    new File(getDataFolder(), "discourse-api-client-0.0.3.jar"),
                    new File(getDataFolder(), "fluent-hc-4.3.4.jar"),
                    new File(getDataFolder(), "gson-2.2.4.jar"),
                    new File(getDataFolder(), "httpclient-4.3.4.jar"),
                    new File(getDataFolder(), "httpclient-cache-4.3.4.jar"),
                    new File(getDataFolder(), "httpcore-4.3.2.jar"),
                    new File(getDataFolder(), "httpmime-4.3.4.jar"),
                    };
            for (final File lib : libs) {
                if (!lib.exists()) {
                    JarUtils.extractFromJar(lib.getName(),
                            lib.getAbsolutePath());
                }
            }
            for (final File lib : libs) {
                if (!lib.exists()) {
                    getLogger().warning(
                            "There was a critical error loading DiscourseAPI! Could not find lib: "
                                    + lib.getName());
                    Bukkit.getServer().getPluginManager().disablePlugin(this);
                    return;
                }
                addClassPath(JarUtils.getJarUrl(lib));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
		
	}
	private void addClassPath(final URL url) throws IOException {
        final URLClassLoader sysloader = (URLClassLoader) ClassLoader
                .getSystemClassLoader();
        final Class<URLClassLoader> sysclass = URLClassLoader.class;
        try {
            final Method method = sysclass.getDeclaredMethod("addURL",
                    new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { url });
        } catch (final Throwable t) {
            t.printStackTrace();
            throw new IOException("Error adding " + url
                    + " to system classloader");
        }
    }
	private final DiscourseApiClient apiclient;
	private final ResponseListener response;
	public DiscourseAPI(String api_url, String api_key, String api_username){
		apiclient = new DiscourseApiClient(api_url, api_key, api_username);
		response = new ResponseListener(){

			@Override
			public void onBegin(String info) {
				//System.out.println("info: "+info);
			}
			@Override
			public void onComplete_wModel(ResponseModel responseModel) {
				// successful result
				//System.out.println("SUCCESS! -> " + responseModel.toString());
			}

			@Override
			public void onError_wMeta(ResponseMeta responseMeta) {
				// error
				System.out.println("ERROR! -> " + responseMeta.toString());
			}
		};
	}
	/**
	 * Creates a new topic
	 * @param topic_title Title of the topic
	 * @param topic_content Body of the topic, as html
	 * @param topic_category Category of the topic, case-sensitive
	 */
	public void createTopic(String topic_title, String topic_content, String topic_category){
		Map<String,String> data = new HashMap<String, String>();
		data.put("title", topic_title);
		data.put("raw", topic_content);
		data.put("category", topic_category);
		apiclient.createTopic(data,response);
	}
	
	/**
	 * Creates a new user with the given parameters on the forum
	 * @param name
	 * @param email
	 * @param username
	 * @param password
	 */
	public void createUser(String name, String email, String username, String password){
		Map<String,String> data = new HashMap<String,String>();
		data.put("name", name);
		data.put("email", email);
		data.put("username", username);
		data.put("password", password);
		apiclient.createUser(data,response);
		
	}
	
	/**
	 * Approves the selected user
	 * @param userid
	 * @param username
	 */
	public void approveUser(String userid,String username){
		Map<String,String> data = new HashMap<String,String>();
		data.put("userid", userid);
		data.put("username", username);
		apiclient.approveUser(data,response);
	}
	
	/**
	 * Activates the selected user
	 * @param userid
	 * @param username
	 */
	public void activateUser(String userid, String username){
		Map<String,String> data = new HashMap<String,String>();
		data.put("userid", userid);
		data.put("username", username);
		apiclient.activateUser(data,response);
	}
	
	/**
	 * Sets a users trust level
	 * @param userid
	 * @param level 0 (new user), 1 (basic user), 2 (regular user), 3 (leader), 4 (elder)
	 */
	public void trustUser(String userid,int level){
		Map<String,String> data = new HashMap<String,String>();
		data.put("userid", userid);
		data.put("level", ""+level);
		apiclient.trustUser(data, response);
	}
	
	/**
	 * Generates an API key for the specified user
	 * @param userid
	 */
	public void generateApiKey(String userid){
		Map<String,String> data = new HashMap<String,String>();
		data.put("userid", userid);
		apiclient.generateApiKey(data,response);
	}
	
	/**
	 * Deletes the specified user
	 * @param userid
	 * @param username
	 */
	public void deleteUser(String userid, String username){
		Map<String,String> data = new HashMap<String,String>();
		data.put("userid", userid);
		data.put("username", username);
		apiclient.deleteUser(data,response);
	}
	
	/**
	 * Logs the specified user in
	 * @param username
	 * @param password
	 */
	public void loginUser(String username, String password){
		Map<String,String> data = new HashMap<String,String>();
		data.put("username", username);
		data.put("password", password);
		apiclient.deleteUser(data,response);
	}
	
	/**
	 * Logs the specified user out
	 * @param username
	 */
	public void logoutUser(String username){
		Map<String,String> data = new HashMap<String,String>();
		data.put("username", username);
		apiclient.logoutUser(data, response);
	}
}
