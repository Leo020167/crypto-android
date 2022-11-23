package com.bitcnew.module.myhome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitcnew.module.myhome.entity.ImageGroup;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.baseadapter.BaseImageLoaderAdapter;
import com.bitcnew.R;
import com.bitcnew.module.myhome.entity.ImageSelectGroup;
import com.bitcnew.util.InflaterUtils;


public class MyhomePopSelectAdapter extends BaseImageLoaderAdapter<ImageGroup> {

    private Context context;
//    protected DisplayImageOption;

    private Group<ImageSelectGroup> imageSelectGroups;

    public MyhomePopSelectAdapter(Context context) {
        super();
        this.context = context;
//       = new DisplayImageOptions.Builder()
////				.showImageOnLoading(R.drawable.ic_head_default_photo).showImageForEmptyUri(R.drawable.ic_head_default_photo).showImageOnFail(R.drawable.ic_head_default_photo)
//                .cacheInMemory(true).considerExifParams(false).postProcessor(new BitmapProcessor() {
//                    @Override
//                    public Bitmap process(Bitmap bmp) {
//                        return Bitmap.createScaledBitmap(bmp, 80, 80, false);
//                    }
//                }).build();
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public void setData(Group<ImageGroup> imageGroups, Group<ImageSelectGroup> imageSelectGroups) {
        super.setGroup(imageGroups);
        this.imageSelectGroups = imageSelectGroups;
    }

    @Override
    public ImageGroup getItem(int position) {
        return super.getItem(position - 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = InflaterUtils.inflateView(context, R.layout.myhome_pop_select_item);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setData(position);

        return convertView;
    }

    class ViewHolder {

        ImageView ivPhoto;
        TextView tvName;
        TextView tvDesc;

        private ViewHolder(View view) {
            ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        }

        public void setData(int position) {
            if (position == 0) {
                if (imageSelectGroups != null) {
                    displayImage("file://" + imageSelectGroups.get(0).getPathStr(), ivPhoto);
                    tvName.setText(context.getResources().getString(R.string.quanbutupian));
                    tvDesc.setText(context.getResources().getString(R.string.gong) + (imageSelectGroups.size()) + context.getResources().getString(R.string.zhang));
                }

            } else {
                ImageGroup item = getItem(position);
                if (item != null) {
                    displayImage("file://" + item.getFirstImgPath(), ivPhoto);
//				displayImage(item.getFirstImgPath(),ivPhoto);
                    tvName.setText(item.getDirName());
                    tvDesc.setText("共" + (item.getImages().size()) + "张");
                }
            }

        }
    }

}
