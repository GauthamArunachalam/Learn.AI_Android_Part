package com.example.gauthama.learnai;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by GAUTHAM A on 25-01-2018.
 */

public class Tab2_frag extends Fragment {
    private static final String TAG = "Tab1Fragement";

    Button quiz, check, next;
    TextView questionTitle, question, op1, op2, op3, op4, ans;
    RadioGroup rg;
    RadioButton rb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        quiz = (Button) view.findViewById(R.id.quiz);
        check = (Button)view.findViewById(R.id.check);
        next = (Button)view.findViewById(R.id.next);
        questionTitle = (TextView)view.findViewById(R.id.questionTitle);
        question = (TextView)view.findViewById(R.id.question);
        op1 = (TextView)view.findViewById(R.id.op1);
        op2 = (TextView)view.findViewById(R.id.op2);
        op3 = (TextView)view.findViewById(R.id.op3);
        op4 = (TextView)view.findViewById(R.id.op4);
        rg = (RadioGroup)view.findViewById(R.id.rbgroup);

        check.setVisibility(view.INVISIBLE);
        next.setVisibility(view.INVISIBLE);
        questionTitle.setVisibility(view.INVISIBLE);
        question.setVisibility(view.INVISIBLE);
        rg.setVisibility(view.INVISIBLE);
        op1.setVisibility(view.INVISIBLE);
        op2.setVisibility(view.INVISIBLE);
        op3.setVisibility(view.INVISIBLE);
        op4.setVisibility(view.INVISIBLE);

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.setVisibility(view.VISIBLE);
                next.setVisibility(view.VISIBLE);
                questionTitle.setVisibility(view.VISIBLE);
                question.setVisibility(view.VISIBLE);
                rg.setVisibility(view.VISIBLE);
                op1.setVisibility(view.VISIBLE);
                op2.setVisibility(view.VISIBLE);
                op3.setVisibility(view.VISIBLE);
                op4.setVisibility(view.VISIBLE);

            }
        });



        return view;
    }

}
