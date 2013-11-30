package ch.mobileking.exception;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import ch.mobileking.login.ServerRequest;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;

import android.os.Environment;

public class CustomExceptionHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler defaultUEH;
    
    private String username;

    private String localPath;

    private String url;
    
    private ServerRequest request;

    /* 
     * if any of the parameters is null, the respective functionality 
     * will not be used 
     */
    public CustomExceptionHandler(String username) {
//        this.localPath = localPath;
    	this.localPath = Utils.getPath("logs/");
        this.url = SharedPrefEditor.getUpdateErrorLogsURL();
        this.username = username;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        
    }

    public void uncaughtException(Thread t, Throwable e) {
    	Date currentTime = new Date();
        String timestamp = currentTime.toString();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        String filename = username+" - "+timestamp + ".stacktrace";

        if (localPath != null) {
            writeToFile(stacktrace, filename);
        }
        if (url != null) {
            sendToServer(stacktrace, filename);
        }

        defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String stacktrace, String filename) {
        try {
            BufferedWriter bos = new BufferedWriter(new FileWriter(localPath + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToServer(String stacktrace, String filename) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        System.out.println("Send to server...");
		File f = new File(localPath, filename);

        MultipartEntity entity = new MultipartEntity();
		entity.addPart("uploadFile", new FileBody(f));
		httpPost.setEntity(entity);
        try {
            httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}