package weibo.april.pakg.interfaces;

/**
 * Created by Duke on 3/25/2017.
 * interface should be implementated by all activities. this interface contains two methods for
 * refreshing UI.
 */

public interface IWeiboRefreshUI {
    /*
    * name: refreshUI
    * params: Object..., length changable params
    * purpose: refresh activity UI
     */
    void refreshUI(Object... params);
    /*
    *name: init
    * params: none
    * purpose: initialize all the context that needed for refresh activity UI
     */
    //void init();
}
