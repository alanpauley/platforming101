package dev.apauley.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import dev.apauley.general.Handler;
import dev.apauley.gfx.Assets;
import dev.apauley.gfx.Text;

/*
 * Handles all debugging
 */

public class DebugManager {

	private Handler handler;
	
	private Font fontHeader = Assets.fontRobotoRegular40;
	private Font fontStats = Assets.fontRobotoRegular30;
	
	private boolean debugPlayer, debugSystem;
	private ArrayList<Boolean> debugs;
	
	//spacing buffer
	private int spBf = 5;
	private int spBfGroupX = 300;
	
	//Bounding box light up size
	private int bbox = 5;
	
	/*STAT HEADER POSITIONS*/
	private int headX = spBf
			  , headY = fontHeader.getSize() - spBf;
  		
	public DebugManager(Handler handler) {
		this.handler = handler;
		debugs = new ArrayList<Boolean>();
		debugs.add(debugPlayer);
		debugs.add(debugSystem);
	}
	
	//Draw Bounding box in your specified color for each side you list as TRUE
	public void drawBoundingBox(Graphics g, boolean top, boolean bottom, boolean left, boolean right, Color c) {

		g.setColor(c);
		
		if(top)
			g.fillRect((int) (handler.getWorld().getEntityManager().getPlayer().getX() + handler.getWorld().getEntityManager().getPlayer().getBounds().x - handler.getGameCamera().getxOffset())
					 , (int) (handler.getWorld().getEntityManager().getPlayer().getY() + handler.getWorld().getEntityManager().getPlayer().getBounds().y - handler.getGameCamera().getyOffset())
					 , handler.getWorld().getEntityManager().getPlayer().getBounds().width, bbox);
		if(bottom)
			g.fillRect((int) (handler.getWorld().getEntityManager().getPlayer().getX() + handler.getWorld().getEntityManager().getPlayer().getBounds().x - handler.getGameCamera().getxOffset())
					 , (int) (handler.getWorld().getEntityManager().getPlayer().getY() + handler.getWorld().getEntityManager().getPlayer().getBounds().y - handler.getGameCamera().getyOffset() + handler.getWorld().getEntityManager().getPlayer().getBounds().height - bbox)
					 , handler.getWorld().getEntityManager().getPlayer().getBounds().width, bbox);
		if(left)
			g.fillRect((int) (handler.getWorld().getEntityManager().getPlayer().getX() + handler.getWorld().getEntityManager().getPlayer().getBounds().x - handler.getGameCamera().getxOffset())
					 , (int) (handler.getWorld().getEntityManager().getPlayer().getY() + handler.getWorld().getEntityManager().getPlayer().getBounds().y - handler.getGameCamera().getyOffset())
					 , bbox, handler.getWorld().getEntityManager().getPlayer().getBounds().height);
		if(right)
			g.fillRect((int) (handler.getWorld().getEntityManager().getPlayer().getX() + handler.getWorld().getEntityManager().getPlayer().getBounds().x - handler.getGameCamera().getxOffset() + handler.getWorld().getEntityManager().getPlayer().getBounds().width - bbox)
					 , (int) (handler.getWorld().getEntityManager().getPlayer().getY() + handler.getWorld().getEntityManager().getPlayer().getBounds().y - handler.getGameCamera().getyOffset())
					 , bbox, handler.getWorld().getEntityManager().getPlayer().getBounds().height);
	}
	
	//Takes in the horizontal int and converts it based on input parameters
	public int getStX(int mult) {
		return headX + spBfGroupX * mult;
	}

	//Takes in the vertical int and converts it based on input parameters
	public int getStY(int mult) {
		return headY + fontStats.getSize() * mult + spBf;
	}

