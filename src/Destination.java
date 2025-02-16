import java.awt.*;

public class Destination {
    private int x, y;

    public Destination(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, 40, 40);
    }

    public boolean isReached(Player player) {
        return player.getBounds().intersects(new Rectangle(x, y, 40, 40));
    }
}
