package com.example.quickcode.main.viewHolders;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcode.databinding.ListItemMainLanguagesBinding;

public class ViewHolderLanguages extends RecyclerView.ViewHolder {

    public final ListItemMainLanguagesBinding biding;

    public ViewHolderLanguages(ListItemMainLanguagesBinding itemView) {
        super(itemView.getRoot());
        biding = itemView;
    }
}
