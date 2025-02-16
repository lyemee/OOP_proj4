import javax.swing.*;
import java.awt.*;

public class Player {
    private int x, y;
    private final int initialX = 200;
    private final int initialY = 300; // 초기 위치 저장
    private int width, height; // 이미지 크기
    private final int MAX_LIVES = 3;
    private final int INITIAL_COIN = 0;
    private int lives;
    private int coins;
    private int currentTrack;
    private Image[] runFrames;
    private int currentFrame;
    private int frameDelay;
    //private Image playerImage;
    private boolean isAnimating;

    public Player() {
        this.x = initialX;
        this.y = initialY;
        this.currentTrack = 1; // 처음에는 중앙 트랙(1번 트랙)에 위치
        this.lives = MAX_LIVES;
        this.coins = INITIAL_COIN;

        this.runFrames = new Image[0];
        this.currentFrame = 0;
        this.frameDelay = 0;
        this.isAnimating = false;
    }

    public void setPlayerImage(String imagePath) {
        this.runFrames = new Image[4];
        for (int i = 0; i < runFrames.length; i++) {
            runFrames[i] = new ImageIcon(imagePath + "run" + (i + 1) + ".png").getImage();
        }
        this.width = runFrames[0].getWidth(null); // 원본 이미지 크기
        this.height = runFrames[0].getHeight(null);
        this.isAnimating = true;
    }

    public void update() {
        // 프레임 업데이트
        if (isAnimating) {
            frameDelay++;
            if (frameDelay % 5 == 0) {
                currentFrame = (currentFrame + 1) % runFrames.length;
            }
        }
    }

    public void moveLeft(boolean leftPressed) {
        if(leftPressed){
            if (currentTrack == 1 || currentTrack == 2) {
                currentTrack--;
                x -=100; //트랙 간 거리만큼 이동
            }
        }
    }

    public void moveRight(boolean rightPressed) {
        if(rightPressed){
            if (currentTrack == 0 || currentTrack == 1) {
                currentTrack++;
                x += 100; //트랙 간 거리만큼 이동
            }
        }
    }

    public void draw(Graphics g) {
        if (runFrames.length > 0) {
            g.drawImage(runFrames[currentFrame], x, y, 50, 50, null);
        }
    }

    public void hitObstacle() {
        if (lives > 0) {
            lives--;
        }
    }
    public void hitCoin() {
        coins++;
    }


    public int getLives() {
        return lives;
    }

    public int getCoins(){return coins;}
    public void resetLives() {
        this.lives = MAX_LIVES;
    }
    public void resetCoins() {
        this.coins = INITIAL_COIN;
    }

    public void resetPosition() {
        this.x = initialX; // 초기 위치로 설정
        this.y = initialY;
        this.currentTrack = 1;
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50);
    }
}