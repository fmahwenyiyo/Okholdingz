package example.com.okholdingz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

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

/**
 * Created by kuda on 3/8/2016.
 */
public class BackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;
    private ProgressDialog dialog;
    BackgroundWorker(Context ctx){
        context=ctx;
        dialog = new ProgressDialog(ctx);
    }


    @Override
    protected String doInBackground(String... params) {
        String type=params[0];
        String login_url= "http://192.168.43.184:3001/okholdings/login.php";
        String register_url= "http://192.168.43.184:3001/okholdings/register.php";
//        String sendmsg_url= "http://192.168.43.131:3001/eservices/zsendmsg.php";
//        String submitpaye_url= "http://192.168.43.131:3001/eservices/zsubmitpayereturn.php";
//        String edit_url= "http://192.168.43.131:3001/eservices/zedit.php";
        if(type.equals("login")){

            try {
                String uname=params[1];
                String pword=params[2];
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("uname","UTF-8")+"="+ URLEncoder.encode(uname,"UTF-8")+"&"
                        + URLEncoder.encode("pword","UTF-8")+"="+ URLEncoder.encode(pword,"UTF-8");
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
        }
       else if(type.equals("register")){

            try {
                String name=params[1];
                String surname=params[2];
                String password=params[3];
                String mobilenum=params[4];
                String email=params[5];
                String address=params[6];
                String username=params[7];
//                String dob=params[8];
//                String snum=params[9];
//                String sname=params[10];
//                String country=params[11];
//                String city=params[12];
//                String mnumber=params[13];
//                String email=params[14];
//                String pword=params[15];

                URL url=new URL(register_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("name","UTF-8")+"="+ URLEncoder.encode(name,"UTF-8")+"&"
                        + URLEncoder.encode("surname","UTF-8")+"="+ URLEncoder.encode(surname,"UTF-8")+"&"
                        + URLEncoder.encode("password","UTF-8")+"="+ URLEncoder.encode(password,"UTF-8")+"&"
                        +  URLEncoder.encode("mobilenum","UTF-8")+"="+ URLEncoder.encode(mobilenum,"UTF-8")+"&"
                        + URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(email,"UTF-8")+"&"
                        + URLEncoder.encode("address","UTF-8")+"="+ URLEncoder.encode(address,"UTF-8")+"&"
                        + URLEncoder.encode("username","UTF-8")+"="+ URLEncoder.encode(username,"UTF-8")+"&";
                        /*+ URLEncoder.encode("dob","UTF-8")+"="+ URLEncoder.encode(dob,"UTF-8")+"&"
                        + URLEncoder.encode("snum","UTF-8")+"="+ URLEncoder.encode(snum,"UTF-8")+"&"
                        + URLEncoder.encode("sname","UTF-8")+"="+ URLEncoder.encode(sname,"UTF-8")+"&"
                        +  URLEncoder.encode("country","UTF-8")+"="+ URLEncoder.encode(country,"UTF-8")+"&"
                        + URLEncoder.encode("city","UTF-8")+"="+ URLEncoder.encode(city,"UTF-8")+"&"
                        + URLEncoder.encode("mnumber","UTF-8")+"="+ URLEncoder.encode(mnumber,"UTF-8")+"&"
                        + URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(email,"UTF-8")+"&"
                        + URLEncoder.encode("pass","UTF-8")+"="+ URLEncoder.encode(pass,"UTF-8");*/
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

            if(result.equals("login success")){
                AlertDialog.Builder ab=new AlertDialog.Builder(context) ;
                ab.setTitle("Login Status").setMessage("Login Successful").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                           //    Intent intent=new Intent(context,MainMenu.class);
                            //    context.startActivity(intent);
                            }
                        }).show();
//            Intent intent=new Intent(context,MainMenu.class);
//            context.startActivity(intent);
            }
            else if(result.equals("registration successful")){
                AlertDialog.Builder ab=new AlertDialog.Builder(context) ;
                ab.setTitle("Registration Status").setMessage("Registration successful").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Intent intent=new Intent(context,loginacti.class);
                             //   context.startActivity(intent);
                            }
                        }).show();

                // Toast.makeText(context,result,Toast.LENGTH_LONG).show();

            }

            else if(result.equals("Business Partner Number not found in Zimra database, register with zimra first")){
//            alertDialog.setMessage(result);
//           alertDialog.show();
                AlertDialog.Builder ab=new AlertDialog.Builder(context) ;
                ab.setTitle("Registration Status").setMessage("Business Partner Number does not exist, register with Zimra first").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                           //     Intent intent=new Intent(context,SignUpActivity.class);
                           //    context.startActivity(intent);
                            }
                        }).show();
            }
            else if(result.equals("you are are already registered,proceed to login")){
//            alertDialog.setMessage(result);
//           alertDialog.show();
                AlertDialog.Builder ab=new AlertDialog.Builder(context) ;
                ab.setTitle("Registration Status").setMessage("You are are already registered,proceed to login").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // Intent intent=new Intent(context,loginactivity.class);
                              //  context.startActivity(intent);
                            }
                        }).show();
            }
            else {
                alertDialog.setTitle("Query Status");
                alertDialog.setMessage(result);
                alertDialog.show();
            }

        }
        catch (NullPointerException ex){
            AlertDialog.Builder alertDialogBuilder = new   AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("No Connection To server...Please try again and if the problem persists, contact Zimra");
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
