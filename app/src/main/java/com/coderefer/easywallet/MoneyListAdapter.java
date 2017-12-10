package com.coderefer.easywallet;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10/12/2560.
 */

public class MoneyListAdapter extends ArrayAdapter<MoneyItem> {

    private Context mContext;
    private int mLayoutResId;
    private ArrayList<MoneyItem> mMoneyItemList;

    public MoneyListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MoneyItem> objects) {
        super(context, resource, objects);
        this.mContext=context;
        this.mLayoutResId=resource;
        this.mMoneyItemList=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemLayout = inflater.inflate(mLayoutResId, null);

        MoneyItem item = mMoneyItemList.get(position);

        ImageView mImageView = itemLayout.findViewById(R.id.imageView);
        TextView mTitleTextView = itemLayout.findViewById(R.id.menu_text_view);
        TextView mMoneyTextView = itemLayout.findViewById(R.id.money_text_view);

        mTitleTextView.setText(item.title);
        mMoneyTextView.setText(item.money);

        String pictureFileName = item.picture;

        AssetManager am = mContext.getAssets();
        try {
            InputStream stream = am.open(pictureFileName);
            Drawable drawable = Drawable.createFromStream(stream, null);
            mImageView.setImageDrawable(drawable);

        } catch (IOException e) {
            e.printStackTrace();

            File pictureFile = new File(mContext.getFilesDir(), pictureFileName);
            Drawable drawable = Drawable.createFromPath(pictureFile.getAbsolutePath());
            mImageView.setImageDrawable(drawable);
        }

        return itemLayout;
    }

}
