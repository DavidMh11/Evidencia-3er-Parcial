package com.example.main.Interface;

import com.example.main.Modelo.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("users")
    Call<List<Usuario>> getUsuarios();

    @GET("users/{id}")
    Call<Usuario> getUsuario(@Path("id")int id);
}
