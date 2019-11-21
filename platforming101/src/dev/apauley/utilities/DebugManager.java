package dev.apauley.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import dev.apauley.entities.Entity;
import dev.apauley.general.Handler;
import dev.apauley.gfx.Assets;
import dev.apauley.gfx.Text;

/*
 * Handles all debugging
 */

public class DebugManager {

	private Handler handler;
	
	private Font fontHeader = Assets.fontRobotoRegular30;
	private Font fontStats = Assets.fontRobotoRegular20;
	
	//Sets transparency for BG rectangles
	private int alpha = 50;
	
	private boolean debugPlayer, debugSystem, debugRandom, debugBoundingBox;
	private ArrayList<Boolean> debugs;
	
	//spacing buffer
	private int spBf = 5;
	private int spBfGroupX = 360;
	
	//Bounding box light up size
	private int bbox = 5;
	
	/*STAT HEADER POSITIONS*/
	private int headX = spBf
			  , headYTop = fontHeader.getSize() - spBf
			  , headYBottom = fontHeader.getSize() - spBf + 480;
	
  		
	public DebugManager(Handler handler) {
		this.handler = handler;
		debugs = new ArrayList<Boolean>();
		debugs.add(debugPlayer);
		debugs.add(debugSystem);
	}
	
	//Draw Bounding box in your specified color for each side you list as TRUE
	public void drawBoundingBoxPlayer(Graphics g, boolean top, boolean bottom, boolean left, boolean right, Color c) {

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
	
	//Draw Bounding box for creatures in your specified color for each side you list as TRUE
	public void drawBoundingBox(Graphics g, Entity e, boolean top, boolean bottom, boolean left, boolean right, Color c) {

		g.setColor(c);
		
		if(top)
			g.fillRect((int) (e.getX() + e.getBounds().x - handler.getGameCamera().getxOffset())
					 , (int) (e.getY() + e.getBounds().y - handler.getGameCamera().getyOffset())
					 , e.getBounds().width, bbox);
		if(bottom)
			g.fillRect((int) (e.getX() + e.getBounds().x - handler.getGameCamera().getxOffset())
					 , (int) (e.getY() + e.getBounds().y - handler.getGameCamera().getyOffset() + e.getBounds().height - bbox)
					 , e.getBounds().width, bbox);
		if(left)
			g.fillRect((int) (e.getX() + e.getBounds().x - handler.getGameCamera().getxOffset())
					 , (int) (e.getY() + e.getBounds().y - handler.getGameCamera().getyOffset())
					 , bbox, e.getBounds().height);
		if(right)
			g.fillRect((int) (e.getX() + e.getBounds().x - handler.getGameCamera().getxOffset() + e.getBounds().width - bbox)
					 , (int) (e.getY() + e.getBounds().y - handler.getGameCamera().getyOffset())
					 , bbox, e.getBounds().height);
	}

	//Draw smaller facing box in your specified color for each side you list as TRUE
	public void drawFacingBox(Graphics g, boolean top, boolean bottom, boolean left, boolean right, Color c) {

		g.setColor(c);
		
		if(top)
			g.fillRect((int) (handler.getWorld().getEntityManager().getPlayer().getX() + handler.getWorld().getEntityManager().getPlayer().getBounds().x - handler.getGameCamera().getxOffset() + bbox)
					 , (int) (handler.getWorld().getEntityManager().getPlayer().getY() + handler.getWorld().getEntityManager().getPlayer().getBounds().y - handler.getGameCamera().getyOffset() + bbox)
					 , handler.getWorld().getEntityManager().getPlayer().getBounds().width - bbox * 2, bbox * 2);
		if(bottom)
			g.fillRect((int) (handler.getWorld().getEntityManager().getPlayer().getX() + handler.getWorld().getEntityManager().getPlayer().getBounds().x - handler.getGameCamera().getxOffset() + bbox)
					 , (int) (handler.getWorld().getEntityManager().getPlayer().getY() + handler.getWorld().getEntityManager().getPlayer().getBounds().y - handler.getGameCamera().getyOffset() + handler.getWorld().getEntityManager().getPlayer().getBounds().height - bbox - bbox * 2)
					 , handler.getWorld().getEntityManager().getPlayer().getBounds().width - bbox * 2, bbox * 2);
		if(left)
			g.fillRect((int) (handler.getWorld().getEntityManager().getPlayer().getX() + handler.getWorld().getEntityManager().getPlayer().getBounds().x - handler.getGameCamera().getxOffset() + bbox)
					 , (int) (handler.getWorld().getEntityManager().getPlayer().getY() + handler.getWorld().getEntityManager().getPlayer().getBounds().y - handler.getGameCamera().getyOffset() + bbox)
					 , bbox * 2, handler.getWorld().getEntityManager().getPlayer().getBounds().height - bbox * 2);
		if(right)
			g.fillRect((int) (handler.getWorld().getEntityManager().getPlayer().getX() + handler.getWorld().getEntityManager().getPlayer().getBounds().x - handler.getGameCamera().getxOffset() + handler.getWorld().getEntityManager().getPlayer().getBounds().width - bbox - bbox * 2)
					 , (int) (handler.getWorld().getEntityManager().getPlayer().getY() + handler.getWorld().getEntityManager().getPlayer().getBounds().y - handler.getGameCamera().getyOffset() + bbox)
					 , bbox * 2, handler.getWorld().getEntityManager().getPlayer().getBounds().height - bbox * 2);
	}

	//Takes in the horizontal int and converts it based on input parameters
	public int getStX(int mult) {
		return headX + spBfGroupX * mult;
	}

	//Takes in the vertical int and converts it based on input parameters
	public int getStY(int mult, String tb) {
		if(tb == "T") //Top
			return headYTop + fontStats.getSize() * mult + spBf;
		if(tb == "B") //Bottom
			return headYBottom + fontStats.getSize() * mult + spBf;
		return -1; //Error
	}

	public void Render(Graphics g) {

		//Only Draw if DebugBoundingBox = True
		if(debugBoundingBox) {
		
			//Draw Bounding boxes around all entities
			for(Entity e : handler.getWorld().getEntityManager().getEntities()) {
				Color c1 = Color.BLACK
					, c2 = Color.WHITE;
				if(e.getName() == "PLAYER") {
					c1 = Color.GREEN;
				    c2 = Color.YELLOW;
				} else if(e.getName() == "BULLET") {
					c1 = Color.PINK;
				    c2 = Color.ORANGE;
				} else if(e.getName() == "ENEMY") {
					c1 = Color.BLUE;
				    c2 = Color.WHITE;
				}
				drawBoundingBox(g, e, true, true, true, true, c1);

			//Light up Small Box when colliding with tile
			drawBoundingBoxPlayer(g, handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileTop()
					      , handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileBottom()
					      , handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileLeft()
					      , handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileRight()
					      , Color.YELLOW);
	
			//Light up Smaller Box with direction facing
			drawFacingBox(g, handler.getWorld().getEntityManager().getPlayer().isFaceTop()
					      , handler.getWorld().getEntityManager().getPlayer().isFaceBottom()
					      , handler.getWorld().getEntityManager().getPlayer().isFaceLeft()
					      , handler.getWorld().getEntityManager().getPlayer().isFaceRight()
					      , Color.CYAN);
			}

			//Light up Bounding Box with collision
			drawBoundingBoxPlayer(g, handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileTop()
					      , handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileBottom()
					      , handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileLeft()
					      , handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileRight()
					      , Color.YELLOW);
	
			//Light up Smaller Box with direction facing
			drawFacingBox(g, handler.getWorld().getEntityManager().getPlayer().isFaceTop()
					      , handler.getWorld().getEntityManager().getPlayer().isFaceBottom()
					      , handler.getWorld().getEntityManager().getPlayer().isFaceLeft()
					      , handler.getWorld().getEntityManager().getPlayer().isFaceRight()
					      , Color.CYAN);
		}

		//Only Draw if DebugSystem = True
		if(debugSystem) {
			
			//Draw System Debug to Screen
			Color color =  new Color(247,95,30); //Orange
			int x = 0, i = 1;

			//Draw Transparent BG Rectangle
			g.setColor(new Color(247,95,30,alpha));
			g.fillRect(0, 0, handler.getPhaseManager().getCurrentPhaseNameLength(), 32 + 21 * 6); //getCurrentPhaseNameLength() is used to keep this variable based on name

			//Draw Text to screen
			Text.drawStringShadow(g, "System", getStX(x), headYTop, false, color, fontHeader);
			Text.drawStringShadow(g, "Phase: " + handler.getPhaseManager().getCurrentPhase() + " - " + handler.getPhaseManager().getCurrentPhaseName(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "Width: " + handler.getGame().getWidth(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "Height: " + handler.getGame().getHeight(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "FPS: " + handler.getGame().getFpsTicks(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "State: " + handler.getGame().getStateName(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "Entity #: " + handler.getWorld().getEntityManager().getEntities().size(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
		}

		//Only Draw if DebugPlayer = True
		if(debugPlayer) {
			
			//Draw Player Debug to Screen
			Color color =  new Color(245,66,149); //Pink
			int x = 2, i = 1;

			//Draw Transparent BG Rectangle
			g.setColor(new Color(245,66,149,alpha));
			g.fillRect(720, 0, 180, 32 + 21 * 9);

			//Draw Text to screen
			Text.drawStringShadow(g, "Player", getStX(x), headYTop, false, color, fontHeader);
			Text.drawStringShadow(g, "X: " + handler.getWorld().getEntityManager().getPlayer().getX(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "Y: " + handler.getWorld().getEntityManager().getPlayer().getY(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "MoveX: " + handler.getWorld().getEntityManager().getPlayer().getxMove(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "MoveY: " + handler.getWorld().getEntityManager().getPlayer().getyMove(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "Speed: " + handler.getWorld().getEntityManager().getPlayer().getSpeed(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "Running: " + handler.getWorld().getEntityManager().getPlayer().isRunning(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "Jumping: " + handler.getWorld().getEntityManager().getPlayer().isJumping(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "Hangtime: " + handler.getWorld().getEntityManager().getPlayer().isHangtime(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "Can Jump: " + handler.getWorld().getEntityManager().getPlayer().isCanJump(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
//			Text.drawStringShadow(g, "EX-Collision: " + handler.getWorld().getEntityManager().getPlayer().checkEntityCollisions(handler.getWorld().getEntityManager().getPlayer().getxMove(), 0), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
//			Text.drawStringShadow(g, "EY-Collision: " + handler.getWorld().getEntityManager().getPlayer().checkEntityCollisions(0, handler.getWorld().getEntityManager().getPlayer().getyMove()), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
//			Text.drawStringShadow(g, "T-Collision: " + handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileTop(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
//			Text.drawStringShadow(g, "B-Collision: " + handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileBottom(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
//			Text.drawStringShadow(g, "L-Collision: " + handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileLeft(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
//			Text.drawStringShadow(g, "R-Collision: " + handler.getWorld().getEntityManager().getPlayer().isCollisionWithTileRight(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
//			Text.drawStringShadow(g, "T-Face: " + handler.getWorld().getEntityManager().getPlayer().isFaceTop(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
//			Text.drawStringShadow(g, "B-Face: " + handler.getWorld().getEntityManager().getPlayer().isFaceBottom(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
//			Text.drawStringShadow(g, "L-Face: " + handler.getWorld().getEntityManager().getPlayer().isFaceLeft(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;
//			Text.drawStringShadow(g, "R-Face: " + handler.getWorld().getEntityManager().getPlayer().isFaceRight(), getStX(x), getStY(i,"T"), false, color, fontStats); i++;		
		}
		
		//Only Draw if DebugRandom = True
		if(debugRandom) {
			
			//Draw System Debug to Screen
			Color color =  new Color(102,102,255); //Purplish Blue
			int x = 0, i = 1;

			//Draw Transparent BG Rectangle
			g.setColor(new Color(102,102,255,alpha));
			g.fillRect(0, headYBottom - 28, 185, 32 + 21 * 4 + 20); //Added +20 cause it doesn't really matter and want to fill to bottom (off screen is okay)

			//Draw Text to screen
			Text.drawStringShadow(g, "Random", getStX(x), headYBottom, false, color, fontHeader);
			Text.drawStringShadow(g, "xOffset: " + handler.getGameCamera().getxOffset(), getStX(x), getStY(i,"B"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "yOffset: " + handler.getGameCamera().getyOffset(), getStX(x), getStY(i,"B"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "xMouse: " + handler.getMouseManager().getMouseX(), getStX(x), getStY(i,"B"), false, color, fontStats); i++;
			Text.drawStringShadow(g, "yMouse: " + handler.getMouseManager().getMouseY(), getStX(x), getStY(i,"B"), false, color, fontStats); i++;
		}
		
	}
		
	/*************** GETTERS and SETTERS ***************/

	//set ALL debugs on/off
	public void setAllDebugs(boolean tf) {
		debugPlayer = tf;
		debugSystem = tf;
		debugRandom = tf;
		debugBoundingBox = tf;
	}

	//set ALL debugs on/off
	public void toggleAllDebugs() {
		boolean tf = isDebugSystem();
		setAllDebugs(false);
		debugSystem = !tf;
		debugPlayer = !tf;
		debugRandom = !tf;
		debugBoundingBox = !tf;
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
	public boolean isDebugRandom() {
		return debugRandom;
	}

	//set debugPlayer to true or false
	public void setDebugRandom(boolean debugRandom) {
		setAllDebugs(false);
		this.debugRandom = debugRandom;
	}

	//Toggles Debug Player to opposite (on >> off, off >> on)
	public void toggleDebugRandom() {
		boolean tf = isDebugRandom();
		setAllDebugs(false);
		debugRandom = !tf;
	}

	//Checks whether debugPlayer is true or false
	public boolean isDebugBoundingBox() {
		return debugBoundingBox;
	}

	//set debugPlayer to true or false
	public void setDebugBoundingBox(boolean debugBoundingBox) {
		setAllDebugs(false);
		this.debugBoundingBox = debugBoundingBox;
	}

	//Toggles Debug Player to opposite (on >> off, off >> on)
	public void toggleDebugBoundingBox() {
		boolean tf = isDebugBoundingBox();
		setAllDebugs(false);
		debugBoundingBox= !tf;
	}
	
	//Temp just for screen capture
	public void setDebugCapture() {
		debugSystem = true;
		debugPlayer = true;
		debugBoundingBox = true;
	}
	
}
