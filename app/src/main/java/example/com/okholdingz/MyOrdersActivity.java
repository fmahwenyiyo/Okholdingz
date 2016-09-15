package example.com.okholdingz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity {
    ListView listView;
    String json_products;
    JSONObject jsonObject;
    JSONArray jsonArray;
    OrdersAdapter ordersAdapter;
    Context context = this;
    String name;
    public static final String PREFS ="name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);
        name="kuda";
        SharedPreferences emailshared= getSharedPreferences(PREFS,0);
        String cname=emailshared.getString("nameKey","No name found");
        new BackgroundTask().execute(cname);

       ordersAdapter = new OrdersAdapter(this,R.layout.orders);
        listView=(ListView)findViewById(R.id.myorders);
        listView.setAdapter(ordersAdapter);
        Toast.makeText(this,"Click on Order to view",Toast.LENGTH_LONG).show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewOrders viewOrders = new ViewOrders();
                     // Toast.makeText(context,"clicked",Toast.LENGTH_LONG).show();
                ArrayList arrayList = new ArrayList();
                JSONArray JA=new JSONArray();
                JSONObject FO=new JSONObject();
                try {

                    // JA= new JSONArray(jsonArray.get(position));
                    // FO= JA.getJSONObject(0);
                    arrayList.add(jsonArray.get(position));

                    JSONObject c = (JSONObject) jsonArray.get(position);

                    String pid  = c.getString("pid");
                    String pcode  = c.getString("pcode");
                    String pname  = c.getString("pname");
                    String ptype  = c.getString("ptype");
                    String pdesc  = c.getString("pdesc");
                    String pprice  = c.getString("pprice");
                   String pqnty  = c.getString("pqnty");

                 Intent intent = new Intent(context,Showorder.class );

                    intent.putExtra("id", pid);
                    intent.putExtra("code", pcode);
                    intent.putExtra("name", pname);
                    intent.putExtra("type",ptype);
                    intent.putExtra("desc", pdesc);
                    intent.putExtra("price", pprice);
                    intent.putExtra("qnty",pqnty);


                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    class BackgroundTask extends AsyncTask<String, Void, String> {
        String json_url;
        String JSON_STRING;
        ProgressDialog dialog= new ProgressDialog(MyOrdersActivity.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait......");
            dialog.show();
            json_url= "http://192.168.43.184:3001/okholdings/myorders.php";
        }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String name=params[0];
                    URL url=new URL(json_url);
                    HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();

                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data= URLEncoder.encode("name","UTF-8")+"="+ URLEncoder.encode(name,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();


                    InputStream inputStream= httpURLConnection.getInputStream();

                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder stringBuilder=new StringBuilder();

                    while ((JSON_STRING=bufferedReader.readLine())!=null){
                        stringBuilder.append(JSON_STRING + "\n");

                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return  stringBuilder.toString().trim();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) {
                dialog.dismiss();}
            json_products = result;
           // Toast.makeText(context,json_products, Toast.LENGTH_SHORT).show();
            try {
                jsonObject= new JSONObject(json_products);
                jsonArray= jsonObject.getJSONArray("server_response");
               // Toast.makeText(context,json_products,Toast.LENGTH_SHORT).show();

                int count=0;
                String name,qnty,code,price;
                while (count<jsonArray.length()){

                    JSONObject JO = jsonArray.getJSONObject(count);

                    name=JO.getString("pname");
                    qnty=JO.getString("pqnty");
                    code=JO.getString("pcode");
                    price=JO.getString("pprice");
                   // Toast.makeText(context,type,Toast.LENGTH_LONG).show();
                    ViewOrders viewOrders = new ViewOrders(code,name,qnty,price);
                    ordersAdapter.add(viewOrders);

                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
