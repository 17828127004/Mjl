package util.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */
public class Group {
    private String groupTime;
    private String groupText;
    private String groupCount;
    private String groupImage;
    private List<Child> list_child = new ArrayList<Child>();
    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }


    public String getGroupTime() {
        return groupTime;
    }
    public void setGroupTime(String groupTime) {
        this.groupTime = groupTime;
    }

    public List<Child> getList_child() {
        return list_child;
    }

    public void setList_child(List<Child> list_child) {
        this.list_child = list_child;
    }

    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }

    public String getGroupText() {
        return groupText;
    }

    public void setGroupText(String groupText) {
        this.groupText = groupText;
    }


}
