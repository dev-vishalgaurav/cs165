package edu.cs.dartmouth.cs165.myruns.vishal.utils;

import android.net.Uri;

/**
 * Created by root on 5/18/16.
 */
public class NetworkUtils {

    public static final String SERVER_URL = "http://10.31.123.105:8080/";
    public static final String API_REGISTRATION = "_ah/api/";
    public static final String API_POST = "post.do";

    public static final Uri URL_POST = Uri.parse(SERVER_URL + API_POST);

}
