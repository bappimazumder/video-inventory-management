package com.bappi.videoinventorymanagement.config;

public class ApiPath {
    public static final String API_CORE_NAME = "/api";
    public static final String API_VERSION = "/v1";
    public static final String API_BASE_PATH = API_CORE_NAME + API_VERSION;
    public static final String PRODUCT_TITLE = "/vim";
    public static final String API_USER = PRODUCT_TITLE + "/user";
    public static final String API_POST_ADD_USER = "/addUser";
    public static final String API_VIDEO = PRODUCT_TITLE + "/video";
    public static final String API_POST_SAVE_VIDEO = "/saveVideo";
    public static final String API_GET_VIDEOS = "/getVideos";
    public static final String API_PUT_UPDATE_VIDEO = "/updateVideo";
    public static final String API_PUT_DELETE_VIDEO = "/deleteVideo";
    public static final String API_PUT_ASSIGN_USER = "/assignUser";
    public static final String API_ACTIVITY_LOG = PRODUCT_TITLE + "/activityLog";
    public static final String API_POST_USER_GET_TOKEN = "/getToken";

}
