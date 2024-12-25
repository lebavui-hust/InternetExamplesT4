package vn.edu.hust.internetexamples

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import vn.edu.hust.internetexamples.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

  val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
  val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("https://jsonplaceholder.typicode.com/")
    .build()
  val service = retrofit.create(TestService::class.java)

  lateinit var binding: ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.main)

    binding.buttonGet.setOnClickListener {
      lifecycleScope.launch(Dispatchers.IO) {
        val url = URL("https://lebavui.github.io/jsons/users.json")
        val conn = url.openConnection() as HttpURLConnection

        Log.v("TAG", "Response code: ${conn.responseCode}")

        val reader = conn.inputStream.reader()
        val content = reader.readText()
        reader.close()

        Log.v("TAG", "$content")
      }
    }

    binding.buttonDownload.setOnClickListener {
      lifecycleScope.launch(Dispatchers.IO) {
        val url = URL("https://lebavui.github.io/videos/ecard.mp4")
        val conn = url.openConnection() as HttpURLConnection

        Log.v("TAG", "Response code: ${conn.responseCode}")

        val total = conn.contentLength

        val reader = conn.inputStream
        val writer = openFileOutput("download.mp4", MODE_PRIVATE)
        val buffer = ByteArray(2048)
        var downloaded = 0

        while (true) {
          val len = reader.read(buffer)
          if (len <= 0)
            break
          writer.write(buffer, 0, len)
          downloaded += len
          // Log.v("TAG", "$downloaded")
          withContext(Dispatchers.Main) {
            binding.textProgress.text = "$downloaded / $total"
            binding.progressBar.progress = downloaded
            binding.progressBar.max = total
          }
        }

        writer.close()
        reader.close()

        Log.v("TAG", "Download completed")
      }
    }

    val jsonString = "[\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 1,\n" +
            "    \"title\": \"delectus aut autem\",\n" +
            "    \"completed\": false\n" +
            "  },\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 2,\n" +
            "    \"title\": \"quis ut nam facilis et officia qui\",\n" +
            "    \"completed\": false\n" +
            "  },\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 3,\n" +
            "    \"title\": \"fugiat veniam minus\",\n" +
            "    \"completed\": false\n" +
            "  }]"

    try {
      val jArray = JSONArray(jsonString)
      for (i in 0..< jArray.length()) {
        val jObj = jArray.getJSONObject(i)
        val id = jObj.getInt("id")
        val title = jObj.getString("title")
        Log.v("TAG", "$id - $title")
      }

    } catch (ex: Exception) {
      ex.printStackTrace()
    }

    try {
      val jsonObject = JSONObject()
      jsonObject.put("hoten", "Student 1")
      jsonObject.put("mssv", "20212345")
      val str = jsonObject.toString()
      Log.v("TAG", "$str")
    } catch (ex: Exception) {
      ex.printStackTrace()
    }

    lifecycleScope.launch(Dispatchers.IO) {
//      val posts = service.getAllPost()
//      for (post in posts) {
//        Log.v("TAG", "$post")
//      }

//      val post = service.createPost(Post(1, 1, "Test title", "Test body"))
//      Log.v("TAG", "$post")

      val post = service.updatePost(1, Post(1, 1, "Test title", "Test body"))
      Log.v("TAG", "$post")

    }


    Glide.with(binding.imageView)
      .load("https://lebavui.github.io/walls/wall999.jpg")
      .apply(RequestOptions()
        .placeholder(R.drawable.baseline_download_24)
        .error(R.drawable.baseline_error_outline_24))
      .into(binding.imageView)
  }
}