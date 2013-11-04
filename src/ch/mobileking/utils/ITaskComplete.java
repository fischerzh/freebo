package ch.mobileking.utils;

public interface ITaskComplete {
	
	void onLoginCompleted(boolean b, String string);
	
	void onUpdateCompleted(boolean b);
	
	void startUpdate();
	
	void startLogin();


}
