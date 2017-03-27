package weibo.april.pakg.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import weibo.april.pakg.R;
import weibo.april.pakg.interfaces.IWeiboRefreshUI;
import weibo.april.pakg.logic.MainService;
import weibo.april.pakg.logic.Task;

/**
 * Created by Duke on 3/25/2017.
 */

public class LogonActivity extends AppCompatActivity implements View.OnClickListener,IWeiboRefreshUI{
    private Button logonButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        Intent intent = new Intent(this,MainService.class);
        bindService(intent,sc,BIND_AUTO_CREATE);
        logonButton = (Button) findViewById(R.id.logon_button);
        logonButton.setOnClickListener(this);
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

        }
    };

    @Override
    public void onClick(View v) {
        Task task = new Task(Task.WEIBO_LOGON,null);
        msBinder.newTask(task);
    }

    @Override
    public void refreshUI(Object... params) {

    }
}
