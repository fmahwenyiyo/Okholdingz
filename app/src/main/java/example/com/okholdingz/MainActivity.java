package example.com.okholdingz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
            private Button viewproducts, shoppingcart, order, parcelcounter, logout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);


            viewproducts = (Button) findViewById(R.id.btnViewProducts);
            shoppingcart = (Button) findViewById(R.id.btnShoppingCart);
            order = (Button) findViewById(R.id.btnOrder);
            parcelcounter = (Button) findViewById(R.id.btnParcelCounter);
            logout = (Button) findViewById(R.id.btnlogout);


            viewproducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Okproducts.class);
                    startActivity(intent);
                }
            });

            shoppingcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MyShoppingCart.class);
                    startActivity(intent);
                }
            });
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MyOrdersActivity.class);
                    startActivity(intent);
                }
            });
            parcelcounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ParcelCounter.class);
                    startActivity(intent);
                }
            });
//            logout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), LogoutActivity.class);
//                    startActivity(intent);
//                }
//            });

        }
    public  void  OnMessage(View view){
        Intent intent=new Intent(this,Message.class);
        startActivity(intent);
    }
    }

