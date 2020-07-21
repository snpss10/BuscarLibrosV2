package com.desarrollo.buscarlibros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Book> mBooks;
    private RecyclerViewAdapter mAdapter;
    private RequestQueue mRequestQueue;

    private static  final  String BASE_URL="https://www.googleapis.com/books/v1/volumes?q=";

    private EditText search_edit_text;
    private Button search_button;
    private ProgressBar loading_indicator;
    private TextView error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_edit_text=findViewById(R.id.search_box);
        search_button= findViewById(R.id.search_buttton);
        loading_indicator=findViewById(R.id.loading_indicator);
        error_message= findViewById(R.id.message_display);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBooks = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBooks.clear();
                search();
            }
        });

        cargarPreferencias();

    }



    private void search()
    {
        String search_query = search_edit_text.getText().toString();

        boolean is_connected = Read_network_state(this);
        if(!is_connected)
        {
            error_message.setText("Por favor revisa tu internet");
            mRecyclerView.setVisibility(View.INVISIBLE);
            error_message.setVisibility(View.VISIBLE);
            return;
        }

        //  Log.d("QUERY",search_query);


        if(search_query.equals(""))
        {
            Toast.makeText(this,"Porfavor ingresa tu busqueda",Toast.LENGTH_SHORT).show();
            return;
        }
        String final_query=search_query.replace(" ","+");
        Uri uri=Uri.parse(BASE_URL+final_query);
        Uri.Builder buider = uri.buildUpon();

        parseJson(buider.toString());
        guardarPreferencias();

    }
    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String libro1=preferences.getString("elibro","no existe la informacion");
        search_edit_text.setText(libro1);
        search();

    }

    private void guardarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String libro=search_edit_text.getText().toString();
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("elibro",libro);
        editor.commit();
    }

    private boolean Read_network_state(Context context)
    {    boolean is_connected;
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        is_connected=info!=null&&info.isConnectedOrConnecting();
        return is_connected;
    }
    private void parseJson(String key) {

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String title ="";
                        String author ="";
                        String categories = "No Existen Categorias ";
                        String price = "No esta en venta";
                        String thumbnail = "";
                        try {
                            JSONArray items = response.getJSONArray("items");

                            for (int i = 0 ; i< items.length() ;i++){
                                JSONObject item = items.getJSONObject(i);
                                JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                                try{
                                    title = volumeInfo.getString("title");

                                    JSONArray authors = volumeInfo.getJSONArray("authors");
                                    if(authors.length() == 1){
                                        author = authors.getString(0);
                                    }else {
                                        author = authors.getString(0) + "|" +authors.getString(1);
                                    }

                                    price = volumeInfo.getString("pageCount");

                                    categories = volumeInfo.getJSONArray("categories").getString(0);

                                }catch (Exception e){

                                }
                                thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");





                                mBooks.add(new Book(title , author , categories , thumbnail , price));


                                mAdapter = new RecyclerViewAdapter(MainActivity.this , mBooks);
                                mRecyclerView.setAdapter(mAdapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG" , e.toString());

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }
}