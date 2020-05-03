package com.example.shopnextdoor.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShopNextDoorServerAPI {

    @GET("/getCustomer")
    Call<Customer> getCustomer(@Query("username") String username, @Query("password") String password);

    @POST("/registerCustomer")
    Call<Customer> registerCustomer(@Body Customer customer);

    @GET("/getShop")
    Call<Shop> getShop(@Query("username") String username, @Query("password") String password);

    @POST("/registerShop")
    Call<Shop> registerShop(@Body Shop shop);

    @GET("/getShopList")
    Call<Shop[]> getShopList();
}
