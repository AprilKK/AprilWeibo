package weibo.april.pakg.model;

/**
 * Created by Duke on 4/11/2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.ArrayList;

import weibo.april.pakg.R;


public class ListItemAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    /**
     * 微博信息列表
     */
    private StatusList mStatusList = new StatusList();
    private ArrayList<StatusList> mStatusListList = new ArrayList<StatusList>();
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_default_image)
            .showImageOnFail(R.drawable.ic_default_image)
            .bitmapConfig(Bitmap.Config.ARGB_8888).cacheInMemory(true)
            .cacheOnDisk(true).build();

    public ListItemAdapter(Context context/*, StatusList list*/) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return mStatusList.statusList == null ? 0 : mStatusList.statusList.size();

        int numOfmStatusList = mStatusListList.size();
        int numOfCount = 0;
        for(int i=0;i<numOfmStatusList;i++)
        {
            numOfCount = numOfCount + mStatusListList.get(i).statusList.size();
        }
        return numOfCount;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        int index = position / 10;
        //return mStatusList == null ? null : mStatusList.statusList.get(position);
        return mStatusListList.get(index).statusList.get(position % 10);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        int index = position / 10;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.weibo_item_listview, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.rly_content = (RelativeLayout) convertView
                    .findViewById(R.id.rly_content);
            viewHolder.lly_comments = (LinearLayout) convertView
                    .findViewById(R.id.lly_comment);
            viewHolder.lly_repost = (LinearLayout) convertView
                    .findViewById(R.id.lly_repost);

            viewHolder.roundImageView = (RoundedImageView) convertView
                    .findViewById(R.id.iv_head);
            viewHolder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            viewHolder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            viewHolder.tv_reContent = (TextView) convertView
                    .findViewById(R.id.tv_recontent);
            viewHolder.tv_like = (TextView) convertView
                    .findViewById(R.id.tv_like);
            viewHolder.tv_repost = (TextView) convertView
                    .findViewById(R.id.tv_repost);
            viewHolder.tv_comment = (TextView) convertView
                    .findViewById(R.id.tv_comment);
            viewHolder.iv_menu = (ImageView) convertView
                    .findViewById(R.id.iv_menu);
            viewHolder.view_line = (View) convertView
                    .findViewById(R.id.view_line);
            viewHolder.nineGridView = (NineGridImageView) convertView
                    .findViewById(R.id.grid_image);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Status mStatus = mStatusListList.get(index).statusList.get(position % 10);

        ImageLoader.getInstance().displayImage(mStatus.user.avatar_hd,
                viewHolder.roundImageView, options);
        viewHolder.tv_name.setText(mStatus.user.screen_name);

        String time = "";
        if (!TextUtils.isEmpty(mStatus.created_at)) {
            time = "";
        }
        String from = "";
        if (!TextUtils.isEmpty(mStatus.source)) {
            from = String.format("%s", Html.fromHtml(mStatus.source));
        }
        viewHolder.tv_time.setText(time + " " + from);
        //TODO April-->next time 1. to find the WeiboAutolinkUtil
        viewHolder.tv_content.setText(mStatus.text);
        if (mStatus.retweeted_status != null) {
            viewHolder.tv_reContent.setVisibility(View.VISIBLE);
            viewHolder.view_line.setVisibility(View.VISIBLE);
            String reUserName = mStatus.retweeted_status.user.screen_name;
            //TODO April-->next time 1. to find the WeiboAutolinkUtil
            viewHolder.tv_reContent.setText("");
            viewHolder.nineGridView.setVisibility(View.VISIBLE);
            viewHolder.nineGridView
                    .setAdapter(new NineGridImageViewAdapter<String>() {

                        @Override
                        protected void onDisplayImage(Context context,
                                                      ImageView imageView, String t) {
                            // TODO Auto-generated method stub
                            ImageLoader.getInstance().displayImage(t,
                                    imageView, options);
                        }

                        protected void onItemImageClick(Context context,
                                                        int index, java.util.List<String> list) {

                        };
                    });
            viewHolder.nineGridView
                    .setImagesData(mStatus.retweeted_status.pic_urls);
        } else {
            if (mStatus.pic_urls != null) {
                viewHolder.tv_reContent.setVisibility(View.GONE);
                viewHolder.view_line.setVisibility(View.GONE);
                viewHolder.nineGridView.setVisibility(View.VISIBLE);
                viewHolder.nineGridView
                        .setAdapter(new NineGridImageViewAdapter<String>() {

                            @Override
                            protected void onDisplayImage(Context context,
                                                          ImageView imageView, String t) {
                                // TODO Auto-generated method stub
                                ImageLoader.getInstance().displayImage(t,
                                        imageView, options);
                            }

                            protected void onItemImageClick(Context context,
                                                            int index, java.util.List<String> list) {

                            };
                        });
                viewHolder.nineGridView.setImagesData(mStatus.pic_urls);

            } else {
                viewHolder.tv_reContent.setVisibility(View.GONE);
                viewHolder.view_line.setVisibility(View.GONE);
                viewHolder.nineGridView.setVisibility(View.GONE);
            }
        }
        viewHolder.tv_like.setText(mStatus.attitudes_count + "");
        viewHolder.tv_repost.setText(mStatus.reposts_count + "");
        viewHolder.tv_comment.setText(mStatus.comments_count + "");

        return convertView;
    }
    public void addStatusList(StatusList weiboList)
    {
        mStatusListList.add(weiboList);
    }

    class ViewHolder {
        private RelativeLayout rly_content;
        private LinearLayout lly_comments;
        private LinearLayout lly_repost;

        private RoundedImageView roundImageView;
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_content;
        private TextView tv_reContent;
        private TextView tv_like;
        private TextView tv_repost;
        private TextView tv_comment;

        private View view_line;
        private ImageView iv_menu;

        private NineGridImageView nineGridView;

    }

}

