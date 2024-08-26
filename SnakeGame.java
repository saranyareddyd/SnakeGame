import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener,KeyListener{
    private class Tile {
        int x;
        int y;
        
        Tile(int x, int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize=25;

    Tile snakeHead;
    ArrayList<Tile>snakeBody;
    Tile food;
    Random random;

    //Game Logic

    int velocityX;
    int velocityY;
    Timer gameloop;
    boolean gameOver=false;
    
    SnakeGame(int boardWidth,int boardHeight){
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        snakeHead=new Tile(5, 5);
        snakeBody=new ArrayList<Tile>();
        food=new Tile(10,10);
        random=new Random();
        placeFood();

        velocityX=1;
        velocityY=0;

        gameloop=new Timer(100,this);
        gameloop.start();
       
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //Grid
        for(int i=0;i<boardWidth/tileSize;i++){
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }
        //Food
        g.setColor(Color.red);
        g.fill3DRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize,true);
        //Snake Head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x *tileSize, snakeHead.y*tileSize, tileSize,tileSize,true);
        //Sanke Body
        for(int i=0;i<snakeBody.size();i++){
            Tile snakePart=snakeBody.get(i);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize,true);
        }
        //Score
        g.setFont(new Font("Arial ",Font.PLAIN,16));

        if(gameOver){
            g.setColor(Color.red);
            g.drawString("GameOver: " + String.valueOf(snakeBody.size()),tileSize - 16,tileSize);
        }
        else{
            g.drawString("Score: " + String.valueOf(snakeBody.size()),tileSize - 16,tileSize);
        }
   
    }
    public void placeFood(){
        food.x=random.nextInt(boardWidth/tileSize);
        food.y=random.nextInt(boardHeight/tileSize);
    }
    public void move(){
        //eat Food
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        //Snake Body
        for(int i=snakeBody.size()-1;i>=0;i--){
            Tile snakePart=snakeBody.get(i);
            if(i==0){
                snakePart.x=snakeHead.x;
                snakePart.y=snakeHead.y;
            }
            else{
                Tile prevSnakePart =snakeBody.get(i-1);
                snakePart.x=prevSnakePart.x;
                snakePart.y=prevSnakePart.y;
            }
        }
        snakeHead.x+=velocityX;
        snakeHead.y+=velocityY;

        //game overConditions

        for(int i=0;i<snakeBody.size();i++){
            Tile snakePart =snakeBody.get(i);
            if(collision(snakeHead, snakePart)){
                gameOver=true;
            }
        }

        if(snakeHead.x*tileSize < 0 || snakeHead.x*tileSize> boardWidth || snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight){
            gameOver=true;

        }
    }
    public boolean collision(Tile tile1,Tile tile2){
        return tile1.x==tile2.x && tile1.y==tile2.y;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameOver){
            gameloop.stop();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityY!=1){
            velocityX=0;
            velocityY=-1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1){
            velocityX=0;
            velocityY=1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1){
            velocityX=-1;
            velocityY=0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1){
            velocityX=1;
            velocityY=0;
        }
       
    }
   
    // //do not need
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
        
    
}
