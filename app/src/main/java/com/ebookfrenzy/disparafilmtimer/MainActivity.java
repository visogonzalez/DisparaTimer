package com.ebookfrenzy.disparafilmtimer;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.net.wifi.WifiManager;
import android.net.DhcpInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Locale;
import java.math.BigInteger;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ebookfrenzy.disparafilmtimer.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public long time0;
    public int numstr = 1;
    int[] stopsi = {2, 3, 4, 6, 8};
    double[] stripsd;
    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    String[] memo = new String[11];
    String memo2 = "750";
    CountDownTimer timer;
    CountDownTimer timer2;
    CountDownTimer timer3;
    CountDownTimer timer4;
    CountDownTimer timer5;
    CountDownTimer timer6;
    CountDownTimer timer7;
    CountDownTimer timer8;
    CountDownTimer timer9;
    CountDownTimer timer10;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        // Hide system bars
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        binding.main.setOnTouchListener((v, m) -> {
                    handleTouch(m);
                    return true;
                }
        );

        binding.st1Par.setOnLongClickListener(v -> parTimer(1));
        binding.st2Par.setOnLongClickListener(v -> parTimer(2));
        binding.st3Par.setOnLongClickListener(v -> parTimer(3));
        binding.st4Par.setOnLongClickListener(v -> parTimer(4));
        binding.st5Par.setOnLongClickListener(v -> parTimer(5));
        binding.st6Par.setOnLongClickListener(v -> parTimer(6));
        binding.st7Par.setOnLongClickListener(v -> parTimer(7));
        binding.st8Par.setOnLongClickListener(v -> parTimer(8));
        binding.st9Par.setOnLongClickListener(v -> parTimer(9));
        binding.st10Par.setOnLongClickListener(v -> parTimer(10));

        binding.st1Ac.setOnLongClickListener(v -> acTimer(1));
        binding.st2Ac.setOnLongClickListener(v -> acTimer(2));
        binding.st3Ac.setOnLongClickListener(v -> acTimer(3));
        binding.st4Ac.setOnLongClickListener(v -> acTimer(4));
        binding.st5Ac.setOnLongClickListener(v -> acTimer(5));
        binding.st6Ac.setOnLongClickListener(v -> acTimer(6));
        binding.st7Ac.setOnLongClickListener(v -> acTimer(7));
        binding.st8Ac.setOnLongClickListener(v -> acTimer(8));
        binding.st9Ac.setOnLongClickListener(v -> acTimer(9));
        binding.st10Ac.setOnLongClickListener(v -> acTimer(10));

        binding.reset.setOnLongClickListener(v -> erase());

        binding.expo.setOnClickListener(v -> {
            String dela = binding.ndelay.getText().toString();
            if (dela.isEmpty()) dela ="0";
            int delay = Integer.parseInt(dela);

            if (!binding.nmethod.getText().toString().equals("AUTO") || binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
                String ini = binding.time.getText().toString();
                double ex1 = Double.parseDouble(ini) * 1000;
                long ex = (int) ex1;
                if (binding.expo.getText().equals(getString(R.string.button)) && !binding.time.getText().toString().equals(getString(R.string._000_0))) {
                    starTimer(ex, getString(R.string.button1), delay);
                } else if (binding.expo.getText().equals(getString(R.string.button1))) {
                    timer.cancel();
                    paquete();
                    binding.expo.setText(getString(R.string.button2));
                    binding.reset.setEnabled(true);
                } else if (binding.expo.getText().equals(getString(R.string.button2))) {
                    starTimer(ex, getString(R.string.button1), delay);
                }
            } else{
                binding.expo.setEnabled(false);
                startimer2(getString(R.string.wait), delay);
            }
        });
    }

    void starTimer(long ex, String st, int delay) {

        timer = new CountDownTimer(ex, 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();
                binding.expo.setText(getString(R.string.button));
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time0) / 1000));
                binding.focus.setEnabled(true);
                binding.reset.setEnabled(true);
                int numstrips = Integer.parseInt(binding.numstrips.getText().toString());
                if (!binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
                    if (numstr < numstrips + 1) {
                        numstr = numstr + 1;
                        stripselect1(numstr);
                    }
                }
            }
        };

        new Handler().postDelayed(() -> {
            paquete();
            timer.start();
            binding.focus.setEnabled(false);
            binding.reset.setEnabled(false);
            binding.expo.setText(st);
        }, delay);
    }

    void handleTouch(MotionEvent m) {
        int pointerCount = m.getPointerCount();
        String strips = binding.numstrips.getText().toString();
        int numstrips = Integer.parseInt(strips);
        String[] stops = getResources().getStringArray(R.array.stops);
        int k = Arrays.asList(stops).indexOf(binding.numstops.getText().toString());

        String expo = binding.time.getText().toString();
        int n = expo.length();
        int cent = Integer.parseInt(expo.substring(0, 1));
        int dece = Integer.parseInt(expo.substring(1, 2));
        int unid = Integer.parseInt(expo.substring(2, 3));
        int deci = Integer.parseInt(expo.substring(4, n));

        double ex = cent * 100 + dece * 10 + unid + (double) deci / 10;

        //double ex = Double.parseDouble(binding.time.getText().toString());

        for (int i = 0; i < pointerCount; i++) {
            int x = (int) m.getX(i);
            int y = (int) m.getY(i);
            int action = m.getActionMasked();
            //int actionIndex =
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int dpi = metrics.densityDpi;

            Rect bounds = new Rect();
            binding.time.getLineBounds(0,bounds);
            int ce = bounds.centerX();
            int ceY=bounds.centerY();
            int bot=bounds.bottom;

            if (action == MotionEvent.ACTION_DOWN) {
                if (x > ce-(460*dpi/440) && x < ce-(360*dpi/440)) {
                    if (y <= bot && y >= ceY) {
                        if (cent >= 1) cent = cent - 1;
                        else cent=9;
                    } else if (y < ceY) {
                        if (cent < 9) cent = cent + 1;
                        else cent=0;
                    }
                    ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
                    time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                }
                if (x > ce-(265*dpi/440) && x < ce-(95*dpi/440)) {
                    if (y <= bot && y >= ceY) {
                        if (dece >= 1) dece = dece - 1;
                        else dece=9;
                    } else if (y < ceY) {
                        if (dece < 9) dece = dece + 1;
                        else dece=0;
                    }
                    ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
                    time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                }
                if (x > ce && x < ce+(100*dpi/440)) {
                    if (y <= bot && y >= ceY) {
                        if (unid >= 1) unid = unid - 1;
                        else unid=9;
                    } else if (y < ceY) {
                        if (unid < 9) unid = unid + 1;
                        else unid=0;
                    }
                    ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
                    time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                }
                if (x > ce+(290*dpi/440) && x < ce+(390*dpi/440)) {
                    if (y <= bot && y >= ceY) {
                        if (deci >= 1) deci = deci - 1;
                        else deci=9;
                    } else if (y < ceY) {
                        if (deci < 9) deci = deci + 1;
                        else deci=0;
                    }
                    ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
                    time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                }

            }

            binding.time.setText(String.format(Locale.US, "%05.1f", ex));
            if (!binding.nmode.getText().toString().equals(getResources().getString(R.string.timer)) && x > 180 && y <= 398) {
                numstr = 1;
                stripsd = stripcount(ex, numstrips, stopsi[k]);
                strippaint(stripsd, numstrips);
                stripselect1(numstr);
            }
            //binding.textView.setText("x = " + x + "   y = " + y);
        }
    }

    public void focus(View view) {
        paquete();
    }

    public void reset(View view) {
        if (binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))){
            double ex = (double) time0 / 1000;
            binding.time.setText(String.format(Locale.US, "%05.1f", ex));
            binding.expo.setText(getString(R.string.button));
            binding.focus.setEnabled(true);
        }
        else{
            stripselect1(1);
        }
    }

    InetAddress IP() {

        WifiManager wifiManager;

        {
            getApplicationContext();
            wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        }

        DhcpInfo d = wifiManager.getDhcpInfo();
        int IpInt = d.gateway;
        IpInt = (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) ?
                Integer.reverseBytes(IpInt) : IpInt;
        byte[] ipAddress = BigInteger.valueOf(IpInt).toByteArray();
        InetAddress myaddr;
        {
            try {
                myaddr = InetAddress.getByAddress(ipAddress);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        return myaddr;
    }

    void paquete() {
        int port = 5005;
        byte[] msg = "a".getBytes();
        try {
            DatagramChannel channel = DatagramChannel.open();
            DatagramSocket socket = channel.socket();
            InetAddress addr = IP();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, addr, port);
            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP,35);
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public double[] stripcount(double ex, int k, int kk) {
        double[] stripsd = new double[10];
        for (int i = 0; i <= k; i++) {
            double expo = (double) i / kk;
            stripsd[i] = ex * Math.pow(2, expo);
        }
        for (int i = k + 1; i < 10; i++) {
            stripsd[i] = 0.0;
        }
        return stripsd;
    }

    public void strippaint(double[] stripsd, int numstrips) {
        TableLayout tblLayout = binding.tableStrips;
        String method = binding.nmethod.getText().toString();
        if (!method.equals("SINGLE")) {
            for (int i = 1; i <= numstrips + 1; i++) {
                TableRow row = (TableRow) tblLayout.getChildAt(i);
                TextView strip1 = (TextView) row.getChildAt(1);
                TextView strip2 = (TextView) row.getChildAt(2);

                if (i == 1) strip1.setText(String.format(Locale.US, "%05.1f", stripsd[0]));
                else {
                    double ex1 = stripsd[i - 1] - stripsd[i - 2];
                    strip1.setText(String.format(Locale.US, "%05.1f", ex1));

                }
                strip2.setText(String.format(Locale.US, "%05.1f", stripsd[i - 1]));
                strip1.setTextColor(Color.parseColor("#80FF0000"));
                strip2.setTextColor(Color.parseColor("#80FF0000"));
            }
            for (int i = numstrips + 2; i < 11; i++) {
                TableRow row = (TableRow) tblLayout.getChildAt(i);
                TextView strip1 = (TextView) row.getChildAt(1);
                TextView strip2 = (TextView) row.getChildAt(2);
                strip1.setText(getString(R.string._000_0));
                strip2.setText(getString(R.string._000_0));
                strip1.setTextColor(0xff000000);
                strip2.setTextColor(0xff000000);
            }
        } else {
            for (int i = 1; i <= numstrips + 1; i++) {
                TableRow row = (TableRow) tblLayout.getChildAt(i);
                TextView strip1 = (TextView) row.getChildAt(1);
                TextView strip2 = (TextView) row.getChildAt(2);

                if (i == 1) strip1.setText(String.format(Locale.US, "%05.1f", stripsd[0]));
                else {
                    strip1.setText(String.format(Locale.US, "%05.1f", stripsd[i - 1]));
                }
                strip2.setText(String.format(Locale.US, "%05.1f", stripsd[i - 1]));
                strip1.setTextColor(Color.parseColor("#80FF0000"));
                strip2.setTextColor(Color.parseColor("#80FF0000"));
            }
            for (int i = numstrips + 2; i < 11; i++) {
                TableRow row = (TableRow) tblLayout.getChildAt(i);
                TextView strip1 = (TextView) row.getChildAt(1);
                TextView strip2 = (TextView) row.getChildAt(2);
                strip1.setText(R.string._000_0);
                strip2.setText(R.string._000_0);
                strip1.setTextColor(0xff000000);
                strip2.setTextColor(0xff000000);
            }
        }
    }

    public void stripClick(View view) {
        if (!binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
            numstr = 1;
            double ex = (double) time0 / 1000;
            String strips = binding.numstrips.getText().toString();
            int numstrips = Integer.parseInt(strips);
            String[] stops = getResources().getStringArray(R.array.stops);
            int k = Arrays.asList(stops).indexOf(binding.numstops.getText().toString());

            if (numstrips < 9) numstrips = numstrips + 1;
            else numstrips = 1;
            binding.numstrips.setText(String.valueOf(numstrips));
            stripsd = stripcount(ex, numstrips, stopsi[k]);
            strippaint(stripsd, numstrips);
            stripselect1(numstr);
        }
    }

    public void stopClick(View view) {
        String strips = binding.numstrips.getText().toString();
        int numstrips = Integer.parseInt(strips);
        String[] stops = getResources().getStringArray(R.array.stops);
        int k = Arrays.asList(stops).indexOf(binding.numstops.getText().toString());

        if (k < stops.length - 1) k = k + 1;
        else k = 0;
        binding.numstops.setText(stops[k]);

        if (!binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
            numstr = 1;
            double ex = (double) time0 / 1000;
            stripcount(ex, numstrips, stopsi[k]);
            stripsd = stripcount(ex, numstrips, stopsi[k]);
            strippaint(stripsd, numstrips);
            stripselect1(numstr);
        }
    }

    public void methodClick(View view) {
        if (!binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
            numstr = 1;
            double ex = (double) time0 / 1000;
            String strips = binding.numstrips.getText().toString();
            int numstrips = Integer.parseInt(strips);
            String[] stops = getResources().getStringArray(R.array.stops);
            int k = Arrays.asList(stops).indexOf(binding.numstops.getText().toString());
            String[] method = getResources().getStringArray(R.array.method);
            int kk = Arrays.asList(method).indexOf(binding.nmethod.getText().toString());

            if (kk < method.length - 1) kk = kk + 1;
            else kk = 0;
            binding.nmethod.setText(method[kk]);
            stripsd = stripcount(ex, numstrips, stopsi[k]);
            strippaint(stripsd, numstrips);
            stripselect1(numstr);
        }
    }

    public void stripselect(View view) {
        numstr = Integer.parseInt(view.getTag().toString());
        stripselect1(numstr);
    }

    public void stripselect1(int numstr) {
        String strips = binding.numstrips.getText().toString();
        int numstrips = Integer.parseInt(strips);
        TableLayout tblLayout = binding.tableStrips;
        TableRow row = (TableRow) tblLayout.getChildAt(numstr);
        TextView strip0 = (TextView) row.getChildAt(0);
        TextView strip1 = (TextView) row.getChildAt(1);
        TextView strip2 = (TextView) row.getChildAt(2);

        if (!strip1.getText().toString().isEmpty() && !strip1.getText().toString().equals(getString(R.string._000_0))) {
            binding.time.setText(strip1.getText());
            String expo = binding.time.getText().toString();
            int n = expo.length();
            int cent = Integer.parseInt(expo.substring(0, 1));
            int dece = Integer.parseInt(expo.substring(1, 2));
            int unid = Integer.parseInt(expo.substring(2, 3));
            int deci = Integer.parseInt(expo.substring(4, n));
            time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
            binding.baseTime.setText("");
        }

        if (!binding.nmode.getText().toString().equals(getResources().getString(R.string.timer)) && !strip1.getText().toString().equals(getString(R.string._000_0))) {
            row.setBackgroundColor(Color.parseColor("#33FF0000"));
            strip0.setTextColor(0xff000000);
            strip1.setTextColor(0xff000000);
            strip2.setTextColor(0xff000000);

            for (int k = 0; k < numstr; k++) {
                row = (TableRow) tblLayout.getChildAt(k);
                strip0 = (TextView) row.getChildAt(0);
                strip1 = (TextView) row.getChildAt(1);
                strip2 = (TextView) row.getChildAt(2);
                row.setBackgroundColor(0xff000000);
                strip0.setTextColor(Color.parseColor("#80FF0000"));
                strip1.setTextColor(Color.parseColor("#80FF0000"));
                strip2.setTextColor(Color.parseColor("#80FF0000"));
            }

            for (int k = numstr + 1; k <= numstrips + 1; k++) {
                row = (TableRow) tblLayout.getChildAt(k);
                strip0 = (TextView) row.getChildAt(0);
                strip1 = (TextView) row.getChildAt(1);
                strip2 = (TextView) row.getChildAt(2);
                row.setBackgroundColor(0xff000000);
                strip0.setTextColor(Color.parseColor("#80FF0000"));
                strip1.setTextColor(Color.parseColor("#80FF0000"));
                strip2.setTextColor(Color.parseColor("#80FF0000"));
            }

            for (int k = numstrips + 2; k < 11; k++) {
                row = (TableRow) tblLayout.getChildAt(k);
                strip0 = (TextView) row.getChildAt(0);
                strip1 = (TextView) row.getChildAt(1);
                strip2 = (TextView) row.getChildAt(2);
                row.setBackgroundColor(0xff000000);
                strip0.setTextColor(Color.parseColor("#80FF0000"));
                strip1.setTextColor(0xff000000);
                strip2.setTextColor(0xff000000);
            }
        }
    }

    public void stripselectA(View view) {
        numstr = Integer.parseInt(view.getTag().toString());
        stripselect1A(numstr);
    }

    public void stripselect1A(int numstr) {
        TableLayout tblLayout = binding.tableStrips;
        TableRow row = (TableRow) tblLayout.getChildAt(numstr);
        TextView strip2 = (TextView) row.getChildAt(2);

        if (binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
            if (!strip2.getText().toString().isEmpty() && !strip2.getText().toString().equals(getString(R.string._000_0))) {
                binding.time.setText(strip2.getText());
                String expo = binding.time.getText().toString();
                int n = expo.length();
                int cent = Integer.parseInt(expo.substring(0, 1));
                int dece = Integer.parseInt(expo.substring(1, 2));
                int unid = Integer.parseInt(expo.substring(2, 3));
                int deci = Integer.parseInt(expo.substring(4, n));
                time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
            }
        }
    }


    public boolean parTimer(int i) {
        TableLayout tblLayout = binding.tableStrips;
        TableRow row = (TableRow) tblLayout.getChildAt(i);
        TextView strip1 = (TextView) row.getChildAt(1);
        if (binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
            strip1.setText(binding.time.getText());
            if (!binding.time.getText().toString().equals(getString(R.string._000_0)))
                strip1.setTextColor(Color.parseColor("#80FF0000"));
            else strip1.setTextColor(0xff000000);
            return true;
        }
        return false;
    }

    public boolean acTimer(int i) {
        TableLayout tblLayout = binding.tableStrips;
        TableRow row = (TableRow) tblLayout.getChildAt(i);
        TextView strip2 = (TextView) row.getChildAt(2);
        if (binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
            strip2.setText(R.string._000_0);
            strip2.setTextColor(0xff000000);
            return true;
        }
        return false;
    }

    public boolean erase(){
        TableLayout tblLayout = binding.tableStrips;

        if (binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
           for(int i=1;i<11;i++) {
               TableRow row = (TableRow) tblLayout.getChildAt(i);
               TextView strip1 = (TextView) row.getChildAt(1);
               TextView strip2 = (TextView) row.getChildAt(2);
               strip1.setText(R.string._000_0);
               strip1.setTextColor(0xff000000);
               strip2.setText(R.string._000_0);
               strip2.setTextColor(0xff000000);
           }
            binding.baseTime.setText("");
            return true;
        }
        return false;
    }


    public void upTimer(View view) {
        String[] stops = getResources().getStringArray(R.array.stops);
        int k = Arrays.asList(stops).indexOf(binding.numstops.getText().toString());

        String expo = binding.time.getText().toString();
        int n = expo.length();
        int cent = Integer.parseInt(expo.substring(0, 1));
        int dece = Integer.parseInt(expo.substring(1, 2));
        int unid = Integer.parseInt(expo.substring(2, 3));
        int deci = Integer.parseInt(expo.substring(4, n));

        double ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
        binding.baseTime.setText(String.format(Locale.US, "%05.1f", (double) time0 / 1000));

        double incr = (double) 1 / stopsi[k];
        ex = ex * Math.pow(2, incr);
        binding.time.setText(String.format(Locale.US, "%05.1f", ex));
    }

    public void dowTimer(View view) {
        String[] stops = getResources().getStringArray(R.array.stops);
        int k = Arrays.asList(stops).indexOf(binding.numstops.getText().toString());


        if (!binding.time.getText().toString().equals(getString(R.string._000_0))) {
            String expo = binding.time.getText().toString();
            int n = expo.length();
            int cent = Integer.parseInt(expo.substring(0, 1));
            int dece = Integer.parseInt(expo.substring(1, 2));
            int unid = Integer.parseInt(expo.substring(2, 3));
            int deci = Integer.parseInt(expo.substring(4, n));

            double ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
            binding.baseTime.setText(String.format(Locale.US, "%05.1f", (double) time0 / 1000));

            double incr = (double) 1 / stopsi[k];
            double ex1 = ex / Math.pow(2, incr);
            BigDecimal bdex = new BigDecimal(ex).setScale(1, RoundingMode.HALF_UP);
            BigDecimal bdex1 = new BigDecimal(ex1).setScale(1, RoundingMode.HALF_UP);
            if (bdex.compareTo(bdex1) != 0)
                binding.time.setText(String.format(Locale.US, "%05.1f", ex1));
            else binding.time.setText(R.string._000_0);
        }
    }

    public void modeClick(View view) {
        String expo = binding.time.getText().toString();
        int n = expo.length();
        String strips = binding.numstrips.getText().toString();
        int numstrips = Integer.parseInt(strips);
        String[] stops = getResources().getStringArray(R.array.stops);
        int k = Arrays.asList(stops).indexOf(binding.numstops.getText().toString());
        TableLayout tblLayout = binding.tableStrips;

        if (binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {

            for (int i = 0; i < 10; i++) {
                TableRow row = (TableRow) tblLayout.getChildAt(i + 1);
                TextView strip1 = (TextView) row.getChildAt(1);
                TextView strip2 = (TextView) row.getChildAt(2);
                memo[i] = strip1.getText().toString();
                strip2.setClickable(false);
                strip2.setLongClickable(false);
            }

            memo[10]=binding.ndelay.getText().toString();
            binding.ndelay.setText(memo2);

            numstr = 1;
            int cent = Integer.parseInt(expo.substring(0, 1));
            int dece = Integer.parseInt(expo.substring(1, 2));
            int unid = Integer.parseInt(expo.substring(2, 3));
            int deci = Integer.parseInt(expo.substring(4, n));

            double ex = cent * 100 + dece * 10 + unid + (double) deci / 10;

            binding.nmode.setText(getResources().getString(R.string.strip));
            binding.stTotal.setText(R.string.total);
            binding.stEach.setText(R.string.each);

            binding.upb.setVisibility(View.INVISIBLE);
            binding.upb.setEnabled(false);
            binding.downb.setVisibility(View.INVISIBLE);
            binding.downb.setEnabled(false);
            binding.baseTime.setVisibility(View.INVISIBLE);
            binding.baseTime.setEnabled(false);
            binding.baseTime.setText("");
            binding.method.setEnabled(true);
            binding.nmethod.setEnabled(true);
            binding.method.setTextColor(Color.parseColor("#809B9B9B"));
            binding.nmethod.setTextColor(Color.parseColor("#80FF0000"));
            binding.nmethod.setBackgroundResource(R.drawable.shape);


            stripsd = stripcount(ex, numstrips, stopsi[k]);
            strippaint(stripsd, numstrips);
            stripselect1(numstr);

        } else {
            binding.nmode.setText(getResources().getString(R.string.timer));
            binding.stTotal.setText(R.string.strip);
            binding.stEach.setText(R.string.memo);
            binding.upb.setVisibility(View.VISIBLE);
            binding.upb.setEnabled(true) ;
            binding.downb.setVisibility(View.VISIBLE);
            binding.downb.setEnabled(true);
            binding.baseTime.setVisibility(View.VISIBLE);
            binding.baseTime.setEnabled(true);

            for (int i = 1; i < 11; i++) {
                TableRow row = (TableRow) tblLayout.getChildAt(i);
                TextView strip0 = (TextView) row.getChildAt(0);
                TextView strip1 = (TextView) row.getChildAt(1);
                TextView strip2 = (TextView) row.getChildAt(2);
                row.setBackgroundColor(0xff000000);
                strip0.setTextColor(Color.parseColor("#80FF0000"));
                strip1.setText(memo[i - 1]);
                strip2.setClickable(true);
                strip2.setLongClickable(true);
                if (strip1.getText().toString().equals(getResources().getString(R.string._000_0))) {
                    strip1.setTextColor(0xff000000);
                } else strip1.setTextColor(Color.parseColor("#80FF0000"));
                if (!strip2.getText().toString().equals(getResources().getString(R.string._000_0))) {
                    strip2.setTextColor(Color.parseColor("#80FF0000"));
                    if(i == numstr) {
                        binding.time.setText(strip2.getText());
                        expo= binding.time.getText().toString();
                        n = expo.length();
                        int cent = Integer.parseInt(expo.substring(0, 1));
                        int dece = Integer.parseInt(expo.substring(1, 2));
                        int unid = Integer.parseInt(expo.substring(2, 3));
                        int deci = Integer.parseInt(expo.substring(4, n));
                        time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                    }
                } else {
                    strip2.setTextColor(0xff000000);
                    strip2.setText(R.string._000_0);
                }
            }
            memo2=binding.ndelay.getText().toString();
            binding.ndelay.setText(memo[10]);
            binding.method.setEnabled(false);
            binding.nmethod.setEnabled(false);
            binding.method.setTextColor(0xff000000);
            binding.nmethod.setTextColor(0xff000000);
            binding.nmethod.setBackgroundColor(0xff000000);
        }
    }

    public void startimer2(String st, int delay) {
        int numstrips = Integer.parseInt(binding.numstrips.getText().toString());
        long time1 = (long) (stripsd[0] * 1000);
        stripselect1(1);
        binding.expo.setText(st);
        binding.focus.setEnabled(false);
        binding.reset.setEnabled(false);

        timer10 = new CountDownTimer((long) ((stripsd[9] - stripsd[8]) * 1000), 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();
                stripselect1(1);
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time1) / 1000));
                binding.expo.setText(getString(R.string.button));
                binding.expo.setEnabled(true);
                binding.focus.setEnabled(true);
                binding.reset.setEnabled(true);
            }
        };

        timer9 = new CountDownTimer((long) ((stripsd[8] - stripsd[7]) * 1000), 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();

                if (10 <= numstrips + 1) {
                    stripselect1(10);
                    new Handler().postDelayed(() -> {
                        paquete();
                        timer10.start();
                    }, delay);
                } else {
                    stripselect1(1);
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time1) / 1000));
                    binding.expo.setText(getString(R.string.button));
                    binding.expo.setEnabled(true);
                    binding.focus.setEnabled(true);
                    binding.reset.setEnabled(true);
                }
            }
        };

        timer8 = new CountDownTimer((long) ((stripsd[7] - stripsd[6]) * 1000), 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();

                if (9 <= numstrips + 1) {
                    stripselect1(9);
                    new Handler().postDelayed(() -> {
                        paquete();
                        timer9.start();
                    }, delay);
                } else {
                    stripselect1(1);
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time1) / 1000));
                    binding.expo.setText(getString(R.string.button));
                    binding.expo.setEnabled(true);
                    binding.focus.setEnabled(true);
                    binding.reset.setEnabled(true);
                }
            }
        };

        timer7 = new CountDownTimer((long) ((stripsd[6] - stripsd[5]) * 1000), 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();

                if (8 <= numstrips + 1) {
                    stripselect1(8);
                    new Handler().postDelayed(() -> {
                        paquete();
                        timer8.start();
                    }, delay);
                } else {
                    stripselect1(1);
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time1) / 1000));
                    binding.expo.setText(getString(R.string.button));
                    binding.expo.setEnabled(true);
                    binding.focus.setEnabled(true);
                    binding.reset.setEnabled(true);
                }
            }
        };

        timer6 = new CountDownTimer((long) ((stripsd[5] - stripsd[4]) * 1000), 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();

                if (7 <= numstrips + 1) {
                    stripselect1(7);
                    new Handler().postDelayed(() -> {
                        paquete();
                        timer7.start();
                    }, delay);
                } else {
                    stripselect1(1);
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time1) / 1000));
                    binding.expo.setText(getString(R.string.button));
                    binding.expo.setEnabled(true);
                    binding.focus.setEnabled(true);
                    binding.reset.setEnabled(true);
                }
            }
        };

        timer5 = new CountDownTimer((long) ((stripsd[4] - stripsd[3]) * 1000), 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();

                if (6 <= numstrips + 1) {
                    stripselect1(6);
                    new Handler().postDelayed(() -> {
                        paquete();
                        timer6.start();
                    }, delay);
                } else {
                    stripselect1(1);
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time1) / 1000));
                    binding.expo.setText(getString(R.string.button));
                    binding.expo.setEnabled(true);
                    binding.focus.setEnabled(true);
                    binding.reset.setEnabled(true);
                }
            }
        };


        timer4 = new CountDownTimer((long) ((stripsd[3] - stripsd[2]) * 1000), 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();

                if (5 <= numstrips + 1) {
                    stripselect1(5);
                    new Handler().postDelayed(() -> {
                        paquete();
                        timer5.start();
                    }, delay);
                } else {
                    stripselect1(1);
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time1) / 1000));
                    binding.expo.setText(getString(R.string.button));
                    binding.expo.setEnabled(true);
                    binding.focus.setEnabled(true);
                    binding.reset.setEnabled(true);
                }
            }
        };

        timer3 = new CountDownTimer((long) ((stripsd[2] - stripsd[1]) * 1000), 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();
                if (4 <= numstrips + 1) {
                    stripselect1(4);
                    new Handler().postDelayed(() -> {
                        paquete();
                        timer4.start();
                    }, delay);
                } else {
                    stripselect1(1);
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time1) / 1000));
                    binding.expo.setText(getString(R.string.button));
                    binding.expo.setEnabled(true);
                    binding.focus.setEnabled(true);
                    binding.reset.setEnabled(true);
                }
            }
        };

        timer2 = new CountDownTimer((long) ((stripsd[1] - stripsd[0]) * 1000), 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();
                if (3 <= numstrips + 1) {
                    stripselect1(3);
                    new Handler().postDelayed(() -> {
                        paquete();
                        timer3.start();
                    }, delay);
                } else {
                    stripselect1(1);
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time1) / 1000));
                    binding.expo.setText(getString(R.string.button));
                    binding.expo.setEnabled(true);
                    binding.focus.setEnabled(true);
                    binding.reset.setEnabled(true);
                }
            }
        };

        timer = new CountDownTimer((long) stripsd[0] * 1000, 100) {
            int n;
            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n=n+1;
                if (n==10) {
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_4,50);
                    n=0;
                }
            }

            public void onFinish() {
                paquete();
                if (2 <= numstrips + 1) {
                    stripselect1(2);
                    new Handler().postDelayed(() -> {
                        paquete();
                        timer2.start();
                    }, delay);
                } else {
                    stripselect1(1);
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time1) / 1000));
                    binding.expo.setText(getString(R.string.button));
                    binding.expo.setEnabled(true);
                    binding.focus.setEnabled(true);
                    binding.reset.setEnabled(true);
                }
            }
        };

        new Handler().postDelayed(() -> {
            paquete();
            timer.start();
        }, delay);

    }
}