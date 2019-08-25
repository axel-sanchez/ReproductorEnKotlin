package com.axel.reproductorenkotlin.ui.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPageAdapter(fm: FragmentManager, private val items: List<itemViewPager>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return items[position].fragment
    }

    override fun getCount(): Int {
        return items.count()
    }

    // This determines the title for each tab
    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position
        return items[position].title
    }


}

class itemViewPager(val title : String ,val fragment: Fragment)