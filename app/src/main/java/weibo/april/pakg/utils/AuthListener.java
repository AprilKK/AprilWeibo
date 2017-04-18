package weibo.april.pakg.utils;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

import weibo.april.pakg.model.Bean.UserInfo;

import static weibo.april.pakg.ui.LogonActivity.uiServices;

/**
 * Created by Duke on 3/30/2017.
 */

public class AuthListener implements WeiboAuthListener {
    private Oauth2AccessToken mAccessToken = null;
    @Override
    public void onComplete(Bundle values) {
        // 从 Bundle 中解析 Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(values);

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
            if(null == uiServices.getUserInfoFromTable(userInfo.getUserId())) {
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
        }
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onWeiboException(WeiboException e) {
    }
}
