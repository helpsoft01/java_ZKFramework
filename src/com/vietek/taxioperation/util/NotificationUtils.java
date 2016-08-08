package com.vietek.taxioperation.util;

public class NotificationUtils {
	private static NotificationUtils sharedInstance = null;
	public static NotificationUtils getSharedInstance() {
		if (sharedInstance == null) {
			sharedInstance = new NotificationUtils();
		}
		return sharedInstance;
	}
	
	public static void sendNotification(String phoneNumber, String email, String title, String content) {
		NotificationUtils.getSharedInstance().doSend(phoneNumber, email, title, content);
	}
	
	public void doSend(String phoneNumber, String email, String title, String content) {
		Worker worker = new Worker(phoneNumber, email, title, content);
		Thread thread = new Thread(worker);
		thread.start();
	}
	
	class Worker implements Runnable {
		private String phoneNumber;
		private String email;
		private String title;
		private String content;
		
		
		public Worker(String phoneNumber, String email, String title, String content) {
			super();
			this.phoneNumber = phoneNumber;
			this.email = email;
			this.title = title;
			this.content = content;
		}


		@Override
		public void run() {
			if (phoneNumber != null && phoneNumber.trim().length() > 0) {
				String phoneNo = CommonUtils.getPhoneForSms(phoneNumber);
				if (phoneNo != null && phoneNo.length() > 0) {
					SMSUtils.sendSMS(phoneNo, content);
				}
			}
			if (email != null && email.trim().length() > 0) {
				EmailUtils.sendMail(email, title, content);
			}
		}
	}
}
