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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class Showpdt extends AppCompatActivity {
TextView tid,tcode,tname,ttype,tdesc,tstatus,tprice,tvq;
    String id,code,name,type,desc,status,price;

    public static final String PREFS ="name";
    public static  final  String cusemail = "email";
    public static  final  String cusmnumber = "mnumber";

    String qnty;
    Button btndel,btnorder,btnfinish;
    EditText quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpdt);
        tid=(TextView)findViewById(R.id.id);
        tcode=(TextView)findViewById(R.id.code);
        tname=(TextView)findViewById(R.id.name);
        ttype=(TextView)findViewById(R.id.type);
        tdesc=(TextView)findViewById(R.id.desc);
        tprice=(TextView)findViewById(R.id.price);


            id=getIntent().getExtras().getString("id");
            code=getIntent().getExtras().getString("code");
            name=getIntent().getExtras().getString("name");
            type=getIntent().getExtras().getString("type");
            desc=getIntent().getExtras().getString("desc");
        price=getIntent().getExtras().getString("price");
       // Toast.makeText(this,id,Toast.LENGTH_LONG).show();
        tid.setText(id);
        tcode.setText(code);
        tname.setText(name);
        ttype.setText(type);
        tdesc.setText(desc);
        tprice.setText(price);

        btndel=(Button)findViewById(R.id.btndel);
        btnfinish=(Button)findViewById(R.id.btnFinish);
        btnorder=(Button)findViewById(R.id.btnOrder);
        tvq=(TextView)findViewById(R.id.textView);
       quantity=(EditText)findViewById(R.id.tqnty);


    }

    public  void OnDelete(View view){
        SharedPreferences names= getSharedPreferences(PREFS,0);
        String myname=names.getString("nameKey","No name found");
        new DBackgroundWorker(this).execute(myname,tid.getText().toString());
    }


public void OnOrder(View view){
    btndel.setVisibility(View.GONE);
    btnfinish.setVisibility(View.VISIBLE);
    btnorder.setVisibility(View.GONE);
    tvq.setVisibility(View.VISIBLE);
    quantity.setVisibility(View.VISIBLE);




}
    public  void  OnFinish(View view){
        qnty=quantity.getText().toString();
        SharedPreferences emailshared= getSharedPreferences(PREFS,0);
        String cname;
        cname=emailshared.getString("nameKey","No name found");

        SharedPreferences cemail= getSharedPreferences(cusemail,0);
        String email=cemail.getString("emailKey","No email found");

        SharedPreferences mnum= getSharedPreferences(cusmnumber,0);
        String mnumber=mnum.getString("mnumKey","No number found");

       // Toast.makeText(this,qnty,Toast.LENGTH_LONG).show();
        new OBackgroundWorker(this).execute(cname,code,name,type,desc,price,qnty,email,mnumber);
    }
   class OBackgroundWorker extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        private ProgressDialog dialog;
        OBackgroundWorker(Context ctx){
            context=ctx;
            dialog = new ProgressDialog(ctx);
        }
        @Override
        protected String doInBackground(String... params) {
            String addtocart_url= "http://192.168.43.184:3001/okholdings/makeorder.php";
                      try {
                    String cname=params[0];
                    String code=params[1];
                    String name=params[2];
                    String type=params[3];
                    String desc=params[4];
                    String price=params[5];
                          qnty=params[6];
                          String email=params[7];
                         String mnumber=params[8];

                    URL url=new URL(addtocart_url);
                    HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data= URLEncoder.encode("cname","UTF-8")+"="+ URLEncoder.encode(cname,"UTF-8")+"&"
                   +URLEncoder.encode("code","UTF-8")+"="+ URLEncoder.encode(code,"UTF-8")+"&"
                   +URLEncoder.encode("name","UTF-8")+"="+ URLEncoder.encode(name,"UTF-8")+"&"
                   +URLEncoder.encode("type","UTF-8")+"="+ URLEncoder.encode(type,"UTF-8")+"&"
                            + URLEncoder.encode("desc","UTF-8")+"="+ URLEncoder.encode(desc,"UTF-8")+"&"
                            + URLEncoder.encode("price","UTF-8")+"="+ URLEncoder.encode(price,"UTF-8")+"&"
                            + URLEncoder.encode("qnty","UTF-8")+"="+ URLEncoder.encode(qnty,"UTF-8")+"&"
                            + URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(email,"UTF-8")+"&"
                            + URLEncoder.encode("mnumber","UTF-8")+"="+ URLEncoder.encode(mnumber,"UTF-8");
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

                if(result.equals("ordered")){
                    AlertDialog.Builder ab=new AlertDialog.Builder(context) ;
                    ab.setTitle("Query Status").setMessage("order placed successfully").setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                      Intent intent=new Intent(context,MyOrdersActivity.class);
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

    class DBackgroundWorker extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        private ProgressDialog dialog;
        DBackgroundWorker(Context ctx){
            context=ctx;
            dialog = new ProgressDialog(ctx);
        }


        @Override
        protected String doInBackground(String... params) {

            String addtocart_url= "http://192.168.43.184:3001/okholdings/deleteformcart.php";

            try {
                String cname=params[0];
                String id=params[1];

                URL url=new URL(addtocart_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("cname","UTF-8")+"="+ URLEncoder.encode(cname,"UTF-8")+"&"
                        +URLEncoder.encode("id","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8");

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

                if(result.equals("deleted")){
                    AlertDialog.Builder ab=new AlertDialog.Builder(context) ;
                    ab.setTitle("Query Status").setMessage("product deleted from shopping cart").setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(context,MyShoppingCart.class);
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
