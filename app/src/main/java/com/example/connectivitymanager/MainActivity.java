package com.example.connectivitymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String explain="Restart Application or minimize and re-launch  to update Changes";
    ConnectivityManager connMgr;
    NetworkInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, explain, Toast.LENGTH_SHORT).show();

    }
//    Override and Implement connectivityStatus in onResume-->
    @Override
    protected void onResume() {
        super.onResume();
        connMgr=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        info=connMgr.getActiveNetworkInfo();
        if (info!=null&&info.isConnected()){
            Thread t=new Thread(run);
            t.start();
            if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
                Toast.makeText(this, "Wifi - On"+"\n"+"MobileData - Off", Toast.LENGTH_SHORT).show();
            }
            else
                if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected())
                {
                    Toast.makeText(this, "Mobile Data - On"+"\n"+"Wifi - Off", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Thread n=new Thread(noCon);
            n.start();
            Toast.makeText(this, "Mobile Data - Off"+"\n"+"Wifi - Off", Toast.LENGTH_SHORT).show();
        }
    }

    //Task when internetConnected
    Runnable run=new Runnable() {
        @Override
        public void run() {
            Handler handler=new Handler(getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,new ConnectedFragment()).commit();
                }
            });
        }
    };
    //Task when There is noConnection
    Runnable noCon=new Runnable() {
        @Override
        public void run() {

            Handler h=new Handler(getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer,new NoConnectionFragment()).commit();
                }
            });
        }
    };


}