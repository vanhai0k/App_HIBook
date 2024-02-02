package com.example.app_readbook

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.text.RelativeDateTimeFormatter.RelativeUnit
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_readbook.adapter.CommentAdapter
import com.example.app_readbook.`interface`.Interact
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.`interface`.Readbook
import com.example.app_readbook.model.ReadBook
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException


class Comment_Activity : AppCompatActivity() {
    lateinit var linlerLike: LinearLayout
    lateinit var idpost: String
    lateinit var iduser: String
    lateinit var adapter: CommentAdapter
    lateinit var recyclerviewComment: RecyclerView
    lateinit var add_image:TextView
    lateinit var opendimage:TextView
    lateinit var img_view: ImageView
    lateinit var content:String
    private val MY_REQUEST_CODE = 10
    private lateinit var mUri: Uri

    private val mActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data == null) {
                    return@registerForActivityResult
                }
                val uri: Uri? = data.data
                if (uri != null) {
                    Log.d("mmm", "Selected Image URI: $uri")

                    // Try to get the real path
                    val strRealPath = RealPathUtil.getRealPath(this, uri)
                    Log.d("kkk", "Real Path: $strRealPath")

                    if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
                        try {
                            val bitmap: Bitmap =
                                MediaStore.Images.Media.getBitmap(contentResolver, uri)
                            img_view.setImageBitmap(bitmap)
                            Log.d("mmm", "Image displayed successfully")
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Log.e("mmm", "Error displaying image: $e")
                        }
                    }
                }
            }
        }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)


        add_image = findViewById(R.id.add_image)
        img_view = findViewById(R.id.img_view)
        opendimage = findViewById(R.id.opendimage)
        add_image.setOnClickListener {
            chonanh()
//            openGallery()
            Toast.makeText(baseContext,"click:",Toast.LENGTH_SHORT).show()
        }
        opendimage.setOnClickListener {
            addComment()
        }
        val sharedPreferences = getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        iduser = sharedPreferences.getString("iduser", null).toString()




        var image = findViewById<ImageView>(R.id.image)
        var username = findViewById<TextView>(R.id.username)
        var imagePost = findViewById<ImageView>(R.id.imagePost)
        var imageback = findViewById<ImageView>(R.id.imageback)
        var imagelikeback = findViewById<ImageView>(R.id.imagelikeback)
        var content = findViewById<TextView>(R.id.content)
        var time = findViewById<TextView>(R.id.time)
        var textlike = findViewById<TextView>(R.id.textlike)
        linlerLike = findViewById(R.id.linlerLike)

        recyclerviewComment = findViewById(R.id.recyclerviewComment)
        recyclerviewComment.layoutManager = LinearLayoutManager(this)
