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

public class Scores extends Activity {

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

        firstName = (TextView) findViewById(R.id.textFirstName);
        secondName = (TextView) findViewById(R.id.textSecondName);
        thirdName = (TextView) findViewById(R.id.textThirdName);
        forthName = (TextView) findViewById(R.id.textForthName);
        fifthName = (TextView) findViewById(R.id.textFifthName);

        firstScore = (TextView) findViewById(R.id.textFirstScore);
        secondScore = (TextView) findViewById(R.id.textSecondScore);
        thirdScore = (TextView) findViewById(R.id.textThirdScore);
        forthScore = (TextView) findViewById(R.id.textForthScore);
        fifthScore = (TextView) findViewById(R.id.textFifthScore);

        firstPred = (TextView) findViewById(R.id.textFirstPred);
        secondPred = (TextView) findViewById(R.id.textSecondPred);
        thirdPred = (TextView) findViewById(R.id.textThirdPred);
        forthPred = (TextView) findViewById(R.id.textForthPred);
        fifthPred = (TextView) findViewById(R.id.textFifthPred);

        im1 = (ImageView) findViewById(R.id.imageFirst);
        im2 = (ImageView) findViewById(R.id.imageSecond);
        im3 = (ImageView) findViewById(R.id.imageThird);
        im4 = (ImageView) findViewById(R.id.imageForth);
        im5 = (ImageView) findViewById(R.id.imageFifth);

        TextView[] namesArray = {firstName, secondName, thirdName, forthName, fifthName};
        TextView[] scoresArray = {firstScore, secondScore, thirdScore, forthScore, fifthScore};
        TextView[] predArray = {firstPred, secondPred, thirdPred, forthPred, fifthPred};
        ImageView[] imageArray = {im1, im2, im3, im4, im5 };

        // Get the positions from the intent and put into array
        int[] positions = extras.getIntArray("positions");

        Player dave = new Player("Dave");
        Player roz = new Player("Roz");
        Player mark = new Player("Mark");
        Player steve = new Player("Steve");
        Player lee = new Player("Lee");

        dave.setPredictions(8, 2, 16, 10);
        roz.setPredictions(15, 5, 15, 15);
        mark.setPredictions(11, 3, 18, 19);
        steve.setPredictions(9, 2, 16, 9);
        lee.setPredictions(10, 5, 16, 21);

        dave.setPic(R.drawable.dave);
        roz.setPic(R.drawable.roz);
        mark.setPic(R.drawable.mark);
        steve.setPic(R.drawable.steve);
        lee.setPic(R.drawable.lee);

        Player[] players = {dave, roz, mark, steve, lee};

        // hide prediction info until name is clicked
        firstPred.setVisibility(View.GONE);
        secondPred.setVisibility(View.GONE);
        thirdPred.setVisibility(View.GONE);
        forthPred.setVisibility(View.GONE);
        fifthPred.setVisibility(View.GONE);

        im1.setVisibility(View.GONE);
        im2.setVisibility(View.GONE);
        im3.setVisibility(View.GONE);
        im4.setVisibility(View.GONE);
        im5.setVisibility(View.GONE);

        //Calculate the players scores
        for (Player player : players) {
            int score;
            score = Math.abs(player.getScfc() - positions[0])
                    + Math.abs(player.getSfc() - positions[1])
                    + Math.abs(player.getCpfc() - positions[2])
                    + Math.abs(player.getBrfc() - positions[3]);
            Log.i("MyActivity", player.getName() + ": Score = " + score);
            player.setScore(score);
        }

        // Sort the array based on who has lowwest score
        Player tempPlayer;
        new Player("temp");
        for (int i = 0; i < players.length - 1; i++) {
            for (int j = 0; j < players.length - 1 - i; j++) {
                if (players[j].getScore() > players[j + 1].getScore()) {
                    tempPlayer = players[j];
                    players[j] = players[j + 1];
                    players[j + 1] = tempPlayer;
                }
            }
        }

        // Put the results out to the textViews / imageViews
        for (int i = 0; i < players.length; i++) {
            namesArray[i].setText(i + 1 + ".  " + players[i].getName());
            scoresArray[i].setText(players[i].getScore() + " pts");
            predArray[i].setText("Swansea City     " +"\t"+ players[i].getScfc() + "\n" +
                    "Sunderland         " +"\t"+ players[i].getSfc() + "\n" +
                    "Crystal Palace   " +"\t"+ players[i].getCpfc() + "\n" +
                    "Bristol Rovers     " +"\t"+ players[i].getBrfc());
            imageArray[i].setImageResource(players[i].getPic());
        }
    }

    public void toggleFirst(View v) {
        if (firstPred.isShown()) {
            Fx.slide_up(this, firstPred);
            firstPred.setVisibility(View.GONE);
            im1.setVisibility(View.GONE);
        } else {
            firstPred.setVisibility(View.VISIBLE);
            im1.setVisibility(View.VISIBLE);
            Fx.slide_down(this, firstPred);
        }
    }

    public void toggleSecond(View v) {
        if (secondPred.isShown()) {
            Fx.slide_up(this, secondPred);
            secondPred.setVisibility(View.GONE);
            im2.setVisibility(View.GONE);
        } else {
            secondPred.setVisibility(View.VISIBLE);
            im2.setVisibility(View.VISIBLE);
            Fx.slide_down(this, secondPred);
        }
    }

    public void toggleThird(View v) {
        if (thirdPred.isShown()) {
            Fx.slide_up(this, thirdPred);
            thirdPred.setVisibility(View.GONE);
            im3.setVisibility(View.GONE);
        } else {
            thirdPred.setVisibility(View.VISIBLE);
            im3.setVisibility(View.VISIBLE);
            Fx.slide_down(this, thirdPred);
        }
    }

    public void toggleForth(View v) {
        if (forthPred.isShown()) {
            Fx.slide_up(this, forthPred);
            forthPred.setVisibility(View.GONE);
            im4.setVisibility(View.GONE);
        } else {
            forthPred.setVisibility(View.VISIBLE);
            im4.setVisibility(View.VISIBLE);
            Fx.slide_down(this, forthPred);
        }
    }

    public void toggleFifth(View v) {
        if (fifthPred.isShown()) {
            Fx.slide_up(this, fifthPred);
            fifthPred.setVisibility(View.GONE);
            im5.setVisibility(View.GONE);
        } else {
            fifthPred.setVisibility(View.VISIBLE);
            im5.setVisibility(View.VISIBLE);
            Fx.slide_down(this, fifthPred);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
