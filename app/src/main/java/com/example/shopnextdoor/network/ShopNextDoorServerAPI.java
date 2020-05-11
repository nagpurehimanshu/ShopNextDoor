package com.example.shopnextdoor.network;

import com.example.shopnextdoor.Data.Customer;
import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.Data.Shop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Call<List<Shop>> getShopList();

    @POST("/placeOrder")
    Call<Orders> placeOrder(@Body Orders orders);

    @GET("/getOrderDetails")
    Call<Orders> getOrderDetails(@Query("order_number") String order_number);

    @GET("/getActiveOrderCount")
    Call<Orders> getActiveOrderCount(@Query("customer_username") String customer_username);

    @GET("/getActiveOrderWithShop")
    Call<String> getActiveOrderWithShop(@Query("customer_username") String customer_username, @Query("shop_username") String shop_username);

    @GET("/getCustomerOrders")
    Call<List<Orders>> getCustomerOrders(@Query("customer_username") String customer_username);

    @GET("/getActiveOrdersList")
    Call<List<Orders>> getActiveOrdersList(@Query("shop_username") String shop_username);
}
