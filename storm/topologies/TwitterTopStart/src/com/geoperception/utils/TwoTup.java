package com.geoperception.utils;

/**
 * Created by johnluke on 3/28/15.
 */
public class TwoTup<TK,TV> {

    public TK key;
    public TV value;

    public TwoTup(TK key, TV value){
        this.key = key;
        this.value = value;
    }

    public TwoTup(){
        this.key = null;
        this.value = null;
    }


}
