import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RunningGame extends JPanel implements ActionListener, KeyListener {
    // 각 이미지들
    private Image startBackground;
    private Image characterBackground;
    private Image heartImage;
    private Image coinImage;
    private Image confirm;
    private Image gameOver;
    // 각 버튼들
    private JButton startButton;
    private JButton yesButton, noButton;
    private JButton retryButton, exitButton;
    private JButton player1Button, player2Button, player3Button, player4Button;

    private Timer timer;
    private Player player;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Coin> coins;
    private int backgroundY1, backgroundY2; // 두 개의 배경을 사용한 반복 스크롤링
    private int scrollSpeed = 5;
    private ArrayList<Track> tracks;
    private final int BASE_WIDTH = 800; // 기본 너비
    private final int BASE_HEIGHT = 600;

    // 게임 상태 플래그
    private boolean isGameStarted = false;
    private boolean isPlayerSelected = false;
    private boolean isGameOver = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public RunningGame() {
        setLayout(null); // 사용자 정의 배치 사용

        startBackground = new ImageIcon("image/start_2.png").getImage();
        characterBackground = new ImageIcon("image/background.png").getImage();
        heartImage = new ImageIcon("image/life.png").getImage(); // 생명 이미지
        coinImage = new ImageIcon("image/coin.png").getImage(); // 코인 이미지

        startButton();

        player = new Player(); // 화면 중앙 고정
        obstacles = new ArrayList<>(); // 장애물
        coins = new ArrayList<>(); // 코인

        tracks = new ArrayList<>();
        tracks.add(new Track(100, 0, 50, BASE_HEIGHT, "image/track.png")); // 왼쪽 트랙
        tracks.add(new Track(200, 0, 50, BASE_HEIGHT, "image/track.png")); // 중앙 트랙
        tracks.add(new Track(300, 0, 50, BASE_HEIGHT, "image/track.png")); // 오른쪽 트랙

        addObstacle(100, -200,"image/impostor.png");
        addObstacle(200,-400,"image/impostor.png");
        addObstacle(300, 0,"image/impostor.png");

        addCoin(100,-100,"image/coin.png");
        addCoin(200,-300,"image/coin.png");
        addCoin(300,-500,"image/coin.png");

        backgroundY1 = 0;
        backgroundY2 = -BASE_HEIGHT; // 화면 크기와 동일한 값

        timer = new Timer(30, this);
        timer.start();

        setFocusable(true);
        addKeyListener(this);
    }
    // 버튼 생성하는 메소드
    private JButton createButton(String imagePath, int x, int y, int width, int height) {
        JButton button = new JButton(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        button.setBounds(x, y, width, height); //버튼 위치 및 크기 설정
        button.setBorderPainted(false); //테두리 제거
        button.setContentAreaFilled(false); //배경 제거
        button.setFocusPainted(false); //포커스 테두리 제거
        button.setOpaque(false); //투명 설정
        return button;
    }

    private void startButton() {
        startButton = createButton("image/start button(title).png", 160, 275, 175, 45);

        add(startButton);

        startButton.addActionListener(e -> {
            isGameStarted = true;
            remove(startButton);
            showPlayerSelection();
            startGame();
            repaint();
        });

    }
    private void showPlayerSelection() {
        setLayout(null);

        player1Button = createButton("image/red/ch.png", 25, 190, 100, 150);
        player2Button = createButton("image/blue/ch.png", 140, 190, 100, 150);
        player3Button = createButton("image/green/ch.png", 260, 190, 100, 150);
        player4Button = createButton("image/pink/ch.png", 375, 190, 100, 150);

        add(player1Button);
        add(player2Button);
        add(player3Button);
        add(player4Button);

        player1Button.addActionListener(e -> {
            player.setPlayerImage("image/red/");
            showConfirmButton();
        });

        player2Button.addActionListener(e -> {
            player.setPlayerImage("image/blue/");
            showConfirmButton();
        });

        player3Button.addActionListener(e -> {
            player.setPlayerImage("image/green/");
            showConfirmButton();
        });

        player4Button.addActionListener(e -> {
            player.setPlayerImage("image/pink/");
            showConfirmButton();
        });

        repaint();
    }
    private void showConfirmButton() {
        confirm = new ImageIcon("image/select character_2.png").getImage();

        yesButton = createButton("image/yes.png", 140, 130, 29, 28);
        noButton = createButton("image/no.png", 320, 130, 27, 25);

        add(yesButton);
        add(noButton);

        yesButton.addActionListener(e -> {
            isPlayerSelected = true;

            remove(player1Button);
            remove(player2Button);
            remove(player3Button);
            remove(player4Button);
            startGame();
            repaint();
        });

        noButton.addActionListener(e -> {
            isPlayerSelected = false;
            confirm = null;
            yesButton.setVisible(false);
            noButton.setVisible(false);
            repaint();
        });

        yesButton.setVisible(true); // Confirm 버튼 보이기
        yesButton.setEnabled(true); // Confirm 버튼 활성화
        noButton.setVisible(true); // Confirm 버튼 보이기
        noButton.setEnabled(true); // Confirm 버튼 활성화
    }

    private void showGameOver() {
        setLayout(null);

        gameOver = new ImageIcon("image/game over.png").getImage();

        retryButton = createButton("image/retry.png", 120, 230, 73, 39);
        exitButton = createButton("image/exit.png", 320, 230, 56, 39);

        add(retryButton);
        add(exitButton);

        retryButton.addActionListener(e -> resetGame());
        exitButton.addActionListener(e -> System.exit(0));

        revalidate();
        repaint();
    }
    private void startGame() {
        if (isGameStarted && isPlayerSelected) {
            removeAll(); // 기존 요소 삭제
            revalidate();
            repaint();
        }
    }

    private void resetGame() {
        isGameOver = false;
        isGameStarted = false;
        isPlayerSelected = false;

        player.resetLives(); // 생명 초기화
        player.resetPosition(); // 위치 초기화
        player.resetCoins(); // 코인 초기화

        // 장애물 초기화
        int obstacley = -200;
        for (Obstacle obstacle : obstacles) {
            int trackIndex = (int) (Math.random() * tracks.size());
            obstacle.setX(tracks.get(trackIndex).getX());
            obstacle.setY(obstacley); // 화면 밖으로 이동
            obstacley -= 100;
        }

        int coiny = -300;
        for (Coin coin : coins) {
            int trackIndex = (int) (Math.random() * tracks.size());
            coin.setX(tracks.get(trackIndex).getX());
            coin.setY(coiny); // 화면 밖으로 이동
            coiny -= 200;
        }

        removeAll();
        revalidate();
        repaint();

        startButton();
    }
    private void addObstacle(int x, int y, String ObstacleImage) {
        // 트랙 중 하나의 x 좌표를 무작위로 선택
        int trackIndex = (int) (Math.random() % tracks.size());
        int trackX = tracks.get(trackIndex).getX();

        // 장애물을 트랙 위에 추가
        obstacles.add(new Obstacle(trackX, y, 50, 50, ObstacleImage));
    }
    private void addCoin(int x, int y, String CoinImage) {
        // 트랙 중 하나의 x 좌표를 무작위로 선택
        int trackIndex = (int) (Math.random() % tracks.size());
        int trackX = tracks.get(trackIndex).getX();

        // 장애물을 트랙 위에 추가
        coins.add(new Coin(trackX, y, 50, 50, CoinImage));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isGameStarted) {
            if (startBackground != null) {
                g.drawImage(startBackground, 0, 0, getWidth(), getHeight(), null);
            }
        } else if (isGameOver) {
            g.drawImage(characterBackground, 0, 0, getWidth(), getHeight(), null);
            if (gameOver != null) {
                g.drawImage(gameOver, 100, 100, 290, 76, this);
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Your Score : " + player.getCoins() + " !", 170, 210);
        } else {
            g.drawImage(characterBackground, 0, 0, getWidth(), getHeight(), null);

            if (confirm != null) {
                g.drawImage(confirm, 65, 30, 363, 76, this);
            }
            if (isPlayerSelected) {
                confirm = null;
                yesButton.setVisible(false);
                noButton.setVisible(false);

                for(Track track : tracks){
                    track.draw(g);
                }

                // 장애물 그리기: 빨간 사각형
                for (Obstacle obstacle : obstacles) {
                    obstacle.draw(g);
                }
                for (Coin coin : coins) {
                    coin.draw(g);
                }

                // 생명 표시
                for (int i = 0; i < player.getLives(); i++) {
                    g.drawImage(heartImage, 10 + (i * 35), 10, 30, 30, null);
                }

                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawImage(coinImage,10,40,30,30,null);
                g.drawString("       X " + player.getCoins(), 10, 60);
                player.draw(g);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameStarted || isGameOver) return;
        backgroundY1 += scrollSpeed;
        backgroundY2 += scrollSpeed;

        // 배경 반복
        if (backgroundY1 >= BASE_HEIGHT) {
            backgroundY1 = -BASE_HEIGHT;
        }
        if (backgroundY2 >= BASE_HEIGHT) {
            backgroundY2 = -BASE_HEIGHT;
        }

        for (Obstacle obstacle : obstacles) {
            obstacle.setY(obstacle.getY() + scrollSpeed); // setY() 메서드를 사용하여 y 업데이트

            // 장애물이 화면을 벗어나면 재배치
            if (obstacle.getY() > BASE_HEIGHT) {
                int trackIndex = (int) (Math.random() * tracks.size());
                obstacle.setY(-200);
                obstacle.setX(tracks.get(trackIndex).getX()); // 트랙의 x 좌표로 설정
            }
            //충돌 감지
            if (obstacle.checkCollision(player)) {
                player.hitObstacle();

                int trackIndex = (int) (Math.random() * tracks.size()); // 여기서도 정의
                obstacle.setY(-200);
                obstacle.setX(tracks.get(trackIndex).getX());
                if (player.getLives() <= 0) {
                    isGameOver = true;
                    showGameOver();
                }
            }
        }

        for (Coin coin : coins) {
            coin.setY(coin.getY() + scrollSpeed);

            // 화면 밖으로 벗어난 코인 재배치
            if (coin.getY() > BASE_HEIGHT) {
                int trackIndex = (int) (Math.random() * tracks.size());
                coin.setY(-200); // 화면 위로 이동
                coin.setX(tracks.get(trackIndex).getX());
            }
            if(coin.checkCollision(player)){
                player.hitCoin();
                int trackIndex = (int) (Math.random() * tracks.size()); // 여기서도 정의
                coin.setY(-200);
                coin.setX(tracks.get(trackIndex).getX());
            }
        }

        player.update();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
            player.moveLeft(leftPressed);
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
            player.moveRight(rightPressed);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
            player.moveLeft(leftPressed);
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
            player.moveRight(rightPressed);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}