	public void Render(Graphics g) {

		//Only Draw if DebugPlayer = True
		if(debugPlayer) {
		
			//Draw player Bounding Box
			drawBoundingBox(g, true, true, true, true, Color.GREEN);
	
			//Light up Bounding Box with collision
			drawBoundingBox(g, handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileTop()
					      , handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileBottom()
					      , handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileLeft()
					      , handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileRight()
					      , Color.YELLOW);
	
			//Draw Player Debug to Screen
			Color color =  new Color(245,66,149); //Pink
			int x = 0;
			Text.drawStringShadow(g, "Player", getStX(x), headY, false, color, fontHeader);
			Text.drawStringShadow(g, "X: " + handler.getWorld().getEntityManager().getPlayer().getX(), getStX(x), getStY(1), false, color, fontStats);
			Text.drawStringShadow(g, "Y: " + handler.getWorld().getEntityManager().getPlayer().getY(), getStX(x), getStY(2), false, color, fontStats);
			Text.drawStringShadow(g, "MoveX: " + handler.getWorld().getEntityManager().getPlayer().getxMove(), getStX(x), getStY(3), false, color, fontStats);
			Text.drawStringShadow(g, "MoveY: " + handler.getWorld().getEntityManager().getPlayer().getyMove(), getStX(x), getStY(4), false, color, fontStats);
			Text.drawStringShadow(g, "Hangtime: " + handler.getWorld().getEntityManager().getPlayer().isHangtime(), getStX(x), getStY(5), false, color, fontStats);
			Text.drawStringShadow(g, "T-Collision: " + handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileTop(), getStX(x), getStY(6), false, color, fontStats);
			Text.drawStringShadow(g, "B-Collision: " + handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileBottom(), getStX(x), getStY(7), false, color, fontStats);
			Text.drawStringShadow(g, "L-Collision: " + handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileLeft(), getStX(x), getStY(8), false, color, fontStats);
			Text.drawStringShadow(g, "R-Collision: " + handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileRight(), getStX(x), getStY(9), false, color, fontStats);
			Text.drawStringShadow(g, "EX-Collision: " + handler.getWorld().getEntityManager().getPlayer().checkEntityCollisions(handler.getWorld().getEntityManager().getPlayer().getxMove(), 0), getStX(x), getStY(10), false, color, fontStats);
			Text.drawStringShadow(g, "EY-Collision: " + handler.getWorld().getEntityManager().getPlayer().checkEntityCollisions(0, handler.getWorld().getEntityManager().getPlayer().getyMove()), getStX(x), getStY(11), false, color, fontStats);
		
		}
		
		//Only Draw if DebugSystem = True
		if(debugSystem) {
			
			//Draw System Debug to Screen
			Color color =  new Color(247,95,30); //Orange
			int x = 1;
			Text.drawStringShadow(g, "System", getStX(x), headY, false, color, fontHeader);
			Text.drawStringShadow(g, "Width: " + handler.getGame().getWidth(), getStX(x), getStY(1), false, color, fontStats);
			Text.drawStringShadow(g, "Height: " + handler.getGame().getHeight(), getStX(x), getStY(2), false, color, fontStats);
			Text.drawStringShadow(g, "FPS: " + handler.getGame().getFpsTicks(), getStX(x), getStY(3), false, color, fontStats);
			Text.drawStringShadow(g, "State: " + handler.getGame().getStateName(), getStX(x), getStY(4), false, color, fontStats);		
		}
		
	}
		
	/*************** GETTERS and SETTERS ***************/

	//set ALL debugs on/off
	public void setAllDebugs(boolean tf) {
		debugPlayer = tf;
		debugSystem = tf;
	}

	//set ALL debugs on/off
	public void toggleAllDebugs() {
		boolean tf = isDebugSystem();
		setAllDebugs(false);
		debugSystem = !tf;
		debugPlayer = !tf;
	}

	//Checks whether debugSystem is true or false
	public boolean isDebugSystem() {
		return debugSystem;
	}

	//set debugSystem to true or false
	public void setDebugSystem(boolean debugSystem) {
		setAllDebugs(false);
		this.debugSystem = debugSystem;
	}

	//Toggles Debug System to opposite (on >> off, off >> on)
	public void toggleDebugSystem() {
		boolean tf = isDebugSystem();
		setAllDebugs(false);
		debugSystem = !tf;
	}
	
	//Checks whether debugPlayer is true or false
	public boolean isDebugPlayer() {
		return debugPlayer;
	}

	//set debugPlayer to true or false
	public void setDebugPlayer(boolean debugPlayer) {
		setAllDebugs(false);
		this.debugPlayer = debugPlayer;
	}

	//Toggles Debug Player to opposite (on >> off, off >> on)
	public void toggleDebugPlayer() {
		boolean tf = isDebugPlayer();
		setAllDebugs(false);
		debugPlayer = !tf;
	}
	
}