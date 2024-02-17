package com.example.quickcode.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quickcode.databinding.FragmentMainBinding;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    public static final String TAG = "MainFragment";
    RecyclerViewLanguagesAdapter languagesAdapter;
    RecyclerViewTutorialsAdapter tutorialsAdapter;
    ArrayList<String> programmingLanguagesName;
    ArrayList<String> programmingLanguagesNameInTutorial;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = FragmentMainBinding.inflate(inflater, container, false);

        programmingLanguagesName = new ArrayList<>();
        programmingLanguagesName.add("Java");
        programmingLanguagesName.add("Python");
        programmingLanguagesName.add("C#");
        programmingLanguagesName.add("C++");
        programmingLanguagesName.add("Html");
        programmingLanguagesName.add("Css");
        programmingLanguagesName.add("Java script");

        programmingLanguagesNameInTutorial = new ArrayList<>();
        programmingLanguagesNameInTutorial.add("Java");
        programmingLanguagesNameInTutorial.add("Python");
        programmingLanguagesNameInTutorial.add("C#");
        programmingLanguagesNameInTutorial.add("C++");
        programmingLanguagesNameInTutorial.add("Html");
        programmingLanguagesNameInTutorial.add("Css");
        programmingLanguagesNameInTutorial.add("Java script");

        binding.recyclerLanguages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true));
        languagesAdapter = new RecyclerViewLanguagesAdapter(requireContext(), programmingLanguagesName);
        binding.recyclerLanguages.setAdapter(languagesAdapter);

        binding.recyclerViewTutorials.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true));
        tutorialsAdapter = new RecyclerViewTutorialsAdapter(requireContext(), programmingLanguagesNameInTutorial);
        binding.recyclerViewTutorials.setAdapter(tutorialsAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
