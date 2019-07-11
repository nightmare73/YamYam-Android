package org.yamyamgoods.yamyam_android.reviewdetail

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_review_detail.*
import org.yamyamgoods.yamyam_android.R
import org.yamyamgoods.yamyam_android.dataclass.ReviewData
import org.yamyamgoods.yamyam_android.home.best.review.BestReviewFragment
import org.yamyamgoods.yamyam_android.network.ApplicationController
import org.yamyamgoods.yamyam_android.network.NetworkServiceGoods
import org.yamyamgoods.yamyam_android.network.get.GetReviewDetailResponse
import org.yamyamgoods.yamyam_android.network.get.ReviewCommentData
import org.yamyamgoods.yamyam_android.network.get.ReviewDetailGoodsData
import org.yamyamgoods.yamyam_android.network.post.PostCommentWriteRequestData
import org.yamyamgoods.yamyam_android.reviewdetail.adapter.ReviewDetailRVAdapter
import org.yamyamgoods.yamyam_android.util.TempData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReviewDetailActivity : AppCompatActivity() {

    val token: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWR4IjoxLCJpYXQiOjE1NjIzMTUzNjYsImV4cCI6MTU2MzYyOTM2Nn0.ZkDGasoDPHTrGvy7yFOT9cPjTQ7gnnUOqekY_zYrAuc"

    val networkService: NetworkServiceGoods by lazy {
        ApplicationController.networkServiceGoods
    }

    lateinit var reviewDetailRVAdapter: ReviewDetailRVAdapter
    lateinit var edtComment: EditText;

    var options: RequestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(10))

    var reviewIndex: Int = 0
    var starCount: Int = 0
    var thumbCount: Int = 0
    var commentCount: Int = 0
    var thumbFlag: Int = 0
    var userIndex: Int = 0
    lateinit var userImage: String
    lateinit var userNickname: String
    lateinit var date: String
    lateinit var reviewContents: String
    lateinit var imageUrl: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_detail)
        getVariables()
        getReviewDetailResponse()
        //configureProduct(TempData.ReviewDetailProducts(), 2)
        configureReview()
        //configureComments()



        // 알람보내기
