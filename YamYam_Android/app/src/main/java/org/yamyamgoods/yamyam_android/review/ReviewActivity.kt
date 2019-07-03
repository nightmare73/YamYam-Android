package org.yamyamgoods.yamyam_android.review

import android.app.PendingIntent.getActivity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.fragment_review_all.*
import kotlinx.android.synthetic.main.top_navigation_tab_review.*
import org.yamyamgoods.yamyam_android.R
import org.yamyamgoods.yamyam_android.home.best.review.adapter.BestReviewRVAdapter
import org.yamyamgoods.yamyam_android.review.adapter.ReviewStatePagerAdapter
import org.yamyamgoods.yamyam_android.review.all.adapter.ReviewAllRVAdapter
import org.yamyamgoods.yamyam_android.util.TempData

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        configureTitleBar()
        configureMainTab()
    }

    private fun configureTitleBar(){
        btn_review_tab_close.setOnClickListener {
            finish()
        }
    }

    private fun configureMainTab() {
        vp_review_nav.adapter = ReviewStatePagerAdapter(supportFragmentManager, 2)
        vp_review_nav.offscreenPageLimit = 2
        tl_review_nav.setupWithViewPager(vp_review_nav)

        val navCategoryMainLayout: View = (this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.top_navigation_tab_review, null, false)
        tl_review_nav.getTabAt(0)!!.customView = navCategoryMainLayout.findViewById(R.id.btn_review_nav_all) as ConstraintLayout
        tl_review_nav.getTabAt((1))!!.customView = navCategoryMainLayout.findViewById(R.id.btn_review_nav_photo) as ConstraintLayout
        tl_review_nav.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                selectedBestTab(position = tab!!.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                selectedBestTab(position = tab!!.position)
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedBestTab(position = tab!!.position)
            }
        })
    }

    private fun selectedBestTab(position: Int) {
        if (position == 0) {
            tv_review_nav_all.setTextColor(resources.getColor(R.color.colorTabDarkGray))
            tv_review_nav_photo.setTextColor(resources.getColor(R.color.colorTabLightGray))
        } else {
            tv_review_nav_all.setTextColor(resources.getColor(R.color.colorTabLightGray))
            tv_review_nav_photo.setTextColor(resources.getColor(R.color.colorTabDarkGray))
        }
    }

}
