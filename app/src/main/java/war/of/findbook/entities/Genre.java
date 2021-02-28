package war.of.findbook.entities;

public class Genre  {

    private boolean fantasy;
    private boolean sci_Fi;
    private boolean mystery;
    private boolean thriller;
    private boolean romance;
    private boolean westerns;
    private boolean horror;
    private boolean history;
    private boolean children;
    private boolean development;


    public boolean isFantasy() {
        return fantasy;
    }

    public Genre setFantasy(boolean fantasy) {
        this.fantasy = fantasy;
        return this;
    }

    public boolean isSci_Fi() {
        return sci_Fi;
    }

    public Genre setSci_Fi(boolean sci_Fi) {
        this.sci_Fi = sci_Fi;
        return this;
    }

    public boolean isMystery() {
        return mystery;
    }

    public Genre setMystery(boolean mystery) {
        this.mystery = mystery;
        return this;
    }

    public boolean isThriller() {
        return thriller;
    }

    public Genre setThriller(boolean thriller) {
        this.thriller = thriller;
        return this;
    }

    public boolean isRomance() {
        return romance;
    }

    public Genre setRomance(boolean romance) {
        this.romance = romance;
        return this;
    }

    public boolean isWesterns() {
        return westerns;
    }

    public Genre setWesterns(boolean westerns) {
        this.westerns = westerns;
        return this;
    }

    public boolean isHorror() {
        return horror;
    }

    public Genre setHorror(boolean horror) {
        this.horror = horror;
        return this;
    }

    public boolean isHistory() {
        return history;
    }

    public Genre setHistory(boolean history) {
        this.history = history;
        return this;
    }

    public boolean isChildren() {
        return children;
    }

    public Genre setChildren(boolean children) {
        this.children = children;
        return this;
    }

    public boolean isDevelopment() {
        return development;
    }

    public Genre setDevelopment(boolean development) {
        this.development = development;
        return this;
    }
}
