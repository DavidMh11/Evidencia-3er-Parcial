package com.example.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.main.Adaptadores.AdapterUsuarios;
import com.example.main.Interface.JsonPlaceHolderApi;
import com.example.main.Modelo.Usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "USUARIOS";
    private Retrofit retrofit;
    private ArrayList<Usuario> listaUsuarios;
    private RecyclerView recyclerView;
    private AdapterUsuarios adapterUsuarios;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.IDRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        obteneDatos();
    }

    private void obteneDatos() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Usuario>> usuarioResponseCall = service.getUsuarios();
        usuarioResponseCall.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    listaUsuarios = (ArrayList<Usuario>) response.body();
                    adapterUsuarios = new AdapterUsuarios(getApplicationContext(), listaUsuarios);
                    adapterUsuarios.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int user = listaUsuarios.get(recyclerView.getChildAdapterPosition(v)).getId();
                            //Toast.makeText(getApplicationContext(), user.getName(),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, InformacionUsuario.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ID", (Serializable) user);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapterUsuarios);
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}