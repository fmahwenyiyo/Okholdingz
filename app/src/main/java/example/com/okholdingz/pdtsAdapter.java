package example.com.okholdingz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuda on 3/29/2016.
 */
public class pdtsAdapter extends ArrayAdapter {
    List list= new ArrayList();
    public pdtsAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(Viewpdts object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }



    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View TextView, ViewGroup parent) {
        View row;
       row=TextView;
        pdtsHolder pdtsHolder;
       // if(row==null){

            LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row=layoutInflater.inflate(R.layout.products,parent,false);
        pdtsHolder =new pdtsHolder();
        pdtsHolder.tx_id= (TextView) row.findViewById(R.id.pid);
        pdtsHolder.tx_pname = (TextView) row.findViewById(R.id.pname);
        pdtsHolder.tx_ptype = (TextView) row.findViewById(R.id.ptype);
        pdtsHolder.tx_price = (TextView) row.findViewById(R.id.pprice);

            Viewpdts viewpdts = (Viewpdts) this.getItem(position);
        pdtsHolder.tx_id.setText(viewpdts.getCode());
        pdtsHolder.tx_pname.setText(viewpdts.getName());
        pdtsHolder.tx_ptype.setText(viewpdts.getType());
        pdtsHolder.tx_price.setText(viewpdts.getPrice());


            row.setTag(pdtsHolder);


        return row;
    }


    static class pdtsHolder {
TextView tx_id, tx_pname, tx_ptype,tx_price;

    }

}
