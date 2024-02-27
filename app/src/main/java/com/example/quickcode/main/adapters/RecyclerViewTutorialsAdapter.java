package com.example.quickcode.main.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcode.databinding.ListItemMainTutorialsBinding;
import com.example.quickcode.main.data.ProgrammingTutorialData;
import com.example.quickcode.main.viewHolders.ViewHolderTutorials;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewTutorialsAdapter extends RecyclerView.Adapter<ViewHolderTutorials> {

    private List<ProgrammingTutorialData> programmingTutorialDataList = new ArrayList<>();

    public void setTutorialData(List<ProgrammingTutorialData> programmingTutorialDataList) {
        this.programmingTutorialDataList = programmingTutorialDataList;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderTutorials onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTutorials(ListItemMainTutorialsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTutorials holder, int position) {
        holder.biding.tutorialPreview.setImageResource(programmingTutorialDataList.get(position).getImage());

        String languageName = programmingTutorialDataList.get(position).getName();
        holder.biding.programmingLanguageName.setText(languageName);

        String tutorialDescription = programmingTutorialDataList.get(position).getDescription();
        holder.biding.tutorialDescription.setText(tutorialDescription);
    }

    @Override
    public int getItemCount() {
        return programmingTutorialDataList.size();
    }
}
