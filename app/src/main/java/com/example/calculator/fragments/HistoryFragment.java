package com.example.calculator.fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculator.Model.History;
import com.example.calculator.R;
import com.example.calculator.adpters.HistoryAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    static boolean isHistoryOpen = false;
    int fullHeight;
    int height;
    ArrayList<History> historyItems;
    float newY;
    float oldY;
    RecyclerView recyclerView;
    int screenHeight;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.historyRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TypedValue typedValue = new TypedValue();
        requireActivity().getTheme().resolveAttribute(R.attr.colorOnPrimary, typedValue, true);
        int color = typedValue.data;
        try {
            getHistory();
        } catch (IOException ignored) {
        }
        recyclerView.setAdapter(new HistoryAdapter(getContext(), historyItems));
        recyclerView.scrollToPosition(historyItems.size() - 1);
        recyclerView.setAlpha(0.0f);
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = container.getHeight();
                fullHeight = ((ViewGroup) container.getParent()).getHeight();
            }
        });
        DisplayMetrics metrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;


        view.findViewById(R.id.dragger).setOnTouchListener((v, event) -> {
            container.setBackgroundColor(color);
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                oldY = event.getRawY();
            } else if (action == MotionEvent.ACTION_UP) {
                newY = event.getRawY();
                if (isHistoryOpen) {
                    if (oldY - newY > ((float) screenHeight) / 3.0f) {
                        isHistoryOpen = false;
                        container.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        changeHeight(container, container.getHeight(), height);
                        changeInputState(true);
                    } else {
                        container.setBackgroundColor(color);
                        changeHeight(container, container.getHeight(), fullHeight);
                    }
                } else {
                    if (newY - oldY > ((float) screenHeight) / 3.0f) {
                        isHistoryOpen = true;
                        container.setBackgroundColor(color);
                        changeHeight(container, container.getHeight(), fullHeight);
                        changeInputState(false);

                    } else {
                        container.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        changeHeight(container, container.getHeight(), height);
                    }
                }
                if (isHistoryOpen) {
                    recyclerView.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .start();
                } else {
                    recyclerView.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .start();
                }
            } else if (action == MotionEvent.ACTION_MOVE) {
                newY = event.getRawY();
                if (isHistoryOpen) {
                    if (newY > oldY) {
                        return true;
                    } else if (fullHeight - oldY + newY > height) {
                        container.setLayoutParams(new ConstraintLayout.LayoutParams(container.getWidth(), (int) (fullHeight - oldY + newY)));
                    }

                } else {

                    if (newY < oldY) {
                        return true;
                    } else if (height + newY - oldY < fullHeight) {
                        container.setLayoutParams(new ConstraintLayout.LayoutParams(container.getWidth(), (int) (height + newY - oldY)));
                    }
                }
                float alpha = ((float) (container.getHeight() - height)) / ((float) (fullHeight - height));
                recyclerView.setAlpha(alpha);
                Log.i("Alpha", String.valueOf(alpha));
            }

            v.performClick();
            return true;
        });
        return view;
    }

    private void getHistory() throws IOException {
        historyItems = new ArrayList<>();
        File file = new File(requireContext().getFilesDir(), "History.pm");
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while (true) {
                String readLine = br.readLine();
                if (readLine != null) {
                    historyItems.add(new History(readLine, br.readLine()));
                } else {
                    return;
                }
            }
        }
    }

    private void changeHeight(View v, int oldHeight, int newHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(oldHeight, newHeight);
        animator.addUpdateListener(valueAnimator -> v.setLayoutParams(new ConstraintLayout.LayoutParams(v.getWidth(), (Integer) animator.getAnimatedValue())));
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    public void changeInputState(boolean isVisible) {
        if (isVisible) {
            requireActivity().findViewById(R.id.input).setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.input).requestFocus();
        } else {
            requireActivity().findViewById(R.id.input).setVisibility(View.INVISIBLE);
        }
    }
}
