package com.example.pingu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
}