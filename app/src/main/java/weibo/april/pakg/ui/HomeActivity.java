package weibo.april.pakg.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import weibo.april.pakg.R;
import weibo.april.pakg.interfaces.IWeiboRefreshUI;
import weibo.april.pakg.logic.MainService;
import weibo.april.pakg.logic.Task;

/**
 * Created by Duke on 4/11/2017.
 */

public class HomeActivity extends AppCompatActivity implements IWeiboRefreshUI, AbsListView.OnScrollListener{
    private ListView listViewWeibos;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



    }

    @Override
    public void refreshUI(Object... params) {

    }

    @Override
    public void init() {
        listViewWeibos = (ListView)findViewById(R.id.home_listview_weibos);
        listViewWeibos.setOnScrollListener(this);
        Intent intent = new Intent(this,MainService.class);
        bindService(intent,sc,BIND_AUTO_CREATE);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                loadMoreData();
            }
        }
    }
    private void loadMoreData()
    {
        Task task = new Task(Task.WEIBO_GETFRIENDTIMELINE,null);
        if(msBinder != null)
        {
            msBinder.newTask(task);
        }
    }
    private MainService.MainServiceBinder msBinder;
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            msBinder = (MainService.MainServiceBinder)service;
            msBinder.addActivity(HomeActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            unbindService(sc);
        }
    };
}
