package com.example.calculator.fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.calculator.Model.Calculator;
import com.example.calculator.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class CalculatorFragment extends Fragment implements View.OnClickListener {
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, addButton, subButton, multiButton, divButton, equalButton, pointButton, clearButton;
    Button inverseButton, angleButton, percentButton, sinButton, cosButton, tanButton, naturalLogButton, logButton, rootButton, piButton, eulerButton, powerButton, openBracket, closeBracket, factorialButton;
    Button slideButton;
    ConstraintLayout parent;
    EditText input;
    final List<Character> operators = Arrays.asList('^', '%', '÷', '×', '+', '-');
    final List<String> extraOperators = Arrays.asList("sin", "cos", "tan", "log", "ln", "sin⁻¹", "cos⁻¹", "tan⁻¹");
    final List<String> powOperators = Arrays.asList("^2", "e^", "10^");
    TextView output;
    TableLayout tableLayout;
    SlidingPaneLayout slidingPaneLayout;
    boolean isInvert = false, isDegree = true;
    final List<Character> numbers = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    ImageView backgroundImage;
    View view;

    public CalculatorFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calculator, container, false);
        setUpViews();
        addListeners();
        this.requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.historyContainer, new HistoryFragment())
                .commit();
        return view;
    }


    private void setUpViews() {
        // This was the most boring part of my life ;)
        button0 = view.findViewById(R.id.button_0);
        button1 = view.findViewById(R.id.button_1);
        button2 = view.findViewById(R.id.button_2);
        button3 = view.findViewById(R.id.button_3);
        button4 = view.findViewById(R.id.button_4);
        button5 = view.findViewById(R.id.button_5);
        button6 = view.findViewById(R.id.button_6);
        button7 = view.findViewById(R.id.button_7);
        button8 = view.findViewById(R.id.button_8);
        button9 = view.findViewById(R.id.button_9);
        addButton = view.findViewById(R.id.add_button);
        subButton = view.findViewById(R.id.sub_button);
        multiButton = view.findViewById(R.id.mul_button);
        divButton = view.findViewById(R.id.divide_button);
        equalButton = view.findViewById(R.id.equal_button);
        pointButton = view.findViewById(R.id.button_point);
        clearButton = view.findViewById(R.id.clear_button);

        inverseButton = view.findViewById(R.id.button_inverse);
        angleButton = view.findViewById(R.id.button_angle);
        percentButton = view.findViewById(R.id.button_percent);
        sinButton = view.findViewById(R.id.button_sin);
        cosButton = view.findViewById(R.id.button_cos);
        tanButton = view.findViewById(R.id.button_tan);
        naturalLogButton = view.findViewById(R.id.button_log_natural);
        logButton = view.findViewById(R.id.button_log_10);
        rootButton = view.findViewById(R.id.button_root);
        piButton = view.findViewById(R.id.button_pi);
        eulerButton = view.findViewById(R.id.button_e);
        powerButton = view.findViewById(R.id.button_power);
        openBracket = view.findViewById(R.id.button_open_bracket);
        closeBracket = view.findViewById(R.id.button_close_bracket);
        factorialButton = view.findViewById(R.id.button_factorial);

        input = view.findViewById(R.id.input);
        output = view.findViewById(R.id.output);
        parent = view.findViewById(R.id.parentLayout);
        tableLayout = view.findViewById(R.id.tableLayout);
        slideButton = view.findViewById(R.id.slide_button);
        slidingPaneLayout = view.findViewById(R.id.slidingPaneLayout);
        backgroundImage = view.findViewById(R.id.bg_image);

        setBackground();
        slidingPaneLayout.openPane();
        slidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        // Don't show keyboard on focus/click
        input.requestFocus();
        input.setShowSoftInputOnFocus(false);
    }


    private void addListeners() {
        // Adding OnClick Listener
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        addButton.setOnClickListener(this);
        subButton.setOnClickListener(this);
        multiButton.setOnClickListener(this);
        divButton.setOnClickListener(this);
        pointButton.setOnClickListener(this);

        percentButton.setOnClickListener(this);
        sinButton.setOnClickListener(this);
        cosButton.setOnClickListener(this);
        tanButton.setOnClickListener(this);
        naturalLogButton.setOnClickListener(this);
        logButton.setOnClickListener(this);
        rootButton.setOnClickListener(this);
        piButton.setOnClickListener(this);
        eulerButton.setOnClickListener(this);
        powerButton.setOnClickListener(this);
        openBracket.setOnClickListener(this);
        closeBracket.setOnClickListener(this);
        factorialButton.setOnClickListener(this);
        inverseButton.setOnClickListener(v -> {
            if (isInvert) {
                isInvert = false;
                inverseButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                sinButton.setText(R.string.sin);
                cosButton.setText(R.string.cos);
                tanButton.setText(R.string.tan);
                rootButton.setText(R.string.root);
                naturalLogButton.setText(R.string.natural_log);
                logButton.setText(R.string.log);
            } else {
                sinButton.setText(R.string.asin);
                cosButton.setText(R.string.acos);
                tanButton.setText(R.string.atan);
                rootButton.setText(R.string.square);
                naturalLogButton.setText(R.string.eRaisedTo);
                logButton.setText(R.string.tenRaisedTo);
                isInvert = true;
                inverseButton.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        });
        angleButton.setOnClickListener(v -> {
            if (isDegree) angleButton.setText(getResources().getString(R.string.radian));
            else angleButton.setText(getResources().getString(R.string.degree));
            isDegree = !isDegree;
        });

        equalButton.setOnClickListener(v -> {
            try{
                String expr = input.getText().toString();
                if (operators.contains(expr.charAt(expr.length() - 1))) {
                    return;
                }
                if (!expr.isEmpty()) {
                    Calculator calc = new Calculator();
                    BigDecimal result = calc.eval(expr, isDegree);
                    input.setText(result.stripTrailingZeros().toPlainString());
                    input.setSelection(input.getText().toString().length());
                    saveToHistory(expr, result.toPlainString());
                }
                output.setText("");
                input.setSelection(input.getText().length());
            } catch (Exception e){
                input.setText(R.string.error_text);
            }

        });

        clearButton.setOnClickListener(v -> {
            int position = input.getSelectionStart();
            if (!(input.getText().toString().isEmpty() || input.getSelectionStart() == 0)) {
                Editable oldInput = input.getText();
                oldInput.delete(position - 1, position);
            }
        });

        clearButton.setOnLongClickListener(v -> {
            input.setText("");
            output.setText("");
            return true;
        });
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String expr = s.toString();
                try {
                    if (expr.isEmpty()) {
                        output.setText("");
                    } else if (!(operators.contains(expr.charAt(expr.length() - 1)))) {
                        Calculator calc = new Calculator();
                        BigDecimal result = calc.eval(expr, isDegree);
                        output.setText(result.stripTrailingZeros().toPlainString());
                    }
                } catch (Exception e) {
                    output.setText("");
                }
            }
        });

        slideButton.setOnClickListener(v -> {
            if (slidingPaneLayout.isOpen()) {
                slidingPaneLayout.closePane();
            } else {
                slidingPaneLayout.openPane();
            }
        });

        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            final Animation move = AnimationUtils.loadAnimation(getContext(), R.anim.move);
            final Animation rotateFirst = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_first);
            final Animation rotateNext = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_next);

            @Override
            public void onPanelSlide(@NonNull View panel, float slideOffset) {
                slideButton.startAnimation(move);
            }

            @Override
            public void onPanelOpened(@NonNull View panel) {
                slideButton.clearAnimation();
            }

            @Override
            public void onPanelClosed(@NonNull View panel) {
                slideButton.clearAnimation();
                slideButton.startAnimation(rotateFirst);
                parent.setClickable(false);
            }
        });


    }
    private void saveToHistory(String expression, String answer) {
        FileWriter writer;
        File history = new File(requireContext().getFilesDir(), "History.pm");
        try {
            if (history.createNewFile()) {
                writer = new FileWriter(history);
            } else {
                writer = new FileWriter(history, true);
            }
            writer.write(expression + "\n" + answer + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
    }


    @Override
    public void onClick(View v) {
        if (input.getText().toString().equals("Not a Number")) {
            input.setText("");
        }
        String buttonText = (String) ((Button) v).getText();
        int currentPosition = input.getSelectionStart();
        String oldInput = input.getText().toString();
        boolean[] validateInput = Calculator.validateInput(currentPosition, buttonText, oldInput);
        if (validateInput[0] && validateInput[1]) {
            String buttonText2 = buttonText + "()";
            input.setText(oldInput.substring(0, currentPosition) + buttonText2 + oldInput.substring(currentPosition));
            input.setSelection((buttonText2.length() + currentPosition) - 1);
        } else if (validateInput[0]) {
            input.setText(oldInput.substring(0, currentPosition) + buttonText + oldInput.substring(currentPosition));
            input.setSelection(buttonText.length() + currentPosition);
        }
    }


    private void setBackground() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String path = prefs.getString("background", "null");
        int blurRadius = prefs.getInt("blur", 0);

        if (prefs.getBoolean("customBG", false) && !path.equals("null")) {
            if (blurRadius != 0) {
                Glide.with(this).load(Uri.parse(path)).apply(RequestOptions.bitmapTransform(new BlurTransformation(blurRadius))).into(backgroundImage);
            } else {
                Glide.with(this).load(Uri.parse(path)).into(this.backgroundImage);
            }

        }
    }

    @Override
    public void onStart() {
        setBackground();
        super.onStart();
    }
}
