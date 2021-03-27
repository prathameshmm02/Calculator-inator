package com.example.calculator.fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.calculator.Data.ConvertData;
import com.example.calculator.Model.Measure;
import com.example.calculator.R;
import com.google.android.material.chip.ChipGroup;


public class ConverterFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    View view;
    Spinner unit1, unit2;
    EditText editText1, editText2;
    TextWatcher textWatcher1, textWatcher2;
    ArrayAdapter<String> unitAdapter;
    Measure currentMeasure;
    ChipGroup chipGroup;

    public ConverterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_converter, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        currentMeasure = ConvertData.getLength();
        setUpSpinners();
        return view;
    }


    private void setUpSpinners() {
        unit1 = view.findViewById(R.id.unit1);
        unit2 = view.findViewById(R.id.unit2);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        chipGroup = view.findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.length) {
                currentMeasure = ConvertData.getLength();
            } else if (checkedId == R.id.mass) {
                currentMeasure = ConvertData.getMass();
            } else if (checkedId == R.id.area) {
                currentMeasure = ConvertData.getArea();
            } else if (checkedId == R.id.speed) {
                currentMeasure = ConvertData.getSpeed();
            } else if (checkedId == R.id.angle) {
                currentMeasure = ConvertData.getAngle();
            } else if (checkedId == R.id.data) {
                currentMeasure = ConvertData.getData();
            } else if (checkedId == R.id.time) {
                currentMeasure = ConvertData.getTime();
            } else if (checkedId == R.id.volume) {
                currentMeasure = ConvertData.getVolume();
            } else if (checkedId == R.id.temperature) {
                currentMeasure = ConvertData.getTemperature();
            } else if (checkedId == R.id.currency) {
                currentMeasure = ConvertData.getCurrency();
            }
            setUpSpinnerAdapter();

            textWatcher1.afterTextChanged(editText1.getText());
        });
        setUpSpinnerAdapter();


        textWatcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double toMain, fromMain;
                if (s.toString().isEmpty()) {
                    editText2.removeTextChangedListener(textWatcher2);
                    editText2.setText("0");
                    editText2.addTextChangedListener(textWatcher2);
                    return;
                }
                double input = Double.parseDouble(s.toString());
                int selectedOne = unit1.getSelectedItemPosition();
                int selectedTwo = unit2.getSelectedItemPosition();
                if (currentMeasure.getName().equals("Temperature")) {
                    if (unit1.getSelectedItem().toString().equals("Celsius")) {
                        toMain = input;
                    } else if (unit1.getSelectedItem().toString().equals("Fahrenheit")) {
                        toMain = (input - 32) * (5 / 9.0d);
                    } else {
                        toMain = input - 273.15;
                    }
                    if (unit2.getSelectedItem().toString().equals("Celsius")) {
                        fromMain = toMain;
                    } else if (unit2.getSelectedItem().toString().equals("Fahrenheit")) {
                        fromMain = toMain * (9 / 5.0d) + 32;
                    } else {
                        fromMain = toMain + 273.15;
                    }
                } else {
                    toMain = input * currentMeasure.getToMain()[selectedOne];
                    fromMain = toMain * currentMeasure.getFromMain()[selectedTwo];
                }

                editText2.removeTextChangedListener(textWatcher2);
                String result = String.valueOf(fromMain);
                if (String.valueOf(fromMain).endsWith(".0")) {
                    editText2.setText(result.substring(0, result.length() - 2));
                } else {
                    editText2.setText(String.valueOf(fromMain));
                }
                editText2.addTextChangedListener(textWatcher2);
            }
        };
        textWatcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double toMain, fromMain;

                if (s.toString().isEmpty()) {
                    editText1.removeTextChangedListener(textWatcher1);
                    editText1.setText("0");
                    editText1.addTextChangedListener(textWatcher1);
                    return;
                }
                double input = Double.parseDouble(s.toString());
                int selectedOne = unit1.getSelectedItemPosition();
                int selectedTwo = unit2.getSelectedItemPosition();
                if (currentMeasure.getName().equals("Temperature")) {
                    if (unit2.getSelectedItem().toString().equals("Celsius")) {
                        toMain = input;
                    } else if (unit2.getSelectedItem().toString().equals("Fahrenheit")) {
                        toMain = (input - 32) * (5 / 9.0d);
                    } else {
                        toMain = input - 273.15;
                    }
                    if (unit1.getSelectedItem().toString().equals("Celsius")) {
                        fromMain = toMain;
                    } else if (unit1.getSelectedItem().toString().equals("Fahrenheit")) {
                        fromMain = toMain * (9 / 5.0d) + 32;
                    } else {
                        fromMain = toMain + 273.15;
                    }
                } else {
                    toMain = input * currentMeasure.getToMain()[selectedTwo];
                    fromMain = toMain * currentMeasure.getFromMain()[selectedOne];
                }

                editText1.removeTextChangedListener(textWatcher1);
                String result = String.valueOf(fromMain);
                if (String.valueOf(fromMain).endsWith(".0")) {
                    editText1.setText(result.substring(0, result.length() - 2));
                } else {
                    editText1.setText(String.valueOf(fromMain));
                }
                editText1.addTextChangedListener(textWatcher1);
            }
        };

        editText1.addTextChangedListener(textWatcher1);
        editText2.addTextChangedListener(textWatcher2);


        unit1.setOnItemSelectedListener(this);
        unit2.setOnItemSelectedListener(this);


    }

    void setUpSpinnerAdapter() {
        unitAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, currentMeasure.getUnits());
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit1.setAdapter(unitAdapter);
        unit2.setAdapter(unitAdapter);
        unit2.setSelection(1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        textWatcher1.afterTextChanged(editText1.getText());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}