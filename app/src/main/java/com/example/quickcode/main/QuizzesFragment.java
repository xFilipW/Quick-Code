package com.example.quickcode.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quickcode.R;
import com.example.quickcode.databinding.FragmentMainQuizzesBinding;
import com.example.quickcode.main.adapters.RecyclerViewQuizzesAdapter;

public class QuizzesFragment extends Fragment {

    public static final String TAG = "QuizzesFragment";

    private FragmentMainQuizzesBinding binding;

    public static QuizzesFragment newInstance() {
        Bundle args = new Bundle();
        QuizzesFragment fragment = new QuizzesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = FragmentMainQuizzesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) binding.toolbar.getLayoutParams();
            layoutParams.topMargin = systemBarsInsets.top;
            binding.toolbar.setLayoutParams(layoutParams);

            return insets;
        });

        binding.recyclerViewTutorials.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        RecyclerViewQuizzesAdapter quizzesAdapter = new RecyclerViewQuizzesAdapter();
        binding.recyclerViewTutorials.addItemDecoration(new SpaceDividerDecorator(requireContext(), R.dimen.spacerMedium));
        quizzesAdapter.setData(Repository.PROGRAMMING_QUIZZES_DATA_LIST);
        binding.recyclerViewTutorials.setAdapter(quizzesAdapter);
    }
}
