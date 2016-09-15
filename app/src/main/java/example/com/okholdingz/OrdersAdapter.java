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
public class OrdersAdapter extends ArrayAdapter {
    List list= new ArrayList();
    public OrdersAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(ViewOrders object) {
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
        OrdersHolder OrdersHolder;
       // if(row==null){

            LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row=layoutInflater.inflate(R.layout.orders,parent,false);
        OrdersHolder =new OrdersHolder();
        OrdersHolder.pname = (TextView) row.findViewById(R.id.pn);
        OrdersHolder.pcode = (TextView) row.findViewById(R.id.pc);
        OrdersHolder.pprice = (TextView) row.findViewById(R.id.pp);
        OrdersHolder.pqnty = (TextView) row.findViewById(R.id.pq);

            ViewOrders viewOrders = (ViewOrders) this.getItem(position);
        OrdersHolder.pname.setText(viewOrders.getName());
        OrdersHolder.pcode.setText(viewOrders.getCode());
        OrdersHolder.pprice.setText(viewOrders.getPrice());
        OrdersHolder.pqnty.setText(viewOrders.getQnty());


            row.setTag(OrdersHolder);


        return row;
    }


    static class OrdersHolder {
TextView pname, pcode, pprice, pqnty;

    }

}
