package com.example.quickcode.main.viewHolders;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcode.databinding.ListItemMainLanguagesBinding;
import com.example.quickcode.databinding.ListItemMainTutorialsBinding;

public class ViewHolderTutorials extends RecyclerView.ViewHolder {

    public final ListItemMainTutorialsBinding biding;

    public ViewHolderTutorials(ListItemMainTutorialsBinding itemView) {
        super(itemView.getRoot());
        biding = itemView;
    }
}
