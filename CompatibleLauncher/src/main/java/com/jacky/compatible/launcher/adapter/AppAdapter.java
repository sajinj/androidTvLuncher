
package com.jacky.compatible.launcher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jacky.compatible.launcher.features.app.AppFragment;
import com.jacky.compatible.launcher.model.AppBean;
import com.jacky.launcher.R;

import java.util.List;
import java.util.Random;

/**
 * AppFragment adapter
 *
 * @author jacky
 * @version 1.0
 * @since 2016.5.10
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.MyViewHolder> {

    private List<AppBean> mAppBeanList;
    private Context mContext;
    private AppFragment mAppFragment;


    private int[] drawableIds = {
            R.drawable.app_blue,
            R.drawable.app_green,
            R.drawable.app_jasper,
            R.drawable.app_lawngreen,
            R.drawable.app_red,
            R.drawable.app_yellow
    };


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView name;
        private ImageView icon;
        private View bg;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.item_app_name);
            icon = (ImageView) v.findViewById(R.id.item_app_icon);
            bg = v.findViewById(R.id.item_app_bg);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AppAdapter(AppFragment appFragment, Context context, List<AppBean> appBeanList) {
        mAppFragment = appFragment;
        mContext = context;
        mAppBeanList = appBeanList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AppAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_app, parent, false);

        // Return a new holder instance
        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Random random = new Random();
        holder.bg.setBackgroundResource(drawableIds[random.nextInt(6)]);
        AppBean appBean = mAppBeanList.get(position);
        holder.icon.setImageDrawable(appBean.getIcon());
        holder.name.setText(appBean.getName());

        final int pos = position;


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mAppBeanList.size();
    }
}

//
//public class AppAdapter extends BaseAdapter {
//
//    private List<AppBean> mAppBeanList;
//    private Context mContext;
//    public Holder mHolder;
//    private AppFragment mAppFragment;
//
//    private int[] drawableIds = {
//            R.drawable.app_blue,
//            R.drawable.app_green,
//            R.drawable.app_jasper,
//            R.drawable.app_lawngreen,
//            R.drawable.app_red,
//            R.drawable.app_yellow
//    };
//
//    public AppAdapter(AppFragment appFragment, Context context, List<AppBean> appBeanList) {
//        mAppFragment = appFragment;
//        mContext = context;
//        mAppBeanList = appBeanList;
//    }
//
//    @Override
//    public int getCount() {
//        return mAppBeanList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mAppBeanList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            mHolder = new Holder();
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_app, null);
//            mHolder.name = (TextView) convertView.findViewById(R.id.item_app_name);
//            mHolder.icon = (ImageView) convertView.findViewById(R.id.item_app_icon);
//            mHolder.bg = convertView.findViewById(R.id.item_app_bg);
//            convertView.setTag(mHolder);
//        } else {
//            mHolder = (Holder) convertView.getTag();
//        }
//        Random random = new Random();
//        mHolder.bg.setBackgroundResource(drawableIds[random.nextInt(6)]);
//        AppBean appBean = mAppBeanList.get(position);
//        mHolder.icon.setImageDrawable(appBean.getIcon());
//        mHolder.name.setText(appBean.getName());
//
//        final int pos = position;
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(
//                        mAppBeanList.get(pos).getPackageName());
//                if (launchIntent != null) {
//                    mContext.startActivity(launchIntent);
//                }
//            }
//        });
//        return convertView;
//    }
//
//    public class Holder {
//        private TextView name;
//        private ImageView icon;
//        private View bg;
//    }
//
//
//
//}
