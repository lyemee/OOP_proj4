public class Main {
    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame("Among us run ");
        RunningGame gamePanel = new RunningGame();

        frame.add(gamePanel);              // 게임 패널 추가
        frame.setSize(500, 400);           // 창 크기 설정
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE); // 창 닫기 동작
        frame.setVisible(true);            // 창 표시
    }
}
