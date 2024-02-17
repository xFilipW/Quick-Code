package com.example.quickcode.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcode.R;

import java.util.List;

public class RecyclerViewLanguagesAdapter extends RecyclerView.Adapter<RecyclerViewLanguagesAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;

    RecyclerViewLanguagesAdapter(Context context, List<String> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_main_languages, parent, false);
        return new RecyclerViewLanguagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = data.get(position);
        holder.programmingLanguageName.setText(title);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView programmingLanguageName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            programmingLanguageName = itemView.findViewById(R.id.programmingLanguageName);
        }
    }

}
