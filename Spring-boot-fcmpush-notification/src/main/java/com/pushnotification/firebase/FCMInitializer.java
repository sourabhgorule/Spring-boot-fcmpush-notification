package com.pushnotification.firebase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FCMInitializer {

	 @Value("${app.firebase-configuration-file}")
	private String firebaseConfigPath;
	
	Logger logger=LoggerFactory.getLogger(FCMInitializer.class);
	
	public void initialize() {
		try {
			
			  FirebaseOptions options = new FirebaseOptions.Builder()
	                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())).build();
			  if(FirebaseApp.getApps().isEmpty()) {
				  FirebaseApp.initializeApp(options);
				  logger.info(firebaseConfigPath);
			  }
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
			
		
	}
	
	
}
