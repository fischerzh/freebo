package ch.mobileking.utils;

public interface ITaskComplete {
	
	void onLoginCompleted(boolean b, String string);
	
	void onUpdateCompleted(boolean b, String string);
	
	void startUpdate();
	
	void startLogin();
	
	void sendProgressUpdate(int progress);


}
