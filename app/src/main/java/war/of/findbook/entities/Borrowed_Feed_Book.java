package war.of.findbook.entities;

public class Borrowed_Feed_Book extends Feed_Book {
    private String userRequest;

    public Borrowed_Feed_Book() {
    }

    public Borrowed_Feed_Book(String name, String desc , String author, Condition condition, String owner, String userRequest) {
        super(name, desc , author, condition, owner);
        this.userRequest = userRequest;
    }

    public Borrowed_Feed_Book(Feed_Book book, String myID) {
        super(book.getName(), book.getDesc() , book.getAuthor(), book.getCondition(), book.getOwner());
        this.userRequest=myID;
    }

    public String getUserRequest() {
        return userRequest;
    }

    public Borrowed_Feed_Book setUserRequest(String userRequest) {
        this.userRequest = userRequest;
        return this;
    }
}
