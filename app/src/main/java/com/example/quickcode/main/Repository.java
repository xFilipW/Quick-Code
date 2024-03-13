package com.example.quickcode.main;

import com.example.quickcode.R;
import com.example.quickcode.main.models.ProgrammingLanguageData;
import com.example.quickcode.main.models.ProgrammingTutorialData;

import java.util.Arrays;
import java.util.List;

public class Repository {

    public final static List<ProgrammingLanguageData> PROGRAMMING_LANGUAGE_DATA_LIST =
            Arrays.asList(
                    new ProgrammingLanguageData(R.drawable.ic_java, "Java"),
                    new ProgrammingLanguageData(R.drawable.ic_python, "Python"),
                    new ProgrammingLanguageData(R.drawable.ic_cs, "C#"),
                    new ProgrammingLanguageData(R.drawable.ic_cpp, "C++"),
                    new ProgrammingLanguageData(R.drawable.ic_android, "Android"),
                    new ProgrammingLanguageData(R.drawable.ic_html, "HTML"),
                    new ProgrammingLanguageData(R.drawable.ic_css, "CSS"),
                    new ProgrammingLanguageData(R.drawable.ic_js, "JS"),
                    new ProgrammingLanguageData(R.drawable.ic_php, "PHP"),
                    new ProgrammingLanguageData(R.drawable.ic_mysql, "MySql")
            );

    public final static List<ProgrammingTutorialData> PROGRAMMING_TUTORIAL_DATA_LIST = Arrays.asList(
            new ProgrammingTutorialData(R.drawable.ic_cpp, "Python", "How to make a simple calculator"),
            new ProgrammingTutorialData(R.drawable.ic_cpp, "C#", "How to make a simple calculator"),
            new ProgrammingTutorialData(R.drawable.ic_cpp, "C++", "How to make a simple calculator"),
            new ProgrammingTutorialData(R.drawable.ic_cpp, "Html", "How to make a simple calculator"),
            new ProgrammingTutorialData(R.drawable.ic_cpp, "Css", "How to make a simple calculator"),
            new ProgrammingTutorialData(R.drawable.ic_cpp, "JavaScript", "How to make a simple calculator"),
            new ProgrammingTutorialData(R.drawable.ic_cpp, "PHP", "How to make a simple calculator"),
            new ProgrammingTutorialData(R.drawable.ic_cpp, "MySql", "How to make a simple calculator")
    );

}
