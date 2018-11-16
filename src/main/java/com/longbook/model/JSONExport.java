package com.longbook.model;

import net.minidev.json.JSONObject;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 4:42 PM
 */
public abstract class JSONExport {
    public abstract JSONObject toJSON();
    public String toJSONString(){
        return toJSON().toJSONString();
    }
}
