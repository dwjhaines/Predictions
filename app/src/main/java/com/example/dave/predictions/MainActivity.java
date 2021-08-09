package com.example.dave.predictions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private static final String DEBUG_TAG = "HttpExample";

    NumberPicker npScfc;
    NumberPicker npBrfc;
    NumberPicker npCpfc;
    NumberPicker npSfc;

    // array to be passed to Scores activity
    int[] positions = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Retrieve stored positions
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        npScfc = (NumberPicker) findViewById(R.id.numberPickerSwansea);
        npScfc.setMinValue(1);
        npScfc.setMaxValue(24);
        npScfc.setWrapSelectorWheel(false);
        npScfc.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npScfc.setValue(sharedPref.getInt("scfcpos", 10));

        npSfc = (NumberPicker) findViewById(R.id.numberPickerSunderland);
        npSfc.setMinValue(1);
        npSfc.setMaxValue(24);
        npSfc.setWrapSelectorWheel(false);
        npSfc.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npSfc.setValue(sharedPref.getInt("sfcpos", 10));

        npCpfc = (NumberPicker) findViewById(R.id.numberPickerPalace);
        npCpfc.setMinValue(1);
        npCpfc.setMaxValue(20);
        npCpfc.setWrapSelectorWheel(false);
        npCpfc.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npCpfc.setValue(sharedPref.getInt("cpfcpos", 10));

        npBrfc = (NumberPicker) findViewById(R.id.numberPickerRovers);
        npBrfc.setMinValue(1);
        npBrfc.setMaxValue(24);
        npBrfc.setWrapSelectorWheel(false);
        npBrfc.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npBrfc.setValue(sharedPref.getInt("brfcpos", 10));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_predictions) {
            Intent intent = new Intent(this, Predictions.class);
            intent.putExtra("positions", positions);
            Log.d("MainActivity", "About to start Scores activity");
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_leagues) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d("MainActivity: ", "onSaveInstanceState");
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("scfcpos", npScfc.getValue());
        editor.putInt("sfcpos", npSfc.getValue());
        editor.putInt("cpfcpos", npCpfc.getValue());
        editor.putInt("brfcpos", npBrfc.getValue());
        editor.apply();
    }

    @Override
    public void onPause() {
        //When exiting the activity, save the four league positions
        super.onPause();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("scfcpos", npScfc.getValue());
        editor.putInt("sfcpos", npSfc.getValue());
        editor.putInt("cpfcpos", npCpfc.getValue());
        editor.putInt("brfcpos", npBrfc.getValue());
        editor.apply();
    }

    /**
     * Called when the user clicks the Calculate button
     */
    public void calculate(View view) {
        //Put the positions of the four teams into the array for sending to next activity
        positions[0] = npScfc.getValue();
        positions[1] = npSfc.getValue();
        positions[2] = npCpfc.getValue();
        positions[3] = npBrfc.getValue();

        Intent intent = new Intent(this, Scores.class);
        intent.putExtra("positions", positions);
        Log.d("MainActivity", "About to start Scores activity");
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Update button
     * Checks the network connection and calls getPositions if OK
     */
    public void update(View view) throws Exception {
        Log.i("MainActivity", "Update Positions");
        // Check to see if there is a network connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i("MainActivity", "Network Connection OK");
            getPositions();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Error: No network connection", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Downloads the league tables from bbc.co.uk
     */
    public void getPositions(){

        // The values in these strings will need to be updated every season
        String premTableUrl = "https://www.bbc.co.uk/sport/football/premier-league/table";
        String champTableUrl = "https://www.bbc.co.uk/sport/football/championship/table";
        String league1TableUrl = "https://www.bbc.co.uk/sport/football/league-one/table";
        String league2TableUrl = "https://www.bbc.co.uk/sport/football/league-two/table";


        DownloadWebpageTask DWTPrem;
        DownloadWebpageTask DWTChamp;
        DownloadWebpageTask DWTLeague1;
        DownloadWebpageTask DWTLeague2;

        DWTPrem = new DownloadWebpageTask();
        DWTPrem.execute(premTableUrl);

        DWTChamp = new DownloadWebpageTask();
        DWTChamp.execute(champTableUrl);

        DWTLeague1 = new DownloadWebpageTask();
        DWTLeague1.execute(league1TableUrl);

        DWTLeague2 = new DownloadWebpageTask();
        DWTLeague2.execute(league2TableUrl);
    }

    /**
     * Gets the html in the form of a string and returns the league position of the team
     */
    public int parseHtml (String html, String team){
        int position;
        Log.d("MainActivity", "Entered parseHTML");
        // Move pointer to team name
        int textPointer = html.indexOf(team);
        Log.d("MainActivity", "Text index " + textPointer);

        // Go back to previous "$row" to get the team's position
        textPointer = html.lastIndexOf("$row", textPointer);
        Log.d("MainActivity", "Text index " + textPointer);

        // There must be a better way but this works!!
        if (html.charAt(textPointer + 6) == '.') {
            // Team is in pos 1 - 9 so only need single char. 17th char is position
            position = Integer.parseInt(Character.toString(html.charAt(textPointer + 5)));
        }
        else{
            // Team is in pos 10 or below so need two chars. 17th and 18th char give the position
            position = Integer.parseInt(Character.toString(html.charAt(textPointer + 5)) + Character.toString(html.charAt(textPointer + 6)));
        }
        Log.d("MainActivity", team + " " + (position));

        return position + 1;
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        String html;

        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.

            try {
                Log.d("MainActivity", "URL " + urls[0]);
                html = downloadUrl(urls[0]);
                Log.d("MainActivity", "Length of html " + html.length());
                return html;
            } catch (IOException e) {
                return "Error";
            }
        }
        // onPostExecute parses the html to find the positions.
        @Override
        protected void onPostExecute(String html) {
            int scfcPos;
            int sfcPos;
            int cpfcPos;
            int brfcPos;
            Log.d("MainActivity", "onPostExecute" + html);

            if (html.contains("Premier League Table")) {
                Log.d("MainActivity", "Premier League");
                cpfcPos = parseHtml(html, "teams/crystal-palace");
                npCpfc.setValue(cpfcPos);
                Log.d("MainActivity", "Premier");
            }
            else if (html.contains("Championship Table")) {
                Log.d("MainActivity", "Championship");
                scfcPos = parseHtml(html, "teams/swansea-city");
                npScfc.setValue(scfcPos);
            }
            else if (html.contains("League One Table")){
                // League 1
                Log.d("MainActivity", "League 1");
                sfcPos = parseHtml(html, "teams/sunderland");
                npSfc.setValue(sfcPos);
            }
            else if (html.contains("League Two Table")){
                // League 1
                Log.d("MainActivity", "League 1");
                brfcPos = parseHtml(html, "teams/bristol-rovers");
                npBrfc.setValue(brfcPos);
            }
            else {
                Log.d("MainActivity", "html " +html);
                Toast toast = Toast.makeText(getApplicationContext(), "Error: Could not download html", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        Log.d("MainActivity", "downloadUrl");
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString;
            contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String fileLine;

        Log.d("MainActivity", "readIt");
        fileLine = bufferedReader.readLine();

        while (fileLine != null) {

                sb.append(fileLine);

            fileLine = bufferedReader.readLine();
        }
        return new String(sb);
    }
}


