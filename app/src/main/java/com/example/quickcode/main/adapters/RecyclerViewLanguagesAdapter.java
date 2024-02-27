package com.example.quickcode.main.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcode.databinding.ListItemMainLanguagesBinding;
import com.example.quickcode.main.data.ProgrammingLanguageData;
import com.example.quickcode.main.viewHolders.ViewHolderLanguages;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewLanguagesAdapter extends RecyclerView.Adapter<ViewHolderLanguages> {

    private List<ProgrammingLanguageData> programmingLanguageData = new ArrayList<>();

    public void setLanguageData(List<ProgrammingLanguageData> programmingLanguageData) {
        this.programmingLanguageData = programmingLanguageData;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderLanguages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderLanguages(ListItemMainLanguagesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLanguages holder, int position) {
        holder.biding.programmingLanguageIcon.setImageResource(programmingLanguageData.get(position).getImage());

        String languageName = programmingLanguageData.get(position).getName();
        holder.biding.programmingLanguageName.setText(languageName);
    }

    @Override
    public int getItemCount() {
        return programmingLanguageData.size();
    }
}
