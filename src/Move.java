public class Move extends Cell{
    public boolean isHit; // set to true if this move hits other player ship
    public Move(int x, int y, boolean isHit) {
        super(x, y); //super() to use the constructor from the parent
        this.isHit = isHit;
    }
}
