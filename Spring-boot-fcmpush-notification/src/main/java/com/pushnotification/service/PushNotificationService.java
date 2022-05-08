package com.pushnotification.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pushnotification.firebase.FCMService;
import com.pushnotification.model.PushNotificationRequest;

@Service
public class PushNotificationService {

	@Value("#{${app.notifications.defaults}}")
	private Map<String, String> defaults;

	private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

	private FCMService fcmService;

	public PushNotificationService(FCMService fcmService) {
		super();
		this.fcmService = fcmService;
	}

	@Scheduled(initialDelay = 60000, fixedDelay = 60000)
	public void sendSamplePushNotification() {

		try {

			fcmService.sendMessageWithoutData(getSamplePushNotificationRequest());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public void sendPushNotification(PushNotificationRequest request) {
		try {
			fcmService.sendMessage(getSamplePayloadData(), request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}

	}

	public void sendPushNotificationWithoutData(PushNotificationRequest request) {
		try {
			fcmService.sendMessageWithoutData(request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	public void sendPushNotificationToToken(PushNotificationRequest request) {

		try {
			fcmService.sendMessageToToken(request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}

	}

	private Map<String, String> getSamplePayloadData() {

		Map<String, String> pushData = new HashMap<String, String>();
		pushData.put("messageId", defaults.get("payloadMessageId"));
		pushData.put("text", defaults.get("payloadData") + " " + LocalDateTime.now());
		return pushData;
	}

	public PushNotificationRequest getSamplePushNotificationRequest() {
		PushNotificationRequest request = new PushNotificationRequest(defaults.get("title"), defaults.get("message"),
				defaults.get("topic"));
		return request;
	}

}
