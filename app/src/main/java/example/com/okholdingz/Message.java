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

public class Message extends AppCompatActivity {
    public static final String PREFS ="name";
    public static  final  String cusemail = "email";
    public static  final  String cusmnumber = "mnumber";

    public EditText tsubject,tbody;
   String subject,body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        tsubject = (EditText) findViewById(R.id.tsubject);
        tbody= (EditText) findViewById(R.id.tbody);




    }
public void OnBack(View view){
    Intent intent=new Intent(this,MainActivity.class);
    startActivity(intent);
}

    public  void OnSend(View view){
        subject=tsubject.getText().toString();
        body=tbody.getText().toString();


        Toast.makeText(this,subject+body,Toast.LENGTH_LONG).show();
        if(subject.isEmpty()||body.isEmpty() )
        {
            Toast.makeText(this,"Please enter all fields before sending",Toast.LENGTH_LONG).show();

        }
        else{
            SharedPreferences emailshared= getSharedPreferences(PREFS,0);
            String cname=emailshared.getString("nameKey","No name found");

            SharedPreferences email= getSharedPreferences(cusemail,0);
            String cmail=email.getString("emailKey","No email found");

            SharedPreferences mnum= getSharedPreferences(cusmnumber,0);
            String m=mnum.getString("mnumKey","No number found");
            new BBackgroundWorker(Message.this).execute(cname,cmail,m,subject,body);
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

            String addtocart_url= "http://192.168.43.184:3001/okholdings/message.php";

            try {
                String cname=params[0];
                String cemail=params[1];
                String cmnumber=params[2];
                String subject=params[3];
                String body=params[4];


                URL url=new URL(addtocart_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("cname","UTF-8")+"="+ URLEncoder.encode(cname,"UTF-8")+"&"
                        +URLEncoder.encode("cemail","UTF-8")+"="+ URLEncoder.encode(cemail,"UTF-8")+"&"
                        +URLEncoder.encode("cmnumber","UTF-8")+"="+ URLEncoder.encode(cmnumber,"UTF-8")+"&"
                        +URLEncoder.encode("mnumber","UTF-8")+"="+ URLEncoder.encode(cmnumber,"UTF-8")+"&"
                        +URLEncoder.encode("subject","UTF-8")+"="+ URLEncoder.encode(subject,"UTF-8")+"&"
                        +URLEncoder.encode("body","UTF-8")+"="+ URLEncoder.encode(body,"UTF-8");

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
                AlertDialog.Builder ab=new AlertDialog.Builder(context) ;
                ab.setTitle("Message Status").setMessage("Message sent successfully").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(context,MainActivity.class);
                                context.startActivity(intent);
                                finish();
                            }
                        }).show();



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
