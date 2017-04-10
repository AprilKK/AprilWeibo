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
import weibo.april.pakg.utils.AuthListener;
import weibo.april.pakg.utils.Constants;

/**
 * Created by Duke on 3/25/2017.
 */

public class LogonActivity extends AppCompatActivity implements View.OnClickListener,IWeiboRefreshUI{
    //Logon button
    private Button logonButton;
    private static AuthInfo mAuthInfo;

    //封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
    private static Oauth2AccessToken mAccessToken;

    //注意：SsoHandler 仅当 SDK 支持 SSO 时有效
    private static SsoHandler mSsoHandler;
    //UserInfoServices
    public static UserInfoServices uiServices;
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
        // SSO 授权, 仅Web
        mSsoHandler.authorizeWeb(new AuthListener());
    }

    @Override
    public void refreshUI(Object... params) {

    }

}
