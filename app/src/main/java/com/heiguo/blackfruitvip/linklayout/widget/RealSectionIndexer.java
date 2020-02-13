package com.heiguo.blackfruitvip.linklayout.widget;

import android.widget.SectionIndexer;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.bean.TypeBean;

import java.util.List;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-10-16
 * Time: 00:17
 * <br/><br/>
 * SectionIndexer 的具体实现类, 作为被代理对象
 */
public class RealSectionIndexer implements SectionIndexer {
    private List<TypeBean> mTypeObjects;
    private List<GoodBean> mContentObjects;

    public RealSectionIndexer(List<TypeBean> typeObjects, List<GoodBean> contentObjects) {
        mTypeObjects = typeObjects;
        mContentObjects = contentObjects;
    }

    @Override
    public String[] getSections() {
//        if(mTypeObjects == null)
//            return new String[0];
//
//        return Stream.of(mTypeObjects)
//                .groupBy(obj -> getSectionForPosition(obj))   // section 作为 key 分组
//                .map(entry -> entry.getKey())   // 获取所有的 key, 也就是 section
//                .sorted()
//                .collect(Collectors.toList())
//                .toArray(new String[0]);
        if (mTypeObjects == null)
            return new String[0];

        String[] objects = new String[mTypeObjects.size()];
        for (int t = 0; t < mTypeObjects.size(); t++) {
            objects[t] = mTypeObjects.get(t).getName();
        }

        return objects;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        int count = 0;
        for (int s = 0; s < sectionIndex + 1; s++) {
            count = count + mTypeObjects.get(s).getCount();
        }
        return count;
    }

    @Override
    public int getSectionForPosition(int position) {
        int section = 0;
        if (position == 0) {
            return 0;
        }
        for (int t = 0; t < mTypeObjects.size(); t++) {
            position = position - mTypeObjects.get(t).getCount();
            if (position < 0) {
                return t;
            }
        }

        return 0;
    }

//    private void selectGoodList(int position) {
//        int type = mTypeObjects.get(position).getId();
//
//        currentList.clear();
//        for (int a = 0; a < mContentObjects.size(); a++) {
//            if (mContentObjects.get(a).getType() == type) {
//                currentList.add(mContentObjects.get(a));
//            }
//        }
//
//    }
}