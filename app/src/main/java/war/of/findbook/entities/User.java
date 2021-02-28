package war.of.findbook.entities;

public class User {
    private long timestampCreate;
    private String uid;
    private String userName;

    public User() {
    }

    public long getTimestampCreate() {
        return timestampCreate;
    }

    public void setTimestampCreate(long timestampCreate) {
        this.timestampCreate = timestampCreate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User(long timestampCreate, String uid, String userName) {
        this.timestampCreate = timestampCreate;
        this.uid = uid;
        this.userName = userName;
    }
}
