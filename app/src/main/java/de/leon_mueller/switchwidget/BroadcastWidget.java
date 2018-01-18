package de.leon_mueller.switchwidget;

import android.app.DownloadManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import de.leon_mueller.switchwidget.MainActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Leon on 27.12.2017.
 */
public class BroadcastWidget extends AppWidgetProvider {
    private static final String ACTION_SIMPLEAPPWIDGET = "ACTION_BROADCASTWIDGETSAMPLE";
    private static boolean state = false;

    private static String out = "An";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.switchwidget);
        // Construct an Intent which is pointing this class.
        Intent intent = new Intent(context, BroadcastWidget.class);
        intent.setAction(ACTION_SIMPLEAPPWIDGET);
        // And this time we are sending a broadcast with getBroadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.tvWidget, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_SIMPLEAPPWIDGET.equals(intent.getAction())) {

            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.switchwidget);

            SharedPreferences sharedPref = context.getSharedPreferences("ip", context.MODE_PRIVATE);
            String ip = sharedPref.getString("ip", null);

            if(!state) {
                state = true;
                RequestQueue queue = Volley.newRequestQueue(context);

                String url =ip+"/gpio/1";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                out ="Aus";
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        out = ":(";
                    }
                });
                views.setTextViewText(R.id.tvWidget, out);

                ComponentName appWidget = new ComponentName(context, BroadcastWidget.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidget, views);
// Add the request to the RequestQueue.
                queue.add(stringRequest);
            } else{

                state = false;
                RequestQueue queue = Volley.newRequestQueue(context);

                String url =ip+"/gpio/0";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                out = "An";
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        out = ":(";
                    }
                });

                views.setTextViewText(R.id.tvWidget, out);

                ComponentName appWidget = new ComponentName(context, BroadcastWidget.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidget, views);
// Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }
    }

}