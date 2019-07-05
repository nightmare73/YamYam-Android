package org.yamyamgoods.yamyam_android.productdetail.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.jetbrains.anko.imageResource
import org.yamyamgoods.yamyam_android.R
import org.yamyamgoods.yamyam_android.review.all.ReviewAllItem
import org.yamyamgoods.yamyam_android.util.dp2px
import org.yamyamgoods.yamyam_android.util.getScreenWidth

/**
 * Created By Yun Hyeok
 * on 7월 04, 2019
 */

class ProductDetailReviewRVAdatper(private val ctx: Context, private val dataList: List<ReviewAllItem>)
    : RecyclerView.Adapter<ProductDetailReviewRVAdatper.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.rv_item_product_detail_act_review, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        dataList[position].let { data ->
            setProfile(data.userImageUrl, holder.ivProfile)
            setImageViewList(data.imageUrl, holder)
            setStarView(data.starCount, holder.ivStars)

            holder.tvNickname.text = data.userNickname
            holder.tvDate.text = data.date
            holder.tvContent.text = data.reviewContents

            holder.tvLikeCount.text = data.thumbCount.toString()
            holder.tvCommentsCount.text = data.commentsCount.toString()

            holder.btnLike.setOnClickListener {
                //버튼눌리면 노래지는 것
            }

        }
    }

    private fun setImageViewList(images: List<String>, holder: Holder) {
        if (images.isEmpty()) {
            return
        }
        var imageCount = images.size
        setImageZoneHeight(holder.clImageZone)
        if (images.size > 3) {
            holder.clImageMask.visibility = View.VISIBLE
            holder.tvImageMoreCount.text = (images.size - 3).toString()
            imageCount = 3
        }
        for (i in 0 until imageCount) {
            holder.ivImages[i].visibility = View.VISIBLE
            Glide
                    .with(ctx)
                    .load(images[i])
                    .into(holder.ivImages[i])
        }
    }

    private fun setImageZoneHeight(zone: ViewGroup) {
        val dynamicHeight = getDynamicImageZoneHeight()
        val layoutParams = zone.layoutParams
        layoutParams.height = dynamicHeight
        zone.layoutParams = layoutParams
    }

    private fun getDynamicImageZoneHeight(): Int {
        val phoneWidth = getScreenWidth(ctx)
        val imageZoneWidth = phoneWidth - dp2px(37f + 16f, ctx)
        return imageZoneWidth / 3
    }

    private fun setProfile(url: String, imageView: ImageView) =
            Glide
                    .with(ctx)
                    .load(url)
                    .into(imageView)

    private fun setStarView(score: Int, stars: List<ImageView>) {
        if (score == 0) {
            return
        }
        for (i in 0 until score) {
            stars[i].imageResource = R.drawable.img_goods_star
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfile: ImageView = itemView.findViewById(R.id.iv_rv_item_product_detail_review_profile)

        val tvNickname: TextView = itemView.findViewById(R.id.tv_rv_item_product_detail_review_nickname)
        val tvDate: TextView = itemView.findViewById(R.id.tv_rv_item_product_detail_review_date)
        val tvContent: TextView = itemView.findViewById(R.id.tv_rv_item_product_detail_review_content)

        val ivStars: List<ImageView> = listOf(
                itemView.findViewById(R.id.iv_rv_item_product_detail_review_star1),
                itemView.findViewById(R.id.iv_rv_item_product_detail_review_star2),
                itemView.findViewById(R.id.iv_rv_item_product_detail_review_star3),
                itemView.findViewById(R.id.iv_rv_item_product_detail_review_star4),
                itemView.findViewById(R.id.iv_rv_item_product_detail_review_star5)
        )

        val ivImages: List<ImageView> = listOf(
                itemView.findViewById(R.id.iv_rv_item_product_detail_review_image1),
                itemView.findViewById(R.id.iv_rv_item_product_detail_review_image2),
                itemView.findViewById(R.id.iv_rv_item_product_detail_review_image3)
        )

        val clImageZone: ConstraintLayout = itemView.findViewById(R.id.cl_rv_item_product_detail_review_image_zone)
        val clImageMask: ConstraintLayout = itemView.findViewById(R.id.cl_rv_item_product_detail_review_image_mask)
        val tvImageMoreCount: TextView = itemView.findViewById(R.id.tv_rv_item_product_detail_review_image_more_count)

        val btnLike: ConstraintLayout = itemView.findViewById(R.id.btn_rv_item_product_detail_review_like)
        val tvLikeCount: TextView = itemView.findViewById(R.id.tv_rv_item_product_detail_review_like_count)
        val tvCommentsCount: TextView = itemView.findViewById(R.id.tv_rv_item_product_detail_review_comments_count)
    }
}