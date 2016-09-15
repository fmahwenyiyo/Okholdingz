package example.com.okholdingz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Okproducts extends AppCompatActivity {
    ListView listView;
    String json_products;
    JSONObject jsonObject;
    JSONArray jsonArray;
    pdtsAdapter pdtsAdapter;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okproducts);
        new BackgroundTask().execute();

        pdtsAdapter = new pdtsAdapter(this,R.layout.products);
        listView=(ListView)findViewById(R.id.listpdts);
        listView.setAdapter(pdtsAdapter);
        Toast.makeText(this,"Click on Product to view",Toast.LENGTH_LONG).show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Viewpdts viewpdts = new Viewpdts();
                //      Toast.makeText(context,"clicked",Toast.LENGTH_LONG).show();
                ArrayList arrayList = new ArrayList();
                JSONArray JA=new JSONArray();
                JSONObject FO=new JSONObject();
                try {

                    // JA= new JSONArray(jsonArray.get(position));
                    // FO= JA.getJSONObject(0);
                    arrayList.add(jsonArray.get(position));

                    JSONObject c = (JSONObject) jsonArray.get(position);

                    String pid  = c.getString("id");
                    String pcode  = c.getString("code");
                    String pname  = c.getString("name");
                    String ptype  = c.getString("type");
                    String pdesc  = c.getString("desc");
                    String pprice  = c.getString("price");
                    String pstatus  = c.getString("status");

                 Intent intent = new Intent(context,Showpdtactivity.class );

                    intent.putExtra("id", pid);
                    intent.putExtra("code", pcode);
                    intent.putExtra("name", pname);
                    intent.putExtra("type",ptype);
                    intent.putExtra("desc", pdesc);
                    intent.putExtra("price", pprice);
                    intent.putExtra("status",pstatus);


                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String json_url;
        String JSON_STRING;
        ProgressDialog dialog= new ProgressDialog(Okproducts.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait......");
            dialog.show();
            json_url= "http://192.168.43.184:3001/okholdings/okproducts.php";
        }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url=new URL(json_url);
                    HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
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
            //Toast.makeText(context,json_products, Toast.LENGTH_SHORT).show();
            try {
                jsonObject= new JSONObject(json_products);
                jsonArray= jsonObject.getJSONArray("server_response");
                //Toast.makeText(context,json_products,Toast.LENGTH_SHORT).show();

                int count=0;
                String name,type,code,price;
                while (count<jsonArray.length()){

                    JSONObject JO = jsonArray.getJSONObject(count);

                    name=JO.getString("name");
                    type=JO.getString("type");
                    code=JO.getString("code");
                    price=JO.getString("price");
                   // Toast.makeText(context,type,Toast.LENGTH_LONG).show();
                    Viewpdts viewpdts = new Viewpdts(code,name,type,price);
                    pdtsAdapter.add(viewpdts);

                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
