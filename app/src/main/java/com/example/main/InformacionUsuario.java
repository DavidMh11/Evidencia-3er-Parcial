package com.example.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class InformacionUsuario extends AppCompatActivity {

    private static final String TAG = "USUARIOS";
    private final int PHONE_CALL_CODE = 100;
    private TextView txtname, txtusername, txtemail, txtphone, txtwebsite, txtcompany, txtaddress, txtzipcode;
    private ImageButton btnLlamar;
    private Retrofit retrofit;
    private int user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_usuario);
        txtname = (TextView) findViewById(R.id.txtUser);
        txtusername = (TextView) findViewById(R.id.txtUserName);
        txtemail = (TextView) findViewById(R.id.txtEmail);
        txtphone = (TextView) findViewById(R.id.txtPhone);
        txtwebsite = (TextView) findViewById(R.id.txtWebsite);
        txtcompany = (TextView) findViewById(R.id.txtCompany);
        txtaddress = (TextView) findViewById(R.id.txtAddress);
        txtzipcode = (TextView) findViewById(R.id.txtZipCode);
        btnLlamar = (ImageButton) findViewById(R.id.btnLlamar);
        Bundle datos = getIntent().getExtras();
        if (datos != null) {
            user = (Integer) datos.getSerializable("ID");
        }
        obteneUsuario(user);
        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtphone.getText().toString();
                if (num != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                    } else {
                        versionesAnteriores(num);
                    }
                }
            }

            private void versionesAnteriores(String num) {
                Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
                if (verificarPermisos(Manifest.permission.CALL_PHONE)) {
                    startActivity(intentLlamada);
                } else {
                    Toast.makeText(InformacionUsuario.this, "Configura los permisos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void obteneUsuario(int id) {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        Call<Usuario> usuarioResponseCall = service.getUsuario(id);
        usuarioResponseCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Usuario user = response.body();
                    txtname.append(user.getName());
                    txtusername.append(user.getUsername());
                    txtemail.append(user.getEmail());
                    txtphone.append(user.getPhone());
                    txtwebsite.append(user.getWebsite());
                    txtcompany.append(user.getCompany().getName());
                    txtaddress.append(user.getAddress().getCity()+", "+user.getAddress().getStreet());
                    txtzipcode.append(user.getAddress().getZipcode());
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PHONE_CALL_CODE:
                String permission = permissions[0];
                int result = grantResults[0];
                if (permission.equals((Manifest.permission.CALL_PHONE))) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        String phoneNumber = txtphone.getText().toString();
                        Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                            Toast.makeText(InformacionUsuario.this, "OK", Toast.LENGTH_SHORT).show();
                        startActivity(intentLlamada);
                    } else {
                        Toast.makeText(InformacionUsuario.this, "Configura los permisos", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean verificarPermisos(String permiso) {
        int resultado = this.checkCallingOrSelfPermission(permiso);
        return resultado == PackageManager.PERMISSION_GRANTED;


    }
}