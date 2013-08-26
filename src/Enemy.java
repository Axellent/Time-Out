import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Enemy {
	
	private int x, y, width, height;
	private boolean visible;
	
	private Image enemyLeft, enemyRight, enemyUp, enemyDown;
	
	private final int DX = 1;

	public Enemy(int x, int y){
		
		enemyLeft = new ImageIcon("resources/enemyLeft.png").getImage();
		enemyRight = new ImageIcon("resources/enemyRight.png").getImage();
		enemyUp = new ImageIcon("resources/enemyUp.png").getImage();
		enemyDown = new ImageIcon("resources/enemyDown.png").getImage();
		
		width = enemyLeft.getWidth(null);
		height = enemyLeft.getHeight(null);
		
		this.x = x;
		this.y = y;
		
		visible = true;
	}
	
	public void moveLeft(){
		x -= DX;
	}
	
	public void moveRight(){
		x += DX;
	}
	
	public void moveUp(){
		y -= DX;
	}
	
	public void moveDown(){
		y += DX;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public Rectangle getbounds(){
		return new Rectangle(x, y, width, height);
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	public Image getImageLeft(){
		return enemyLeft;
	}
	
	public Image getImageRight(){
		return enemyRight;
	}
	
	public Image getImageUp(){
		return enemyUp;
	}
	
	public Image getImageDown(){
		return enemyDown;
	}
}
