package com.priyadarshan.donoralert.Adapter;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.priyadarshan.donoralert.Adapter.SearchAdapter.ViewHolder;
import com.priyadarshan.donoralert.DataModels.Donor;
import com.priyadarshan.donoralert.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Donor> dataSet;
    private Context context;

    public SearchAdapter(
            List<Donor> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_layout  , parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        String str = "Name: "+dataSet.get(position).getName();
        str+="\nCity: "+dataSet.get(position).getCity();

        holder.message.setText(str);

        holder.call.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if(PermissionChecker.checkSelfPermission(context, permission.CALL_PHONE ) == PermissionChecker.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + dataSet.get(position).getNumber()));
                    context.startActivity(intent);
                }else{
                    ((Activity) context).requestPermissions(new String[]{permission.CALL_PHONE}, 401);
                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        ImageView call, share;

        ViewHolder(final View itemView) {

            super(itemView);
            message = itemView.findViewById(R.id.donor_body);
            call = itemView.findViewById(R.id.donor_call);
            share = itemView.findViewById(R.id.donor_share);

        }

    }

}