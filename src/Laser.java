import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Laser {
	
	private int x, y, width1, height1, width2, height2;
	private boolean visible;
	
	private Image laserHorizontal, laserVertical;
	
	private final int DX = 3;

	public Laser(int x, int y){
		
		laserHorizontal = new ImageIcon("resources/laserHorizontal.png").getImage();
		laserVertical = new ImageIcon("resources/laserVertical.png").getImage();
		
		width1 = laserHorizontal.getWidth(null);
		height1 = laserHorizontal.getHeight(null);
		width2 = laserVertical.getWidth(null);
		height2 = laserVertical.getHeight(null);
		
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
	
	public int getWidth1(){
		return width1;
	}
	
	public int getHeight1(){
		return height1;
	}
	
	public int getWidth2(){
		return width2;
	}
	
	public int getHeight2(){
		return height2;
	}
	
	public Rectangle getbounds1(){
		return new Rectangle(x, y, width1, height1);
	}
	
	public Rectangle getbounds2(){
		return new Rectangle(x, y, width2, height2);
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	public Image getImage1(){
		return laserHorizontal;
	}
	
	public Image getImage2(){
		return laserVertical;
	}
}
