import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Battery {
	
	private int x, y, width, height;
	private boolean visible;
	
	private Image battery;

	public Battery(int x, int y){
		
		battery = new ImageIcon("resources/battery.png").getImage();
		
		width = battery.getWidth(null);
		height = battery.getHeight(null);
		
		this.x = x;
		this.y = y;
		
		visible = true;
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
	
	public Image getImage(){
		return battery;
	}
}
