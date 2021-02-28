package war.of.findbook.entities;

public class My_Book extends Feed_Book {
    private boolean hasAlreadyBorrowed;

    public My_Book(String name, String desc , String author, Condition condition, String owner, boolean hasAlreadyBorrowed) {
        super(name, desc , author, condition, owner);
        this.hasAlreadyBorrowed = hasAlreadyBorrowed;
    }



    public My_Book() {
    }

    public My_Book(boolean hasAlreadyBorrowed) {
        this.hasAlreadyBorrowed = hasAlreadyBorrowed;
    }

    public boolean getHasAlreadyBorrowed() {
        return hasAlreadyBorrowed;
    }

    public My_Book setHasAlreadyBorrowed(boolean hasAlreadyBorrowed) {
        this.hasAlreadyBorrowed = hasAlreadyBorrowed;
        return this;
    }


    @Override
    public String toString() {
        return "MyBook{" +
                "hasAlreadyBorrowed=" + hasAlreadyBorrowed +
                '}';
    }
}
