package com.odelan.yang.aggone.Utils;

public class Constants {
    public static final String DEVELOPER_KEY = "AIzaSyAhJSVnQ9zi5zr7lyxXP4WYfoj7SMrA67M";//AIzaSyC3XUPm32wVDRVo6t_2pTqUZv3vv2D2-iQ
    public static final String PLACE_API_KEY = "AIzaSyAhJSVnQ9zi5zr7lyxXP4WYfoj7SMrA67M";
    public static final String ONE_SIGNAL_REST_KEY = "NGFiOTI2ZDUtMjgzZC00Y2U1LWJmMjUtNmE3MWRlZTZmNWU5";

    public static int MAX_DESCRIPTION = 5;
    public static int MAX_SKILL = 5;

    public static final String LOGIN_ISLOGGEDIN = "login_isloggedin";
    public static final String LOGIN_EMAIL = "login_email";
    public static final String LOGIN_PASSWORD = "login_password";
    public static final String PUSH_TOKEN = "push_token";

    //signup mode
    public static final String SIGNUP_MODE        = "signup_mode";
    public static final int SIGNUP_EMAIL        = 0;
    public static final int SIGNUP_SOCIAL       = 1;

//    firebase
    public static final String FIREBASE_CHAT            = "Chat";
    public static final String FIREBASE_CONTACT         = "Contact";
    public static final String FIREBASE_NOTIFICATION    = "Notification";
    public static final String FIREBASE_TIMESTAMP       = "timestamp";
    public static final int FIREBASE_TEXT               = 0;
    public static final int FIREBASE_IMAGE              = 1;

//    chat
    public static final int CHAT_MY                     = 0;
    public static final int CHAT_OTHER                  = 1;

    //PickerImage mode
    public static final int PICK_MODE_NEWS              = 2;
    public static final int PICK_MODE_CAREER            = 1;
    public static final int PICK_MODE_STORY             = 0;

    //Story reply type
    public static final int REPLY_TYPE_TEXT             = 1;
    public static final int REPLY_TYPE_IMAGE            = 2;

    //Story show mode
    public static final int STORY_SHOW_SELF            = 0;
    public static final int STORY_SHOW_OTHER           = 1;

//    intent
    public static final String EMAIL                    = "email";
    public static final String PASSWORD                 = "password";
    public static final String USER                     = "user";
    public static final String FEED                     = "feed";
    public static final String VIDEO_URL                = "video_url";
    public static final String VIDEO_TITLE              = "video_title";
    public static final String THUMBNAIL_URL            = "thumbnail_url";
    public static final String YOUTUBE_ITEM             = "youtube_item";
    public static final String YOUTUBE_ID               = "youtube_id";

    public static final int NORMAL                      = 0;
    public static final int YOUTUBE                     = 1;
    public static final int NEWS                        = 2;

    public static final int WORLD                       = 0;
    public static final int MY                          = 1;

    //Gender
    public static final int GENDER_MAN                  = 0;
    public static final int GENDER_WOMAN                = 1;
    public static final int GENDER_OTHER                = 2;

    //    user type
    public static final int PLAYER                      = 1;
    public static final int COACH                       = 2;
    public static final int TEAM_CLUB                   = 3;
    public static final int AGENT                       = 4;
    public static final int STAFF                       = 5;
    public static final int COMPANY                     = 6;

    //MIN and MAX
    public static final int AGE_MIN                     = 10;
    public static final int AGE_MAX                     = 100;
    public static final int AGE_DEFAULT                 = 20;
    public static final int WEIGHT_MIN                  = 10;
    public static final int WEIGHT_MAX                  = 160;
    public static final int WEIGHT_DEFAULT              = 80;
    public static final int HEIGHT_MIN                  = 100;
    public static final int HEIGHT_MAX                  = 300;
    public static final int HEIGHT_DEFAULT              = 180;

    //Feed property
    public static final int FEED_PUBLIC                 = 0;
    public static final int FEED_PRIVATE                = 1;

    //Home type
    public static final int HOME_MAIN                   = 0;
    public static final int HOME_CHAT                   = 1;

    //Audience
    public static final int AUDIENCE_TODAY              = 0;
    public static final int AUDIENCE_WEEK               = 1;
    public static final int AUDIENCE_MONTH              = 2;

//  sports
    public static final int DefaultSport                = 500;
    public static final int Football                    = 1000;
    public static final int Basketball                  = 1001;
    public static final int American_Football           = 1002;
    public static final int Cricket                     = 1003;
    public static final int Tennis                      = 1004;
    public static final int Rugby                       = 1005;
    public static final int Golf                        = 1006;
    public static final int BaseBall                    = 1007;
    public static final int Ice_Hockey                  = 1008;
    public static final int Field_Hockey                = 1009;
    public static final int VolleyBall                  = 1010;
    public static final int Handball                    = 1011;
    public static final int Badminton                   = 1012;
    public static final int Squash                      = 1013;
    public static final int TableTennis                 = 1014;
    public static final int Boxing                      = 1015;
    public static final int MartialArt                  = 1016;
    public static final int MMA                         = 1017;
    public static final int KickBoxing                  = 1018;
    public static final int Wrestling                   = 1019;
    public static final int Gymnastic                   = 1020;
    public static final int Athletics                   = 1021;
    public static final int Fencing                     = 1022;
    public static final int Judo                        = 1023;
    public static final int Swimming                    = 1024;
    public static final int WaterSports                 = 1025;
    public static final int WaterPolo                   = 1026;
    public static final int AutoRacing                  = 1027;
    public static final int MotoRacing                  = 1028;
    public static final int Cycling                     = 1029;
    public static final int Equestrianism               = 1030;
    public static final int PoloSport                   = 1031;
    public static final int Dance                       = 1032;
    public static final int Softball                    = 1033;
    public static final int SepakTakraw                 = 1034;
    public static final int Korfball                    = 1035;
    public static final int Floorball                   = 1036;
    public static final int Climbing                    = 1037;
    public static final int Mountaineering              = 1038;
    public static final int Canyoning                   = 1039;
    public static final int Trail                       = 1040;
    public static final int Triathlon                   = 1041;
    public static final int Archery                     = 1042;
    public static final int Snowboarding                = 1043;
    public static final int Skiing                      = 1044;
    public static final int IceSkating                  = 1045;
    public static final int Petanque                    = 1046;
    public static final int SkateBoard                  = 1047;
    public static final int Weightlifting               = 1048;
    public static final int Crossfit_Fitness            = 1049;
    public static final int Bowling                     = 1050;
    public static final int Surf                        = 1051;
    public static final int Sailing                     = 1052;
    public static final int Canoeing                    = 1053;
    public static final int Rowing                      = 1054;
    public static final int Air_Sports                  = 1055;
    public static final int WinterSports                = 1056;

