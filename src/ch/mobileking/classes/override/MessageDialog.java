package ch.mobileking.classes.override;


import ch.mobileking.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;


public class MessageDialog extends DialogFragment{
	
	public static MessageDialog newInstance(int styleID)
	{
		MessageDialog f = new MessageDialog();
		
		Bundle args = new Bundle();
		args.putInt("style", styleID);
		f.setArguments(args);
		return f;
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		int styleID = getArguments().getInt("style");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    builder.setView(inflater.inflate(styleID, null))
	           .setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // sign in the user ...
	               }
	           });
	    
	    builder.setCancelable(true);
	    
	    builder.setInverseBackgroundForced(true);
	    
	    return builder.create();
    }
}
