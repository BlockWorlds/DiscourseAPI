package com.thedreamsanctuary.discourseapi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscourseAPIPlugin extends JavaPlugin{
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
}
