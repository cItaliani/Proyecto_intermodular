package com.example.pingu;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/pass-remember")
    Call<ApiResponse> rememberPassword(@Body RememberRequest request);

    @POST("users")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("posts")
    Call<CreatePostResponse> createPost(@Body CreatePostRequest request);

    @GET("posts")
    Call<List<PostResponse>> getPosts();

    @GET("users/{id}")
    Call<UserResponse> getUserById(@Path("id") String id);

    @PUT("users/{id}")
    Call<ApiResponse> updateUser(@Path("id") String id, @Body UpdateUserRequest request);

    @DELETE("users/{id}")
    Call<ApiResponse> deleteUser(@Path("id") String id);
    @GET("users")
    Call<List<UserResponse>> getUsers();

    @GET("users/{id}/followed")
    Call<List<SeguidorResponse>> getFollowedUsers(@Path("id") String id);

    @POST("users/{id}/follow")
    Call<ResponseBody> followUser(@Path("id") String id, @Body FollowRequest request);

    @POST("users/{id}/unfollow")
    Call<ResponseBody> unfollowUser(@Path("id") String id, @Body FollowRequest request);
    @POST("posts/{postId}/like")
    Call<ApiResponse> likePost(@Path("postId") String postId, @Body ReaccionRequest request);

    @POST("posts/{postId}/dislike")
    Call<ApiResponse> dislikePost(@Path("postId") String postId, @Body ReaccionRequest request);

    @GET("posts/{postId}/likes")
    Call<List<LikeResponse>> getLikes(@Path("postId") String postId);

    @DELETE("posts/{postId}")
    Call<ApiResponse> deletePost(@Path("postId") String postId);

    @GET("posts/{postId}/replies")
    Call<List<PostResponse>> getReplies(@Path("postId") String postId);

    @GET("users/{id}/followers")
    Call<List<SeguidorResponse>> getFollowers(@Path("id") String id);

}