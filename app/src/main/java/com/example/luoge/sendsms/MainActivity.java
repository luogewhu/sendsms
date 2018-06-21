package com.example.luoge.sendsms;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {
    //private static final String TAG ="COPY" ;

    static{
        System.loadLibrary("sendsms");
    }

    public native String SendSmsReflect();
    public void SendSmsApi(){
        String called = "18972623255";
        String msg ="send sms by api";
        SmsManager smsManager = SmsManager.getDefault();
        //sendTextMessage()的参数1 ：String destinationAddressString destinationAddress 是对方号码，
        // 参数2：String smscAddress短信中心，在以前早期的功能手机中，会有短信中心设置，Android还一下没找到在哪，短信中心的号码和运营商及手机归属地有关，OEM在手机出厂的时候会设置号，用null，表示用手机现有的设置
        // 参数3：短信内容
        // 参数4：PendingIntent sentIntent，短信发送触发的Intent
        // 参数5：Pending deliveryIntent，对方接受短信触发的Intent。
        smsManager.sendTextMessage(called, null, msg, null, null);

    }
    public void SendSmsJavaReflect(){
        String clazzName ="android.telephony.SmsManager";
        Class<?> c=null;
        try{  c = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
           //Object sms = c.newInstance();
            Method send = c.getMethod("getDefault");
            //Method send = c.getMethod("getDefault");
            send.setAccessible(true);
            Object smsManager2 = send.invoke(c);
            //SmsManager smsManager2= SmsManager.getDefault();
            Method stext = smsManager2.getClass().getMethod("sendTextMessage",String.class,String.class, String.class,PendingIntent.class, PendingIntent.class);
            stext.setAccessible(true);
            stext.invoke(smsManager2,"18972623255",null,"send by java reflect1",null,null);
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void SendSmsBinder()
    {   try {
        exe_file = new File(exe_path);
        copyBigDataToSD(exe_path);
        exe_file.setExecutable(true, true);
       // execCmd(exe_path);
        Process nativeApp = Runtime.getRuntime().exec("data/data/com.example.luoge.sendsms/binder");


        BufferedReader reader = new BufferedReader(new InputStreamReader(nativeApp.getInputStream()));
        int read;
        char[] buffer = new char[4096];
        StringBuffer output = new StringBuffer();
        while ((read = reader.read(buffer)) > 0) {
            output.append(buffer, 0, read);
        }
        reader.close();

        // Waits for the command to finish.
        nativeApp.waitFor();

        String nativeOutput =  output.toString();
        System.out.println(nativeOutput);

    } catch (IOException e1) {
        e1.printStackTrace();
    }catch(InterruptedException e){

    }
        /*try{


        }catch(IOException e){

        }catch(InterruptedException e){

        }*/
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.sms);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                String result = SendSmsReflect();
                System.out.println(result);
            }
        });
        Button b2 = (Button) findViewById(R.id.binder);
        Button b3 = (Button) findViewById(R.id.api);
        Button b4 = (Button) findViewById(R.id.java);
        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                SendSmsBinder();
            }
        });
        b3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                 SendSmsApi();
            }
        });
        b4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                SendSmsJavaReflect();
            }
        });

    }



    public void execCmd(String cmd) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(cmd);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while (null != (line = br.readLine())) {
            Log.e("########", line);
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void copyBigDataToSD(String strOutFileName) throws IOException
    {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFileName);
        myInput = this.getAssets().open("binder");
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
    }

    public String exe_path = "data/data/com.example.luoge.sendsms/binder";
    public File exe_file;
}

