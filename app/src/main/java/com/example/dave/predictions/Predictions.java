package com.example.dave.predictions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Predictions extends Activity {
    TextView firstName, firstScore, firstPred;
    TextView secondName, secondScore, secondPred;
    TextView thirdName, thirdScore, thirdPred;
    TextView forthName, forthScore, forthPred;
    TextView fifthName, fifthScore, fifthPred;
    ImageView im1, im2, im3, im4, im5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Scores", "***********************");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
    }

}
