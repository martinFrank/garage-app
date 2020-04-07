package com.github.martinfrank.garageapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RequestQueue queue = null;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("http://192.168.0.64:8081");

        LinearLayout rows = findViewById(R.id.buttonRows);
        for (int i = 0; i < 4; i++) {
            LinearLayout row = (LinearLayout) rows.getChildAt(i);
            TextView text = row.findViewById(R.id.buttonRowText);
            Button button = row.findViewById(R.id.buttonRowbutton);
            setupRow(text, button, i);
        }

        textView = findViewById(R.id.textView);
        queue = Volley.newRequestQueue(this);
    }

    private void setupRow(TextView text, Button button, int i) {
        if (i == 0) {
            text.setText(R.string.textview_status_gate);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    statusGate();
                }
            });
        }

        if (i == 1) {
            text.setText(R.string.textview_push_gate);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushGate();
                }
            });
        }

        if (i == 2) {
            text.setText(R.string.textview_status_light);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    statusLight();
                }
            });
        }

        if (i == 3) {
            text.setText(R.string.textview_push_light);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushLight();
                }
            });
        }
    }

    private void pushLight() {
        queue.add(createLightJsonRequest(Request.Method.POST, "http://192.168.0.64:8082/garage/light"));
    }

    private void statusLight() {
        queue.add(createLightJsonRequest(Request.Method.GET, "http://192.168.0.64:8082/garage/light"));
    }

    private void pushGate() {
        queue.add(createGarageJsonRequest(Request.Method.POST, "http://192.168.0.64:8082/garage/gate"));
    }

    private void statusGate() {
        queue.add(createGarageJsonRequest(Request.Method.GET, "http://192.168.0.64:8082/garage/gate"));
    }


    private JsonObjectRequest createGarageJsonRequest(int method, String url) {
        return new JsonObjectRequest(method, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleGarageResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleGarageError(error);
                    }
                });
    }

    private JsonObjectRequest createLightJsonRequest(int method, String url) {
        return new JsonObjectRequest(method, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleLightResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleLightError(error);
                    }
                });
    }

    private void handleGarageError(VolleyError error) {
        Log.d(TAG, "error:" + error);
        textView.setText("Fehler\n" + error);
    }

    private void handleGarageResponse(JSONObject response) {
        String status = "Tor Status: unbekannt";
        String date = "letzte Benutzung: unbekannt";
        try {
            status = "Tor Status: " + response.getString("state");
            date = "letzte Benutzung: " + response.getString("lastRequest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(TAG, "response:"+status+"\n"+date);
        textView.setText(status + "\n" + date);
    }

    private void handleLightError(VolleyError error) {
        Log.d(TAG, "error:" + error);
        textView.setText("Fehler\n" + error);
    }

    private void handleLightResponse(JSONObject response) {
        String status = "Licht Status: unbekannt";
        String date = "letzte Benutzung: unbekannt";
        try {
            status = "Licht Status: " + response.getString("state");
            date = "letzte Benutzung: " + response.getString("lastRequest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(TAG, "response:"+status+"\n"+date);
        textView.setText(status + "\n" + date);
    }
}
