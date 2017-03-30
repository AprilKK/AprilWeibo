package weibo.april.pakg.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import weibo.april.pakg.R;
import weibo.april.pakg.interfaces.IWeiboRefreshUI;
import weibo.april.pakg.logic.MainService;
import weibo.april.pakg.logic.Task;
import weibo.april.pakg.model.Bean.UserInfo;
import weibo.april.pakg.model.UserInfoServices;
import weibo.april.pakg.utils.Constants;

/**
 * Created by Duke on 3/25/2017.
 */

public class LogonActivity extends AppCompatActivity implements View.OnClickListener,IWeiboRefreshUI,WeiboAuthListener{
    //Logon button
    private Button logonButton;
    private static AuthInfo mAuthInfo;

    //封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
    private static Oauth2AccessToken mAccessToken;

    //注意：SsoHandler 仅当 SDK 支持 SSO 时有效
    private static SsoHandler mSsoHandler;
    //UserInfoServices
    UserInfoServices uiServices;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        Intent intent = new Intent(this,MainService.class);
        bindService(intent,sc,BIND_AUTO_CREATE);
        logonButton = (Button) findViewById(R.id.logon_button);
        logonButton.setOnClickListener(this);

        //init weibo Auth affrrs.
        init();
    }
    @Override
    public void init()
    {
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        uiServices = new UserInfoServices(this);
    }
    private MainService.MainServiceBinder msBinder;
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            msBinder = (MainService.MainServiceBinder)service;
            msBinder.addActivity(LogonActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            unbindService(sc);
        }
    };

    @Override
    public void onClick(View v) {
        Task task = new Task(Task.WEIBO_LOGON,null);
        msBinder.newTask(task);
        // SSO 授权, 仅Web
        mSsoHandler.authorizeWeb(this);
    }

    @Override
    public void refreshUI(Object... params) {

    }

    @Override
    public void onComplete(Bundle values) {
        // 从 Bundle 中解析 Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        //从这里获取用户输入的 电话号码信息
        //String  phoneNum =  mAccessToken.getPhoneNum();
        if (mAccessToken.isSessionValid()) {
            // 显示 Token
            //updateTokenView(false);
            //Toast.makeText(this, mAccessToken.toString(), Toast.LENGTH_LONG).show();
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(mAccessToken.getUid());
            userInfo.setToken(mAccessToken.getToken());
            userInfo.setRefreshToken(mAccessToken.getRefreshToken());
            userInfo.setExpiresTime(mAccessToken.getExpiresTime());
            userInfo.setPhoneNum(mAccessToken.getPhoneNum());
            // 保存 userInfo 到 database
            if(null != uiServices.getUserInfoFromTable(userInfo.getUserId())) {
                uiServices.InsertUserInfoToTable(userInfo);
            }
            else
            {
                uiServices.updateUserInfoToTable(userInfo);
            }
            Log.v("LogonActivity","write to DB");
            //uiServices.getUserInfoFromTable(userInfo.getUserId());
        } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            String code = values.getString("code");
            String message = "auth failed";
            if (!TextUtils.isEmpty(code)) {
                message = message + "\nObtained the code: " + code;
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancel() {
        Toast.makeText(this,
                "auth canceled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Toast.makeText(this,
                "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
