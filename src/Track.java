import javax.swing.*;
import java.awt.*;

public class Track {
    private int x, y, width, height;
    private Image trackImage;

    public Track(int x, int y, int width, int height,String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.trackImage = new ImageIcon(imagePath).getImage();
    }

    public void draw(Graphics g) {
        g.drawImage(trackImage, x, y, width, height, null); // 트랙 이미지 그리기
        g.drawImage(trackImage, x, y - height, width, height, null); // 반복 스크롤
        g.drawImage(trackImage, x, y + height, width, height, null); // 반복 스크롤
    }

    public int getY() {
        return y;
    }

    // Setter for y
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }

    // Setter for y
    public void setX(int x) {
        this.x = x;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
