package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import edu.cmu.pocketsphinx.SpeechRecognizer;

//import android.speech.RecognitionListener;
//import android.speech.SpeechRecognizer;

public class MainActivity extends Activity implements ColorPickerDialog.OnColorChangedListener {

    TextView tv_nowrgb;
    public static int nowR = 0;
    public static int nowG = 0;
    public static int nowB = 0;
    public static boolean islighton = false;

    public static boolean firstweather = true;
    public static boolean isorder = false;
    public static boolean isspeak = false;
    public static boolean isevent = false;
    public static String phonelocation = "default";
    boolean firstplay = true;
    public static int mode = 0;
    public static String category = "main";
    public static String dialstate = "default";
    public static String nowcustom = "default";
    public static String nowappname = "default";
    public static String nowweather = "default";

    public static int selectedapp = 0;
    public static String[] spl;
    public Context maincontext = this;

    public static VideoView vedioView;
    ImageButton btn_setting;
    //Button btn_db;
    Button btn_weather;
    Button btn_clock;
    Button btn_bt;
    Button btn_admin;
    Button btn_spam;
    Button btn_lighton;
    Button btn_lightoff;
    public static Button btn_color_change;
    ImageButton btn_color_changer;

    public static LinearLayout main;
    SharedPreferencesMethod spm = new SharedPreferencesMethod();
    ScreenLockMethod slm = new ScreenLockMethod();
    MethodCollection mc = new MethodCollection();
    TtsMethod tm = new TtsMethod();

    public static DBManager dbManager;
    public static DBManager_PhoneBook dbManager_phoneBook;

    public static ArrayList<String> mainList = new ArrayList<String>();
    public static ArrayList<String> mainSpamList = new ArrayList<String>();

    Thread thread;
    Thread threadbt;
    Thread threadvoice;
    Thread threadcount;
    ////////////////////////////////BT//////////////////////////////////////
    // 사용자 정의 함수로 블루투스 활성 상태의 변경 결과를 App으로 알려줄때 식별자로 사용됨 (0보다 커야함)
    static final int REQUEST_ENABLE_BT = 10;
    int mPariedDeviceCount = 0;
    Set<BluetoothDevice> mDevices;
    // 폰의 블루투스 모듈을 사용하기 위한 오브젝트.
    BluetoothAdapter mBluetoothAdapter;
    /**
     * BluetoothDevice 로 기기의 장치정보를 알아낼 수 있는 자세한 메소드 및 상태값을 알아낼 수 있다.
     * 연결하고자 하는 다른 블루투스 기기의 이름, 주소, 연결 상태 등의 정보를 조회할 수 있는 클래스.
     * 현재 기기가 아닌 다른 블루투스 기기와의 연결 및 정보를 알아낼 때 사용.
     */
    BluetoothDevice mRemoteDevie;
    // 스마트폰과 페어링 된 디바이스간 통신 채널에 대응 하는 BluetoothSocket
    public static BluetoothSocket mSocket = null;
    public static OutputStream mOutputStream = null;
    public static InputStream mInputStream = null;
    public static String mStrDelimiter = "\n";
    char mCharDelimiter = '\n';

    byte[] readBuffer;
    int readBufferPosition;
    ////////////////////////////////////////////////////////////////////////
    String state = "default";
    Intent intent_voice;
    SpeechRecognizer mRecognizer;
    //boolean voiceagain = true;
    TextView tv_voice;
    public static boolean readyvoice = false;
    public static int countvoice = 0;

    ////////////////////////////키워드/////////////////////////////////////
    private static final String KWS_SEARCH = "wakeup";
    //private static final String KEYPHRASE = "helloo hollip";
    //private static final String KEYPHRASE = "hello(2)";
    private static final String KEYPHRASE = "computer";
    //private static final String KEYPHRASE = "narcisse";
    //private static final String KEYPHRASE = "marron";

