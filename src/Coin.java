import java.awt.*;
import javax.swing.*;
public class Coin {
    private int x, y, width, height;
    private Image CoinImage;
    public Coin(int x, int y, int width, int height, String imagePath){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.CoinImage = new ImageIcon(imagePath).getImage();
    }
    public void draw(Graphics g) {
        if (CoinImage != null) {
            g.drawImage(CoinImage, x, y, width, height, null);
        } else {
            // 이미지가 없을 경우 기본 색상으로 표시
            g.setColor(Color.GREEN);
            g.fillRect(x, y, width, height);
        }
    }
    public boolean checkCollision(Player player) {
        return player.getBounds().intersects(new Rectangle(x, y, width, height));
    }
    public int getY() {
        return y;
    }

    // Setter for y
    public void setY(int y) {
        this.y = y;
    }
    public void setX(int x) {
        this.x = x;
    }

}
