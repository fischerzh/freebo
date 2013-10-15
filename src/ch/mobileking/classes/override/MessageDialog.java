package ch.mobileking.classes.override;


import ch.mobileking.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;


public class MessageDialog extends DialogFragment{

	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    builder.setView(inflater.inflate(R.layout.loyalty_instructions, null))
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
