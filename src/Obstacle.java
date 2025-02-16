import java.awt.*;
import javax.swing.*;

public class Obstacle {
    private int x, y, width, height;
    private Image obstacleImage;
    public Obstacle(int x, int y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // 이미지 로드
        this.obstacleImage = new ImageIcon(imagePath).getImage();
    }

    public void draw(Graphics g) {
        if (obstacleImage != null) {
            g.drawImage(obstacleImage, x, y, width, height, null);
        } else {
            // 이미지가 없을 경우 기본 색상으로 표시
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }

    // Running Game 클래스에서도 x,y를 접근할 수 있도록 getter setter 설정
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


    public boolean checkCollision(Player player) {
        return player.getBounds().intersects(new Rectangle(x, y, width, height));
    }
}
