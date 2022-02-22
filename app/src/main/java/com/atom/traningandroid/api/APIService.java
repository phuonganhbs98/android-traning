package com.atom.traningandroid.api;

import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.model.Gender;
import com.atom.traningandroid.model.GenderList;
import com.atom.traningandroid.model.Role;
import com.atom.traningandroid.model.RoleList;
import com.atom.traningandroid.model.Statistic;
import com.atom.traningandroid.model.StatisticList;
import com.atom.traningandroid.model.User;
import com.atom.traningandroid.model.UserList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    /**
     * API User
     */
    @POST(Constant.PATH + "/users/login")
    Call<String> login(@Body User user);

    @POST(Constant.PATH + "/users/search")
    Call<UserList> search(@Header("Authorization") String token,
                          @Body User user);

    @GET(Constant.PATH + "/users/{userId}")
    Call<User> getUserByUserId(@Header("Authorization") String token,
                               @Path("userId") String userId);

    @GET(Constant.PATH + "/users/profile")
    Call<User> getProfile(@Header("Authorization") String token);

    @POST(Constant.PATH + "/users")
    Call<User> createUser(@Header("Authorization") String token,
                          @Body User user);

    @PUT(Constant.PATH + "/users")
    Call<User> updateUser(@Header("Authorization") String token,
                          @Body User user);

    @DELETE(Constant.PATH + "/users/{userId}")
    Call<User> deleteUser(@Header("Authorization") String token,
                          @Path("userId") String userId);

    /**
     * API statistic
     */
    @POST(Constant.PATH + "/users/statistics")
    Call<StatisticList> statistic(@Header("Authorization") String token);


    /**
     * API Role
     */

    @GET(Constant.PATH + "/roles")
    Call<RoleList> findAllRoles(@Header("Authorization") String token);

    @GET(Constant.PATH + "/roles/{authorityId}")
    Call<Role> findRoleById(@Header("Authorization") String token,
                                         @Path("authorityId") Integer authorityId);

    /**
     * API Gender
     */

    @GET(Constant.PATH + "/genders")
    Call<GenderList> findAllGenders(@Header("Authorization") String token);

    @GET(Constant.PATH + "/genders/{genderId}")
    Call<Gender> findGenderById(@Header("Authorization") String token,
                                    @Path("genderId") Integer genderId);

}
