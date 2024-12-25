package vn.edu.hust.internetexamples

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TestService {
  @GET("posts")
  suspend fun getAllPost(): List<Post>
  @GET("posts/{userId}")
  suspend fun getPostsByUser(@Path("userId") userId: Int): List<Post>
  @POST("posts")
  suspend fun createPost(@Body post: Post): Post
  @PUT("posts/{postId}")
  suspend fun updatePost(@Path("postId") postId: Int, @Body post: Post): Post
  @DELETE("posts/{postId}")
  suspend fun deletePost(@Path("postId") postId: Int)
}