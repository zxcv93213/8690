package com.zz.sharebike;

import cn.bmob.v3.BmobObject;

/**
 * Created by zz on 2016/11/25.
 */

public class Bikes extends BmobObject{

    private  int id;
    private  int password;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getPassword() {
        return password;
    }




}
