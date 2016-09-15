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
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
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

public class login extends AppCompatActivity {
EditText tuname,tupword;
    String uname,pword;
    String json_userdata;
    String cname,email,mnumber,address;
   // public static  final  String cemail = "email";
   public static final String PREFS ="name";

    public static  final  String cusemail = "email";

    public static  final  String cusmnumber = "mnumber";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tuname= (EditText) findViewById(R.id.username);
        tupword= (EditText) findViewById(R.id.password);




    }
    public void Register(View view){
        Intent intent= new Intent(this,Register.class);
        startActivity(intent);
    }
    public void OnLogin(View view){
        uname=tuname.getText().toString();
        pword=tuname.getText().toString();
if(!uname.isEmpty()|| !pword.isEmpty()){

       // Toast.makeText(login.this, uname + pword, Toast.LENGTH_SHORT).show();
    new LBackgroundWorker(login.this).execute(uname,pword);


//    SharedPreferences username= getSharedPreferences(cuname,0);
//    SharedPreferences.Editor editor1=username.edit();
//    editor1.putString("unameKey",uname);
//    editor1.commit();
//    SharedPreferences usern= getSharedPreferences(cusname,0);
//    SharedPreferences.Editor editor2=usern.edit();
//    editor2.putString("nameKey",cname);
//    editor2.commit();
////    SharedPreferences useraddress= getSharedPreferences(cuaddress,0);
////    SharedPreferences.Editor editor3=useraddress.edit();
////    editor3.putString("addrKey",address);
////    editor3.commit();
//    SharedPreferences usermnumber= getSharedPreferences(cumnumber,0);
//    SharedPreferences.Editor editor4=usermnumber.edit();
//    editor4.putString("numKey",mnumber);
//    editor4.commit();

//    Toast.makeText(this, (CharSequence) useremail,Toast.LENGTH_LONG ).show();



}
        else {
   Toast.makeText(login.this, "Enter all fields to login", Toast.LENGTH_SHORT).show();
       }
    }

    class LBackgroundWorker extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        private ProgressDialog dialog;
        LBackgroundWorker(Context ctx){
            context=ctx;
            dialog = new ProgressDialog(ctx);
        }


        @Override
        protected String doInBackground(String... params) {

            String addtocart_url= "http://192.168.43.184:3001/okholdings/login.php";

            try {
                String uname=params[0];
                String pword=params[1];


                URL url=new URL(addtocart_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("uname","UTF-8")+"="+ URLEncoder.encode(uname,"UTF-8")+"&"
                                               +URLEncoder.encode("pword","UTF-8")+"="+ URLEncoder.encode(pword,"UTF-8");

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


                if(result.equals("failed")){
                    AlertDialog.Builder ab= new AlertDialog.Builder(context) ;
                    ab.setTitle("Login Status").setMessage("Login Failed, username or password incorrect").setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                }

                else {
                   json_userdata=result;
                  //  Toast.makeText(context,json_userdata,Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject;
                        JSONArray jsonArray;
                        jsonObject = new JSONObject(json_userdata);
                        jsonArray = jsonObject.getJSONArray("server_response");
                        JSONObject finalObject = jsonArray.getJSONObject(0);


                        cname = finalObject.getString("name");
                        email = finalObject.getString("email");
                        mnumber = finalObject.getString("mnumber");
                   //     address = finalObject.getString("address");


                        SharedPreferences useremail= getSharedPreferences(PREFS,0);
                        SharedPreferences.Editor editor=useremail.edit();
                        editor.putString("nameKey",cname);
                        editor.commit();



                        SharedPreferences mail= getSharedPreferences(cusemail,0);
                        SharedPreferences.Editor editor1=mail.edit();
                        editor1.putString("emailKey",email);
                        editor1.commit();

                        SharedPreferences mnum= getSharedPreferences(cusmnumber,0);
                        SharedPreferences.Editor editor2=mnum.edit();
                        editor2.putString("mnumKey",mnumber);
                        editor2.commit();


                       // Toast.makeText(login.this, cname, Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(login.this,MainActivity.class);

                        startActivity(intent);
                        finish();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
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
