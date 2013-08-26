import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener{
	
	int x, y, dx, dy, width, height, levelcount, charge, count;
	boolean paused = true;
	boolean playing = false;
	boolean reloading = false;
	
	Font f = new Font("serif", Font.PLAIN, 25);
	Font f2 = new Font("serif", Font.PLAIN, 14);
	
	String direction, gameover, chargelevel, timeout, timeleft;
	Image player, playerLeft, playerRight, playerUp, playerDown;
	Timer mainTimer = new Timer(5, this);
	Timer countdown;
	ArrayList leftLasers, rightLasers, upLasers, downLasers, leftEnemies, rightEnemies, upEnemies, downEnemies, batteries;
	
	final int DELAY = 1000;
	
	int [][] batteryPositions = {
			{450, 390}, {50, 90}, {250, 250},
			{240, 360}, {390, 220}, {80, 240},
			{210, 60}, {450, 5}, {10, 360},
	};
	
	int [][] leftPositions = {
			{-1900, 170}, {-1860, 430}, {-1780, 110},
			{-1670, 360}, {-1600, 50}, {-1510, 220},
			{-1350, 220}, {-1100, 50}, {-1030, 400},
			{-960, 140}, {-820, 20}, {-740, 330},
			{-690, 150}, {-610, 210}, {-550, 430},
	};

	int [][] rightPositions = {
			{1990, 170}, {1900, 430}, {1880, 110},
			{1770, 360}, {1660, 50}, {1610, 220},
			{1250, 220}, {1100, 50}, {1030, 400},
			{960, 140}, {820, 20}, {740, 330},
			{690, 150}, {610, 210}, {550, 430},
	};

	int [][] upPositions = {
			{170, 1990}, {430, 1900}, {110, 1880},
			{360, 1770}, {50, 1660}, {220, 1610},
			{220, 1250}, {50, 1100}, {400, 1030},
			{140, 960}, {20, 820}, {330, 740},
			{150, 690}, {210, 610}, {430, 550},
	};

	int [][] downPositions = {
			{170, -1900}, {430, -1860}, {110, -1780},
			{360, -1670}, {50, -1600}, {220, -1510},
			{220, -1250}, {50, -1100}, {400, -1030},
			{140, -960}, {20, -820}, {330, -740},
			{150, -690}, {210, -610}, {430, -550},
	};

	public Game(){
		
		setFocusable(true);
		setDoubleBuffered(true);
		setBackground(Color.BLACK);
		
		addKeyListener(new Adapter());
		
		playerLeft = new ImageIcon("resources/playerLeft.png").getImage();
		playerRight = new ImageIcon("resources/playerRight.png").getImage();
		playerUp = new ImageIcon("resources/playerUp.png").getImage();
		playerDown = new ImageIcon("resources/playerDown.png").getImage();
		
		width = playerLeft.getWidth(null);
		height = playerLeft.getHeight(null);
		
		x = 230;
		y = 230;
		levelcount = 1;
		charge = 10000;
		count = 10;
		
		direction = "faceUp";
		gameover = "New Game? Press Enter!";
		chargelevel = "Charge: " + chargelevel;
		timeout = "Time Out!";
		timeleft = "Time: " + count;
		
		leftLasers = new ArrayList();
		rightLasers = new ArrayList();
		upLasers = new ArrayList();
		downLasers = new ArrayList();
		
		leftEnemies = new ArrayList();
		rightEnemies = new ArrayList();
		upEnemies = new ArrayList();
		downEnemies = new ArrayList();
		
		batteries = new ArrayList();
		
		newEnemies();
		
		countdown = new Timer(1000, new Countdown());
		countdown.start();
		
		mainTimer.start();
	}
	
	public void newBatteries(){
		for (int i = 0; i < batteryPositions.length; i++) {
            batteries.add(new Battery(batteryPositions[i][0], batteryPositions[i][1]));
        }
	}
	
	public void newEnemies(){
		
		for (int i = 0; i < rightPositions.length; i++) {
            rightEnemies.add(new Enemy(rightPositions[i][0], rightPositions[i][1]));
        }
		
		if(levelcount >= 0){
			for (int i = 0; i < leftPositions.length; i++) {
	            leftEnemies.add(new Enemy(leftPositions[i][0], leftPositions[i][1]));
	        }
		}
		
		if(levelcount >= 0){
			for (int i = 0; i < upPositions.length; i++) {
	            upEnemies.add(new Enemy(upPositions[i][0], upPositions[i][1]));
	        }
		}
		
		if(levelcount >= 0){
			for (int i = 0; i < downPositions.length; i++) {
	            downEnemies.add(new Enemy(downPositions[i][0], downPositions[i][1]));
	        }
		}
	}
	
	public void play(){
		paused = false;
		count = 10;
	}
	
	public void pause(){
		paused = true;
		levelcount++;
		newEnemies();
		batteries = new ArrayList();
		newBatteries();
		count = 5;
	}
	
	public void gameover(){
		leftLasers = new ArrayList();
		rightLasers = new ArrayList();
		upLasers = new ArrayList();
		downLasers = new ArrayList();
		
		leftEnemies = new ArrayList();
		rightEnemies = new ArrayList();
		upEnemies = new ArrayList();
		downEnemies = new ArrayList();
		
		batteries = new ArrayList();
		
		x = 230;
		y = 230;
		
		levelcount = 1;
		
		charge = 10000;
		
		playing = false;
	}
	
	public void movePlayer(){
		x += dx;
		y += dy;
		
		if(x < 0)
			x = 0;
		if(y < 0)
			y = 0;
		if(x > 470)
			x = 470;
		if(y > 448)
			y = 448;
	}
	
	public void moveLasers(){
		
		ArrayList lLasers = leftLasers;
		
        for (int i = 0; i < lLasers.size(); i++) {
            Laser l = (Laser) lLasers.get(i);
            if(l.isVisible()){
                l.moveLeft();
            }
            else lLasers.remove(i);
        }
        
		ArrayList rLasers = rightLasers;
		
        for (int i = 0; i < rLasers.size(); i++) {
            Laser l = (Laser)rLasers.get(i);
            if(l.isVisible()){
                l.moveRight();
            }
            else rLasers.remove(i);
        }
        
        ArrayList uLasers = upLasers;
		
        for (int i = 0; i < uLasers.size(); i++) {
            Laser l = (Laser)uLasers.get(i);
            if(l.isVisible()){
                l.moveUp();
            }
            else uLasers.remove(i);
        }
        
		ArrayList dLasers = downLasers;
		
        for (int i = 0; i < dLasers.size(); i++) {
            Laser l = (Laser) dLasers.get(i);
            if(l.isVisible()){
                l.moveDown();
            }
            else dLasers.remove(i);
        }
	}
	
	public void moveEnemies(){
        
        for (int i = 0; i < leftEnemies.size(); i++) {
            Enemy e = (Enemy) leftEnemies.get(i);
            if (e.isVisible()) 
                e.moveRight();
            else leftEnemies.remove(i);
        }
        
        for (int i = 0; i < rightEnemies.size(); i++) {
            Enemy e = (Enemy) rightEnemies.get(i);
            if (e.isVisible()) 
                e.moveLeft();
            else rightEnemies.remove(i);
        }
        
        for (int i = 0; i < upEnemies.size(); i++) {
            Enemy e = (Enemy) upEnemies.get(i);
            if (e.isVisible()) 
                e.moveUp();
            else upEnemies.remove(i);
        }
        
        for (int i = 0; i < downEnemies.size(); i++) {
            Enemy e = (Enemy) downEnemies.get(i);
            if (e.isVisible()) 
                e.moveDown();
            else downEnemies.remove(i);
        }
	}
	
	public void collisions(){
		
		Rectangle playerRect = new Rectangle(x, y, width, height);
		
        for (int k = 0; k < batteries.size(); k++) {
            Battery b = (Battery) batteries.get(k);
            Rectangle batRect = b.getbounds();
            
            if(playerRect.intersects(batRect)){
            	charge += 500;
            	batteries.remove(k);
            }
        }
            
        for (int i = 0; i < rightEnemies.size(); i++) {
            Enemy e = (Enemy) rightEnemies.get(i);
            Rectangle enemyRect1 = e.getbounds();
                
            if(playerRect.intersects(enemyRect1)){
             	gameover();
            }
                        
            ArrayList lLasers = leftLasers;

            for (int j = 0; j < lLasers.size(); j++) {
            	Laser l = (Laser) lLasers.get(j);
            	Rectangle laserRect1 = l.getbounds1();

            	if(laserRect1.intersects(enemyRect1)){
            		rightEnemies.remove(i);
            	}
            }

            ArrayList rLasers = rightLasers;

            for (int j = 0; j < rLasers.size(); j++) {
            	Laser l = (Laser)rLasers.get(j);
            	Rectangle laserRect2 = l.getbounds1();

            	if(laserRect2.intersects(enemyRect1)){
            		rightEnemies.remove(i);
            	}
            }

            ArrayList uLasers = upLasers;

            for (int j = 0; j < uLasers.size(); j++) {
            	Laser l = (Laser)uLasers.get(j);
            	Rectangle laserRect3 = l.getbounds2();

            	if(laserRect3.intersects(enemyRect1)){
            		rightEnemies.remove(i);
            	}
            }

            ArrayList dLasers = downLasers;

            for (int j = 0; j < dLasers.size(); j++) {
            	Laser l = (Laser) dLasers.get(j);
            	Rectangle laserRect4 = l.getbounds1();

            	if(laserRect4.intersects(enemyRect1)){
            		rightEnemies.remove(i);
            	}
            }
        }
        
        for (int i = 0; i < leftEnemies.size(); i++) {
            Enemy e = (Enemy) leftEnemies.get(i);
            Rectangle enemyRect1 = e.getbounds();
                
            if(playerRect.intersects(enemyRect1)){
             	gameover();
            }
                        
            ArrayList lLasers = leftLasers;

            for (int j = 0; j < lLasers.size(); j++) {
            	Laser l = (Laser) lLasers.get(j);
            	Rectangle laserRect1 = l.getbounds1();

            	if(laserRect1.intersects(enemyRect1)){
            		leftEnemies.remove(i);
            	}
            }

            ArrayList rLasers = rightLasers;

            for (int j = 0; j < rLasers.size(); j++) {
            	Laser l = (Laser)rLasers.get(j);
            	Rectangle laserRect2 = l.getbounds1();

            	if(laserRect2.intersects(enemyRect1)){
            		leftEnemies.remove(i);
            	}
            }

            ArrayList uLasers = upLasers;

            for (int j = 0; j < uLasers.size(); j++) {
            	Laser l = (Laser)uLasers.get(j);
            	Rectangle laserRect3 = l.getbounds2();

            	if(laserRect3.intersects(enemyRect1)){
            		leftEnemies.remove(i);
            	}
            }

            ArrayList dLasers = downLasers;

            for (int j = 0; j < dLasers.size(); j++) {
            	Laser l = (Laser) dLasers.get(j);
            	Rectangle laserRect4 = l.getbounds1();

            	if(laserRect4.intersects(enemyRect1)){
            		leftEnemies.remove(i);
            	}
            }
        }
        
        for (int i = 0; i < upEnemies.size(); i++) {
            Enemy e = (Enemy) upEnemies.get(i);
            Rectangle enemyRect1 = e.getbounds();
                
            if(playerRect.intersects(enemyRect1)){
             	gameover();
            }
                        
            ArrayList lLasers = leftLasers;

            for (int j = 0; j < lLasers.size(); j++) {
            	Laser l = (Laser) lLasers.get(j);
            	Rectangle laserRect1 = l.getbounds1();

            	if(laserRect1.intersects(enemyRect1)){
            		upEnemies.remove(i);
            	}
            }

            ArrayList rLasers = rightLasers;

            for (int j = 0; j < rLasers.size(); j++) {
            	Laser l = (Laser)rLasers.get(j);
            	Rectangle laserRect2 = l.getbounds1();

            	if(laserRect2.intersects(enemyRect1)){
            		upEnemies.remove(i);
            	}
            }

            ArrayList uLasers = upLasers;

            for (int j = 0; j < uLasers.size(); j++) {
            	Laser l = (Laser)uLasers.get(j);
            	Rectangle laserRect3 = l.getbounds2();

            	if(laserRect3.intersects(enemyRect1)){
            		upEnemies.remove(i);
            	}
            }

            ArrayList dLasers = downLasers;

            for (int j = 0; j < dLasers.size(); j++) {
            	Laser l = (Laser) dLasers.get(j);
            	Rectangle laserRect4 = l.getbounds1();

            	if(laserRect4.intersects(enemyRect1)){
            		upEnemies.remove(i);
            	}
            }
        }
        
        for (int i = 0; i < downEnemies.size(); i++) {
            Enemy e = (Enemy) downEnemies.get(i);
            Rectangle enemyRect1 = e.getbounds();
                
            if(playerRect.intersects(enemyRect1)){
             	gameover();
            }
                        
            ArrayList lLasers = leftLasers;

            for (int j = 0; j < lLasers.size(); j++) {
            	Laser l = (Laser) lLasers.get(j);
            	Rectangle laserRect1 = l.getbounds1();

            	if(laserRect1.intersects(enemyRect1)){
            		downEnemies.remove(i);
            	}
            }

            ArrayList rLasers = rightLasers;

            for (int j = 0; j < rLasers.size(); j++) {
            	Laser l = (Laser)rLasers.get(j);
            	Rectangle laserRect2 = l.getbounds1();

            	if(laserRect2.intersects(enemyRect1)){
            		downEnemies.remove(i);
            	}
            }

            ArrayList uLasers = upLasers;

            for (int j = 0; j < uLasers.size(); j++) {
            	Laser l = (Laser)uLasers.get(j);
            	Rectangle laserRect3 = l.getbounds2();

            	if(laserRect3.intersects(enemyRect1)){
            		downEnemies.remove(i);
            	}
            }

            ArrayList dLasers = downLasers;

            for (int j = 0; j < dLasers.size(); j++) {
            	Laser l = (Laser) dLasers.get(j);
            	Rectangle laserRect4 = l.getbounds1();

            	if(laserRect4.intersects(enemyRect1)){
            		downEnemies.remove(i);
            	}
            }
        }
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(playing){
			movePlayer();
			moveLasers();
			if(!paused)
				moveEnemies();
			collisions();
			repaint();
			charge--;
			chargelevel = "Charge: " + charge;
			
			if(charge <= 0){
				gameover();
			}
		}
	}
	
	public void addLaser(){
		
		switch (direction){
		
		case "faceLeft": leftLasers.add(new Laser(x - 6, y + 12));
		break;
		
		case "faceRight": rightLasers.add(new Laser(x + 25, y + 12));
		break;
		
		case "faceUp": upLasers.add(new Laser(x + 12, y - 6));
		break;
		
		case "faceDown": downLasers.add(new Laser(x + 12, y + 25));
		break;
		
		default:
		break;
		}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		
		switch (direction){
		
		case "faceLeft": g2d.drawImage(playerLeft, x, y, this);
		break;
		
		case "faceRight": g2d.drawImage(playerRight, x, y, this);
		break;
		
		case "faceUp": g2d.drawImage(playerUp, x, y, this);
		break;
		
		case "faceDown": g2d.drawImage(playerDown, x, y, this);
		break;
		
		default: g2d.drawImage(playerUp, x, y, this);
		break;
		}
        
		ArrayList lLasers = leftLasers;
		
        for (int i = 0; i < lLasers.size(); i++) {
            Laser l = (Laser) lLasers.get(i);
            if(l.isVisible())
            	g2d.drawImage(l.getImage1(), l.getX(), l.getY(), this);
        }
        
		ArrayList rLasers = rightLasers;
		
        for (int i = 0; i < rLasers.size(); i++) {
            Laser l = (Laser)rLasers.get(i);
            if(l.isVisible())
            	g2d.drawImage(l.getImage1(), l.getX(), l.getY(), this);
        }
        
        ArrayList uLasers = upLasers;
		
        for (int i = 0; i < uLasers.size(); i++) {
            Laser l = (Laser)uLasers.get(i);
            if(l.isVisible())
            	g2d.drawImage(l.getImage2(), l.getX(), l.getY(), this);
        }
        
		ArrayList dLasers = downLasers;
		
        for (int i = 0; i < dLasers.size(); i++) {
            Laser l = (Laser) dLasers.get(i);
            if(l.isVisible())
            	g2d.drawImage(l.getImage2(), l.getX(), l.getY(), this);
        }
        
        for (int i = 0; i < leftEnemies.size(); i++) {
            Enemy e = (Enemy)leftEnemies.get(i);
            if (e.isVisible())
                g2d.drawImage(e.getImageLeft(), e.getX(), e.getY(), this);
        }
        
        for (int i = 0; i < rightEnemies.size(); i++) {
            Enemy e = (Enemy)rightEnemies.get(i);
            if (e.isVisible())
                g2d.drawImage(e.getImageRight(), e.getX(), e.getY(), this);
        }
        
        for (int i = 0; i < upEnemies.size(); i++) {
            Enemy e = (Enemy)upEnemies.get(i);
            if (e.isVisible())
                g2d.drawImage(e.getImageUp(), e.getX(), e.getY(), this);
        }
        
        for (int i = 0; i < downEnemies.size(); i++) {
            Enemy e = (Enemy)downEnemies.get(i);
            if (e.isVisible())
                g2d.drawImage(e.getImageDown(), e.getX(), e.getY(), this);
        }
        
        for (int i = 0; i < batteries.size(); i++) {
            Battery b = (Battery) batteries.get(i);
            if(b.isVisible())
            	g2d.drawImage(b.getImage(), b.getX(), b.getY(), this);
        }
        
		if(!playing){
			g2d.setFont(f);
			g2d.setColor(Color.WHITE);
			g2d.drawString(gameover, 130, 50);
		}
		
		else{
			g2d.setFont(f2);
			g2d.setColor(Color.WHITE);
			g2d.drawString(chargelevel, 10, 460);
			
	        if(paused){
	        	g2d.setFont(f);
	        	g2d.setColor(Color.WHITE);
	        	g2d.drawString(timeout, 30, 30);
	        }
	        
	        else{
	        	g2d.setFont(f);
	        	g2d.setColor(Color.WHITE);
	        	g2d.drawString(timeleft, 30, 30);
	        }
		}
		
	}
	
	class Countdown implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			if(count > 0){
				count--;
			}
			
			else if(!paused)
				pause();
			else play();
			
			timeleft = "Time: " + count;
		}
		
	}
	
	class Adapter extends KeyAdapter{
		
		public void keyPressed(KeyEvent e){
			
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_LEFT){
				direction = "faceLeft";
				dx = -1;
			}
			
			if(key == KeyEvent.VK_RIGHT){
				direction = "faceRight";
				dx = 1;
			}
			
			if(key == KeyEvent.VK_UP){
				direction = "faceUp";
				dy = -1;
			}
			
			if(key == KeyEvent.VK_DOWN){
				direction = "faceDown";
				dy = 1;
			}
			
			if(key == KeyEvent.VK_SPACE){
				if(!reloading)
					addLaser();
				reloading = true;
			}
			
			if(key == KeyEvent.VK_ENTER){
				playing = true;
				play();
				newEnemies();
			}
		}
		
		public void keyReleased(KeyEvent e){
			
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_LEFT){
				if(dx == -1){
					dx = 0;
				}
			}
			
			if(key == KeyEvent.VK_RIGHT){
				if(dx == 1){
					dx = 0;
				}
			}
			
			if(key == KeyEvent.VK_UP){
				if(dy == -1){
					dy = 0;
				}
			}
			
			if(key == KeyEvent.VK_DOWN){
				if(dy == 1){
					dy = 0;
				}
			}
			
			if(key == KeyEvent.VK_SPACE){
				reloading = false;
			}
		}
	}
}
