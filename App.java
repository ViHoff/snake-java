import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

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

        JLabel highScore = new JLabel("High Score: " +  highScoreRead());
        highScore.setForeground(java.awt.Color.GREEN);
        highScore.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        highScore.setBounds(10, -10, 200, 60);

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

        JButton howToPlayButton = new JButton("Como Jogar:");
        howToPlayButton.setBounds(200, 270, 200, 50);
        howToPlayButton.addActionListener(e -> abrirComoJogarFrame());

        JButton exitButton = new JButton("Sair");
        exitButton.setBounds(200, 340, 200, 50);
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(title);
        menuPanel.add(highScore);
        menuPanel.add(startButton);
        menuPanel.add(howToPlayButton);
        menuPanel.add(exitButton);

        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        howToPlayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


        frame.add(menuPanel);
        frame.setVisible(true);
    }

    private static void abrirComoJogarFrame() {
        JFrame comoJogarFrame = new JFrame("Como Jogar");
        comoJogarFrame.setSize(500, 500);
        comoJogarFrame.setLocationRelativeTo(null);
        comoJogarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //textos
        JLabel texto = new JLabel("Use as setas para controlar a cobra e comer os alimentos!");
        texto.setHorizontalAlignment(SwingConstants.CENTER);
        texto.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel texto2 = new JLabel("<html>O Snake Game é um jogo simples e divertido no qual você controla uma cobra " +
                "que cresce ao comer alimentos. O objetivo principal é fazer a maior pontuação " +
                "possível sem bater em paredes ou em si mesma.</html>");
        texto2.setFont(new Font("Arial", Font.PLAIN, 14));  // Ajusta o tamanho da fonte
        texto2.setHorizontalAlignment(SwingConstants.CENTER);
        texto2.setPreferredSize(new Dimension(400, 100));  // Ajusta o tamanho do texto para caber na janela


        //Inserção de imagem
        ImageIcon imagem = new ImageIcon("png-transparent-arrow-keys-free-thumbnail.png");
        Image imagemReduzida = imagem.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); //redução
        ImageIcon imagem2 = new ImageIcon(imagemReduzida);
        JLabel labelImagem = new JLabel(imagem2);

        panel.add(texto, BorderLayout.NORTH);
        panel.add(labelImagem, BorderLayout.CENTER);
        panel.add(texto2, BorderLayout.SOUTH);

        comoJogarFrame.add(panel);

        comoJogarFrame.setVisible(true);
    }

    private static String highScoreRead(){
        if(highScoreFileCheck()){
            try(BufferedReader reader = new BufferedReader(new FileReader("score.txt"))){
                return reader.readLine();
            } catch (IOException e){
                System.out.println("Erro ao ler o arquivo");
                e.printStackTrace();
                return "Erro";
            }
        } else{
            try(FileWriter writer = new FileWriter("score.txt")){
                writer.write("0");
                return "0";
            } catch(IOException e){
                System.out.println("Erro ao escrever o arquivo");
                e.printStackTrace();
                return "Erro";
            }
        }

    } 

    private static boolean highScoreFileCheck(){
        File score = new File("score.txt");
        if(score.exists()){
            return true;
        }
        else{
            return false;
        }
    }
}