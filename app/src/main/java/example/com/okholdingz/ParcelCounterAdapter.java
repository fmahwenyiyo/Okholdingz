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
public class ParcelCounterAdapter extends ArrayAdapter {
    List list= new ArrayList();
    public ParcelCounterAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(ViewParcelCounter object) {
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
        SlotsHolder SlotsHolder;
       // if(row==null){

            LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row=layoutInflater.inflate(R.layout.parcelcounter,parent,false);
        SlotsHolder =new SlotsHolder();
        SlotsHolder.tx_sid = (TextView) row.findViewById(R.id.sid);
        SlotsHolder.tx_status = (TextView) row.findViewById(R.id.status);
        SlotsHolder.tx_booker = (TextView) row.findViewById(R.id.bby);


            ViewParcelCounter viewParcelCounter = (ViewParcelCounter) this.getItem(position);
        SlotsHolder.tx_sid.setText(viewParcelCounter.getSid());
        SlotsHolder.tx_status.setText(viewParcelCounter.getStatus());
        SlotsHolder.tx_booker.setText(viewParcelCounter.getBooker());
        if (SlotsHolder.tx_booker.getText()==""){
            SlotsHolder.tx_booker.setText("N/A");
        }



            row.setTag(SlotsHolder);


        return row;
    }


    static class SlotsHolder {
TextView tx_sid, tx_status, tx_booker;

    }

}
