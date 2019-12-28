package com.heiguo.blackfruitvip.bean;

import java.util.List;

public class StoreDetailBean {
    private List<GoodBean> goods;
    private List<TypeBean> storeTypes;

    public List<GoodBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodBean> goods) {
        this.goods = goods;
    }

    public List<TypeBean> getStoreTypes() {
        return storeTypes;
    }

    public void setStoreTypes(List<TypeBean> storeTypes) {
        this.storeTypes = storeTypes;
    }
}
