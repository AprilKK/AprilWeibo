package weibo.april.pakg.logic;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Handler;

import weibo.april.pakg.interfaces.IWeiboRefreshUI;
import weibo.april.pakg.ui.LogonActivity;

/**
 * Created by Duke on 3/25/2017.
 */

public class MainService extends Service implements Runnable {
    //task queue that queried by run() to take out every task to process
    private static Queue<Task> tasks = new LinkedList<Task>();
    //indicator to mark that the service is runing or not.
    private boolean isRun;
    //activity list that contains all the activities that has bound to this service.
    private static ArrayList<Activity> activities = new ArrayList<>();
    //message handler to return the data back to activity and refresh the activity UI
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            IWeiboRefreshUI activity = (IWeiboRefreshUI)getActivityByName("LogonActivity");
            activity.refreshUI(msg.obj);
        }
    };

    private static void addActivity(Activity activity)
    {
        activities.add(activity);
    }
    private static void newTask(Task t)
    {
        tasks.add(t);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        isRun = true;
        Thread thread = new Thread(this);
        thread.start();
    }
    public class MainServiceBinder extends Binder{
        public void newTask(Task t)
        {
            MainService.this.newTask(t);
        }
        public void addActivity(Activity ac)
        {
            MainService.this.addActivity(ac);
        }

    }
    MainServiceBinder msBinder = new MainServiceBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return msBinder;
    }

    @Override
    public void run() {
        while(isRun)
        {
            if(!tasks.isEmpty())
            {
                Task t = tasks.poll();
                if(null != t)
                {
                    doTask(t);
                }

            }
        }
    }
    private void doTask(Task t)
    {
        switch (t.getTaskId())
        {
            case Task.WEIBO_LOGON:
                Log.v("MainService","doTask >>>>> Log on to weibo");
                Message msg = new Message();
                msg.obj="Hello,You have logged into Weibo";
                handler.sendMessage(msg);
                break;
            default:
                break;
        }
    }
    private Activity getActivityByName(String name)
    {
        if(!activities.isEmpty())
        {
            for(Activity ac:activities)
            {
                if(null != ac)
                {
                    if(ac.getClass().getName().indexOf(name)>0)
                    {
                        return ac;
                    }
                }
            }
        }
        return null;
    }
}