    public static final int STATS_COACH                = 1060;

    public static final String PERFORMANCE                       = "Performance";
    public static final String RANKING                           = "Ranking";
    public static final String DISCIPLINE                        = "discipline";

    public static final String STATS_GAMES_PLAYED          = "Games_played";
    public static final String STATS_RACES                 = "Races";
    public static final String STATS_VICTORIES             = "Victories";
    public static final String STATS_DEFEATS               = "Defeats";
    public static final String STATS_DRAWS                 = "Draws";
//    description
    public static final int DESCRIPTION_1                    = 0;
    public static final int DESCRIPTION_2                    = 1;
    public static final int DESCRIPTION_3                    = 2;
    public static final int DESCRIPTION_4                    = 3;
    public static final int DESCRIPTION_5                    = 4;
    public static final int DESCRIPTION_6                    = 5;
    public static final int DESCRIPTION_7                    = 6;
    public static final int DESCRIPTION_8                    = 7;
    public static final int DESCRIPTION_9                    = 8;
    public static final int DESCRIPTION_10                   = 9;
    public static final int DESCRIPTION_11                   = 10;
    public static final int DESCRIPTION_12                   = 11;
    public static final int DESCRIPTION_13                   = 12;
    public static final int DESCRIPTION_14                   = 13;
    public static final int DESCRIPTION_15                   = 14;
    public static final int DESCRIPTION_16                   = 15;
    public static final int DESCRIPTION_17                   = 16;
    public static final int DESCRIPTION_18                   = 17;
    public static final int DESCRIPTION_19                   = 18;
    public static final int DESCRIPTION_20                   = 19;
    public static final int DESCRIPTION_21                   = 20;
    public static final int DESCRIPTION_22                   = 21;
    public static final int DESCRIPTION_23                   = 22;
    public static final int DESCRIPTION_24                   = 23;

    public static final int CDESCRIPTION_1                    = 24;
    public static final int CDESCRIPTION_2                    = 25;
    public static final int CDESCRIPTION_3                    = 26;
    public static final int CDESCRIPTION_4                    = 27;
    public static final int CDESCRIPTION_5                    = 28;
    public static final int CDESCRIPTION_6                    = 29;
    public static final int CDESCRIPTION_7                    = 30;
    public static final int CDESCRIPTION_8                    = 31;
    public static final int CDESCRIPTION_9                    = 32;
    public static final int CDESCRIPTION_10                   = 33;
    public static final int CDESCRIPTION_11                   = 34;
    public static final int CDESCRIPTION_12                   = 35;
    public static final int CDESCRIPTION_13                   = 36;
    public static final int CDESCRIPTION_14                   = 37;
    public static final int CDESCRIPTION_15                   = 38;
    public static final int CDESCRIPTION_16                   = 39;
    public static final int CDESCRIPTION_17                   = 40;
    public static final int CDESCRIPTION_18                   = 41;
    public static final int CDESCRIPTION_19                   = 42;
    public static final int CDESCRIPTION_20                   = 43;
    public static final int CDESCRIPTION_21                   = 44;
    public static final int CDESCRIPTION_22                   = 45;
    public static final int CDESCRIPTION_23                   = 46;
    public static final int CDESCRIPTION_24                   = 47;


//    prize
    public static final int PRIZE1                      = 0;
    public static final int PRIZE2                      = 1;
    public static final int PRIZE3                      = 2;
    public static final int PRIZE4                      = 3;
    public static final int PRIZE5                      = 4;

//    skills
    public static final String SPEED                    = "Speed";
    public static final String POWER                    = "Power";
    public static final String STAMINA                  = "Stamina";
    public static final String MOTOR_SKILLS             = "Motor Skills";
    public static final String MASTERY                  = "Mastery";
    public static final String DRIBBLING                = "Dribbling";

    public static final String BALL_STRIKE              = "Ball Strike";
    public static final String BALL_CONTROL             = "Ball Control";
    public static final String KEEPING                  = "Keeping";
    public static final String ATTACK                   = "Attack";
    public static final String OFFENSE                  = "Offense";
    public static final String DEFENSE                  = "Defense";

    public static final String MOTIVATION               = "Motivation";
    public static final String RAGE                     = "Rage";
    public static final String REFLECTION               = "Reflection";
    public static final String SELF_CONFIDENCE          = "Self-Confidence";
    public static final String AGGRESSIVE               = "Aggressive";
    public static final String CALM                     = "Calm";

    public static final float wh_ratio = 1.413793103f;
}
