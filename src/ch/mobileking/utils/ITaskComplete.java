package ch.mobileking.utils;

public interface ITaskComplete {
	
	void onLoginCompleted(boolean b);
	
	void onUpdateCompleted(boolean b);
	
	void startUpdate();
	
	void startLogin();
}
