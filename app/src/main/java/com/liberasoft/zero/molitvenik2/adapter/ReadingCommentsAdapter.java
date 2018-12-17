package com.liberasoft.zero.molitvenik2.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.model.ReadingCommentsModel;

import java.util.List;

public class ReadingCommentsAdapter {

  private Activity activity;
  private List<ReadingCommentsModel> rCommentsList;

  public ReadingCommentsAdapter(Activity activity, List<ReadingCommentsModel> rCommentsList) {
    this.activity = activity;
    this.rCommentsList = rCommentsList;
  }

  public void getRecyclerView(RecyclerView recyclerView) {

    recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayout.VERTICAL, false));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(new CommentRecycler());
  }

  private class CommentRecycler extends RecyclerView.Adapter<CommentRecycler.ViewHolder> {

    // Auto scrolling recycler view to the latest comment posted
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
      super.onAttachedToRecyclerView(recyclerView);
      // If new message is received recyclerView will automatically scroll to that message
      recyclerView.scrollToPosition(rCommentsList.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View view = LayoutInflater.from(activity).inflate(R.layout.recycler_item_comments, viewGroup, false);

      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
      // Adding data to a view item
      h.commentNameView.setText(rCommentsList.get(i).getUsername());
      h.commentDateView.setText(rCommentsList.get(i).getTimeStamp());
      h.commentTextView.setText(rCommentsList.get(i).getMessage());
    }

    @Override
    public int getItemCount() {
      return rCommentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
      private TextView commentTextView, commentNameView, commentDateView;

      ViewHolder(@NonNull View itemView) {
        super(itemView);
        commentNameView = itemView.findViewById(R.id.comment_name_text_view);
        commentDateView = itemView.findViewById(R.id.comment_date_text_view);
        commentTextView = itemView.findViewById(R.id.comment_text_text_view);
      }
    }
  }

}
