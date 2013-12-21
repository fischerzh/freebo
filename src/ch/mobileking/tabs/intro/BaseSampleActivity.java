package ch.mobileking.tabs.intro;

import java.util.Random;

import ch.mobileking.R;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseSampleActivity extends FragmentActivity {
    private static final Random RANDOM = new Random();

    IntroFragmentAdapter mAdapter;
    ViewPager mPager;
    CirclePageIndicator mIndicator;
    Button intro_button_finish;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.agb:
            	createAlert("AGB: ", "AGB", R.drawable.ic_empfehlungen);
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	private void createAlert(String message, String title, int iconId) {
		// Build the dialog
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(title);
		alert.setMessage(message);
		// Create TextView
		final TextView input = new TextView (this);
		alert.setView(input);
		alert.setIcon(iconId);

		alert.setPositiveButton("Zurück", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		    // Do something with value!
		  }
		});

		alert.show();
	}
}