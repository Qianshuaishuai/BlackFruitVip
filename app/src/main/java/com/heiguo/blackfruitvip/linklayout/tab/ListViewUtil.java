package com.heiguo.blackfruitvip.linklayout.tab;

import android.graphics.Color;
import android.view.View;
import android.widget.ListView;

import com.heiguo.blackfruitvip.linklayout.base.BaseViewGroupUtil;


/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-10-16
 * Time: 05:49
 * <br/><br/>
 */
public class ListViewUtil extends BaseViewGroupUtil<ListView> {
    public ListViewUtil(ListView listView) {
        super(listView);
    }

    @Override
    public void updateEdges(){
        beginPos = mViewGroup.getFirstVisiblePosition();
        endPos = mViewGroup.getLastVisiblePosition();
    }

    @Override
    public void smoothScrollTo(int pos) {
        mViewGroup.smoothScrollToPosition(pos);
    }

    /**
     * 限定边界在 [beginPos, endPos] 之间
     * @param pos
     * @return
     */
    public int updatePosOnScrolled(int pos) {
        updateEdges();
        int newPos = pos;
        newPos = Math.max(newPos, beginPos);
        newPos = Math.min(newPos, endPos);
        setViewSelected(newPos);
        return newPos;
    }

    @Override
    protected void setViewSelected(View view, boolean isSelected){
        view.setBackgroundColor(isSelected? Color.rgb(245,245,245): Color.rgb(229,229,229));
    }
}