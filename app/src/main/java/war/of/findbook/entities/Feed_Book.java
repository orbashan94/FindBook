package war.of.findbook.entities;

public class Feed_Book {
    public static final String PROSE = "@string/prose";
    public static final String BIOGRAPHY = "biography";
    public static final String DEVELOPMENT = "development";
    public static final String CHILDREN = "children";
    public static final String STUDY = "study";


    public enum Condition {
        NEW, OLD;
    }

    protected long timestamp;
    private String name = "";
    private String desc = "";
    private String imageURL = "";
    private String author = "";
    private String address = "";
    private Condition condition;
    private String owner;
    private String id;
    private String genre;

    public Feed_Book() {
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public Feed_Book setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }


    public String getName() {
        return name;
    }

    public Feed_Book setName(String name) {
        this.name = name;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public Feed_Book setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Feed_Book setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Feed_Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getId() {
        return id;
    }

    public Feed_Book setId(String id) {
        this.id = id;
        return this;
    }

    public Condition getCondition() {
        return condition;
    }

    public Feed_Book setCondition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public Feed_Book setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public Feed_Book(String name, String desc, String author, Condition condition, String owner) {
        this.name = name;
        this.desc = desc;
        this.imageURL = imageURL;
        this.author = author;
        this.condition = condition;
        this.owner = owner;
    }

}
