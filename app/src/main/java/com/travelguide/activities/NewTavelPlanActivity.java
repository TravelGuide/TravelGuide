package com.travelguide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.travelguide.R;

public class NewTavelPlanActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    //declare variables
    SeekBar seekbar1; //Your SeekBar
    int value;        //The SeekBar value output
    TextView result;  //The TextView which will display the result

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tavel_plan);
        seekbar1 = (SeekBar)findViewById(R.id.sb_TravelDays);
        result = (TextView)findViewById(R.id.tvResult);
        seekbar1.setOnSeekBarChangeListener(this);
        //Spinner for Month
        Spinner spinner = (Spinner) findViewById(R.id.sp_Months);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.label_month, R.layout.spinner_style);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter1);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        value = progress;
        result.setText (value+" "+"  Days "      );

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

//    public void travellerType(View view) {
//
//        switch (view.getId()){
//            case R.id.iv_Male:
//                RelativeLayout maleIcon = (RelativeLayout) findViewById(R.id.rl_Male);
//                maleIcon.setBackgroundColor(Color.RED);
//
//                //Reset
//                RelativeLayout fI = (RelativeLayout) findViewById(R.id.rl_Female);
//                fI.setBackgroundColor(Color.TRANSPARENT);
//                RelativeLayout cI = (RelativeLayout) findViewById(R.id.rl_CoupleImage);
//                cI.setBackgroundColor(Color.TRANSPARENT);
//                RelativeLayout faI = (RelativeLayout) findViewById(R.id.rl_Family);
//                faI.setBackgroundColor(Color.TRANSPARENT);
//
//                break;
//            case R.id.iv_Female:
//                RelativeLayout femaleIcon = (RelativeLayout) findViewById(R.id.rl_Female);
//                femaleIcon.setBackgroundColor(Color.RED);
//
//
//                //Reset
//                RelativeLayout maleI = (RelativeLayout) findViewById(R.id.rl_Male);
//                maleI.setBackgroundColor(Color.TRANSPARENT);
//                RelativeLayout coupleI = (RelativeLayout) findViewById(R.id.rl_CoupleImage);
//                coupleI.setBackgroundColor(Color.TRANSPARENT);
//                RelativeLayout familyI = (RelativeLayout) findViewById(R.id.rl_Family);
//                familyI.setBackgroundColor(Color.TRANSPARENT);
//
//                break;
//            case R.id.iv_Couple:
//                RelativeLayout coupleIcon = (RelativeLayout) findViewById(R.id.rl_CoupleImage);
//                coupleIcon.setBackgroundColor(Color.RED);
//
//                //Reset
//                RelativeLayout maleIconReset = (RelativeLayout) findViewById(R.id.rl_Male);
//                maleIconReset.setBackgroundColor(Color.TRANSPARENT);
//                RelativeLayout femaleIconReset = (RelativeLayout) findViewById(R.id.rl_Female);
//                femaleIconReset.setBackgroundColor(Color.TRANSPARENT);
//                RelativeLayout familyIconReset = (RelativeLayout) findViewById(R.id.rl_Family);
//                familyIconReset.setBackgroundColor(Color.TRANSPARENT);
//
//
//                break;
//            case R.id.iv_Family:
//                RelativeLayout familyIcon = (RelativeLayout) findViewById(R.id.rl_Family);
//                familyIcon.setBackgroundColor(Color.RED);
//
//                //Reset
//                RelativeLayout maleIconResetBack = (RelativeLayout) findViewById(R.id.rl_Male);
//                maleIconResetBack.setBackgroundColor(Color.TRANSPARENT);
//                RelativeLayout femaleIconResetBack = (RelativeLayout) findViewById(R.id.rl_Female);
//                femaleIconResetBack.setBackgroundColor(Color.TRANSPARENT);
//                RelativeLayout coupleIconResetBack = (RelativeLayout) findViewById(R.id.rl_CoupleImage);
//                coupleIconResetBack.setBackgroundColor(Color.TRANSPARENT);
//
//                break;
//
//        }
//
//    }

    public void upatePlan(View view) {
        Intent i = new Intent(this,PlanDetails.class);
        i.putExtra("days","2");
        startActivity(i);
    }
}
