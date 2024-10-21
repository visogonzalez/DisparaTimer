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
import android.util.TypedValue;
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
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ebookfrenzy.disparafilmtimer.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public long time0;
    public long timei;
    public int numstr = 1;
    int[] stopsi = {1, 2, 3, 4, 6, 8};
    int paso = 4;
    int incre;
    double[] stripsd;
    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 75);
    String[] memo = new String[11];
    String memo2 = "750";
    String memo3 = "0";
    CountDownTimer timer;
    CountDownTimer[] timers = new CountDownTimer[10];

    public Boolean Focus = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full screen without navigation buttons
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

        //Tex size and layout rescaling
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        double factor = Math.sqrt(Math.pow(metrics.heightPixels / metrics.ydpi, 2) + Math.pow(metrics.widthPixels / metrics.xdpi, 2)) / 5.742;

        binding.time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (150.0 * factor));
        binding.mode.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.nmode.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (16.0 * factor));
        binding.strips.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.numstrips.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.stops.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.numstops.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.method.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.nmethod.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.delay.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.ndelay.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.unit.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.upb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.downb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.baseTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.expo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (20.0 * factor));
        binding.focus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (16.0 * factor));
        binding.reset.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (16.0 * factor));

        ViewGroup.LayoutParams params = binding.expo.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        params = binding.reset.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        params = binding.focus.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);

        params = binding.mode.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) binding.mode.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);

        params = binding.nmode.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        params2 = (ViewGroup.MarginLayoutParams) binding.nmode.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);

        params2 = (ViewGroup.MarginLayoutParams) binding.tableStrips.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);

        params = binding.numstrips.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        params2 = (ViewGroup.MarginLayoutParams) binding.numstrips.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);


        params = binding.strips.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        params2 = (ViewGroup.MarginLayoutParams) binding.strips.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);

        params = binding.stops.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        params2 = (ViewGroup.MarginLayoutParams) binding.stops.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);

        params = binding.numstops.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        params2 = (ViewGroup.MarginLayoutParams) binding.numstops.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);

        params = binding.delay.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        params2 = (ViewGroup.MarginLayoutParams) binding.delay.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);

        params = binding.ndelay.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);


        params = binding.method.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        params2 = (ViewGroup.MarginLayoutParams) binding.method.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);

        params = binding.nmethod.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);

        params2 = (ViewGroup.MarginLayoutParams) binding.baseTime.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);

        params = binding.upb.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);
        params2 = (ViewGroup.MarginLayoutParams) binding.upb.getLayoutParams();
        params2.topMargin = (int) (params2.topMargin * factor);

        params = binding.downb.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);

        params = binding.unit.getLayoutParams();
        params.height = (int) (params.height * factor);
        params.width = (int) (params.width * factor);

        TableLayout tblLayout = binding.tableStrips;

        for (int i = 0; i < 11; i++) {
            TableRow row = (TableRow) tblLayout.getChildAt(i);
            params = row.getChildAt(0).getLayoutParams();
            params.height = (int) (params.height * factor);
            params.width = (int) (params.width * factor);
            params = row.getChildAt(1).getLayoutParams();
            params.height = (int) (params.height * factor);
            params.width = (int) (params.width * factor);
            params = row.getChildAt(2).getLayoutParams();
            params.height = (int) (params.height * factor);
            params.width = (int) (params.width * factor);
            TextView strip0 = (TextView) row.getChildAt(0);
            TextView strip1 = (TextView) row.getChildAt(1);
            TextView strip2 = (TextView) row.getChildAt(2);

            if (i == 0) {
                strip0.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (24.0 * factor));
                strip1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (24.0 * factor));
                strip2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (24.0 * factor));
            } else {
                strip0.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (34.0 * factor));
                strip1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (34.0 * factor));
                strip2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (34.0 * factor));
            }
        }

        //ndelay cursor visible on input
        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            view.getWindowVisibleDisplayFrame(r);
            int heightDiff = view.getRootView().getHeight() - (r.bottom - r.top);
            if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                if (binding.ndelay != null) {
                    binding.ndelay.setCursorVisible(true);
                }
            } else {
                if (binding.ndelay != null) {
                    binding.ndelay.setCursorVisible(false);
                }
            }
        });

        binding.reset.setOnLongClickListener(v -> erase());

        binding.expo.setOnClickListener(v -> {
            String dela = binding.ndelay.getText().toString();
            if (dela.isEmpty()) dela = "0";
            int delay = Integer.parseInt(dela);

            if (!binding.time.getText().toString().equals(getString(R.string._000_0))) {

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
                } else if (binding.nmethod.getText().toString().equals("AUTO") && !binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {

                    String ini = binding.time.getText().toString();
                    double ex1 = Double.parseDouble(ini) * 1000;
                    long ex = (int) ex1;
                    if (binding.expo.getText().equals(getString(R.string.button)) && !binding.time.getText().toString().equals(getString(R.string._000_0))) {
                        startimer2(ex, getString(R.string.button1), delay);
                    } else if (binding.expo.getText().equals(getString(R.string.button1))) {
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 50);
                        timers[numstr - 1].cancel();
                        paquete();
                        binding.expo.setText(getString(R.string.button2));
                        binding.reset.setEnabled(true);
                    } else if (binding.expo.getText().equals(getString(R.string.button2))) {
                        startimer2(ex, getString(R.string.button1), delay);
                    }
                }
            }
        });
    }

    void starTimer(long ex, String st, int delay) {
        toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 50);
        binding.expo.setText(R.string.wait);
        timer = new CountDownTimer(ex, 100) {
            int n;
            long m;

            public void onTick(long millisUntilFinished) {
                binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                n = n + 1;
                m = m + 100;
                if (n == 10 && m < ex && binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
                    toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP, 50);
                    n = 0;
                }
            }

            public void onFinish() {
                paquete();
                binding.expo.setText(getString(R.string.button));
                if (incre == 0)
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) time0) / 1000));
                else
                    binding.time.setText(String.format(Locale.US, "%05.1f", ((double) timei) / 1000));
                binding.focus.setEnabled(true);
                binding.reset.setEnabled(true);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 50);
                int numstrips = Integer.parseInt(binding.numstrips.getText().toString());
                if (!binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
                    if (numstr < numstrips) {
                        numstr = numstr + 1;
                        stripselect1(numstr);
                    } else {
                        numstr = 1;
                        stripselect1(numstr);
                    }
                }
            }
        };

        new Handler().postDelayed(() -> {
            binding.expo.setText(R.string.button1);
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
            double factor = Math.sqrt(Math.pow(metrics.heightPixels / metrics.ydpi, 2) + Math.pow(metrics.widthPixels / metrics.xdpi, 2)) / 5.742;

            Rect bounds = new Rect();
            binding.time.getLineBounds(0, bounds);
            int ce = bounds.centerX();
            int ceY = bounds.centerY();
            int bot = bounds.bottom;

            if (action == MotionEvent.ACTION_DOWN) {
                if (x > ce - (460 * factor * metrics.densityDpi / 440) && x < ce - (360 * factor * metrics.densityDpi / 440)) {
                    if (y <= bot && y >= ceY) {
                        if (cent >= 1) cent = cent - 1;
                        else cent = 9;
                    } else if (y < ceY) {
                        if (cent < 9) cent = cent + 1;
                        else cent = 0;
                    }
                    ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
                    time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                }
                if (x > ce - (265 * factor * metrics.densityDpi / 440) && x < ce - (95 * factor * metrics.densityDpi / 440)) {
                    if (y <= bot && y >= ceY) {
                        if (dece >= 1) dece = dece - 1;
                        else dece = 9;
                    } else if (y < ceY) {
                        if (dece < 9) dece = dece + 1;
                        else dece = 0;
                    }
                    ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
                    time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                }
                if (x > ce && x < ce + (100 * factor * metrics.densityDpi / 440)) {
                    if (y <= bot && y >= ceY) {
                        if (unid >= 1) unid = unid - 1;
                        else unid = 9;
                    } else if (y < ceY) {
                        if (unid < 9) unid = unid + 1;
                        else unid = 0;
                    }
                    ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
                    time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                }
                if (x > ce + (290 * factor * metrics.densityDpi / 440) && x < ce + (390 * factor * metrics.densityDpi / 440)) {
                    if (y <= bot && y >= ceY) {
                        if (deci >= 1) deci = deci - 1;
                        else deci = 9;
                    } else if (y < ceY) {
                        if (deci < 9) deci = deci + 1;
                        else deci = 0;
                    }
                    ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
                    time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                }

            }

            binding.time.setText(String.format(Locale.US, "%05.1f", ex));
            if (!binding.nmode.getText().toString().equals(getResources().getString(R.string.timer)) && x > ce - (460 * factor * metrics.densityDpi / 440) && y <= bot) {
                numstr = 1;
                stripsd = stripcount(ex, numstrips, stopsi[k]);
                strippaint(stripsd, numstrips);
                stripselect1(numstr);
            } else {
                binding.baseTime.setText("");
                incre = 0;
            }

            //binding.textView.setText("x = " + x + "   y = " + y);
        }
    }

    public void focus(View view) {
        toneGen1.startTone(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 50);
        if (!Focus) {
            paquete();
            Focus = true;
            binding.focus.setTextColor(Color.parseColor("#80FF0000"));
            binding.expo.setEnabled(false);
            binding.expo.setTextColor(0xff000000);
            binding.reset.setEnabled(false);
            binding.reset.setTextColor(0xff000000);
        } else {
            paquete();
            Focus = false;
            binding.focus.setTextColor(0xff000000);
            binding.expo.setEnabled(true);
            binding.expo.setTextColor(Color.parseColor("#80FF0000"));
            binding.reset.setEnabled(true);
            binding.reset.setTextColor(Color.parseColor("#80FF0000"));
        }
    }

    public void reset(View view) {
        toneGen1.startTone(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 50);
        double ex;
        if (binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
            if (incre == 0) ex = (double) time0 / 1000;
            else ex = (double) timei / 1000;
            binding.time.setText(String.format(Locale.US, "%05.1f", ex));
            binding.expo.setText(getString(R.string.button));
            binding.focus.setEnabled(true);
        } else {
            stripselect1(1);
            binding.expo.setText(getString(R.string.button));
            binding.focus.setEnabled(true);
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
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public double[] stripcount(double ex, int k, int kk) {
        double[] stripsd = new double[10];
        for (int i = 0; i <= k - 1; i++) {
            double expo = (double) i / kk;
            stripsd[i] = ex * Math.pow(2, expo);
        }
        for (int i = k; i < 10; i++) {
            stripsd[i] = 0.0;
        }
        return stripsd;
    }

    public void strippaint(double[] stripsd, int numstrips) {
        TableLayout tblLayout = binding.tableStrips;
        String method = binding.nmethod.getText().toString();
        if (!method.equals(getResources().getString(R.string.single))) {
            for (int i = 1; i <= numstrips; i++) {
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
            for (int i = numstrips + 1; i < 11; i++) {
                TableRow row = (TableRow) tblLayout.getChildAt(i);
                TextView strip1 = (TextView) row.getChildAt(1);
                TextView strip2 = (TextView) row.getChildAt(2);
                strip1.setText(getString(R.string._000_0));
                strip2.setText(getString(R.string._000_0));
                strip1.setTextColor(0xff000000);
                strip2.setTextColor(0xff000000);
            }
        } else {
            for (int i = 1; i <= numstrips; i++) {
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
            for (int i = numstrips + 1; i < 11; i++) {
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

            if (numstrips < 10) numstrips = numstrips + 1;
            else numstrips = 2;
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

            if (kk == 0) memo2 = binding.ndelay.getText().toString();
            else memo3 = binding.ndelay.getText().toString();

            if (kk < method.length - 1) kk = kk + 1;
            else kk = 0;
            binding.nmethod.setText(method[kk]);
            if (kk == 0) binding.ndelay.setText(memo2);
            else binding.ndelay.setText(memo3);
            stripsd = stripcount(ex, numstrips, stopsi[k]);
            strippaint(stripsd, numstrips);
            stripselect1(numstr);
        }
    }

    public void stripselect(View view) {
        numstr = Integer.parseInt(view.getTag().toString());
        stripselect1(numstr);
    }

    public void stripselect1(int numstri) {
        String strips = binding.numstrips.getText().toString();
        int numstrips = Integer.parseInt(strips);
        TableLayout tblLayout = binding.tableStrips;
        TableRow row = (TableRow) tblLayout.getChildAt(numstri);
        TextView strip0 = (TextView) row.getChildAt(0);
        TextView strip1 = (TextView) row.getChildAt(1);
        TextView strip2 = (TextView) row.getChildAt(2);
        numstr = numstri;

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
            incre = 0;
        }

        if (!binding.nmode.getText().toString().equals(getResources().getString(R.string.timer)) && !strip1.getText().toString().equals(getString(R.string._000_0))) {
            row.setBackgroundColor(Color.parseColor("#33FF0000"));
            strip0.setTextColor(0xff000000);
            strip1.setTextColor(0xff000000);
            strip2.setTextColor(0xff000000);

            for (int k = 0; k < numstri; k++) {
                row = (TableRow) tblLayout.getChildAt(k);
                strip0 = (TextView) row.getChildAt(0);
                strip1 = (TextView) row.getChildAt(1);
                strip2 = (TextView) row.getChildAt(2);
                row.setBackgroundColor(0xff000000);
                strip0.setTextColor(Color.parseColor("#80FF0000"));
                strip1.setTextColor(Color.parseColor("#80FF0000"));
                strip2.setTextColor(Color.parseColor("#80FF0000"));
            }

            for (int k = numstri + 1; k <= numstrips; k++) {
                row = (TableRow) tblLayout.getChildAt(k);
                strip0 = (TextView) row.getChildAt(0);
                strip1 = (TextView) row.getChildAt(1);
                strip2 = (TextView) row.getChildAt(2);
                row.setBackgroundColor(0xff000000);
                strip0.setTextColor(Color.parseColor("#80FF0000"));
                strip1.setTextColor(Color.parseColor("#80FF0000"));
                strip2.setTextColor(Color.parseColor("#80FF0000"));
            }

            for (int k = numstrips + 1; k < 11; k++) {
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
                binding.baseTime.setText("");
                incre = 0;
            }
        }
    }


    public boolean parTimer(int i) {
        TableLayout tblLayout = binding.tableStrips;
        TableRow row = (TableRow) tblLayout.getChildAt(i);
        TextView strip1 = (TextView) row.getChildAt(1);
        if (binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
            if (binding.time.getText().toString().equals(strip1.getText().toString())) {
                strip1.setText(R.string._000_0);
                strip1.setTextColor(0xff000000);
            } else if (!binding.time.getText().toString().equals(getString(R.string._000_0))) {
                strip1.setTextColor(Color.parseColor("#80FF0000"));
                strip1.setText(binding.time.getText());
            } else {
                strip1.setTextColor(0xff000000);
                strip1.setText(binding.time.getText());
            }
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

    public boolean erase() {
        TableLayout tblLayout = binding.tableStrips;

        if (binding.nmode.getText().toString().equals(getResources().getString(R.string.timer))) {
            for (int i = 1; i < 11; i++) {
                TableRow row = (TableRow) tblLayout.getChildAt(i);
                TextView strip1 = (TextView) row.getChildAt(1);
                TextView strip2 = (TextView) row.getChildAt(2);
                strip1.setText(R.string._000_0);
                strip1.setTextColor(0xff000000);
                strip2.setText(R.string._000_0);
                strip2.setTextColor(0xff000000);
            }
            binding.baseTime.setText("");
            incre = 0;
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
        String increme = "";

        double ex;

        if (!binding.time.getText().toString().equals(getString(R.string._000_0))) {
            if (k != paso) {
                time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                paso = k;
                incre = 0;
                timei=0;
            }
            incre = incre + 1;

            if (timei == 0) ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
            else ex = (double) timei / 1000;

            if (incre > 0) increme = "+" + incre + "/" + stopsi[k];
            else if (incre < 0) increme = incre + "/" + stopsi[k];

            binding.baseTime.setText(String.format(Locale.US, "%05.1f", (double) time0 / 1000) + " " + increme);


            double incr = (double) 1 / stopsi[k];
            ex = ex * Math.pow(2, incr);
            timei = (long) ((Math.round(ex * 1000d) / 1000d) * 1000);
            binding.time.setText(String.format(Locale.US, "%05.1f", (Math.round(ex * 10d) / 10d)));
        }
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
            String increme = "";

            double ex;

             if (k != paso) {
                time0 = cent * 100000L + dece * 10000L + unid * 1000L + deci * 100L;
                paso = k;
                incre = 0;
                timei=0;
            }
            incre = incre - 1;

            if (timei == 0) ex = cent * 100 + dece * 10 + unid + (double) deci / 10;
            else ex = (double) timei / 1000;

            if (incre > 0) increme = "+" + incre + "/" + stopsi[k];
            else if (incre < 0) increme = incre + "/" + stopsi[k];

            binding.baseTime.setText(String.format(Locale.US, "%05.1f", (double) time0 / 1000) + " " + increme);


            double incr = (double) 1 / stopsi[k];
            double ex1 = ex / Math.pow(2, incr);
            BigDecimal bdex = new BigDecimal(ex).setScale(1, RoundingMode.HALF_UP);
            BigDecimal bdex1 = new BigDecimal(ex1).setScale(1, RoundingMode.HALF_UP);
            if (bdex.compareTo(bdex1) != 0) {
                binding.time.setText(String.format(Locale.US, "%05.1f", (Math.round(ex1 * 10d) / 10d)));
                timei = (long) ((Math.round(ex1 * 1000d) / 1000d) * 1000);
            } else {
                binding.time.setText(R.string._000_0);
                timei = 0;
            }
        }
    }

    public void modeClick(View view) {
        String expo = binding.time.getText().toString();
        int n = expo.length();
        String strips = binding.numstrips.getText().toString();
        int numstrips = Integer.parseInt(strips);
        String[] stops = getResources().getStringArray(R.array.stops);
        String[] method = getResources().getStringArray(R.array.method);
        int kk = Arrays.asList(method).indexOf(binding.nmethod.getText().toString());
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

            memo[10] = binding.ndelay.getText().toString();
            if (kk == 0) binding.ndelay.setText(memo2);
            else binding.ndelay.setText(memo3);

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
            incre = 0;
            binding.method.setEnabled(true);
            binding.nmethod.setEnabled(true);
            binding.method.setTextColor(Color.parseColor("#809B9B9B"));
            binding.nmethod.setTextColor(Color.parseColor("#80FF0000"));
            binding.nmethod.setBackgroundResource(R.drawable.shape);

            binding.numstrips.setEnabled(true);
            binding.strips.setEnabled(true);
            binding.strips.setTextColor(Color.parseColor("#809B9B9B"));
            binding.numstrips.setTextColor(Color.parseColor("#80FF0000"));
            binding.numstrips.setBackgroundResource(R.drawable.shape);


            stripsd = stripcount(ex, numstrips, stopsi[k]);
            strippaint(stripsd, numstrips);
            stripselect1(numstr);

        } else {
            binding.nmode.setText(getResources().getString(R.string.timer));
            binding.stTotal.setText(R.string.strip);
            binding.stEach.setText(R.string.memo);
            binding.upb.setVisibility(View.VISIBLE);
            binding.upb.setEnabled(true);
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
                    if (i == numstr) {
                        binding.time.setText(strip2.getText());
                        expo = binding.time.getText().toString();
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

            if (kk == 0) memo2 = binding.ndelay.getText().toString();
            else memo3 = binding.ndelay.getText().toString();

            memo2 = binding.ndelay.getText().toString();
            binding.ndelay.setText(memo[10]);
            binding.method.setEnabled(false);
            binding.nmethod.setEnabled(false);
            binding.method.setTextColor(0xff000000);
            binding.nmethod.setTextColor(0xff000000);
            binding.nmethod.setBackgroundColor(0xff000000);
            binding.numstrips.setEnabled(false);
            binding.strips.setEnabled(false);
            binding.strips.setTextColor(0xff000000);
            binding.numstrips.setTextColor(0xff000000);
            binding.numstrips.setBackgroundColor(0xff000000);

        }
    }

    public void startimer2(long ex, String st, int delay) {
        int numstrips = Integer.parseInt(binding.numstrips.getText().toString());
        toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 50);
        binding.expo.setText(R.string.wait);
        binding.focus.setEnabled(false);
        binding.reset.setEnabled(false);

        for (int i = numstrips - 1; i >= numstr - 1; i--) {
            String ii = String.valueOf(i);

            if (i == numstr - 1) {
                timers[0] = new CountDownTimer(ex, 100) {
                    public void onTick(long millisUntilFinished) {
                        binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                    }

                    public void onFinish() {
                        paquete();
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 50);
                        if (Integer.parseInt(ii) + 1 <= numstrips - 1) {
                            binding.expo.setText(R.string.wait);
                            stripselect1(Integer.parseInt(ii) + 2);
                            new Handler().postDelayed(() -> {
                                binding.expo.setText(getString(R.string.button1));
                                paquete();
                                timers[Integer.parseInt(ii) + 1].start();
                            }, delay);
                        } else {
                            stripselect1(1);
                            binding.expo.setText(getString(R.string.button));
                            binding.expo.setEnabled(true);
                            binding.focus.setEnabled(true);
                            binding.reset.setEnabled(true);
                        }
                    }
                };
                new Handler().postDelayed(() -> {
                    paquete();
                    binding.expo.setText(st);
                    timers[0].start();
                }, delay);
            } else {
                timers[i] = new CountDownTimer((long) ((stripsd[i] - stripsd[i - 1]) * 1000), 100) {
                    public void onTick(long millisUntilFinished) {
                        binding.time.setText(String.format(Locale.US, "%05.1f", ((double) millisUntilFinished) / 1000));
                    }

                    public void onFinish() {
                        paquete();
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 50);
                        if (Integer.parseInt(ii) + 1 <= numstrips - 1) {
                            binding.expo.setText(R.string.wait);
                            stripselect1(Integer.parseInt(ii) + 2);
                            new Handler().postDelayed(() -> {
                                binding.expo.setText(getString(R.string.button1));
                                paquete();
                                binding.expo.setText(st);
                                timers[Integer.parseInt(ii) + 1].start();
                            }, delay);
                        } else {
                            stripselect1(1);
                            binding.expo.setText(getString(R.string.button));
                            binding.expo.setEnabled(true);
                            binding.focus.setEnabled(true);
                            binding.reset.setEnabled(true);
                        }
                    }
                };
            }
        }
    }
}