package com.example.shopnextdoor.network;

import android.text.Editable;

import com.example.shopnextdoor.Data.Customer;
import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.Data.Shop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ShopNextDoorServerAPI {

    @GET("/getCustomer")
    Call<Customer> getCustomer(@Query("username") String username);

    @POST("/registerCustomer")
    Call<Customer> registerCustomer(@Body Customer customer);

    @GET("/getCustomerList")
    Call<List<Customer>> getCustomerList();

    @GET("/getShop")
    Call<Shop> getShop(@Query("username") String username);

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

    @PUT("/updateOrderStatus")
    Call<String> updateOrderStatus(@Query("order_number") String order_number, @Query("order_status") String order_status, @Query("amount") int amount, @Query("rejection_msg") String rejection_msg);

    @GET("/getOrdersByShop")
    Call<List<Orders>> getOrdersByShop(@Query("shop_username") String shop_username);

    @PUT("/updateCustomer")
    Call<String> updateCustomer(@Query("username") String username, @Query("mobile") String mobile, @Query("address") String address, @Query("email") String email);

    @PUT("/updateShop")
    Call<String> updateShop(@Query("username") String username, @Query("owner_mobile") String owner_mobile, @Query("address") String address, @Query("email") String email);
}
