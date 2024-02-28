package com.example.app_readbook


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.app_readbook.adapter.CommentAdapter
import com.example.app_readbook.`interface`.Interact
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.`interface`.Readbook
import com.example.app_readbook.model.ReadBook
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Comment_Activity : AppCompatActivity() {
    lateinit var linlerLike: LinearLayout
    lateinit var idpost: String
    lateinit var user_idpost: String
    lateinit var iduser: String
    lateinit var adapter: CommentAdapter
    lateinit var recyclerviewComment: RecyclerView
    lateinit var contentComment:EditText
    lateinit var imageLike: ImageView
    lateinit var contentmess : String
    var likeCount: Int? = null
    var commentCount: Int? = null
    lateinit var countLike:TextView
    lateinit var countcomment:TextView
    lateinit var linnerComment:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        contentComment = findViewById(R.id.contentComment)
        imageLike = findViewById(R.id.imageLike)
        countLike = findViewById(R.id.countLike)
        linnerComment = findViewById(R.id.linnerComment)
        countcomment = findViewById(R.id.countcomment)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val sharedPreferences = getSharedPreferences("UserLogin", MODE_PRIVATE)
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
            user_idpost = receBundle.getString("user_idpost").toString()
            likeCount = intent.getIntExtra("likeCount", 0)
            commentCount = intent.getIntExtra("commentCount", 0)

            commentCount = (commentCount ?: 0)
            if (commentCount!! > 0){
                countcomment.text = commentCount.toString()
            }else{
                linnerComment.visibility = View.GONE
            }


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

            likeCount = (likeCount ?: 0) - 1
            if (likeCount!! > 0) {
                countLike.text = "Bạn, $likeCount người khác"
            } else {
                countLike.text = "Bạn"
            }

        } else {
            Glide.with(this)
                .load(R.drawable.like)
                .into(imagelikeback)
            textlike.setTextColor(ContextCompat.getColor(this, R.color.colorxam))
            countLike.text = "$likeCount"
        }
        linlerLike.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserLogin", MODE_PRIVATE)
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
        imageLike.setOnClickListener{
                contentmess = contentComment.text.toString()
            if (contentmess.isEmpty()){
                Toast.makeText(baseContext,"Nhập nội dung!!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val retrofit = Interact.retrofit.create(Interact::class.java)
            val commentNould = Interact.CommentNould(iduser, contentmess)
            val call = retrofit.addObjImage(idpost, commentNould)
            call.enqueue(object : Callback<Interact.CommentApiResponse> {
                override fun onResponse(
                    call: Call<Interact.CommentApiResponse>,
                    response: Response<Interact.CommentApiResponse>
                ) {
                    if (response.isSuccessful) {
                        val newComment = response.body()?.nouvelle
                        newComment?.let {
                            commentsList.add(it)
                            contentComment.setText("")
                            getData()
                            adapter.notifyDataSetChanged()
                        }
                        Toast.makeText(
                            baseContext,
                            "Da binh luan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<Interact.CommentApiResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(baseContext, "Fail: " + t, Toast.LENGTH_SHORT)
                        .show()
                }

            })
        }

    }
    private val commentsList: MutableList<Interact.CommentNould> = mutableListOf()


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
    private val MY_REQUEST_CODE = 102
    private val PICK_IMAGE_REQUEST = 1
    override fun onResume() {
        super.onResume()
        getData()
    }

}