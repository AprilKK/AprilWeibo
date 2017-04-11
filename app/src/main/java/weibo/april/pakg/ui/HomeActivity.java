package weibo.april.pakg.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import weibo.april.pakg.R;
import weibo.april.pakg.interfaces.IWeiboRefreshUI;

/**
 * Created by Duke on 4/11/2017.
 */

public class HomeActivity extends AppCompatActivity implements IWeiboRefreshUI {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ListView listView_weibos = (ListView)findViewById(R.id.home_listview_weibos);

    }

    @Override
    public void refreshUI(Object... params) {

    }

    @Override
    public void init() {

    }
}
