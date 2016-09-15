package example.com.okholdingz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Showorder extends AppCompatActivity {
    private TextView tid, tcode, tname, ttype, tdesc, tqnty, tprice, ttprice;
    private String id, code, name, type, desc, qnty, price;
    double cost;
    // String cname="kuda",email="kuda@gmail.com",mnumber="772789390";

    //  Button btndel,btnorder,btnfinish;
    //  EditText quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showorder);
        tid = (TextView) findViewById(R.id.oid);
        tcode = (TextView) findViewById(R.id.ocode);
        tname = (TextView) findViewById(R.id.oname);
        ttype = (TextView) findViewById(R.id.otype);
        tdesc = (TextView) findViewById(R.id.odesc);
        tprice = (TextView) findViewById(R.id.oprice);
        tqnty = (TextView) findViewById(R.id.oqnty);
        ttprice = (TextView) findViewById(R.id.ocost);


        id = getIntent().getExtras().getString("id");
        code = getIntent().getExtras().getString("code");
        name = getIntent().getExtras().getString("name");
        type = getIntent().getExtras().getString("type");
        desc = getIntent().getExtras().getString("desc");
        price = getIntent().getExtras().getString("price");
        qnty = getIntent().getExtras().getString("qnty");
        // Toast.makeText(this,id,Toast.LENGTH_LONG).show();
        cost = (Double.parseDouble(price) * Double.parseDouble(qnty));

        tid.setText(id);
        tcode.setText(code);
        tname.setText(name);
        ttype.setText(type);
        tdesc.setText(desc);
        tprice.setText(price);
        tqnty.setText(qnty);
        ttprice.setText(String.valueOf(cost));


    }


}
