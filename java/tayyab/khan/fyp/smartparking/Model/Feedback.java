package tayyab.khan.fyp.smartparking.Model;

import java.io.Serializable;

public class Feedback implements Serializable {
    private String title, desc, sender, time;

    public Feedback() {
    }

    public Feedback(String title, String desc, String sender, String time) {
        this.title = title;
        this.desc = desc;
        this.sender = sender;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
