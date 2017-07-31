package me.asl.assel.bakingapp.Presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.asl.assel.bakingapp.R;
import me.asl.assel.bakingapp.RecipeFragmentActivity;
import me.asl.assel.bakingapp.model.Recipe;

/**
 * Created by assel on 7/18/17.
 */

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.Holder> {
    private List<Recipe> list;
    private Context mContext;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final Recipe data = list.get(position);
        holder.mTitle.setText(data.getName());
        holder.count.setText(String.valueOf(data.getId()));
        holder.mThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 7/21/17 go to another activity
                Log.d("CLICK", "click on = "+data.getName());
                Intent i = new Intent(mContext, RecipeFragmentActivity.class);
                i.putExtra("recipe", data);
                mContext.startActivity(i);
            }
        });

        if(!data.getImage().equals("")) {
            Picasso.with(mContext)
                    .load(data.getImage())
                    .into(holder.mThumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.noImg.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                           imageNotLoad(holder);
                        }
                    });
        } else imageNotLoad(holder);


    }

    private void imageNotLoad(Holder holder) {
        Picasso.with(mContext)
                .load(R.drawable.unbaked)
                .into(holder.mThumbnail);
        holder.noImg.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public RecipeCardAdapter(List<Recipe> list) {
        this.list = list;
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView mThumbnail;
        TextView mTitle;
        TextView count;
        TextView noImg;

        Holder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.title);
            count = (TextView)itemView.findViewById(R.id.count);
            mThumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
            noImg = (TextView)itemView.findViewById(R.id.textView_noImg);
        }

    }
}
