package weibo.april.pakg.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import weibo.april.pakg.R;
import weibo.april.pakg.model.UserInfoServices;

public class WelcomActivity extends AppCompatActivity {

    //UserInfoServices
    public static UserInfoServices uiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        uiServices = new UserInfoServices(this);
        ImageView imageView = (ImageView)findViewById(R.id.image_Logo);

        AlphaAnimation animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //if there is account in the database, then just use that account,if not, do the auth.
                if(null == uiServices.getAllUserInfoFromTable())
                {
                    Intent intent = new Intent(WelcomActivity.this,LogonActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(WelcomActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.setAnimation(animation);
    }
}
