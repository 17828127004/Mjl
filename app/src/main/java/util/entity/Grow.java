package util.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */
public class Grow implements Serializable {
    private List<Pic> pic;
    private String text;
    private String time;
    public List<Pic> getPic() {
        return pic;
    }

    public void setPic(List<Pic> pic) {
        this.pic = pic;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
