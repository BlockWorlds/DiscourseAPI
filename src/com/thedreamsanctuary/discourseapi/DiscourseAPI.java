package com.thedreamsanctuary.discourseapi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
	public void onDisable(){
		
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
	private static Handler handler;
	public static void initiate(String api_url, String api_key, String api_username){
		handler = new Handler(api_url,api_key,api_username);
	}
	/**
	 * Creates a new topic
	 * @param topic_title Title of the topic
	 * @param topic_content Body of the topic, as html
	 * @param topic_category Category of the topic, case-sensitive
	 */
	public static void createTopic(String topic_title, String topic_content, String topic_category){
		handler.createTopic(topic_title, topic_content, topic_category);
	}
	
	/**
	 * Creates a new user with the given parameters on the forum
	 * @param name
	 * @param email
	 * @param username
	 * @param password
	 */
	public static void createUser(String name, String email, String username, String password){
		
	}
	
	/**
	 * Approves the selected user
	 * @param userid
	 * @param username
	 */
	public static void approveUser(String userid,String username){
	}
	
	/**
	 * Activates the selected user
	 * @param userid
	 * @param username
	 */
	public static void activateUser(String userid, String username){
	}
	
	/**
	 * Sets a users trust level
	 * @param userid
	 * @param level 0 (new user), 1 (basic user), 2 (regular user), 3 (leader), 4 (elder)
	 */
	public static void trustUser(String userid,int level){
	}
	
	/**
	 * Generates an API key for the specified user
	 * @param userid
	 */
	public static void generateApiKey(String userid){
	}
	
	/**
	 * Deletes the specified user
	 * @param userid
	 * @param username
	 */
	public static void deleteUser(String userid, String username){
	}
	
	/**
	 * Logs the specified user in
	 * @param username
	 * @param password
	 */
	public static void loginUser(String username, String password){
	}
	
	/**
	 * Logs the specified user out
	 * @param username
	 */
	public static void logoutUser(String username){
	}
}
