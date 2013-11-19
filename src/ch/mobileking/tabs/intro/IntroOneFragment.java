package ch.mobileking.tabs.intro;

import ch.mobileking.R;
import ch.mobileking.utils.SharedPrefEditor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IntroOneFragment  extends Fragment{
	
	private int layoutId;
	
	public void setLayoutId(int layoutId)
	{
		this.layoutId = layoutId;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(this.layoutId, container, false);
        
        
        return rootView;
    }
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        
    }

}
