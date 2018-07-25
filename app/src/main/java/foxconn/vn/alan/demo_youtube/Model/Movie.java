package foxconn.vn.alan.demo_youtube.Model;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class Movie implements Serializable {
    private static final String TAG = Movie.class.getSimpleName();

    static final long serialVersionUID = 727566175075960653L;
    private long id;
    private String title;
    private String imageURL;
    private String videoURL;
    private HashMap<Integer,String> videoUrls;

    public HashMap<Integer, String> getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(HashMap<Integer, String> videoUrls) {
        this.videoUrls = videoUrls;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public Movie() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCardImageUrl() {
        return imageURL;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.imageURL = cardImageUrl;
    }

    public URI getCardImageURI() {
        try {
            return new URI(getCardImageUrl());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public URI getVideoURI(){
        try {
                return new URI(getVideoURL());
        } catch (URISyntaxException e){
            return null;
        }
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", videoURL='" + videoURL + '\'' +
                '}';
    }

}