//        btn_review_detail_comment_input.setOnClickListener{
//            makeAlarm()
//        }
    }

    private fun getVariables() {
        setResult(Activity.RESULT_OK, intent)
        var rvDTO: ReviewData = intent.getParcelableExtra("dto")
        reviewIndex = rvDTO.goods_review_idx
        userNickname = rvDTO.user_name
        date = rvDTO.goods_review_date
        reviewContents = rvDTO.goods_review_content
        userImage = rvDTO.user_img
        starCount = rvDTO.goods_review_rating
        imageUrl = rvDTO.goods_review_img
        thumbFlag = rvDTO.review_like_flag
        thumbCount = rvDTO.goods_review_like_count
        commentCount = rvDTO.goods_review_cmt_count
    }

    fun getReviewDetailResponse(){
        networkService.getReviewDetailResponse(
            "application/json", token, reviewIndex).enqueue(object: Callback<GetReviewDetailResponse> {
            override fun onFailure(call: Call<GetReviewDetailResponse>, t: Throwable) {
                Log.e("현주", t.toString())
            }

            override fun onResponse(call: Call<GetReviewDetailResponse>, response: Response<GetReviewDetailResponse>) {

                var productStarRate: List<ImageView> = listOf(
                    findViewById(R.id.iv_review_detail_poduct_star1),
                    findViewById(R.id.iv_review_detail_poduct_star2),
                    findViewById(R.id.iv_review_detail_poduct_star3),
                    findViewById(R.id.iv_review_detail_poduct_star4),
                    findViewById(R.id.iv_review_detail_poduct_star5)
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        /* 굿즈  */
                        it.data!!.goods.let { goods ->
                            Glide.with(this@ReviewDetailActivity)
                                .load(goods.goods_img)
                                .apply(options)
                                .into(iv_review_detail_product)

                            tv_review_detail_store_name.text = goods.store_name
                            tv_review_detail_product_name.text = goods.goods_name
                            tv_review_detail_product_price.text = goods.goods_price

                            tv_review_detail_product_star_rate.text = roundString(goods.goods_rating)
                            var productStarCount = goods.goods_rating
                            var intStarCount: Int = productStarCount.toInt()
                            var remainder: Float = goods.goods_rating - intStarCount.toFloat()

                            for (i in 0 until (intStarCount)) {
                                productStarRate[i].setImageResource(R.drawable.img_goods_star)
                                if (0.5 > remainder && remainder >= 0)
                                    continue
                                if (1 > remainder && remainder >= 0.5)
                                    productStarRate[i + 1].setImageResource(R.drawable.img_goods_star_half)
                            }
                        }

                        /* 대댓글:comment 부분*/
                        it.data!!.review_comment.let { comment ->
                            var tmp: ArrayList<ReviewCommentData> = comment
                            rv_review_detail_comment_list.apply{
                                adapter = ReviewDetailRVAdapter(this@ReviewDetailActivity, tmp)
                                layoutManager = LinearLayoutManager(this@ReviewDetailActivity)
                            }
                            reviewDetailRVAdapter = ReviewDetailRVAdapter(this@ReviewDetailActivity, tmp)
                        }
                    }
                }
            }
        })
    }

    // 굿즈 + 상점 부분
    /*
    private fun configureProduct(dataList: List<ProductShortInfo>, position: Int) {
        dataList[position].let { item ->
            Glide.with(this)
                    .load(item.productImageUrl)
                    .apply(options)
                    .into(findViewById(R.id.iv_review_detail_product))

            tv_review_detail_store_name.text = item.storeName
            tv_review_detail_product_name.text = item.productName
            tv_review_detail_product_price.text = item.amount

            var productStarRate: List<ImageView> = listOf(
                    findViewById(R.id.iv_review_detail_poduct_star1),
                    findViewById(R.id.iv_review_detail_poduct_star2),
                    findViewById(R.id.iv_review_detail_poduct_star3),
                    findViewById(R.id.iv_review_detail_poduct_star4),
                    findViewById(R.id.iv_review_detail_poduct_star5)
            )

            var productStarCount = item.starRate
            var intStarCount: Int = productStarCount.toInt()
            var remainder: Float = item.starRate - intStarCount.toFloat()

            for (i in 0 until (intStarCount)) {
                productStarRate[i].setImageResource(R.drawable.img_goods_star)
                if (0.5 > remainder && remainder >= 0)
                    continue
                if (1 > remainder && remainder >= 0.5)
                    productStarRate[i + 1].setImageResource(R.drawable.img_goods_star_half)
            }
        }
    }
    */

    // 리뷰 부분
    private fun configureReview() {
        Glide.with(this)
                .load(userImage)
                .centerCrop()
                .circleCrop()
                .into(iv_review_detail_review_user_img)

        tv_review_detail_review_user_nickname.text = userNickname
        tv_review_detail_review_date.text = date
        tv_review_detail_review_contents.text = reviewContents

        var starRate: List<ImageView> = listOf(
                findViewById(R.id.iv_review_detail_review_star1),
                findViewById(R.id.iv_review_detail_review_star2),
                findViewById(R.id.iv_review_detail_review_star3),
                findViewById(R.id.iv_review_detail_review_star4),
                findViewById(R.id.iv_review_detail_review_star5)
        )

        var reviewImage: List<ImageView> = listOf(
                findViewById(R.id.iv_review_detail_review_image1),
                findViewById(R.id.iv_review_detail_review_image2),
                findViewById(R.id.iv_review_detail_review_image3)
        )

        for (i in 0 until (starCount)) {
            starRate[i].setImageResource(R.drawable.icon_colorstar)
        }

        var imageNum = imageUrl.size
        var etcImageNum: TextView = findViewById(R.id.tv_review_detail_review_etc_image_num) as TextView
        if (imageNum > 3) {
            setVisible(etcImageNum)
            reviewImage[2].setColorFilter(Color.parseColor("#333333"), PorterDuff.Mode.MULTIPLY)
            etcImageNum.text = "+" + (imageNum - 3).toString()
            imageNum = 3
            Log.v("현주", "이미지 3 초과")
        }
        for (i in 0 until imageNum) {
            Log.v("현주", "이미지 3 이하")
            setVisible(reviewImage[i])
            Glide.with(this)
                    .load(imageUrl[i])
                    .apply(options)
                    .into(reviewImage[i])
        }

/*
        if (thumbFlag == 1)
            iv_review_detail_review_thumbs.isSelected  = !(iv_review_detail_review_thumbs.isSelected)
        btn_review_detail_review_thumbs.setOnClickListener{
            thumbFlag = Math.abs(thumbFlag - 1)
            iv_review_detail_review_thumbs.isSelected  = !(iv_review_detail_review_thumbs.isSelected)
            BestReviewFragment.instance.flag = 1
        }
*/
        tv_review_detail_review_thumbs_num.text = thumbCount.toString()
        tv_rv_item_best_review_all_comments_num.text = commentCount.toString()
    }

    /*
        val reviewIdx: Int,
    val contents: String,
    val userIdxForAlarm: Int,
    val recommentFlag: Int  //0: 댓글, 1: 대댓글
     */

    // 댓글 작성 서버 통신 : 대댓글 아님 : 0
    private fun postCommentWriteResponse
                (reviewIdx: Int, contents: String, userIdxForAlarm: Int, recommentFlag: Int){
        networkService.postCommentWriteRequest("application/json", token,
            PostCommentWriteRequestData(reviewIdx, contents, userIdxForAlarm, recommentFlag))
            .enqueue(object: Callback<PostCommentWriteRequestData>{
                override fun onFailure(call: Call<PostCommentWriteRequestData>, t: Throwable) {
                    Log.e("현주", t.toString())
                }

                override fun onResponse(
                    call: Call<PostCommentWriteRequestData>,
                    response: Response<PostCommentWriteRequestData>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let{
                            Log.v("현주", "댓글 작성 통신 성공  response : ${response.body()}")
                        }
                    }
                }
            })
    }

    // 댓글 작성자에게 알람 보내기
    /*
    private fun makeAlarm(){
        var content : String = edt_review_detail_input_comment.text.toString()
        PostCommentWriteRequestData(reviewIndex, content, user_idx, 0)  //user_idx를 받아올 수 없다.
    }*/

    // 태그 기능
    fun editTextTag(nickname: String, cmtIdx: Int){
        edtComment =  findViewById(R.id.edt_review_detail_input_comment)
        var originText: String = edtComment.getText().toString()

        var tagUser: String = originText.plus("@").plus(nickname).plus(" ")
        edt_review_detail_input_comment.setText(tagUser)
        edt_review_detail_input_comment.setSelection(edt_review_detail_input_comment.length())

        // 댓글을 입력했을 때
        var content : String = edt_review_detail_input_comment.text.toString()
        btn_review_detail_comment_input.setOnClickListener{
            PostCommentWriteRequestData(reviewIndex, content, cmtIdx, 1)
        }
    }

   fun setVisible(view: View) {
        view.visibility = View.VISIBLE
    }

    // 소수점 첫째자리까지만 표현하기
    fun roundString(value: Float): String{
        var strFloat: String= String.format("%.1f ", value)
        return strFloat
    }
}