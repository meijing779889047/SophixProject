package com.mj.sophixproject;

/**
 * Created by lenovo on 2017/7/24.
 */

public class EventbusBean {
    private int      pos;
    private  String  tag;
    private  Object  obj;

    public EventbusBean(int pos, String tag, Object obj) {
        this.pos = pos;
        this.tag = tag;
        this.obj = obj;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
