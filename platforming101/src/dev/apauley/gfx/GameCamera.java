package dev.apauley.gfx;

import dev.apauley.entities.Entity;
import dev.apauley.general.Handler;
import dev.apauley.tiles.Tile;

/*
 * Tracks and follows player (or more accurately moves all tiles as player "moves" to appear like there is movement)
 */

public class GameCamera {

	//main game object
	private Handler handler;
	
	//the amount we move tiles on the x and y axis in relation to original position
	private float xOffset, yOffset;
	
	public GameCamera(Handler handler, float xOffset, float yOffset) {
		this.handler = handler;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	//Checks if camera is showing any blank space. If it is, will fix it.
	public void checkBlankSpace() {
		
		//Add +/- Tile.TILEWIDTH from below because we want to hide the "wall" that keeps the player in.
		if(xOffset < 0) {
			xOffset = 0;
		} else if(xOffset > handler.getWorld().getWidth() * Tile.TILEWIDTH - handler.getWidth()) {
			xOffset = handler.getWorld().getWidth() * Tile.TILEWIDTH - handler.getWidth();
		}
		if(yOffset < 0) {
			yOffset = 0;
		} else if(yOffset > handler.getWorld().getHeight() * Tile.TILEHEIGHT - handler.getHeight()) {
			yOffset = handler.getWorld().getHeight() * Tile.TILEHEIGHT - handler.getHeight();
		}
	}

	//Centers camera on target entity utilizing x and y offsets
	public void centerOnEntity(Entity e) {
		
		//We divide by 2 so centered on screen (and not on edge) and centered on entity
		xOffset = e.getX() - handler.getWidth() / 2 + e.getWidth() / 2;
		
		//Only do this is multiplier is above 1, otherwise it starts off too high (the camera)
		//if(GVar.getMultiplier() > 1)
			//yOffset = e.getY() - game.getHeight() / 2 + e.getHeight() / 2 + Tile.TILEHEIGHT * GVar.getMultiplier();
		yOffset = e.getY() - handler.getHeight() / 2 + e.getHeight() / 2;
		
		//Checks if camera is showing any blank space. If it is, will fix it. 
		checkBlankSpace();			
	}

	//Takes inputs and adds them to the associated offset variables respectively
	public void move(float xAmt, float yAmt) {
		xOffset += xAmt;
		yOffset += yAmt;		

		//Checks if camera is showing any blank space. If it is, will fix it. 
		checkBlankSpace();			
	}
	
	//Resets GameCamera
	public void resetGameCamera() {
		xOffset = 0;
		yOffset = 0;
	}
	
	/*************** GETTERS and SETTERS ***************/
	
	//Gets X Offset
	public float getxOffset() {
		return xOffset;
	}

	//Sets X Offset
	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	//Gets Y Offset
	public float getyOffset() {
		return yOffset;
	}

	//Sets Y Offset
	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}
	
}
