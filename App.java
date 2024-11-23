import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
	frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
        createMenu(frame, boardWidth, boardHeight);
    }

    private static void createMenu(JFrame frame, int boardWidth, int boardHeight) {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBackground(java.awt.Color.BLACK);

        JLabel title = new JLabel("Snake Game");
        title.setForeground(java.awt.Color.GREEN);
        title.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 36));
        title.setBounds(180, 100, 300, 50);

        JButton startButton = new JButton("Iniciar Jogo");
        startButton.setBounds(200, 200, 200, 50);
        startButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
            frame.add(snakeGame);
            frame.pack();
            frame.setVisible(true);
            snakeGame.requestFocus();
        });

        JButton settingsButton = new JButton("Configurações");
        settingsButton.setBounds(200, 270, 200, 50);
        settingsButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Configurações ainda não implementadas."));

        JButton exitButton = new JButton("Sair");
        exitButton.setBounds(200, 340, 200, 50);
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(title);
        menuPanel.add(startButton);
        menuPanel.add(settingsButton);
        menuPanel.add(exitButton);

        frame.add(menuPanel);
        frame.setVisible(true);
    }
}
