package org.yamyamgoods.yamyam_android.home

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_home.*
import org.yamyamgoods.yamyam_android.R
import org.yamyamgoods.yamyam_android.home.best.BestTabFragment
import org.yamyamgoods.yamyam_android.home.Goods.GoodsFragment
import org.yamyamgoods.yamyam_android.home.store.ranking.StoreRankingFragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setOnClickListener()

        startView()
    }
    fun startView(){
        //TODO 처음 뷰 넣어주세요
        addFragment(BestTabFragment())
        iv_bottom_best_icon.isSelected=true
        tv_bottom_best_text.isSelected=true
        setOnClickListener()
    }
    fun setOnClickListener(){
        btn_bottom_best.setOnClickListener{
            //TODO 프래그먼트 넣어주세요
            replaceFragment(BestTabFragment())
            clearBtnSelect()
            iv_bottom_best_icon.isSelected=true
            tv_bottom_best_text.isSelected=true
        }
        btn_bottom_store.setOnClickListener{
            replaceFragment(StoreRankingFragment())

            clearBtnSelect()
            iv_bottom_store_icon.isSelected=true
            tv_bottom_store_text.isSelected=true
        }
        btn_bottom_goods.setOnClickListener{
            replaceFragment(GoodsFragment())
            clearBtnSelect()
            iv_bottom_goods_icon.isSelected=true
            tv_bottom_goods_text.isSelected=true
        }
        btn_bottom_like.setOnClickListener{
//            replaceFragment()
            clearBtnSelect()
            iv_bottom_like_icon.isSelected=true
            tv_bottom_like_text.isSelected=true
        }
    }

    fun replaceFragment(fragment:Fragment){
        val transaction:FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_main_fragment,fragment)
        transaction.commit()
    }
    fun addFragment(fragment: Fragment){
        val transaction:FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_main_fragment,fragment)
        transaction.commit()
    }

    fun clearBtnSelect(){
        iv_bottom_best_icon.isSelected=false
        tv_bottom_best_text.isSelected=false
        iv_bottom_store_icon.isSelected=false
        tv_bottom_store_text.isSelected=false
        iv_bottom_goods_icon.isSelected=false
        tv_bottom_goods_text.isSelected=false
        iv_bottom_like_icon.isSelected=false
        tv_bottom_like_text.isSelected=false
    }

    //액티비티에서는 Back버튼을 두번 눌렀을 때, 종료되도록
//    override fun onBackPressed() {
//        val fragment = this.supportFragmentManager.findFragmentById(R.id.fl_goods_fragment_frag)
//        (fragment as? GoodsFragment.IOnBackPrssed)?.onBackPressed()?.not()?.let{
//            super.onBackPressed()
//        }
//    }
}
