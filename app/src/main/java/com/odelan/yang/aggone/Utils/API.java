package com.odelan.yang.aggone.Utils;

import android.content.Context;
import android.util.Pair;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.odelan.yang.aggone.Helpers.SaveSharedPreference;
import com.odelan.yang.aggone.Model.Admob;
import com.odelan.yang.aggone.Model.Audience;
import com.odelan.yang.aggone.Model.Career;
import com.odelan.yang.aggone.Model.ClubStat;
import com.odelan.yang.aggone.Model.Description;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.Notification;
import com.odelan.yang.aggone.Model.Prize;
import com.odelan.yang.aggone.Model.Result;
import com.odelan.yang.aggone.Model.Resume;
import com.odelan.yang.aggone.Model.Story;
import com.odelan.yang.aggone.Model.StoryMsg;
import com.odelan.yang.aggone.Model.StoryView;
import com.odelan.yang.aggone.Model.Summary;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.Model.ViewStatic;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class API {
    public static final String baseUrl                      = "https://www.aggone.org/";//"https://www.rocmail10.com/";
    public static final String imgDirUrl                    = "uploads/images/";
    public static final String videoDirUrl                  = "uploads/videos/";
    public static final String resumeDirUrl                 = "uploads/resumes/";
    public static final String articleDirUrl                = "uploads/articles/";
    public static final String storyDirUrl                  = "uploads/stories/";
    private static final String apiUrl                      = baseUrl +"api/";
    private static final String loginWithEmailAndPassword   = apiUrl + "user/login_email_password";
    private static final String loginWithSocial             = apiUrl + "user/login_social";
    private static final String signupWithEmailAndPassword  = apiUrl + "user/signup_email_password";
    private static final String getUserByEmail              = apiUrl + "user/get_user_by_email";
    private static final String getUserByName               = apiUrl + "user/get_user_by_name";
    private static final String getAllUsers                 = apiUrl + "user/get_all_user";
    private static final String getUsersByType              = apiUrl + "user/get_users_by_type";
    private static final String searchUsers                 = apiUrl + "user/search_users";
    private static final String updateUser                  = apiUrl + "user/update_user";
    private static final String getTaggedUsers              = apiUrl + "user/get_tagged_users";
    private static final String resetPassword               = apiUrl + "user/reset_password";
    private static final String forgotPassword              = apiUrl + "user/forgot_password";
    private static final String filterUser                  = apiUrl + "user/get_user_by_filter";
    private static final String changePassword              = apiUrl + "user/change_password";
    private static final String updateUserField             = apiUrl + "user/update_user_field";
    private static final String uploadResume                = apiUrl + "user/upload_resume";
    private static final String viewProfile                 = apiUrl + "user/view_profile";
    private static final String getAllFeeds                 = apiUrl + "feed/get_all_feeds";
    private static final String getAllFeedsByUserid         = apiUrl + "feed/get_all_feeds_by_user";
    private static final String getSportsFeeds              = apiUrl + "feed/get_sports_feeds";
    private static final String getMyFeeds                  = apiUrl + "feed/get_my_feeds";
    private static final String saveFeed                    = apiUrl + "feed/save_feed";
    private static final String addViewFeed                 = apiUrl + "feed/add_view_feed";
    private static final String deleteFeed                  = apiUrl + "feed/delete_feed";
    private static final String privateFeed                 = apiUrl + "feed/private_feed";
    private static final String uploadFile                  = apiUrl + "feed/upload_file";
    private static final String uploadArticles              = apiUrl + "feed/upload_articles";
    private static final String searchVideoFeeds            = apiUrl + "feed/search_video_feeds";
    private static final String uploadImage                 = apiUrl + "user/upload_image";
    private static final String recommendUser               = apiUrl + "user/recommend_user";
    private static final String reportUser                  = apiUrl + "user/report_user";

    private static final String addLikeFeed                 = apiUrl + "like/like_feed";
    private static final String reportFeed                  = apiUrl + "feed/report_feed";

    private static final String saveResult                  = apiUrl + "result/save_result";
    private static final String deleteUserClubSummary       = apiUrl + "result/delete_user_club_summary";
    private static final String getYearResultSummary        = apiUrl + "result/get_year_summary";
    private static final String getUserResultSummary        = apiUrl + "result/get_user_summary";
    private static final String getClubMonthResultSummary   = apiUrl + "result/get_club_month_summary";
    private static final String getSportResultSummary       = apiUrl + "result/get_sport_summary";
    private static final String getClubSummary              = apiUrl + "result/get_club_summary";
    private static final String getClubYearSummary          = apiUrl + "result/get_club_year_summary";
    private static final String deleteClubYearSummary       = apiUrl + "result/delete_club_year_summary";
    private static final String adjustStatValue             = apiUrl + "result/adjust_summary";
    private static final String getSummaryByClub            = apiUrl + "result/get_summary_by_club";

    private static final String getAllAdmobs                = apiUrl + "admob/get_all_admobs";
    private static final String saveAdmob                   = apiUrl + "admob/save_admob";
    private static final String filterAdmobs                = apiUrl + "admob/filter_admobs";
    private static final String deleteAdmob                 = apiUrl + "admob/delete_admob";

    private static final String getAllCareersByUserid       = apiUrl + "career/get_all_careers_by_userid";
    private static final String saveCareer                  = apiUrl + "career/save_career";
    private static final String updateCareer                = apiUrl + "career/update_career";
    private static final String deleteCareer                = apiUrl + "career/delete_career";

    private static final String getAllDescriptionsByUserid  = apiUrl + "description/get_all_descriptions_by_userid";
    private static final String saveDescription             = apiUrl + "description/save_description";
    private static final String updateDescription           = apiUrl + "description/update_description";

    private static final String getAllPrizesByUserid        = apiUrl + "prize/get_all_prizes_by_user";
    private static final String savePrize                   = apiUrl + "prize/save_prize";
    private static final String updatePrize                 = apiUrl + "prize/update_prize";
    private static final String deletePrize                 = apiUrl + "prize/delete_prize";
    private static final String getAllMessage               = apiUrl + "message/get_all_messages_by_roomid";
    private static final String saveMessage                 = apiUrl + "message/add_message";
    private static final String getContact                  = apiUrl + "contact/get_contact_by_userid";

    private static final String getFollower                 = apiUrl + "follow/get_follower";
    private static final String checkFollow                 = apiUrl + "follow/check_follow";
    private static final String deleteFollow                = apiUrl + "follow/delete_follow";
    private static final String getFollowing                = apiUrl + "follow/get_following";
    private static final String addFollow                   = apiUrl + "follow/add_follow";

    private static final String addBlock                    = apiUrl + "block/add_block";
    private static final String deleteBlock                 = apiUrl + "block/remove_block";
    private static final String getBlocks                   = apiUrl + "block/get_blocked_users";

    private static final String saveBookmark                = apiUrl + "bookmark/save_bookmark";
    private static final String getBookmarks                = apiUrl + "bookmark/get_bookmarks";
    private static final String deleteBookmark              = apiUrl + "bookmark/delete_bookmark";

    private static final String getAllResumes                = apiUrl + "user/get_all_resumes";
    private static final String deleteResume                 = apiUrl + "user/delete_resume";

    private static final String getStrengths                 = apiUrl + "user/get_strengths";
    private static final String saveStrengths                = apiUrl + "user/save_strengths";

    private static final String getTeamMembers               = apiUrl + "user/get_team_members";
    private static final String joinTeam                     = apiUrl + "user/join_team";
    private static final String leaveTeam                    = apiUrl + "user/leave_team";

    private static final String getAllNotis                  = apiUrl + "alarm/get_all_notifications";
    private static final String removeNoti                   = apiUrl + "alarm/remove_notification";
    private static final String chatNotification             = apiUrl + "alarm/push_chat_notification";

    private static final String uploadStory                  = apiUrl + "story/upload_story";
    private static final String saveStory                    = apiUrl + "story/save_story";
    private static final String getAllStory                  = apiUrl + "story/get_all_story";
    private static final String getStoriesByUser             = apiUrl + "story/get_stories_user";
    private static final String viewStory                    = apiUrl + "story/view_story";
    private static final String replyStory                   = apiUrl + "story/reply_story";
    private static final String getStoryViews                = apiUrl + "story/get_story_views";
    private static final String getStoryMsg                  = apiUrl + "story/get_story_msg";
    private static final String deleteStory                  = apiUrl + "story/delete_story";
    private static final String getStoryViewStatics          = apiUrl + "story/get_view_statics";
    private static final String getAudience                  = apiUrl + "user/get_audience";
    private static final String reportStory                  = apiUrl + "story/report_story";


    public static void loginWithEmailAndPassword(final Context ctx, String email, String password, final APICallback<User> callback) {
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String oneSignalId = status.getSubscriptionStatus().getUserId();
        if (oneSignalId == null)
            oneSignalId = "1234567890";
        AndroidNetworking.post(loginWithEmailAndPassword)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .addBodyParameter("push_token", oneSignalId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                String token = response.getString("token");
                                putAccessToken(ctx, token);
                                JSONObject json_user = response.getJSONObject("user");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                callback.onSuccess(user);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure("Network error");
                    }
                });
    }

    public static void loginWithSocial(final Context ctx, String email, String username, String photo_url, final APICallback<User> callback) {
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String oneSignalId = status.getSubscriptionStatus().getUserId();
        if (oneSignalId == null)
            oneSignalId = "1234567890";
        AndroidNetworking.post(loginWithSocial)
                .addBodyParameter("email", email)
                .addBodyParameter("username", username)
                .addBodyParameter("photo_url", photo_url)
                .addBodyParameter("push_token", oneSignalId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                String token = response.getString("token");
                                putAccessToken(ctx, token);
                                JSONObject json_user = response.getJSONObject("user");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                callback.onSuccess(user);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure("Network error");
                    }
                });
    }

    public static void resetPassword(String email, String password, final APICallback<Boolean> callback) {
        AndroidNetworking.post(resetPassword)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure("Something went wrong");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure("Network error");
                    }
                });
    }

    public static void changePassword(final Context ctx, String password, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(changePassword)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure("Something went wrong");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void forgotPassword(String email, final APICallback<Boolean> callback) {
        AndroidNetworking.post(forgotPassword)
                .addBodyParameter("email", email)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(null);
                            } else {
                                callback.onFailure("Something went wrong");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure("Network error");
                    }
                });
    }

    public static void signupWithEmailAndPassword(final Context ctx, String email, String password, final APICallback<User> callback) {
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String oneSignalId = status.getSubscriptionStatus().getUserId();
        if (oneSignalId == null)
            oneSignalId = "1234567890";
        AndroidNetworking.post(signupWithEmailAndPassword)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .addBodyParameter("push_token", oneSignalId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                String token = response.getString("token");
                                putAccessToken(ctx, token);
                                JSONObject json_user = response.getJSONObject("user");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                callback.onSuccess(user);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure("Network error");
                    }
                });
    }

    private static void putAccessToken(Context ctx, String sToken){
        SaveSharedPreference.putString(ctx, SaveSharedPreference.KEY_USER_TOKEN, sToken);
    }

    private static String getAccessToken(Context ctx){
        return SaveSharedPreference.getString(ctx, SaveSharedPreference.KEY_USER_TOKEN);
    }

    public static void getUserByEmail(String email, final APICallback<User> callback) {
        AndroidNetworking.post(getUserByEmail)
                .addBodyParameter("email", email)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                JSONObject json_user = response.getJSONObject("user");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                callback.onSuccess(user);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure("Network error");
                    }
                });
    }

    public static void getUsersByFilter(int sport,
                                        int gender,
                                        int type,
                                        String name,
                                        String city,
                                        String country,
                                        int age,
                                        int height,
                                        int weight,
                                        final APICallback<List<User>> callback) {
        AndroidNetworking.post(filterUser)
                .addBodyParameter("sport_id", String.valueOf(sport))
                .addBodyParameter("gender_id", String.valueOf(gender))
                .addBodyParameter("group_id", String.valueOf(type))
                .addBodyParameter("name", name)
                .addBodyParameter("city", city)
                .addBodyParameter("country", country)
                .addBodyParameter("age", String.valueOf(age))
                .addBodyParameter("height", String.valueOf(height))
                .addBodyParameter("weight", String.valueOf(weight))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<User> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray users = response.getJSONArray("users");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_user = (JSONObject)users.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                result.add(0, user);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getUsersByType(final Context ctx, int type, final APICallback<List<User>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getUsersByType)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("group_id", String.valueOf(type))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<User> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray users = response.getJSONArray("users");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_user = (JSONObject)users.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                result.add(0, user);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getTaggedUsers(final Context ctx, String tagged, final APICallback<List<User>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getTaggedUsers)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("tagged", tagged)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<User> result = new ArrayList<>();
                            JSONArray users = response.getJSONArray("users");
                            for (int i = 0; i < users.length(); i++) {
                                JSONObject json_user = (JSONObject)users.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                result.add(0, user);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getAllUsers(final Context ctx, final APICallback<List<User>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getAllUsers)
                .addHeaders("Authorization", "Bearer " + sToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<User> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray users = response.getJSONArray("users");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_user = (JSONObject)users.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                result.add(0, user);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void searchUsers(final Context ctx, String keyword, final APICallback<List<User>> callback) {
        AndroidNetworking.post(searchUsers)
                .addBodyParameter("name", keyword)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<User> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray users = response.getJSONArray("users");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_user = (JSONObject)users.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                if (user.id != AppData.user.id)
                                    result.add(0, user);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void updateUser(final Context ctx, User user, final APICallback<User> callback) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(user);
        String sToken = getAccessToken(ctx);
        try {
            JSONObject json_user = new JSONObject(json);
            AndroidNetworking.post(updateUser)
                    .addHeaders("Authorization", "Bearer " + sToken)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(json_user)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_user = response.getJSONObject("user");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    User user = gson.fromJson(json_user.toString(), User.class);
                                    callback.onSuccess(user);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void updateUserFields(final Context ctx, String fieldname, String fieldvalue, final APICallback<User> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(updateUserField)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("key", fieldname)
                .addBodyParameter("value", fieldvalue)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                JSONObject json_user = response.getJSONObject("user");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                callback.onSuccess(user);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void searchVideoFeeds(final Context ctx, String keyword, final APICallback<List<Feed>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(searchVideoFeeds)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("name", keyword.toLowerCase())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Feed> result = new ArrayList<>();
                        try {
                            int count = response.getInt("count");
                            JSONArray feeds = response.getJSONArray("feeds");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_feed = (JSONObject)feeds.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Feed feed = gson.fromJson(json_feed.toString(), Feed.class);
                                result.add(feed);
                            }
                        } catch (Exception e) {
                            //callback.onFailure("Json parse error");
                        }
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getAllFeeds(int page, String u_id ,final APICallback<List<Feed>> callback) {
        AndroidNetworking.post(getAllFeeds)
                .addBodyParameter("page", String.valueOf(page))
                .addBodyParameter("u_id", u_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Feed> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray feeds = response.getJSONArray("feeds");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_feed = (JSONObject)feeds.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Feed feed = gson.fromJson(json_feed.toString(), Feed.class);
                                result.add(feed);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getSportsFeeds(final Context ctx, int page, String sport, String user_id, String u_id, final APICallback<List<Feed>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getSportsFeeds)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("page", String.valueOf(page))
                .addBodyParameter("sport_id", sport)
                //.addBodyParameter("user_id", user_id)
                //.addBodyParameter("u_id", u_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Feed> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONObject dataobj = response.getJSONObject("feeds");

                            JSONArray feeds = dataobj.getJSONArray("data");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_feed = (JSONObject)feeds.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Feed feed = gson.fromJson(json_feed.toString(), Feed.class);
                                result.add(feed);
                            }
                            callback.onSuccess(result);
                        } catch (JSONException e) {
                            callback.onFailure(e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    private static void updateLastStory(Story st){
        if(st.user != null){
            Story ls_story = AppData.last_stories.get(st.user.sport_id);
            if(ls_story != null){
                if (ls_story.timebeg < st.timebeg){
                    AppData.last_stories.put(st.user.sport_id, st);
                }
            } else {
                AppData.last_stories.put(st.user.sport_id, st);
            }
        }

    }

    public static void getMyFeeds(final Context ctx, int page, String sport, String user_id, String u_id, final APICallback<List<Feed>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getMyFeeds)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("page", String.valueOf(page))
                .addBodyParameter("sport_id", sport)
                //.addBodyParameter("user_id", user_id)
                //.addBodyParameter("u_id", u_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Feed> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONObject dataobj = response.getJSONObject("feeds");
                            JSONArray feeds = dataobj.getJSONArray("data");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_feed = (JSONObject)feeds.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Feed feed = gson.fromJson(json_feed.toString(), Feed.class);
                                result.add(feed);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getAllFeedsByUserid(final Context ctx, int page, String user_id, String u_id,  final APICallback<List<Feed>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getAllFeedsByUserid)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("page", String.valueOf(page))
                //.addBodyParameter("u_id", u_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Feed> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONObject dataobj = response.getJSONObject("feeds");
                            JSONArray feeds = dataobj.getJSONArray("data");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_feed = (JSONObject)feeds.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Feed feed = gson.fromJson(json_feed.toString(), Feed.class);
                                result.add(feed);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void saveFeed(final Context ctx, Feed feed, final APICallback<Feed> callback) {
        String sToken = getAccessToken(ctx);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(feed);
        try {
            JSONObject json_feed = new JSONObject(json);
            AndroidNetworking.post(saveFeed)
                    .addHeaders("Authorization", "Bearer " + sToken)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(json_feed)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_feed = response.getJSONObject("feed");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Feed feed = gson.fromJson(json_feed.toString(), Feed.class);
                                    callback.onSuccess(feed);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void addViewFeed(final Context ctx, Feed feed, final APICallback<Feed> callback) {
        String sToken = getAccessToken(ctx);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(feed);
        try {
            JSONObject json_feed = new JSONObject(json);
            AndroidNetworking.post(addViewFeed)
                    .addHeaders("Authorization", "Bearer " + sToken)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(json_feed)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_feed = response.getJSONObject("feed");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Feed feed = gson.fromJson(json_feed.toString(), Feed.class);
                                    callback.onSuccess(feed);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void recommendUser(final Context ctx, String uid, final APICallback<Integer> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(recommendUser)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", uid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                int recommends = response.getInt("count");
                                callback.onSuccess(recommends);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void deleteFeed(final Context ctx, Feed feed, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(deleteFeed)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("feed_id", feed.id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void privateFeed(final Context ctx, String feed_id, boolean flag, final APICallback<Boolean> callback) {
        int val = flag?Constants.FEED_PRIVATE:Constants.FEED_PUBLIC;
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(privateFeed)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("feed_id", feed_id)
                .addBodyParameter("val", String.valueOf(val))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void reportUser(final Context ctx, String uid, String content, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(reportUser)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", uid)
                .addBodyParameter("report", content)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void reportFeed(final Context ctx, Feed feed, String content, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(reportFeed)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("feed_id", feed.id)
                .addBodyParameter("report", content)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void uploadFile(String name, File video, File thumbnail, final APICallback<Pair<String, String>> callback) {
        AndroidNetworking.upload(uploadFile)
                .addMultipartFile("video", video)
                .addMultipartFile("thumbnail", thumbnail)
                .addMultipartParameter("name", name)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(Pair.create(response.getString("video_url"),
                                                            response.getString("thumbnail_url")));
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure(e.toString());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        callback.onFailure(error.toString());
                    }
                });
    }

    public static void uploadArticles(String name, String filepaths, final APICallback<String> callback) {
        String[] filenames = filepaths.split(",");
        ANRequest.MultiPartBuilder multiPartBuilder = AndroidNetworking.upload(uploadArticles)
                .addMultipartParameter("name", name);
        for (int i = 0; i < filenames.length; i ++){
            String sParam = "image" + i;
            File file = new File(filenames[i]);
            multiPartBuilder.addMultipartFile(sParam, file);
        }
        multiPartBuilder.setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(response.getString("paths"));
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure(e.toString());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        callback.onFailure(error.toString());
                    }
                });
    }

    public static void uploadImage(String name, File image, final APICallback<String> callback) {
        AndroidNetworking.upload(uploadImage)
                .addMultipartFile("image", image)
                .addMultipartParameter("name", name)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(response.getString("image_url"));
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure(e.toString());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        callback.onFailure(error.toString());
                    }
                });
    }

    public static void uploadResume(final Context ctx, String name, File pdf_file, final APICallback<String> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.upload(uploadResume)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addMultipartFile("pdf", pdf_file)
                .addMultipartParameter("name", name)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(response.getString("resume_url"));
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure(e.toString());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        callback.onFailure(error.toString());
                    }
                });
    }

    public static void addLikeFeed(final Context ctx, Feed feed, User user, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(addLikeFeed)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("feed_id", feed.id)
                //.addBodyParameter("user_id", user.id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(response.getBoolean("result"));
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }


    public static void getYearResultSummary(final Context ctx, User user, int year, final APICallback<List<List<Pair<String, Integer>>>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getYearResultSummary)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user.id)
                .addBodyParameter("sport_id", String.valueOf(user.sport_id))
                .addBodyParameter("year", String.valueOf(year))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<List<Pair<String, Integer>>> result = new ArrayList<>();
                            if (response.getBoolean("status")) {
                                JSONArray summary = response.getJSONArray("result");
                                for (int i = 0; i < summary.length(); i++) {
                                    List<Pair<String, Integer>> one = new ArrayList<>();
                                    Object obj = summary.get(i);
                                    if (obj instanceof JSONObject){
                                        JSONObject data = (JSONObject)obj;
                                        Iterator<String> iterator = data.keys();
                                        while (iterator.hasNext()) {
                                            String key = iterator.next();
                                            try {
                                                Integer value = Integer.valueOf(data.get(key).toString());
                                                one.add(Pair.create(key, value));
                                            } catch (JSONException e) {
                                            }
                                        }
                                    }
                                    result.add(one);
                                }
                                while (result.size() < 12) result.add(new ArrayList<Pair<String, Integer>>());
                                callback.onSuccess(result);
                            } else {
                                callback.onSuccess(result);
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getSportResultSummary(final Context ctx, User user, final APICallback<List<Pair<String, Integer>>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getSportResultSummary)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user.id)
                .addBodyParameter("sport_id", String.valueOf(user.sport_id))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                JSONObject summary = response.getJSONObject("result");
                                List<Pair<String, Integer>> result = new ArrayList<>();
                                Iterator<String> iterator = summary.keys();
                                while (iterator.hasNext()) {
                                    String key = iterator.next();
                                    try {
                                        Integer value = Integer.valueOf(summary.get(key).toString());
                                        result.add(Pair.create(key, value));
                                    } catch (JSONException e) {
                                    }
                                }
                                callback.onSuccess(result);
                            } else {
                                callback.onSuccess(new ArrayList<Pair<String, Integer>>());
                            }
                        } catch (Exception e) {
                            callback.onSuccess(new ArrayList<Pair<String, Integer>>());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getUserResultSummary(final Context ctx, User user, final APICallback<List<Summary>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getUserResultSummary)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user.id)
                .addBodyParameter("club", user.club)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                List<Summary> result = new ArrayList<>();
                                JSONArray obj_array = response.getJSONArray("result");
                                for (int i = 0; i < obj_array.length(); i++)
                                {
                                    Summary summary = new Summary();
                                    JSONObject obj = obj_array.getJSONObject(i);
                                    Iterator<String> iterator = obj.keys();
                                    while (iterator.hasNext()) {
                                        String club = iterator.next();
                                        try {
                                            JSONObject data = (JSONObject)obj.get(club);
                                            JSONObject stats = data.getJSONObject("data");
                                            List<Pair<String, Integer>> one = new ArrayList<>();
                                            Iterator<String> iterator1 = stats.keys();
                                            while (iterator1.hasNext()) {
                                                String key = iterator1.next();
                                                try {
                                                    Integer value = Integer.valueOf(stats.get(key).toString());
                                                    summary.stats.add(Pair.create(key, value));
                                                } catch (JSONException e) { }
                                            }
                                            JSONArray year_array = data.getJSONArray("years");
                                            for (int j = 0; j < year_array.length(); j++){
                                                JSONObject year_obj = year_array.getJSONObject(j);
                                                iterator = year_obj.keys();
                                                while (iterator.hasNext()) {
                                                    String year_name = iterator.next();
                                                    try {
                                                        JSONArray sss = (JSONArray) year_obj.get(year_name);
                                                        List<String> months = new ArrayList<>();
                                                        for (int k = 0; k < sss.length(); k++){
                                                            months.add(sss.getString(k));
                                                        }
                                                        summary.years.add(new Pair<>(year_name, months));
                                                    } catch (JSONException e) { }
                                                }
                                            }
                                            summary.club_name = club;
                                        } catch (JSONException e) { }
                                    }
                                    result.add(summary);
                                }
                                callback.onSuccess(result);
                            } else {
                                callback.onSuccess(new ArrayList<>());

                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getClubYearSummary(final Context ctx, User user, final APICallback<List<Summary>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getClubYearSummary)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user.id)
//                .addBodyParameter("club", user.club)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                List<Summary> result = new ArrayList<>();
                                JSONArray obj_array = response.getJSONArray("result");
                                for (int i = 0; i < obj_array.length(); i++)
                                {
                                    Summary summary = new Summary();
                                    JSONObject obj = obj_array.getJSONObject(i);
                                    Iterator<String> iterator = obj.keys();
                                    while (iterator.hasNext()) {
                                        String club = iterator.next();
                                        try {
                                            JSONObject data = (JSONObject)obj.get(club);
                                            JSONObject stats = data.getJSONObject("data");
                                            List<Pair<String, Integer>> one = new ArrayList<>();
                                            Iterator<String> iterator1 = stats.keys();
                                            while (iterator1.hasNext()) {
                                                String key = iterator1.next();
                                                try {
                                                    Integer value = Integer.valueOf(stats.get(key).toString());
                                                    summary.stats.add(Pair.create(key, value));
                                                } catch (JSONException e) { }
                                            }
                                            JSONArray year_array = data.getJSONArray("years");
                                            for (int j = 0; j < year_array.length(); j++){
                                                JSONObject year_obj = year_array.getJSONObject(j);
                                                iterator = year_obj.keys();
                                                while (iterator.hasNext()) {
                                                    String year_name = iterator.next();
                                                    try {
                                                        JSONArray sss = (JSONArray) year_obj.get(year_name);
                                                        List<String> months = new ArrayList<>();
                                                        for (int k = 0; k < sss.length(); k++){
                                                            months.add(sss.getString(k));
                                                        }
                                                        summary.years.add(new Pair<>(year_name, months));
                                                    } catch (JSONException e) { }
                                                }
                                            }
                                            summary.club_name = club;
                                        } catch (JSONException e) { }
                                    }
                                    result.add(summary);
                                }
                                callback.onSuccess(result);
                            } else {
                                callback.onSuccess(new ArrayList<>());

                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getClubMonthResultSummary(final Context ctx, User user, String club, int year, int month, final APICallback<List<Pair<String, Integer>>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getClubMonthResultSummary)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user.id)
                .addBodyParameter("sport_id", String.valueOf(user.sport_id))
                .addBodyParameter("club", club)
                .addBodyParameter("year", String.valueOf(year))
                .addBodyParameter("month", String.valueOf(month))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                List<Pair<String, Integer>> result = new ArrayList<>();
                                Object obj = response.get("result");
                                if(obj instanceof JSONObject){
                                    JSONObject summary = (JSONObject) obj;
                                    Iterator<String> iterator = summary.keys();
                                    while (iterator.hasNext()) {
                                        String key = iterator.next();
                                        try {
                                            Integer value = Integer.valueOf(summary.get(key).toString());
                                            result.add(Pair.create(key, value));
                                        } catch (JSONException e) { }
                                    }
                                }
                                callback.onSuccess(result);
                            } else {
                                callback.onSuccess(new ArrayList<Pair<String, Integer>>());
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getClubSummary(final Context ctx, User user, final APICallback<List<Pair<String, List<List<Pair<String, Integer>>>>>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getClubSummary)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user.id)
//                .addBodyParameter("club", user.club)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                List<Pair<String, List<List<Pair<String, Integer>>>>> result = new ArrayList<>();
                                Object obj = response.get("result");
                                if(obj instanceof JSONObject){
                                    JSONObject summary = (JSONObject)obj;
                                    Iterator<String> iterator = summary.keys();
                                    while (iterator.hasNext()) {
                                        String club = iterator.next();
                                        try {
                                            List<List<Pair<String, Integer>>> next = new ArrayList<>();
                                            JSONArray array = (JSONArray)summary.get(club);
                                            for(int i = 0 ; i < array.length(); i++){
                                                List<Pair<String, Integer>> one = new ArrayList<>();
                                                Object month_obj = array.get(i);
                                                if(month_obj instanceof JSONObject){
                                                    JSONObject stats = (JSONObject)month_obj;
                                                    Iterator<String> iterator1 = stats.keys();
                                                    while (iterator1.hasNext()) {
                                                        String key = iterator1.next();
                                                        try {
                                                            Integer value = Integer.valueOf(stats.get(key).toString());
                                                            one.add(Pair.create(key, value));
                                                        } catch (JSONException e) { }
                                                    }
                                                }
                                                next.add(one);
                                            }
                                            result.add(Pair.create(club, next));
                                        } catch (JSONException e) { }
                                    }
                                }
                                callback.onSuccess(result);
                            } else {
                                callback.onSuccess(new ArrayList<Pair<String, List<List<Pair<String, Integer>>>>>());

                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getSummaryByClub(final Context ctx, String uid, final APICallback<List<ClubStat>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getSummaryByClub)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", uid)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                List<ClubStat> result = new ArrayList<>();
                                JSONArray data_array = response.getJSONArray("result");
                                for (int i = 0; i< data_array.length(); i ++){
                                    ClubStat oneClub = new ClubStat();
                                    JSONObject obj = data_array.getJSONObject(i);
                                    oneClub.name = obj.getString("name");
                                    JSONArray summary = obj.getJSONArray("data");
                                    List<List<Pair<String, Integer>>> stats = new ArrayList<>();
                                    for (int j = 0; j < summary.length(); j++) {
                                        List<Pair<String, Integer>> one = new ArrayList<>();
                                        Object mobj = summary.get(j);
                                        if (mobj instanceof JSONObject){
                                            JSONObject data = (JSONObject)mobj;
                                            Iterator<String> iterator = data.keys();
                                            while (iterator.hasNext()) {
                                                String key = iterator.next();
                                                try {
                                                    Integer value = Integer.valueOf(data.get(key).toString());
                                                    one.add(Pair.create(key, value));
                                                } catch (JSONException e) {
                                                }
                                            }
                                        }
                                        stats.add(one);
                                    }
                                    oneClub.stats = stats;

                                    List<Pair<String, List<Pair<String, Integer>>>> years_stat = new ArrayList<>();
                                    JSONArray years_array = obj.getJSONArray("years");
                                    for (int k = 0; k < years_array.length(); k++){
                                        Object mobj = years_array.get(k);
                                        if (mobj instanceof JSONObject){
                                            JSONObject yr_data = (JSONObject)mobj;
                                            String yr_name = yr_data.getString("name");
                                            List<Pair<String, Integer>> yr_value = new ArrayList<>();
                                            Object vobj = yr_data.get("value");
                                            if(vobj instanceof  JSONObject){
                                                JSONObject vdata = (JSONObject)vobj;
                                                Iterator<String> iterator = vdata.keys();
                                                while (iterator.hasNext()) {
                                                    String key = iterator.next();
                                                    try {
                                                        Integer value = Integer.valueOf(vdata.get(key).toString());
                                                        yr_value.add(Pair.create(key, value));
                                                    } catch (JSONException e) {
                                                    }
                                                }
                                                years_stat.add(Pair.create(yr_name, yr_value));
                                            }
                                        }
                                    }
                                    oneClub.years_stat = years_stat;
                                    result.add(oneClub);
                                }
                                callback.onSuccess(result);
                            } else {
                                callback.onSuccess(new ArrayList<>());

                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void saveResult(final Context ctx, Result result, final APICallback<Result> callback) {
        String sToken = getAccessToken(ctx);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(result);
        try {
            JSONObject json_result = new JSONObject(json);
            AndroidNetworking.post(saveResult)
                    .addHeaders("Authorization", "Bearer " + sToken)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(json_result)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_result = response.getJSONObject("result");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Result result = gson.fromJson(json_result.toString(), Result.class);
                                    callback.onSuccess(result);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void deleteUserClubSummary(final Context ctx, String user_id, String club, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(deleteUserClubSummary)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("club", club)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void deleteClubYearSummary(final Context ctx, User user, String year, final APICallback<Boolean> callback) {
        String[] str_param = year.split("-");
        if (str_param.length == 1) return;
        String club_str = year.substring(str_param[0].length()+1);
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(deleteClubYearSummary)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user.id)
                .addBodyParameter("club", club_str)
                .addBodyParameter("year", str_param[0])
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void adjustStatValue(final Context ctx, Result result, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(result);
        try {
            JSONObject json_result = new JSONObject(json);
            AndroidNetworking.post(adjustStatValue)
                    .addHeaders("Authorization", "Bearer " + sToken)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(json_result)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    callback.onSuccess(true);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void getAllPrizesByUserid(final Context ctx, String user_id, final APICallback<List<Prize>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getAllPrizesByUserid)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Prize> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray careers = response.getJSONArray("prizes");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_prize = (JSONObject)careers.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Prize prize = gson.fromJson(json_prize.toString(), Prize.class);
                                result.add(0, prize);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void savePrize(final Context ctx, Prize prize, final APICallback<Prize> callback) {
        String sToken = getAccessToken(ctx);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(prize);
        try {
            JSONObject json_prize = new JSONObject(json);
            AndroidNetworking.post(savePrize)
                    .addHeaders("Authorization", "Bearer " + sToken)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(json_prize)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_career = response.getJSONObject("prize");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Prize prize = gson.fromJson(json_career.toString(), Prize.class);
                                    callback.onSuccess(prize);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void deletePrize(final Context ctx, Prize prize, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(deletePrize)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("prize_id", prize.id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void updatePrize(Prize prize, final APICallback<Prize> callback) {
        //String sToken = getAccessToken(ctx);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(prize);
        try {
            JSONObject json_prize = new JSONObject(json);
            AndroidNetworking.post(updatePrize)
                    .addJSONObjectBody(json_prize)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_career = response.getJSONObject("prize");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Prize prize = gson.fromJson(json_career.toString(), Prize.class);
                                    callback.onSuccess(prize);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void getAllCareersByUserid(final Context ctx, String userid, final APICallback<List<Career>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getAllCareersByUserid)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", userid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Career> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray careers = response.getJSONArray("careers");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_career = (JSONObject)careers.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Career career = gson.fromJson(json_career.toString(), Career.class);
                                result.add(0, career);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void saveCareer(final Context ctx, Career career, final APICallback<Career> callback) {
        String sToken = getAccessToken(ctx);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(career);
        try {
            JSONObject json_career = new JSONObject(json);
            AndroidNetworking.post(saveCareer)
                    .addHeaders("Authorization", "Bearer " + sToken)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(json_career)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_career = response.getJSONObject("career");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Career career = gson.fromJson(json_career.toString(), Career.class);
                                    callback.onSuccess(career);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void updateCareer(Career career, final APICallback<Career> callback) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(career);
        try {
            JSONObject json_career = new JSONObject(json);
            AndroidNetworking.post(updateCareer)
                    .addJSONObjectBody(json_career)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_career = response.getJSONObject("career");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Career career = gson.fromJson(json_career.toString(), Career.class);
                                    callback.onSuccess(career);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void deleteCareer(final Context ctx,String career_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(deleteCareer)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("career_id", career_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure("Unknown Error");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getAllDescriptionsByUserid(final Context ctx, String userid, final APICallback<List<Description>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getAllDescriptionsByUserid)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", userid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Description> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray descriptions = response.getJSONArray("descriptions");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_description = (JSONObject)descriptions.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Description description = gson.fromJson(json_description.toString(), Description.class);
                                result.add(0, description);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void updateDescription(final Context ctx, Description description, final APICallback<Description> callback) {
        String sToken = getAccessToken(ctx);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(description);
        try {
            JSONObject json_career = new JSONObject(json);
            AndroidNetworking.post(updateDescription)
                    .addHeaders("Authorization", "Bearer " + sToken)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(json_career)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_description = response.getJSONObject("description");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Description description = gson.fromJson(json_description.toString(), Description.class);
                                    callback.onSuccess(description);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void getAllAdmobs(final Context ctx, final APICallback<List<Admob>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getAllAdmobs)
                .addHeaders("Authorization", "Bearer " + sToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Admob> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray admobs = response.getJSONArray("admobs");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_admob = (JSONObject)admobs.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Admob admob = gson.fromJson(json_admob.toString(), Admob.class);
                                result.add(0, admob);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void filterAdmobs(final Context ctx, int sport, String city, String country, final APICallback<List<Admob>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(filterAdmobs)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("sport_id", String.valueOf(sport))
                .addBodyParameter("city", city)
                .addBodyParameter("country", country)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Admob> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray admobs = response.getJSONArray("admobs");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_admob = (JSONObject)admobs.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Admob admob = gson.fromJson(json_admob.toString(), Admob.class);
                                result.add(0, admob);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void saveAdmob(final Context ctx, Admob admob, final APICallback<Admob> callback) {
        String sToken = getAccessToken(ctx);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(admob);
        try {
            JSONObject json_feed = new JSONObject(json);
            AndroidNetworking.post(saveAdmob)
                    .addHeaders("Authorization", "Bearer " + sToken)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(json_feed)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_admob = response.getJSONObject("admob");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Admob admob = gson.fromJson(json_admob.toString(), Admob.class);
                                    callback.onSuccess(admob);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void deleteAdvert(final Context ctx, String ad_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(deleteAdmob)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("admob_id", ad_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else{
                                callback.onSuccess(false);
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getFollower(final Context ctx, final APICallback<List<User>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getFollower)
                .addHeaders("Authorization", "Bearer " + sToken)
                //.addBodyParameter("user_id", user_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<User> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray users = response.getJSONArray("followers");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_user = (JSONObject)users.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                result.add(0, user);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getFollowing(final Context ctx, final APICallback<List<User>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getFollowing)
                .addHeaders("Authorization", "Bearer " + sToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<User> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray users = response.getJSONArray("followings");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_user = (JSONObject)users.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                result.add(0, user);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void checkFollow(final Context ctx, String user_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(checkFollow)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user_id)
                //.addBodyParameter("follower_id", follower_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                if (response.getBoolean("result")) callback.onSuccess(true);
                                else callback.onSuccess(false);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void deleteFollow(final Context ctx, String user_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(deleteFollow)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user_id)
                //.addBodyParameter("follower_id", follower_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void addFollow(final Context ctx, String user_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(addFollow)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user_id)
                //.addBodyParameter("follower_id", follower_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getBlocks(final Context ctx, final APICallback<List<User>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getBlocks)
                .addHeaders("Authorization", "Bearer " + sToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<User> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray users = response.getJSONArray("blocks");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_user = (JSONObject)users.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                result.add(0, user);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void addBlock(final Context ctx, String blocked_user_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(addBlock)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("blocked_user_id", blocked_user_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void deleteBlock(final Context ctx, String blocked_user_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(deleteBlock)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("blocked_user_id", blocked_user_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure("Unknown Error");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void saveBookmark(final Context ctx, String feed_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(saveBookmark)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("feed_id", feed_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onSuccess(false);
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getBookmarks(Context ctx, final APICallback<List<Feed>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getBookmarks)
                .addHeaders("Authorization", "Bearer " + sToken)
//                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Feed> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray bookmarks = response.getJSONArray("bookmarks");
                            for (int i = 0; i < count; i++) {
                                JSONObject json = (JSONObject)bookmarks.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Feed feed = gson.fromJson(json.toString(), Feed.class);
                                result.add(feed);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getAllResumes(final Context ctx, String uid, final APICallback<List<Resume>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getAllResumes)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", uid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Resume> result = new ArrayList<>();
                            int count = response.getInt("count");
                            JSONArray resumes = response.getJSONArray("resumes");
                            for (int i = 0; i < count; i++) {
                                JSONObject json_user = (JSONObject)resumes.get(i);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Resume resume= gson.fromJson(json_user.toString(), Resume.class);
                                result.add(0, resume);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void deleteResume(final Context ctx, int rid, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(deleteResume)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("id", String.valueOf(rid))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure("Unknown Error");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }
    public static void getStrengths(final Context ctx, String uid, final APICallback<List<String>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getStrengths)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("uid", uid)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                List<String> strengths = new ArrayList<>();
                                String data_str = response.getString("strength");
                                if (data_str != null && data_str.length() > 0){
                                    String[] str = data_str.split(",");
                                    if(str.length > 0){
                                        strengths.add(str[0]);
                                        if(str.length > 1){
                                            strengths.add(str[1]);
                                            if(str.length > 2){
                                                strengths.add(str[2]);
                                            }
                                        }
                                    }
                                }
                                callback.onSuccess(strengths);
                            } else {
                                callback.onFailure("Unknown Error");
                            }
                        } catch (JSONException e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }
    public static void saveStrengths(final Context ctx, String data, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(saveStrengths)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("strength", data)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure("Unknown Error");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void joinTeam(final Context ctx, String team_id, String user_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(joinTeam)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("team_id", team_id)
                .addBodyParameter("user_id", user_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure("Failed");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void leaveTeam(final Context ctx, String team_id, String user_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(leaveTeam)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("team_id", team_id)
                .addBodyParameter("user_id", user_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure("failed");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getTeamMembers(final Context ctx, String team_id, final APICallback<List<User>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getTeamMembers)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("team_id", team_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<User> result = new ArrayList<>();
                            JSONArray users = response.getJSONArray("team");
                            for (int i = 0; i < users.length(); i++) {
                                JSONObject json_data = (JSONObject)users.get(i);
                                JSONObject json_user = json_data.getJSONObject("user");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                User user = gson.fromJson(json_user.toString(), User.class);
                                result.add(0, user);
                            }
                            callback.onSuccess(result);
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getAllNotifications(final Context ctx, final APICallback<List<Notification>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getAllNotis)
                .addHeaders("Authorization", "Bearer " + sToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                List<Notification> result = new ArrayList<>();
                                JSONArray notis = response.getJSONArray("data");
                                for (int i = 0; i < notis.length(); i++) {
                                    JSONObject json_data = (JSONObject)notis.get(i);
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Notification one = gson.fromJson(json_data.toString(), Notification.class);
                                    result.add(0, one);
                                }
                                callback.onSuccess(result);
                            } else {
                                callback.onFailure("failed");
                            }
                        } catch (JSONException e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void removeNotification(final Context ctx, String noti_id, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(removeNoti)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("id", noti_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure("failed");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void pushChatNotification(final Context ctx, String user_id, String msg,final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(chatNotification)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("msg", msg)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure("failed");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void uploadStory(String name, File image, final APICallback<String> callback) {
        AndroidNetworking.upload(uploadStory)
                .addMultipartFile("image", image)
                .addMultipartParameter("name", name)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(response.getString("image_url"));
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure(e.toString());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        callback.onFailure(error.toString());
                    }
                });
    }

    public static void saveStory(final Context ctx, Story story, final APICallback<Story> callback) {
        String sToken = getAccessToken(ctx);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(story);
        try {
            JSONObject json_feed = new JSONObject(json);
            AndroidNetworking.post(saveStory)
                    .addHeaders("Authorization", "Bearer " + sToken)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(json_feed)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    JSONObject json_feed = response.getJSONObject("story");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Story story = gson.fromJson(json_feed.toString(), Story.class);
                                    updateLastStory(story);
                                    callback.onSuccess(story);
                                } else {
                                    callback.onFailure(response.getString("error"));
                                }
                            } catch (Exception e) {
                                callback.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.onFailure(anError.getErrorBody());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Json parse error");
        }
    }

    public static void getAllStory(final Context ctx, final APICallback<Boolean> callback) {
        AppData.last_stories.clear();
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getAllStory)
                .addHeaders("Authorization", "Bearer " + sToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Story> result = new ArrayList<>();
                            if (response.getBoolean("status")) {
                                JSONArray resumes = response.getJSONArray("story");
                                for (int i = 0; i < resumes.length(); i++) {
                                    JSONObject json_user = (JSONObject)resumes.get(i);
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Story story= gson.fromJson(json_user.toString(), Story.class);
                                    updateLastStory(story);
                                }
                            }
                            callback.onSuccess(true);

                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getStoriesByUser(final Context ctx, String uid, final APICallback<List<Story>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getStoriesByUser)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", uid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                JSONArray stories = response.getJSONArray("story");
                                List<Story> result = new ArrayList<>();
                                for (int i = 0; i < stories.length(); i++){
                                    JSONObject story_obj = stories.getJSONObject(i);
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Story story= gson.fromJson(story_obj.toString(), Story.class);
//                                    updateLastStory(story);
                                    result.add(story);
                                }

                                callback.onSuccess(result);
                            } else{
                                callback.onSuccess(new ArrayList<>());
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void viewStory(final Context ctx, String sid, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(viewStory)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("story_id", sid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else{
                                callback.onSuccess(false);
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void replyStory(final Context ctx, String sid, int type, String content, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(replyStory)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("story_id", sid)
                .addBodyParameter("reply_type", String.valueOf(type))
                .addBodyParameter("content", content)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else{
                                callback.onSuccess(false);
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getStoryViews(final Context ctx, String sid, final APICallback<Pair<List<StoryView>, Integer>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getStoryViews)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("story_id", sid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<StoryView> result = new ArrayList<>();
                            Integer view_count = 0;
                            if (response.getBoolean("status")) {
                                view_count = response.getInt("view_count");
                                JSONArray resumes = response.getJSONArray("storyviews");
                                for (int i = 0; i < resumes.length(); i++) {
                                    JSONObject json_user = (JSONObject)resumes.get(i);
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    StoryView storyview= gson.fromJson(json_user.toString(), StoryView.class);
                                    result.add(0, storyview);
                                }
                            }
                            callback.onSuccess(Pair.create(result, view_count));

                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getStoryMsg(final Context ctx, String sid, String uid, final APICallback<List<StoryMsg>> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getStoryMsg)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("story_id", sid)
                .addBodyParameter("user_id", uid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<StoryMsg> result = new ArrayList<>();
                            if (response.getBoolean("status")) {
                                JSONArray resumes = response.getJSONArray("storymsg");
                                for (int i = 0; i < resumes.length(); i++) {
                                    JSONObject json_user = (JSONObject)resumes.get(i);
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    StoryMsg storyview= gson.fromJson(json_user.toString(), StoryMsg.class);
                                    result.add(0, storyview);
                                }
                            }
                            callback.onSuccess(result);

                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void deleteStory(final Context ctx, String sid, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(deleteStory)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("story_id", sid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else{
                                callback.onSuccess(false);
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void viewProfile(final Context ctx, String uid, final APICallback<Integer> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(viewProfile)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("user_id", uid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                int nCount = response.getInt("recommend");
                                callback.onSuccess(nCount);
                            } else{
                                callback.onSuccess(0);
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getStoryViewStatics(final Context ctx, long basetime, final APICallback<ViewStatic> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getStoryViewStatics)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("basetime", String.valueOf(basetime))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                JSONObject json_data = (JSONObject)response.getJSONObject("result");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                ViewStatic data= gson.fromJson(json_data.toString(), ViewStatic.class);
                                callback.onSuccess(data);
                            } else{
                                callback.onFailure("error");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void getAudienceStatics(final Context ctx, final APICallback<Audience> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(getAudience)
                .addHeaders("Authorization", "Bearer " + sToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                JSONObject json_data = (JSONObject)response.getJSONObject("result");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Audience data= gson.fromJson(json_data.toString(), Audience.class);
                                callback.onSuccess(data);
                            } else{
                                callback.onFailure("error");
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

    public static void reportStory(final Context ctx, String sid, String content, final APICallback<Boolean> callback) {
        String sToken = getAccessToken(ctx);
        AndroidNetworking.post(reportStory)
                .addHeaders("Authorization", "Bearer " + sToken)
                .addBodyParameter("story_id", sid)
                .addBodyParameter("report", content)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getString("error"));
                            }
                        } catch (Exception e) {
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getErrorBody());
                    }
                });
    }

}
