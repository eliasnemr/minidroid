package global.org.minidapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;

import org.minima.NativeListener;
import org.minima.Start;
import org.minima.objects.TxPOW;
import org.minima.system.Main;
import org.minima.system.brains.ConsensusHandler;
import org.minima.utils.messages.Message;
import org.w3c.dom.Node;

/** Foreground Service for the Minima Node
 *
 *  Elias Nemr
 *
 * 23 April 2020
 * */
public class NodeService extends Service {

    Handler mHandler;
    Start mStart;
    boolean mStarted = false;
    NotificationManager mNotificationManager, mNotificationManagerLow;
    android.app.Notification mNotificationBuilder;
    PendingIntent mPendingIntent;
    TxPOW txpow;
    public String mBLOCK_NUMBER;

    public static final String CHANNEL_ID = "MinimaNodeServiceChannel";

    public static NodeService mNodeService;

    @Override
    public void onCreate() {
        super.onCreate();

        mNodeService = this;

        //Start Minima
        mStart = new Start();
        mStart.fireStarter(getFilesDir().getAbsolutePath());


        Log.d("Minima Call:", "" + "Minima is running");
        Toast.makeText(mNodeService, "", Toast.LENGTH_SHORT).show();

        mHandler = new Handler(Looper.getMainLooper());

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Minima Node Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );

            mNotificationManager =
                    getSystemService(NotificationManager.class);
            mNotificationManager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create our notification channel here & add channel to Manager's list
        createNotificationChannel();

        Intent NotificationIntent = new Intent(this, MainActivity.class);
        mPendingIntent = PendingIntent.getActivity(this, 0
                , NotificationIntent, PendingIntent.FLAG_NO_CREATE);

        // Create our notification
        mNotificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Minima Node Status")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_minima)
                .setContentIntent(mPendingIntent)
                .build();

        startForeground(1, mNotificationBuilder);


        // do heavy work on background thread
        if (!mStarted) {
            mStarted = true;
            try {
                Thread.sleep(1000);
            } catch (Exception exc) {
            }
        }

        mStart.getServer().getConsensusHandler().addListener(new NativeListener() {
            @Override
            public void processMessage(final Message zMessage) {
                //THIS GETS CALLED!
                try {
                    if (zMessage.isMessageType(ConsensusHandler.CONSENSUS_NOTIFY_BALANCE)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                mNotificationBuilder = new NotificationCompat.Builder(NodeService.this, CHANNEL_ID)
                                        .setContentTitle("Minima Node: ")
                                        .setContentText("You just received coins!")
                                        .setSmallIcon(R.drawable.ic_minima)
                                        .setContentIntent(mPendingIntent)
                                        .build();

                                startForeground(1, mNotificationBuilder);

                                Toast.makeText(NodeService.this,
                                        "You just received some coins!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (zMessage.isMessageType(ConsensusHandler.CONSENSUS_NOTIFY_NEWBLOCK)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                TxPOW txpow = (TxPOW) zMessage.getObject("txpow");

                                mBLOCK_NUMBER = txpow.getBlockNumber().toString();

                                mNotificationBuilder = new NotificationCompat.Builder(NodeService.this, CHANNEL_ID)
                                        .setContentTitle("Block "+mBLOCK_NUMBER)
                                        .setContentText("Minima Node Channel")
                                        .setSmallIcon(R.drawable.ic_minima)
                                        .setContentIntent(mPendingIntent)
                                        .build();

                                startForeground(1, mNotificationBuilder);

                            }
                        });

                    }
                } catch (Exception exc) {

                }
            }
        });

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Minima stopped running.", Toast.LENGTH_LONG).show();
        super.onDestroy();

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    }
