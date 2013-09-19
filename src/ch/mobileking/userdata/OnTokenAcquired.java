package ch.mobileking.userdata;

import java.io.IOException;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.Bundle;

public class OnTokenAcquired implements AccountManagerCallback<Bundle> {
    @Override
    public void run(AccountManagerFuture<Bundle> result) {
        // Get the result of the operation from the AccountManagerFuture.
        Bundle bundle = null;
		try {
			bundle = result.getResult();
		} catch (OperationCanceledException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (AuthenticatorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    
        // The token is a named value in the bundle. The name of the value
        // is stored in the constant AccountManager.KEY_AUTHTOKEN.
        String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
        Intent launch = null;
		try {
			launch = (Intent) result.getResult().get(AccountManager.KEY_INTENT);
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (launch != null) {
//            startActivityForResult(launch, 0);
        	System.out.println("SUCCESSFULLY AUTHENTICATED");
            return;
        }
    }
}