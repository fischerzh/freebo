package ch.mobileking.activity.salesslips;

import java.io.File;
import java.util.ArrayList;

import ch.mobileking.R;
import ch.mobileking.R.drawable;
import ch.mobileking.R.id;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.SalesSlip;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class SalesSlipImageViewer extends Activity implements ViewFactory {

	private ImageSwitcher imageSwitcher1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_salesslips_item_detail);
	    String filename = getIntent().getStringExtra("filename");
	    Integer totalParts = getIntent().getIntExtra("totalparts", 0);

	    
		final ArrayList<Uri> imageList = new ArrayList<Uri>();
		for(int i = 0; i < totalParts; i++)
		{
			imageList.add(Uri.fromFile(new File(Utils.getPath(null), filename+"_part"+ i +".png")));

		}
//		imageList.add(Uri.fromFile(new File(Utils.getPath(null), filename+"_part1"+".png")));
//		imageList.add(Uri.fromFile(new File(Utils.getPath(null), filename+"_part2"+".png")));

		
		imageSwitcher1 = (ImageSwitcher) findViewById(R.id.salesslip_image_imageSwitcher);
		imageSwitcher1.setFactory(this);
		imageSwitcher1.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));
		imageSwitcher1.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_out));
		
		imageSwitcher1.setImageURI(imageList.get(0));
		
		final int maxSize = imageList.size()-1;
		
		imageSwitcher1.setOnTouchListener(new OnTouchListener() {
			private int downX;
			private int upX;
			int curIndex = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {downX = (int) event.getX();
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					upX = (int) event.getX();
					if (upX - downX > 100) {
						imageSwitcher1.setInAnimation(AnimationUtils.loadAnimation(SalesSlipImageViewer.this,android.R.anim.slide_in_left));
						imageSwitcher1.setOutAnimation(AnimationUtils.loadAnimation(SalesSlipImageViewer.this,android.R.anim.slide_out_right));
                        curIndex--;
						if (curIndex < 0) {
                            curIndex = maxSize;
                        }
						imageSwitcher1.setImageURI(imageList.get(curIndex));

					} else if (downX - upX > 100) {
						imageSwitcher1.setInAnimation(AnimationUtils.loadAnimation(SalesSlipImageViewer.this,R.anim.slide_in_right));
						imageSwitcher1.setOutAnimation(AnimationUtils.loadAnimation(SalesSlipImageViewer.this,R.anim.slide_out_left));
                        curIndex++;
                        if (curIndex > maxSize) {
                            curIndex = 0;
                        }
						imageSwitcher1.setImageURI(imageList.get(curIndex));
					}
					return true;
				}
				return false;
			}
		});

	}

	@Override
	public View makeView() {
		ImageView iView = new ImageView(this);
        iView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iView.setLayoutParams(new    ImageSwitcher.LayoutParams( LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        iView.setBackgroundColor(0xFFFFFFFF);
        return iView;
	}
}
