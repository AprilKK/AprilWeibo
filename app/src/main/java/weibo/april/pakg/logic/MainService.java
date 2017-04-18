package weibo.april.pakg.logic;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Handler;

import weibo.april.pakg.interfaces.IWeiboRefreshUI;
import weibo.april.pakg.model.Bean.UserInfo;
import weibo.april.pakg.model.UserInfoServices;
import weibo.april.pakg.ui.LogonActivity;
import weibo.april.pakg.utils.Constants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

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


            switch (msg.what)
            {
                case Task.WEIBO_GETFRIENDTIMELINE:
                    IWeiboRefreshUI activity = (IWeiboRefreshUI)getActivityByName("HomeActivity");
                    activity.refreshUI(msg.obj);
                    break;
            }
        }
    };

    //mStatusesAPI used to get the weibo info,such as friends timeline, comments,user info etc...
    private StatusesAPI mStatusesAPI;
    //accessToken
    private Oauth2AccessToken mAccessToken;

    //UserInfoServices
    public static UserInfoServices uiServices;

    Message msg = new Message();
    private void init()
    {
        uiServices = new UserInfoServices(this);
        ArrayList<UserInfo> userinfos= uiServices.getAllUserInfoFromTable();
        UserInfo userInfo = userinfos.get(0);
        mAccessToken.setExpiresTime(userInfo.getExpiresTime());
        mAccessToken.setRefreshToken(userInfo.getRefreshToken());
        mAccessToken.setToken(userInfo.getToken());
        mAccessToken.setUid(userInfo.getUserId());
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);

    }
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
        init();
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
        msg.what=t.getTaskId();
        switch (t.getTaskId())
        {
            case Task.WEIBO_LOGON:
                Log.v("MainService","doTask >>>>> Log on to weibo");
                msg.obj="Hello,You have logged into Weibo";
                handler.sendMessage(msg);
                break;
            case Task.WEIBO_GETFRIENDTIMELINE:
                Log.v("MainService","doTask>>>>get friends timeline");
                if (mAccessToken != null && mAccessToken.isSessionValid()) {
                    mStatusesAPI.friendsTimeline(0L, 0L, 10, 1, false, 0, false, mListener);
                }
                else{
                    Toast.makeText(this,
                            "AccessToken is empty or invalid,please update the token.",
                            Toast.LENGTH_LONG).show();
                }
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

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList mList = StatusList.parse(response);
                    if (mList != null && mList.total_number > 0) {
                        msg.obj=mList;
                        handler.sendMessage(msg);
                    }
                } else if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
                    Status status = Status.parse(response);
                } else {

                }
            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("MainServices", e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
        }
    };
}
