package test.vemishra.cafepic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "https://api.androidhive.info/json/movies.json";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;
    //


    private TextView textView;
    //Some url endpoint that you may have
    String myUrl = "http://10.77.133.54:8090/api/products/";
    //String myUrl = "http://www.google.com";
    //String to place our result in
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                intent.putExtra("Name",movieList.get(position).getTitle());
                intent.putExtra("Price", movieList.get(position).getRating());
                intent.putExtra("Description",movieList.get(position).getDescription());
                intent.putExtra("category",movieList.get(position).getCategory());
                intent.putExtra("isVeg",movieList.get(position).isVeg());
                intent.putExtra("imageUrl",movieList.get(position).getThumbnailUrl());
                intent.putExtra("formattedImage",movieList.get(position).getFormattedImage());
                intent.putExtra("servedOnMonday",movieList.get(position).isServedOnMonday());
                intent.putExtra("servedOnTuesday",movieList.get(position).isServedOnTuesday());
                intent.putExtra("servedOnWednesday",movieList.get(position).isServedOnWednesday());
                intent.putExtra("servedOnThursday",movieList.get(position).isServedOnThursday());
                intent.putExtra("servedOnFriday",movieList.get(position).isServedOnFriday());
                intent.putExtra("_id",movieList.get(position).get_id());
                startActivity(intent);
            }
        });
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
       // getActionBar().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#1b1b1b")));

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(Request.Method.GET,myUrl,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setTitle(obj.getString("name"));
                          //      movie.setThumbnailUrl("https"+obj.getString("imageUrl").substring(4));
                                movie.setThumbnailUrl(obj.getString("imageUrl"));
                                movie.setRating(((Number) obj.get("price")).doubleValue());
                                movie.setCategory(obj.getString("category"));
                                movie.setDescription(obj.getString("description"));
                                movie.setServedOnMonday(obj.getBoolean("servedOnMonday"));
                                movie.setServedOnTuesday(obj.getBoolean("servedOnTuesday"));
                                movie.setServedOnWednesday(obj.getBoolean("servedOnWednesday"));
                                movie.setServedOnThursday(obj.getBoolean("servedOnThursday"));
                                movie.setServedOnFriday(obj.getBoolean("servedOnFriday"));
                                movie.setVeg(obj.getBoolean("isVeg"));
                                movie.setFormattedImage(obj.getString("formattedImage"));
                                movie.set_id(obj.getString("_id"));
                               // movie.setYear(obj.getInt("releaseYear"));

                                // Genre is json array
                              /*  JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);
                                    */
                                // adding movie to movies array
                                movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);





        /*
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText("Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work! bcoz: "+error.toString());

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        */


    }


    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
