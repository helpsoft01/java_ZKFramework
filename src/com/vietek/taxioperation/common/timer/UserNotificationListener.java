package com.vietek.taxioperation.common.timer;

import java.io.File;
import java.net.URL;

import com.vietek.taxioperation.util.FileWatcher;

public class UserNotificationListener{
	
	public void StartListener() {
		try {
			 String propFileName = "config.properties";
			 URL url = getClass().getClassLoader().getResource(propFileName);
			 File file = new File(url.toURI());
			 FileWatcher fileWatcher = new FileWatcher(file);
			 fileWatcher.start();
		} catch (Exception e) {
			 // TODO: handle exception
			 e.printStackTrace();
		}		
	}
}