// Khởi tạo Adapter và truyền Interface vào
        adapter = CommentAdapter(baseContext, mutableListOf(), this)
        recyclerviewComment.adapter = adapter

        imageback.setOnClickListener {
            onBackPressed()
        }

        val receBundle : Bundle? = intent.extras
        if (receBundle != null) {
            val id = receBundle.getString("id").toString()
            val name = receBundle.getString("username")
            val img = receBundle.getString("image")
            val imagepost = receBundle.getString("imagepost")
            val datepost = receBundle.getString("datepost")
            val contents = receBundle.getString("content")
            idpost = receBundle.getString("idpost").toString()
            username.setText(name)
            time.setText(datepost)
            content.setText(contents)
            Glide.with(this).load(Link.url_mage + img).into(image)
            Glide.with(this).load(Link.url_mage + imagepost).into(imagePost)

        }
        var isUserLiked = intent.getBooleanExtra("isUserLiked", false)
        if (isUserLiked) {
            Glide.with(this)
                .load(R.drawable.likecolor)
                .into(imagelikeback)
            textlike.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaty))
        } else {
            Glide.with(this)
                .load(R.drawable.like)
                .into(imagelikeback)
            textlike.setTextColor(ContextCompat.getColor(this, R.color.colorxam))
        }
        linlerLike.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
            val iduser_Login = sharedPreferences.getString("iduser", null)
            if (iduser_Login != null) {
                val likeRequest = Interact.LikeRequest(user_id = iduser_Login)

                val retofit = Interact.retrofit.create(Interact::class.java)
                if (isUserLiked) {
                    // If the post is liked, send an "unlike" request
                    val call = retofit.likeNols(idpost, likeRequest)
                    call.enqueue(object : Callback<Interact.LikeRequest> {
                        override fun onResponse(
                            call: Call<Interact.LikeRequest>,
                            response: Response<Interact.LikeRequest>
                        ) {
                            if (response.isSuccessful) {
                                // Update UI to reflect that the post has been unliked
                                isUserLiked = false
                                Glide.with(baseContext)
                                    .load(R.drawable.like)
                                    .into(imagelikeback)
                                textlike.setTextColor(ContextCompat.getColor(baseContext, R.color.colorxam))
                            }
                        }

                        override fun onFailure(call: Call<Interact.LikeRequest>, t: Throwable) {
                            // Handle failure
                            // You might want to show a Toast or handle the error in some way
                        }
                    })
                }else {
                    val call = retofit.likeNols(idpost, likeRequest)
                    call.enqueue(object : Callback<Interact.LikeRequest> {
                        override fun onResponse(
                            call: Call<Interact.LikeRequest>,
                            response: Response<Interact.LikeRequest>
                        ) {
                            if (response.isSuccessful) {
                                // Update UI to reflect that the post has been liked
                                isUserLiked = true
                                Glide.with(baseContext)
                                    .load(R.drawable.likecolor)
                                    .into(imagelikeback)
                                textlike.setTextColor(
                                    ContextCompat.getColor(
                                        baseContext,
                                        R.color.colorPrimaty
                                    )
                                )
                            }
                        }

                        override fun onFailure(call: Call<Interact.LikeRequest>, t: Throwable) {
                            // Handle failure
                            // You might want to show a Toast or handle the error in some way
                        }
                    })
                }
                }
        }
        getData()
    }

    private fun addComment() {
         content = ""

        // Requesting READ_EXTERNAL_STORAGE permission if not granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_REQUEST_CODE
            )
            return
        }

        val requestcontent = RequestBody.create(MediaType.parse("multipart/form-data"), content)
        val requestiduser = RequestBody.create(MediaType.parse("multipart/form-data"), iduser)
        val strRealPath= RealPathUtil.getRealPath(this,mUri)
        if (strRealPath.isNullOrEmpty()) {
            // Handle the case when the file path is invalid
            return
        }


        val image = File(strRealPath)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image)
        val mPart = MultipartBody.Part.createFormData("image",image.name,requestFile)

        val retrofit = Interact.retrofit.create(Interact::class.java)
        val call = retrofit.addObjImage(idpost,requestiduser,requestcontent,mPart)
        call.enqueue(object : Callback<Interact.CommentApiResponse>{
            override fun onResponse(
                call: Call<Interact.CommentApiResponse>,
                response: Response<Interact.CommentApiResponse>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(baseContext,"Da camment",Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<Interact.CommentApiResponse>, t: Throwable) {
                Toast.makeText(baseContext,"fail",Toast.LENGTH_SHORT).show()
                Log.d("llll", "onFailure: $t")
            }

        })
    }

    private fun getData() {
        val retrofit = Readbook.retrofit.create(Readbook::class.java)
        val call = retrofit.getComment(idpost)
        call.enqueue(object : Callback<ReadBook> {
            override fun onResponse(call: Call<ReadBook>, response: Response<ReadBook>) {
                if (response.isSuccessful) {
                    val readBook = response.body()
                    // Process the data as needed
                    if (readBook != null) {
                        adapter.updateData(readBook)
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<ReadBook>, t: Throwable) {
                Log.d("loiii", "onFailure: " + t)
                Toast.makeText(baseContext,"loi"+ t, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun onLikeClicked(postId: String) {
        // Thực hiện các hành động khi click vào like, ví dụ: gọi hàm getData
        getData()
    }

    private fun chonanh() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || isStoragePermissionGranted()) {
            openGallery()
        } else {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permission, MY_REQUEST_CODE)
        }
    }
    private fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        handlePermissionResult(requestCode, grantResults)
    }

    private fun handlePermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == MY_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            // Handle the case when permission is denied
            // You might want to inform the user or take alternative actions
            // Don't call openGallery() here
            Toast.makeText(baseContext, "Permission denied", Toast.LENGTH_SHORT).show()

        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        mActivityResultLauncher.launch(intent)
    }

}