import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }  

    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    
    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    //game logic
    int velocityX;
    int velocityY;
    Timer gameLoop;

    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 1;
        velocityY = 0;
        
		//game timer
		gameLoop = new Timer(100, this); //how long it takes to start timer, milliseconds gone between frames 
        gameLoop.start();
	}	
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
        //Grid Lines
        for(int i = 0; i < boardWidth/tileSize; i++) {
            //(x1, y1, x2, y2)
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize); 
        }

        //Food
        g.setColor(Color.red);
        // g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, true);

        //Snake Head
        g.setColor(Color.blue);
        // g.fillRect(snakeHead.x, snakeHead.y, tileSize, tileSize);
        // g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        
        //Snake Body
        g.setColor(Color.green);
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
		}

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
	}

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
		food.y = random.nextInt(boardHeight/tileSize);
	}

    public void move() {
        //eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //move snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { //right before the head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        //move snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //collide with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || //passed left border or right border
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) { //passed top border or bottom border
            gameOver = true;
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameOver) {
            /*int resposta = JOptionPane.showOptionDialog(this, "Game Over! Sua pontuação final foi: " + snakeBody.size(),"Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,null, new Object[] {"Reiniciar", "Sair"}, "Reiniciar");

            if (resposta == JOptionPane.YES_OPTION) {
                restartGame();
            }  else if (resposta == JOptionPane.NO_OPTION) {
                System.exit(0);
            }*/

            //Escreve novo highscore ao arquivo
            System.out.println(snakeBody.size());
            highScoreWrite();

            //Para o jogo
            gameLoop.stop();

            //Cria o frame Game Over
            gameOverScreen();

        }
    }  

    //private void setaButton(){}
    //POSSIBILIDADES DE OTIMIZAÇÃO
    //private void setLabel(JLabel label){}

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private void gameOverScreen(){
        //Cria o frame Game Over
        JFrame gameOverFrame = new JFrame("Game Over");
        gameOverFrame.setSize(800, 500);
        gameOverFrame.setLocationRelativeTo(null);
        gameOverFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Cria o painel de Game Over
        JPanel gameOverPanel = new JPanel();
        gameOverPanel.setLayout(null);
        gameOverPanel.setBackground(java.awt.Color.BLACK);
        
        //Título do Game Over
        JLabel gameOverTitle = new JLabel("Game Over");
        //Centraliza texto
        gameOverTitle.setHorizontalAlignment(SwingConstants.CENTER);
        //Tamanho e posição
        gameOverTitle.setBounds(100, 100, 600, 100);
        //Fonte e tamanho
        gameOverTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 54));
        //Cor da letra
        gameOverTitle.setForeground(java.awt.Color.RED);
        
        

        //Texto HighScore
        JLabel gameOverText = new JLabel("HighScore: " + String.valueOf(snakeBody.size()));
        //Centraliza texto
        gameOverText.setHorizontalAlignment(SwingConstants.CENTER);
        //Tamanho e posição
        gameOverText.setBounds(300, 200, 200, 50);
        //Fonte e tamanho
        gameOverText.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        //Cor da letra
        gameOverText.setForeground(java.awt.Color.WHITE);

        //Botão de Jogar novamente
        JButton gameRestartButton = new JButton("Jogar Novamente");
        //Centraliza texto
        gameRestartButton.setHorizontalAlignment(SwingConstants.CENTER);
        //Tamanho e posição
        gameRestartButton.setBounds(80, 300, 300, 100);
        //Fonte e tamanho
        gameRestartButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        //Listener
        gameRestartButton.addActionListener(f -> {
            gameOverFrame.dispose();
            restartGame();
        });
        
        //Botão de saída
        JButton gameLeaveButton = new JButton("Sair");
        //Centraliza texto
        gameLeaveButton.setHorizontalAlignment(SwingConstants.CENTER);
        //Tamanho e posição
        gameLeaveButton.setBounds(400, 300, 300, 100);
        //Fonte e tamanho
        gameLeaveButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        //Cor da letra
        gameLeaveButton.addActionListener(f -> {
            System.exit(0);
        });

        gameOverPanel.add(gameOverTitle);
        gameOverPanel.add(gameOverText);
        gameOverPanel.add(gameRestartButton);
        gameOverPanel.add(gameLeaveButton);

        gameOverFrame.add(gameOverPanel);

        gameOverFrame.setVisible(true);
    }

    private void restartGame(){
        snakeBody.clear();
        snakeHead = new Tile(5, 5);
        placeFood();
        velocityX = 1;
        velocityY = 0;
        gameOver = false;
        gameLoop.start();
    }
    
    private boolean highScoreFileCheck(){
        File score = new File("score.txt");
        if(score.exists()){
            return true;
        }
        else{
            return false;
        }
    }

    private int highScoreRead(){
        try(BufferedReader reader = new BufferedReader(new FileReader("score.txt"))){
            return Integer.parseInt(reader.readLine());
        } catch (IOException e){
            System.out.println("Erro ao ler o arquivo");
            e.printStackTrace();
        }
        return -1;
    } 

    private void highScoreWrite(){
        //Se o arquivo existe
        if(highScoreFileCheck()){
                //Se score atual for maior que o lido
                if(snakeBody.size() > highScoreRead()){
                    //Escrever novo high score no arquivo
                    String newScore = String.valueOf(snakeBody.size());
                    try(FileWriter writer = new FileWriter("score.txt")){
                        writer.write(newScore);
                    } catch(IOException e){
                        System.out.println("Erro ao escrever ao arquivo");
                    }
                }
        }
        else{
            //Criar arquivo
            try(FileWriter writer = new FileWriter("score.txt")){
                writer.write(snakeBody.size());
            } catch(IOException e){
                System.out.println("Erro ao criar o arquivo");
            }
        }
    }