package com.longbook.dao;

import lib.DB;

import java.security.MessageDigest;

/**
 * Created by Long
 * Date: 11/16/2018
 * Time: 4:07 PM
 */
public class AdminDao {
    private static DB db = new DB();
    public static boolean isValid(String username, String password){
        //encrypt md5 password
        try {
            byte[] bytesOfPassword = password.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfPassword);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < thedigest.length; ++i) {
                sb.append(Integer.toHexString((thedigest[i] & 0xFF) | 0x100).substring(1,3));
            }
            password = sb.toString();
        } catch (Exception e) {
//            e.printStackTrace();
            return false ;
        }

        if(db.select("password", "admin", "username = '" + DB.validSql(username) + "'")){
            return db.getResult().get(0).get(0).equals(password);
        } else return false;
    }
}
