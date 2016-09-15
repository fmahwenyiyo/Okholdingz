package example.com.okholdingz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class Register extends AppCompatActivity {


    public EditText tfname,tlname,tpword,tmnumber,temail,taddress,tuname;
   String fname,lname,pword,mnumber,email,address,uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tfname = (EditText) findViewById(R.id.tfname);
        tlname= (EditText) findViewById(R.id.tlname);
        tpword= (EditText) findViewById(R.id.tpword);
        tmnumber= (EditText) findViewById(R.id.tmnumber);
        temail= (EditText) findViewById(R.id.temail);
        taddress= (EditText) findViewById(R.id.taddress);
        tuname= (EditText) findViewById(R.id.tuname);




    }
    public void OnBack(View view){
        Intent intent=new Intent(this,login.class);
        startActivity(intent);
    }


    public  void OnRegister(View view){
        fname=tfname.getText().toString();
        lname=tlname.getText().toString();
        pword=tpword.getText().toString();
        mnumber=tmnumber.getText().toString();
        email=temail.getText().toString();
        address=taddress.getText().toString();
        uname=tuname.getText().toString();

       // Toast.makeText(this,fname+lname+pword+mnumber+email+address+uname,Toast.LENGTH_LONG).show();
        if(uname.isEmpty()||address.isEmpty() ||email.isEmpty() ||
                mnumber.isEmpty() || pword.isEmpty() || lname.isEmpty() ||fname.isEmpty())
        {
            Toast.makeText(this,"Please enter all fields before submitting",Toast.LENGTH_LONG).show();

        }
        else{

            new BBackgroundWorker(Register.this).execute(fname,lname,pword,mnumber,email,address,uname);
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

            String addtocart_url= "http://192.168.43.184:3001/okholdings/register.php";

            try {
                String fname=params[0];
                String lname=params[1];
                String pword=params[2];
                String mnumber=params[3];
                String email=params[4];
                String address=params[5];
                String uname=params[6];

                URL url=new URL(addtocart_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("fname","UTF-8")+"="+ URLEncoder.encode(fname,"UTF-8")+"&"
                        +URLEncoder.encode("lname","UTF-8")+"="+ URLEncoder.encode(lname,"UTF-8")+"&"
                        +URLEncoder.encode("pword","UTF-8")+"="+ URLEncoder.encode(pword,"UTF-8")+"&"
                        +URLEncoder.encode("mnumber","UTF-8")+"="+ URLEncoder.encode(mnumber,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(email,"UTF-8")+"&"
                        +URLEncoder.encode("address","UTF-8")+"="+ URLEncoder.encode(address,"UTF-8")+"&"
                        +URLEncoder.encode("uname","UTF-8")+"="+ URLEncoder.encode(uname,"UTF-8");

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
                alertDialog=new AlertDialog.Builder(Register.this).create();
if(result=="Registration Successful"){
  //  Toast.makeText(Register.this, result, Toast.LENGTH_SHORT).show();
}
else {
    alertDialog.setTitle("Registration Status");
    alertDialog.setMessage(result);
    alertDialog.show();
    Intent i=new Intent(Register.this,login.class);
    startActivity(i);
    finish();
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
