package example.com.okholdingz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class ParcelCounter extends AppCompatActivity {
    public static final String PREFS ="name";
    String name;
    ListView listView;
    String json_products;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ParcelCounterAdapter parcelCounterAdapter;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelcounter);
        new BackgroundTask().execute();

        parcelCounterAdapter = new ParcelCounterAdapter(this,R.layout.parcelcounter);
        listView=(ListView)findViewById(R.id.parcelcounter);
        listView.setAdapter(parcelCounterAdapter);
        Toast.makeText(this,"Click on Slot to Book",Toast.LENGTH_LONG).show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewParcelCounter viewParcelCounter = new ViewParcelCounter();
                //      Toast.makeText(context,"clicked",Toast.LENGTH_LONG).show();
                ArrayList arrayList = new ArrayList();
                JSONArray JA=new JSONArray();
                JSONObject FO=new JSONObject();
                try {

                    // JA= new JSONArray(jsonArray.get(position));
                    // FO= JA.getJSONObject(0);
                    arrayList.add(jsonArray.get(position));

                    JSONObject c = (JSONObject) jsonArray.get(position);

                    final String sid  = c.getString("sid");
                    String status  = c.getString("status");
                    String booker  = c.getString("booker");
if (status.equals("free")) {
    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ParcelCounter.this);
    alertDialogBuilder.setTitle("Confirm Booking");
    alertDialogBuilder.setMessage("Are you Sure you want to make this booking?");
    alertDialogBuilder.setPositiveButton("Yes",
            new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    SharedPreferences names= getSharedPreferences(PREFS,0);
                    String myname=names.getString("nameKey","No name found");
                    BBackgroundWorker bBackgroundWorker = new BBackgroundWorker(ParcelCounter.this);
                    bBackgroundWorker.execute(myname, sid);
                }
            });
    alertDialogBuilder.setNegativeButton("cancel",
            new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(ParcelCounter.this, ParcelCounter.class);
                    startActivity(intent);
                    finish();
                }
            });

    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

    alertDialog.show();
}
                    else{
    Toast.makeText(ParcelCounter.this,"This Slot is already Booked",Toast.LENGTH_LONG).show();
                    }


//
//                 Intent intent = new Intent(context,Showpdtactivity.class );
//
//                    intent.putExtra("sid", sid);
//                    intent.putExtra("status", status);
//                    intent.putExtra("booker", booker);
//
//
//
//                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String json_url;
        String JSON_STRING;
        ProgressDialog dialog= new ProgressDialog(ParcelCounter.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait......");
            dialog.show();
            json_url= "http://192.168.43.184:3001/okholdings/pcounter.php";
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
                String sid,status,booker;
                while (count<jsonArray.length()){

                    JSONObject JO = jsonArray.getJSONObject(count);

                    sid=JO.getString("sid");
                    status=JO.getString("status");
                    booker=JO.getString("booker");

                   // Toast.makeText(context,type,Toast.LENGTH_LONG).show();
                    ViewParcelCounter viewParcelCounter = new ViewParcelCounter(sid,status,booker);
                    parcelCounterAdapter.add(viewParcelCounter);

                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    class BBackgroundWorker extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        private ProgressDialog dialog;
        BBackgroundWorker(Context ctx){
            context=ctx;
            dialog = new ProgressDialog(ctx);
        }


        @Override
        protected String doInBackground(String... params) {

            String addtocart_url= "http://192.168.43.184:3001/okholdings/bookslot.php";

            try {
                String cname=params[0];
                String sid=params[1];

                URL url=new URL(addtocart_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("cname","UTF-8")+"="+ URLEncoder.encode(cname,"UTF-8")+"&"
                        +URLEncoder.encode("sid","UTF-8")+"="+ URLEncoder.encode(sid,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream= httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line;
                while((line=bufferedReader.readLine())!=null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }





        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait......");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                alertDialog=new AlertDialog.Builder(context).create();

                if(result.equals("booked")){
                    AlertDialog.Builder ab=new AlertDialog.Builder(context) ;
                    ab.setTitle("Request Status").setMessage("Slot Booked").setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(context,ParcelCounter.class);
                                    context.startActivity(intent);
                                    finish();
                                }
                            }).show();
//            Intent intent=new Intent(context,MainMenu.class);
//            context.startActivity(intent);
                }

                else {
                    alertDialog.setTitle("Status");
                    alertDialog.setMessage(result);
                    alertDialog.show();
                }

            }
            catch (NullPointerException ex){
                AlertDialog.Builder alertDialogBuilder = new   AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("connection to server lost ");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
