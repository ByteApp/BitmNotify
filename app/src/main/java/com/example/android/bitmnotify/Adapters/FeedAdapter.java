package com.example.android.bitmnotify.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bitmnotify.ObjectClasses.Feed;
import com.example.android.bitmnotify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    final private ListItemClickListener mListItemClickListener;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public List<Feed> feedList;
    public Context context;
    boolean like = false;

    public FeedAdapter(List<Feed> list, Context context, ListItemClickListener listener) {
        feedList = list;
        this.context = context;
        mListItemClickListener = listener;
    }

    public interface ListItemClickListener {

        void onListItemClicked(int clickedItemIndex);
        void onUsernameClicked(int clickedItemIndex);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textViewTitle.setText(feedList.get(position).getTitle());
        holder.textViewContent.setText(feedList.get(position).getContent());
        String url = feedList.get(position).getImageUrl();
        Glide.with(context).load(url).centerCrop().crossFade().into(holder.imageView);
        if(user != null){
            String timeFB = feedList.get(position).getTimeStamp().toString();
            Long timeLong = Long.parseLong(timeFB);
            String timePassedString = (String) getRelativeTimeSpanString(timeLong,System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            holder.textViewUsername.setText(feedList.get(position).getUsername());
            holder.textViewTime.setText(timePassedString);
            Glide.with(context).load(feedList.get(position).getDpUrl()).centerCrop().crossFade().into(holder.imageViewDp);
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle;
        TextView textViewContent;
        TextView textViewUsername;
        TextView textViewTime;
        CircleImageView imageViewDp;
        LinearLayout linearLayoutFeed;
        LinearLayout linearLayoutUsername;
        LinearLayout linearLayoutDp;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewTitle = (TextView) itemView.findViewById(R.id.textView_title);
            textViewContent = (TextView) itemView.findViewById(R.id.textView_content);
            textViewUsername = (TextView) itemView.findViewById(R.id.textView_username);
            textViewTime = (TextView) itemView.findViewById(R.id.textView_time);
            imageViewDp = (CircleImageView) itemView.findViewById(R.id.imageView_dp);
            linearLayoutFeed = (LinearLayout) itemView.findViewById(R.id.linearLayout_feed);
            linearLayoutUsername = (LinearLayout) itemView.findViewById(R.id.linearLayout_username);
            linearLayoutDp = (LinearLayout) itemView.findViewById(R.id.layout_dp_username);
            linearLayoutFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mListItemClickListener.onListItemClicked(clickedPosition);
                }
            });
            linearLayoutUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mListItemClickListener.onUsernameClicked(clickedPosition);
                }
            });
            linearLayoutDp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mListItemClickListener.onUsernameClicked(clickedPosition);
                }
            });
        }
    }
}

