package ch.mobileking.tabs.intro;

import ch.mobileking.R;
import ch.mobileking.classes.override.RobotoTextView;
import ch.mobileking.utils.SharedPrefEditor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class IntroFragment extends Fragment {
    private static final String KEY_CONTENT = "IntroFragment:Content";
    private static final String KEY_ICON = "IntroFragment:Icon";

    public static IntroFragment newInstance(String content, int resourceID) {
    	IntroFragment fragment = new IntroFragment();

    	System.out.println("content: " + content);
    	System.out.println("resourceId: " +resourceID);

        fragment.mContent = content.toString();
        fragment.mIconResource = resourceID;
        return fragment;
    }

    private String mContent = "???";
    private int mIconResource = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView text = new RobotoTextView(getActivity());
        text.setGravity(Gravity.CENTER);
        text.setText(mContent);
        text.setTextSize(10 * getResources().getDisplayMetrics().density);
        text.setTextColor(getResources().getColor(R.color.grey_light));
        text.setPadding(20, 20, 20, 20);
        
        ImageView image = new ImageView(getActivity());
        image.setImageResource(mIconResource);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.addView(text);
        layout.addView(image);

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
        outState.putInt(KEY_ICON, mIconResource);
    }
}