    private SpeechRecognizer recognizer;
    private Timer timer;
    ///////////////////////////////////////////////////////////////////////
    private static final String COLOR_PREFERENCE_KEY = "color";
    public static CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = (LinearLayout) findViewById(R.id.main_back);
        //SetBright();
        dbManager = new DBManager(getApplicationContext(), "app.db", null, 1);
        dbManager_phoneBook = new DBManager_PhoneBook(getApplicationContext(), "app.db", null, 1);


        btn_color_changer = (ImageButton) findViewById(R.id.imageButton);

        //지민짱
        if (spm.getPreferences(this, "firsttime", "checked").equals("")) {

            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent, 0);

            slm.LockEnable(this); // 꺼짐 권한

            spm.savePreferences(this, "firsttime", "checked", "check");//체크 저장

            spm.savePreferences(this, "DB", "PUSH", "com.kakao.talk/");//처음에 카톡저장

            spm.savePreferences(this, "RGB", "R", 0 + "");
            spm.savePreferences(this, "RGB", "G", 0 + "");
            spm.savePreferences(this, "RGB", "B", 0 + "");

            spm.savePreferences(this, "Light", "onoff", "false");

        } else getList();
        checkBox = (CheckBox) findViewById(R.id.check_box_onoff);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked() == true) {
                    /*int color = PreferenceManager.getDefaultSharedPreferences(
                            MainActivity.this).getInt(COLOR_PREFERENCE_KEY, Color.WHITE);
                    new ColorPickerDialog(MainActivity.this, MainActivity.this,
                            color).show();*/
                    //main.setBackgroundResource(R.drawable.onback);
                    SaveLightOnOff(getApplicationContext(), "true");
                    sendData(getApplicationContext(), nowR + "," + nowG + "," + nowB);
                    //블루투스 연결됐으면
                    if(mSocket != null) {
                        btn_color_change.setBackgroundColor(Color.rgb(nowR, nowG, nowB));
                    }
                } else if (checkBox.isChecked() == false) {
                    //main.setBackgroundResource(R.drawable.offback);
                    SaveLightOnOff(getApplicationContext(), "false");
                    sendData(getApplicationContext(), "0,0,0");
                    btn_color_change.setBackgroundColor(Color.rgb(255, 255, 255));
                }
            }
        });

        nowR = Integer.valueOf(spm.getPreferences(this, "RGB", "R"));
        nowG = Integer.valueOf(spm.getPreferences(this, "RGB", "G"));
        nowB = Integer.valueOf(spm.getPreferences(this, "RGB", "B"));

        if (spm.getPreferences(this, "Light", "onoff").equals("true")) {
            //main.setBackgroundResource(R.drawable.onback);
            checkBox.setChecked(true);
        } else if (spm.getPreferences(this, "Light", "onoff").equals("false")) {
            // main.setBackgroundResource(R.drawable.offback);
            checkBox.setChecked(false);
        }


        startService(new Intent(getApplicationContext(), GyroService.class));
        //startService(new Intent(getApplicationContext(), SpottingService.class));
        timer = new Timer();
        timer.schedule(new MyTimer(), 1000);


        vedioView = (VideoView) findViewById(R.id.videoView);
        vedioView.setBackgroundResource(R.drawable.mainhollip);
        tv_voice = (TextView) findViewById(R.id.tv_voice);
        tv_nowrgb = (TextView) findViewById(R.id.tv_nowrgb);
        tv_nowrgb.setText(nowR + "\n" + nowG + "\n" + nowB);
        //////////////////////////////////////////키워드///////////////////////////////////////////////
        /*new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(MainActivity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    *//*((TextView) findViewById(R.id.caption_text))
                            .setText("Failed to init recognizer " + result);*//*
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();*/
        ///////////////////////////////////////////////////////////////////////////////////////////////

        btn_setting = (ImageButton) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SwipeActivity.class));
            }
        });

        btn_weather = (Button) findViewById(R.id.btn_weather);
        btn_weather.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(), OnService.class));
            }
        });

        btn_clock = (Button) findViewById(R.id.btn_clock);
        btn_clock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mc.StartCloseActivity(getApplicationContext());//투명close창
                startService(new Intent(getApplicationContext(), ClockActivity.class));
                //slm.LockScreen(getApplicationContext());
            }
        });

        btn_bt = (Button) findViewById(R.id.btn_bt);
        btn_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 블루투스 활성화 시키는 메소드
                checkBluetooth();
            }
        });

        btn_spam = (Button) findViewById(R.id.btn_spam);
        btn_spam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SpamlistActivity.class));
            }
        });

        btn_admin = (Button) findViewById(R.id.btn_admin);
        btn_admin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //권한 삭제
                slm.LockDisable(getApplicationContext());
            }
        });

       /* btn_lighton = (Button) findViewById(R.id.btn_lighton);
        btn_lighton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendData("163,101,20");
                int color = PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this).getInt(COLOR_PREFERENCE_KEY, Color.WHITE);
                new ColorPickerDialog(MainActivity.this, MainActivity.this,
                        color).show();
                main.setBackgroundResource(R.drawable.onback1);
            }
        });

        btn_lightoff = (Button) findViewById(R.id.btn_lightoff);
        btn_lightoff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("0,0,0");
                main.setBackgroundResource(R.drawable.offback1);
            }
        });*/


        btn_color_change = (Button) findViewById(R.id.color_change);

        btn_color_changer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendData("163,101,20");
                int color = PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this).getInt(COLOR_PREFERENCE_KEY, Color.WHITE);
                new ColorPickerDialog(MainActivity.this, MainActivity.this,
                        color).show();
                //btn_color_change.setBackgroundColor(Color.rgb(nowR, nowG, nowB));

            }
        });


        final ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.white));

        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setBackgroundResource(R.drawable.background_hollip_watch_2);
                vedioView.setBackgroundResource(0);
                FloatingActionsMenu.mExpanded = false;
                Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/test1");
                vedioView.setVideoURI(video);
                MediaPlayer.OnCompletionListener mComplete = new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/test1");
                        vedioView.setVideoURI(video);
                        vedioView.start();
                    }
                };
                //리스너 등록
                vedioView.setOnCompletionListener(mComplete);
                //비디오 시작
                vedioView.start();
                //마지막 디스플레이 저장
                spm.savePreferences(maincontext, "maindisplay", "category", "clock");
            }
        });

        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setBackgroundResource(R.drawable.background_hollip_weather_2);
                vedioView.setBackgroundResource(0);
                FloatingActionsMenu.mExpanded = false;
                Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/test2");
                vedioView.setVideoURI(video);
                MediaPlayer.OnCompletionListener mComplete = new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/test2");
                        vedioView.setVideoURI(video);
                        vedioView.start();
                    }
                };
                //리스너 등록
                vedioView.setOnCompletionListener(mComplete);
                //비디오 시작
                vedioView.start();
                //마지막 디스플레이 저장
                spm.savePreferences(maincontext, "maindisplay", "category", "weather");
            }
        });

        final FloatingActionButton actionC = (FloatingActionButton) findViewById(R.id.action_c);
        actionC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setBackgroundResource(R.drawable.background_hollip_main_2);
                vedioView.setBackgroundResource(0);
                FloatingActionsMenu.mExpanded = false;
                vedioView.setBackgroundResource(R.drawable.mainhollip);
                //마지막 디스플레이 저장
                spm.savePreferences(maincontext, "maindisplay", "category", "none");
            }
        });
    }

    @Override
    public void colorChanged(int color) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(COLOR_PREFERENCE_KEY, color).commit();
        //tv.setText(color+"");

    }

    @Override
    public void onResume() {
        super.onResume();

        firstplay = true;
        thread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(100);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    if (firstplay) {
                        handler.sendEmptyMessage(0);
                    }
                }
            }
        };
        thread.start();
    }

    public void getList() {
        mainList = dbManager.selectAll();
        mainSpamList = dbManager.selectAll_prank();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (spm.getPreferences(maincontext, "maindisplay", "category").equals("clock")) {
                main.setBackgroundResource(R.drawable.background_hollip_watch_2);
                vedioView.setBackgroundResource(0);
                FloatingActionsMenu.mExpanded = false;
                Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/test1");
                vedioView.setVideoURI(video);
                MediaPlayer.OnCompletionListener mComplete = new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/test1");
                        vedioView.setVideoURI(video);
                        vedioView.start();
                    }
                };
                //리스너 등록
                vedioView.setOnCompletionListener(mComplete);
                //비디오 시작
                vedioView.start();
                //마지막 디스플레이 저장
                spm.savePreferences(maincontext, "maindisplay", "category", "clock");
            } else if (spm.getPreferences(maincontext, "maindisplay", "category").equals("weather")) {
                main.setBackgroundResource(R.drawable.background_hollip_weather_2);
                vedioView.setBackgroundResource(0);
                FloatingActionsMenu.mExpanded = false;
                Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/test2");
                vedioView.setVideoURI(video);
                MediaPlayer.OnCompletionListener mComplete = new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/test2");
                        vedioView.setVideoURI(video);
                        vedioView.start();
                    }
                };
                //리스너 등록
                vedioView.setOnCompletionListener(mComplete);
                //비디오 시작
                vedioView.start();
                //마지막 디스플레이 저장
                spm.savePreferences(maincontext, "maindisplay", "category", "weather");
            }
            firstplay = false;
        }
    };

    /////////////////////////BT///////////////////////////////
    // 블루투스 장치의 이름이 주어졌을때 해당 블루투스 장치 객체를 페어링 된 장치 목록에서 찾아내는 코드.
    BluetoothDevice getDeviceFromBondedList(String name) {
        // BluetoothDevice : 페어링 된 기기 목록을 얻어옴.
        BluetoothDevice selectedDevice = null;
        // getBondedDevices 함수가 반환하는 페어링 된 기기 목록은 Set 형식이며,
        // Set 형식에서는 n 번째 원소를 얻어오는 방법이 없으므로 주어진 이름과 비교해서 찾는다.
        for (BluetoothDevice deivce : mDevices) {
            // getName() : 단말기의 Bluetooth Adapter 이름을 반환
            if (name.equals(deivce.getName())) {
                selectedDevice = deivce;
                break;
            }
        }
        return selectedDevice;
    }

    // 문자열 전송하는 함수(쓰레드 사용 x)
    public static void sendData(Context context, String msg) {
        msg += mStrDelimiter;  // 문자열 종료표시 (\n)
        try {
            // getBytes() : String을 byte로 변환
            // OutputStream.write : 데이터를 쓸때는 write(byte[]) 메소드를 사용함. byte[] 안에 있는 데이터를 한번에 기록해 준다.
            mOutputStream.write(msg.getBytes());  // 문자열 전송.
        } catch (Exception e) {  // 문자열 전송 도중 오류가 발생한 경우
            //main.setBackgroundResource(R.drawable.offback);
            SaveLightOnOff(context, "false");
            checkBox.setChecked(false);
            Toast.makeText(context, "홀립을 연결하세요", Toast.LENGTH_LONG).show();
            //finish();  // App 종료
        }
    }

    //  connectToSelectedDevice() : 원격 장치와 연결하는 과정을 나타냄.
    //        실제 데이터 송수신을 위해서는 소켓으로부터 입출력 스트림을 얻고 입출력 스트림을 이용하여 이루어 진다.
    void connectToSelectedDevice(String selectedDeviceName) {
        //void connectToSelectedDevice(String mac_id) {
        // BluetoothDevice 원격 블루투스 기기를 나타냄.
        mRemoteDevie = getDeviceFromBondedList(selectedDeviceName);
        Log.e("맥", mRemoteDevie + "");
        //mRemoteDevie = mBluetoothAdapter.getRemoteDevice(mac_id);

        // java.util.UUID.fromString : 자바에서 중복되지 않는 Unique 키 생성.
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // 소켓 생성, RFCOMM 채널을 통한 연결.
            // createRfcommSocketToServiceRecord(uuid) : 이 함수를 사용하여 원격 블루투스 장치와 통신할 수 있는 소켓을 생성함.
            // 이 메소드가 성공하면 스마트폰과 페어링 된 디바이스간 통신 채널에 대응하는 BluetoothSocket 오브젝트를 리턴함.
            mSocket = mRemoteDevie.createRfcommSocketToServiceRecord(uuid);
            mSocket.connect(); // 소켓이 생성 되면 connect() 함수를 호출함으로써 두기기의 연결은 완료된다.

            // 데이터 송수신을 위한 스트림 얻기.
            // BluetoothSocket 오브젝트는 두개의 Stream을 제공한다.
            // 1. 데이터를 보내기 위한 OutputStrem
            // 2. 데이터를 받기 위한 InputStream
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();

            readBufferPosition = 0;                 // 버퍼 내 수신 문자 저장 위치.
            readBuffer = new byte[1024];            // 수신 버퍼.
            threadbt = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            sleep(1000);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                        handlerbt.sendEmptyMessage(0);
                    }
                }
            };
            threadbt.start();

        } catch (Exception e) { // 블루투스 연결 중 오류 발생
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            //finish();  // App 종료
        }
    }

    private Handler handlerbt = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                // InputStream.available() : 다른 스레드에서 blocking 하기 전까지 읽은 수 있는 문자열 개수를 반환함.
                int byteAvailable = mInputStream.available();   // 수신 데이터 확인
                if (byteAvailable > 0) {                        // 데이터가 수신된 경우.
                    byte[] packetBytes = new byte[byteAvailable];
                    // read(buf[]) : 입력스트림에서 buf[] 크기만큼 읽어서 저장 없을 경우에 -1 리턴.
                    mInputStream.read(packetBytes);
                    for (int i = 0; i < byteAvailable; i++) {
                        byte b = packetBytes[i];
                        if (b == mCharDelimiter) {
                            byte[] encodedBytes = new byte[readBufferPosition];
                            //  System.arraycopy(복사할 배열, 복사시작점, 복사된 배열, 붙이기 시작점, 복사할 개수)
                            //  readBuffer 배열을 처음 부터 끝까지 encodedBytes 배열로 복사.
                            System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                            final String data = new String(encodedBytes, "US-ASCII");
                            readBufferPosition = 0;

                            handler.post(new Runnable() {
                                // 수신된 문자열 데이터에 대한 처리.
                                @Override
                                public void run() {
                                    // mStrDelimiter = '\n';
                                    //mEditReceive.setText(mEditReceive.getText().toString() + data+ mStrDelimiter);
                                    //
                                    if ((data).equals("one\r")) { //말씀하세요
                                        stopService(new Intent(getApplicationContext(), WaitActivity.class));
                                        stopService(new Intent(getApplicationContext(), ClockActivity.class));
                                        stopService(new Intent(getApplicationContext(), WeatherActivity.class));
                                        slm.BrokeLock(getApplicationContext());//락깨기
                                        startService(new Intent(getApplicationContext(), WaitActivity.class));
                                    } else if ((data).equals("two\r")) { //시계
                                        stopService(new Intent(getApplicationContext(), WaitActivity.class));
                                        stopService(new Intent(getApplicationContext(), ClockActivity.class));
                                        stopService(new Intent(getApplicationContext(), WeatherActivity.class));
                                        slm.BrokeLock(getApplicationContext());//락깨기
                                        startService(new Intent(getApplicationContext(), ClockActivity.class));
                                    } else if ((data).equals("three\r")) { //날씨
                                        stopService(new Intent(getApplicationContext(), WaitActivity.class));
                                        stopService(new Intent(getApplicationContext(), ClockActivity.class));
                                        stopService(new Intent(getApplicationContext(), WeatherActivity.class));
                                        slm.BrokeLock(getApplicationContext());//락깨기
                                        startService(new Intent(getApplicationContext(), WeatherActivity.class));
                                    }
                                }

                            });
                        } else {
                            readBuffer[readBufferPosition++] = b;
                        }
                    }
                }

            } catch (Exception e) {    // 데이터 수신 중 오류 발생.
                Toast.makeText(getApplicationContext(), "데이터 수신 중 오류가 발생 했습니다.", Toast.LENGTH_LONG).show();
                //finish();            // App 종료.
            }

        }
    };

    // 블루투스 지원하며 활성 상태인 경우.
    void selectDevice() {
        // 블루투스 디바이스는 연결해서 사용하기 전에 먼저 페어링 되어야만 한다
        // getBondedDevices() : 페어링된 장치 목록 얻어오는 함수.
        mDevices = mBluetoothAdapter.getBondedDevices();
        mPariedDeviceCount = mDevices.size();

        if (mPariedDeviceCount == 0) { // 페어링된 장치가 없는 경우.
            Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            //finish(); // App 종료.
        }
        // 페어링된 장치가 있는 경우.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 선택");

        // 각 디바이스는 이름과(서로 다른) 주소를 가진다. 페어링 된 디바이스들을 표시한다.
        List<String> listItems = new ArrayList<String>();
        for (BluetoothDevice device : mDevices) {
            // device.getName() : 단말기의 Bluetooth Adapter 이름을 반환.
            listItems.add(device.getName());
        }
        listItems.add("취소");  // 취소 항목 추가.


        // CharSequence : 변경 가능한 문자열.
        // toArray : List형태로 넘어온것 배열로 바꿔서 처리하기 위한 toArray() 함수.
        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
        // toArray 함수를 이용해서 size만큼 배열이 생성 되었다.
        listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                if (item == mPariedDeviceCount) { // 연결할 장치를 선택하지 않고 '취소' 를 누른 경우.
                    Toast.makeText(getApplicationContext(), "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    //finish();
                } else { // 연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도함.
                    connectToSelectedDevice(items[item].toString());
                }
            }

        });

        builder.setCancelable(false);  // 뒤로 가기 버튼 사용 금지.
        AlertDialog alert = builder.create();
        alert.show();
    }


    void checkBluetooth() {
        /**
         * getDefaultAdapter() : 만일 폰에 블루투스 모듈이 없으면 null 을 리턴한다.
         이경우 Toast를 사용해 에러메시지를 표시하고 앱을 종료한다.
         */
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {  // 블루투스 미지원
            Toast.makeText(getApplicationContext(), "기기가 블루투스를 지원하지 않습니다.", Toast.LENGTH_LONG).show();
            //finish();  // 앱종료
        } else { // 블루투스 지원
            /** isEnable() : 블루투스 모듈이 활성화 되었는지 확인.
             *               true : 지원 ,  false : 미지원
             */
            if (!mBluetoothAdapter.isEnabled()) { // 블루투스 지원하며 비활성 상태인 경우.
                Toast.makeText(getApplicationContext(), "현재 블루투스가 비활성 상태입니다.", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // REQUEST_ENABLE_BT : 블루투스 활성 상태의 변경 결과를 App 으로 알려줄 때 식별자로 사용(0이상)
                /**
                 startActivityForResult 함수 호출후 다이얼로그가 나타남
                 "예" 를 선택하면 시스템의 블루투스 장치를 활성화 시키고
                 "아니오" 를 선택하면 비활성화 상태를 유지 한다.
                 선택 결과는 onActivityResult 콜백 함수에서 확인할 수 있다.
                 */
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else // 블루투스 지원하며 활성 상태인 경우.
                selectDevice();
        }
    }

    // onDestroy() : 어플이 종료될때 호출 되는 함수.
    //               블루투스 연결이 필요하지 않는 경우 입출력 스트림 소켓을 닫아줌.
    @Override
    protected void onDestroy() {
        try {
            if (timer != null) {
                timer.cancel();
            }
            stopService(new Intent(getApplicationContext(), SpottingService.class));
            stopService(new Intent(getApplicationContext(), SttService.class));
            stopService(new Intent(getApplicationContext(), GyroService.class));
            tm.myTTS.shutdown();
            /*if (mRecognizer != null) {
                mRecognizer.destroy();
                mRecognizer = null;
            }*/
            //mInputStream.close();
            //mSocket.close();
            //thread.interrupt();
            //threadbt.interrupt(); // 데이터 수신 쓰레드 종료
            //threadbt.stop();
            //thread.stop();

            //threadbt.stop();
            //thread.stop();

        } catch (Exception e) {
        }
        super.onDestroy();
    }

    /*@Override
    protected void onPause() {
        super.onPause();

        stopService(new Intent(getApplicationContext(), GyroService.class));
    }*/


    // onActivityResult : 사용자의 선택결과 확인 (아니오, 예)
    // RESULT_OK: 블루투스가 활성화 상태로 변경된 경우. "예"
    // RESULT_CANCELED : 오류나 사용자의 "아니오" 선택으로 비활성 상태로 남아 있는 경우  RESULT_CANCELED

    /**
     * 사용자가 request를 허가(또는 거부)하면 안드로이드 앱의 onActivityResult 메소도를 호출해서 request의 허가/거부를 확인할수 있다.
     * 첫번째 requestCode : startActivityForResult 에서 사용했던 요청 코드. REQUEST_ENABLE_BT 값
     * 두번째 resultCode  : 종료된 액티비티가 setReuslt로 지정한 결과 코드. RESULT_OK, RESULT_CANCELED 값중 하나가 들어감.
     * 세번째 data        : 종료된 액티비티가 인테트를 첨부했을 경우, 그 인텐트가 들어있고 첨부하지 않으면 null
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // startActivityForResult 를 여러번 사용할 땐 이런 식으로 switch 문을 사용하여 어떤 요청인지 구분하여 사용함.
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) { // 블루투스 활성화 상태
                    selectDevice();
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 비활성화 상태 (종료)
                    Toast.makeText(getApplicationContext(), "블루투스를 사용할 수 없어 프로그램을 종료합니다", Toast.LENGTH_LONG).show();
                    //finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void SetBright() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = (float) 10 / 100;
        getWindow().setAttributes(params);
    }

    ////////////////////////////////키워드////////////////////////////////////////////////////
    /*@Override
    public void onPartialResult(Hypothesis hypothesis) {
        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE)) {
            slm.BrokeLock(getApplicationContext());//락깨기
            startService(new Intent(getApplicationContext(), WaitActivity.class));
            isorder = true;

            switchSearch(KWS_SEARCH);
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            //String text = hypothesis.getHypstr();
            //makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onEndOfSpeech() {
        switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();
        recognizer.startListening(searchName);
    }

    private void setupRecognizer(File assetsDir) {
        File modelsDir = new File(assetsDir, "models");
        recognizer = defaultSetup()
                .setAcousticModel(new File(modelsDir, "hmm/en-us-semi"))
                .setDictionary(new File(modelsDir, "dict/cmu07a.dic"))
                .setRawLogDir(assetsDir).setKeywordThreshold(1e-20f)
                .getRecognizer();
        recognizer.addListener(this);

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
    }*/
    ////////////////////////////////////////////////////////////////////////////////////////

    private class MyTimer extends TimerTask {
        @Override
        public void run() {
            startService(new Intent(getApplicationContext(), SpottingService.class));
        }
    }

    public static void SaveRGB(Context context, int r, int g, int b) {
        nowR = r;
        nowG = g;
        nowB = b;

        SharedPreferencesMethod.savePreferences(context, "RGB", "R", r + "");
        SharedPreferencesMethod.savePreferences(context, "RGB", "G", g + "");
        SharedPreferencesMethod.savePreferences(context, "RGB", "B", b + "");
    }

    public static void SaveLightOnOff(Context context, String value) {
        if (value.equals("true")) {
            islighton = true;
            SharedPreferencesMethod.savePreferences(context, "Light", "onoff", "true");
        } else if (value.equals("false")) {
            islighton = false;
            SharedPreferencesMethod.savePreferences(context, "Light", "onoff", "false");
        }
    }

}

