package net.errol.emart.lib;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.errol.emart.Index;
import net.errol.emart.R;
import net.errol.emart.tasks.RetrieveBitmapFromURL;

import java.util.ArrayList;

/**
 * Created by JasmineGayle on 3/10/2015.
 */
public class ImageListAdapter extends BaseAdapter {

    private ArrayList data;
    private Activity activity;
    private static LayoutInflater inflater = null;

    ImageListModel model = null;

    public ImageListAdapter(Activity activity, ArrayList data){
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(data.size()<=0){
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder{
        public ImageView img;
        public TextView ProductName;
        public TextView ProductPrice;

        ViewHolder(ImageView i, TextView pn, TextView pp){
            img = i;
            ProductName = pn;
            ProductPrice = pp;
        }
    }

    private class OnItemClickListener implements View.OnClickListener {

        private int pos;

        OnItemClickListener(int _pos){
            pos = _pos;
        }

        @Override
        public void onClick(View v) {
            Index i = (Index) activity;
            i.OnListItemClick(pos);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if(convertView==null){
            view = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(
                    (ImageView)view.findViewById(R.id.ProductPicture),
                    (TextView)view.findViewById(R.id.ProductName),
                    (TextView)view.findViewById(R.id.ProductDesc)
            );
            view.setTag(holder);
        }
        else
            holder = (ViewHolder)view.getTag();
        if(data.size()==0){
            holder.ProductName.setText("NO DATA");
        }
        else{
            model = null;
            model = (ImageListModel)data.get(position);
            try{
                holder.img.setImageBitmap(new RetrieveBitmapFromURL().execute(model.getImgUrl()).get());
            }
            catch(Exception ex){
                Log.e("Bitmap Retrieve Error: ", ex.getMessage());
                ex.printStackTrace();
            }
            holder.ProductName.setText(model.getProductName());
            holder.ProductPrice.setText(model.getProductPrice());
            view.setOnClickListener(new OnItemClickListener(position));
        }
        return view;
    }
}
