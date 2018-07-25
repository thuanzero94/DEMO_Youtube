package foxconn.vn.alan.demo_youtube.config;
/*
*   Alan 24/07/18
*/
public class WebServices {
    public static final String API_KEY = "AIzaSyCgmuvCgJ59IL-Cn0Ne8CQ6dRVDZPMHPbk";

    public static final String[] CATEGORY_ARRAY = {"Trending", "Films", "Music", "Sports", "Gaming", "Entertainment",
            "Education", "Science and Technology"};
    public static final String[] CATEGORY_ID_ARRAY = {"", "1", "10", "17", "20", "24", "27", "28"};
    public static final String[] MUSIC_CATEGORY_ARRAY = {"Top Tracks", "Latest Music Videos", "Pop Music", "Electronic Music"};
    public static final String[] MUSIC_CATEGORY_ID_ARRAY = {"PLFgquLnL59anfy66Thi2waVYeMxbfgHr1", "PLFgquLnL59anY3ZwTGcV_5ROFhyGF1U4l"
            , "PLDcnymzs18LWrKzHmzrGH1JzLBqrHi3xQ", "PLFPg_IUxqnZNnACUGsfn50DySIOVSkiKI"};
    public static final String[] GAMING_CATEGORY_ARRAY = {"Latest Videos", "Popular With Gamers"};
    public static final String[] GAMING_CATEGORY_ID_ARRAY = {"PLiCvVJzBupKmtncNx-0Nb2EHLYIe2khyn", "PLiCvVJzBupKluhBK8ldeevex__CTV4vHV"};
    public static final String[] SPORT_CATEGORY_ARRAY = {"Highlights", "Popular Videos", "Soccer", "Boxing"};
    public static final String[] SPORT_CATeGORY_ID_ARRAY = {"PL8fVUTBmJhHKEJjTNWn-ykf67rVrFWYtC", "PL8fVUTBmJhHJDHX7QniLt-omXc74OAOgS",
            "PL8fVUTBmJhHLdTL6m5BhavLCbslnCYncp", "PL8fVUTBmJhHI5EkNR56n731PMhCNC2Bi2"};

    public static final String SPORTS_WEBSERVICES_REQUEST_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=15&type=video";
    public static final String GAMING_WEBSERVICES_REQUEST_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=15&type=video";
    public static final String HOME_WEBSERVICES_REQUEST_URL = "https://www.googleapis.com/youtube/v3/videos?part=snippet&chart=mostPopular&maxResults=15&regionCode=VN&type=video";
    public static final String TRENDING_WEBSERVICES_REQUEST_URL = "https://www.googleapis.com/youtube/v3/videos?part=snippet&chart=mostPopular&maxResults=15&regionCode=VN&type=video";
    public static final String MUSIC_WEBSERVICE_REQUEST_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=15";
    public static final String SEARCH_WEBSERVICE_REQUEST_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&type=video";
    public static final String RELATED_VIDEO_REQUEST_URL="https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=50&type=video";
}