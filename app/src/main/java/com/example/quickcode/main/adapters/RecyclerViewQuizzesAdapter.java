package com.example.quickcode.main.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcode.databinding.ListQuizzesMainBinding;
import com.example.quickcode.main.models.ProgrammingQuizzesData;
import com.example.quickcode.main.viewHolders.ViewHolderQuizzes;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewQuizzesAdapter extends RecyclerView.Adapter<ViewHolderQuizzes> {

    private List<ProgrammingQuizzesData> programmingQuizzesDataList = new ArrayList<>();

    public void setData(List<ProgrammingQuizzesData> programmingQuizzesDataList) {
        this.programmingQuizzesDataList = programmingQuizzesDataList;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderQuizzes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderQuizzes(ListQuizzesMainBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderQuizzes holder, int position) {
        holder.biding.imageLanguage.setImageResource(programmingQuizzesDataList.get(position).getImage());

        String languageName = programmingQuizzesDataList.get(position).getTitle();
        holder.biding.textLanguage.setText(languageName);

        String tutorialDescription = programmingQuizzesDataList.get(position).getDescription();
        holder.biding.amountOfQuizzes.setText(tutorialDescription);
    }

    @Override
    public int getItemCount() {
        return programmingQuizzesDataList.size();
    }
}
