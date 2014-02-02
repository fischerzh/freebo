package ch.mobileking.nfcreceipt;

import ch.mobileking.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InvoiceAdapter extends BaseAdapter {

	InvoiceOperation io;
	private LayoutInflater mInflater;

	public InvoiceAdapter(Context context, InvoiceOperation _io) {

		this.io = _io;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ((C0401Operation) io).details.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = mInflater.inflate(R.layout.nfc_invoice_list, null);
		String[] str = (String[]) ((C0401Operation) io).details.elementAt(position);

		TextView pn = (TextView) view.findViewById(R.id.pName);
		pn.setText(str[0]);
		TextView pq = (TextView) view.findViewById(R.id.pQuantity);
		pq.setText(str[1]);
		TextView pp = (TextView) view.findViewById(R.id.pPrice);
		pp.setText(str[3]);
		TextView pa = (TextView) view.findViewById(R.id.pAmount);
		pa.setText(str[4]);
		return view;
	}

}
