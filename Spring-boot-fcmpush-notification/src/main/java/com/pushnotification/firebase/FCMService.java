package com.pushnotification.firebase;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.pushnotification.model.PushNotificationRequest;

@Service
public class FCMService {

	private Logger logger = LoggerFactory.getLogger(FCMService.class);

	public void sendMessage(Map<String, String> data, PushNotificationRequest request)
			throws InterruptedException, ExecutionException {

		Message message = getPreconfiguredMessageWithData(data, request);
		String responce = sendAndGetResponse(message);

		logger.info("sent msg with data. Topic:", request.getTopic() + "," + responce);

	}

	public void sendMessageWithoutData(PushNotificationRequest request)
			throws InterruptedException, ExecutionException {

		Message message = getPreconfiguredMessageWithoutData(request);
		String responce = sendAndGetResponse(message);

		logger.info("send msg without data: " + request.getTopic() + ", " + responce);

	}

	public void sendMessageToToken(PushNotificationRequest request) throws InterruptedException, ExecutionException {
		Message message = getPreconfiguredMessageToToken(request);
		String responce = sendAndGetResponse(message);
		logger.info("sent msg to token: " + request.getToken() + "," + responce);
	}

	private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
		return FirebaseMessaging.getInstance().sendAsync(message).get();
	}

	private AndroidConfig getAndroidConfig(String topic) {

		return AndroidConfig.builder().setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
				.setPriority(AndroidConfig.Priority.HIGH)
				.setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
						.setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build())
				.build();

	}

	private ApnsConfig getApnsConfig(String topic) {
		// TODO Auto-generated method stub
		return ApnsConfig.builder().setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
	}

	private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic()).build();
	}

	private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).setToken(request.getToken()).build();
	}

	private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).putAllData(data).setTopic(request.getTopic()).build();
	}

	private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {

		AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
		ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
		return Message.builder().setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
				.setNotification(new Notification(request.getTitle(), request.getMessage()));
	}

}
