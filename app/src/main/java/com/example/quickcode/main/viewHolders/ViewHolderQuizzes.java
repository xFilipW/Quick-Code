package com.example.quickcode.main.viewHolders;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcode.databinding.ListQuizzesMainBinding;

public class ViewHolderQuizzes extends RecyclerView.ViewHolder {

    public final ListQuizzesMainBinding biding;

    public ViewHolderQuizzes(ListQuizzesMainBinding itemView) {
        super(itemView.getRoot());
        biding = itemView;
    }
}
