package com.mcmah113.mcmah113weatherviewer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsDialog extends DialogFragment {
    public interface OnCompleteListener {
        void onComplete(Bundle callbackData);
    }

    public SettingsDialog() {

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final OnCompleteListener dialogCompleteListenerSettings = (OnCompleteListener) getActivity();

        final AlertDialog.Builder dialogSettings = new AlertDialog.Builder(getActivity());

        final LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setPadding(35, 5, 25, 5);

        //radio group of how many unique cards to show
        final String temperatureUnitsList[] = getArguments().getStringArray("unitTemperatureList");
        final String currentUnit = getArguments().getString("currentUnit");
        final String timeUnit = getArguments().getString("currentTime");

        final RadioGroup radioGroup = new RadioGroup(getContext());

        String radioText;
        int i = 0;

        final CheckBox checkBox = new CheckBox(getContext());
        checkBox.setText("24h Time");
        checkBox.setPadding(0,0,0,15);
        checkBox.setTextSize(20);

        if(timeUnit.equals("24h")) {
            checkBox.setChecked(true);
        }

        for(String unit : temperatureUnitsList) {
            RadioButton radioButton = new RadioButton(getContext());
            if(currentUnit.equals(unit)) {
                radioButton.setChecked(true);
            }

            radioText = unit;
            radioButton.setText(radioText);
            radioButton.setTextSize(20);
            radioButton.setId(i ++);
            radioGroup.addView(radioButton);

            i++;
        }

        linearLayout.addView(checkBox);
        linearLayout.addView(radioGroup);

        dialogSettings.setView(linearLayout);
        dialogSettings.setTitle("Settings");
        dialogSettings.setMessage("Select your preferred measurement system and clock time");
        dialogSettings.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();
                bundle.putString("response", "Apply");

                if(checkBox.isChecked()) {
                    bundle.putString("currentTime", "24h");
                }
                else {
                    bundle.putString("currentTime", "12h");
                }

                RadioButton tempRadioButton = linearLayout.findViewById(radioGroup.getCheckedRadioButtonId());
                bundle.putString("currentUnit", tempRadioButton.getText().toString());
                dialogCompleteListenerSettings.onComplete(bundle);
            }
        });
        dialogSettings.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();
                bundle.putString("response", "Cancel");
                bundle.putString("currentTime", timeUnit);
                bundle.putString("currentUnit", currentUnit);
                dialogCompleteListenerSettings.onComplete(bundle);
            }
        });

        return dialogSettings.create();
    }
}
