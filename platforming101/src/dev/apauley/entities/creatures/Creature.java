package dev.apauley.entities.creatures;

import dev.apauley.entities.Entity;
import dev.apauley.general.Handler;
import dev.apauley.tiles.Tile;

/*
 * The base shell for all Creatures in game
 */
public abstract class Creature extends Entity {

	//Gravity to level off jumps
	protected float gravityHangtime = handler.getGVar().get_DEFAULT_GRAVITY();
	protected int gravityHangTimeTick = 0;
	
	//Tracks max bullets an entity can fire
	protected int BULLET_MAX = 5;
	
	//Tracks whether player is running/walking or not
	protected boolean running, walking;
	
	//Tracks whether creature IS jumping (meaning still ascending
	protected boolean jumping, canJump;
	
	//Tracks creature jumping hangtime (whether should be floating after jumping)
	protected boolean hangtime;
		
	//Creature Constructor. Establishes some defaults
	public Creature(Handler handler, float x, float y, int width, int height, float xMove, float yMove, String name, String group) {
		super(handler, x,y, width, height, xMove, yMove, name, group);
		speed = handler.getGVar().getGSpeed();
		gravity = handler.getGVar().getGGravity();
		this.xMove = xMove;
		this.yMove = yMove;
		jumpTimer = jumpCooldown;

	}

	//Gravity on creatures
	public void gravity() {
		
		//if game is paused, don't check these collisions
		if(handler.getGVar().getGSpeed() == 0)
			return;
				
		//Update JumpTimer
		jumpTimer += System.currentTimeMillis() - lastJumpTimer;
		lastJumpTimer = System.currentTimeMillis();

		//check if can ready to fall yet yet
		if(jumpTimer < jumpCooldown)
			return;
		
		jumping = false;
		canJump = false;
		
		//Level off Gravity
//		if(gravityHangtime < handler.getGVar().getGGravity()) {
//			gravityHangtime += (0.1f * speed);
//			
//			//At middle point, level off player to imitate "hangtime"
//			if(gravityHangtime >= handler.getGVar().getGGravity() / 2 - (0.5f * speed) && gravityHangtime <= handler.getGVar().getGGravity() / 2 + (0.5f * speed)) {
//				gravityHangTimeTick++;
//				yMove += gravityHangtime - (0.1f * speed) * gravityHangTimeTick;
//				System.out.println(fullName + ": gravityHangtime: " + gravityHangtime);
//				System.out.println(fullName + ": gGravity: " + handler.getGVar().getGGravity());
//				System.out.println(fullName + ": Math: " + (gravityHangtime - (0.1f * speed) * gravityHangTimeTick));
//				System.out.println(fullName + ": yMove: " + yMove);
//
//			//Otherwise apply full gravityHangtime amount
//			} else {
//				yMove += gravityHangtime;
//				System.out.println("--------------------We made it!!--------------------");
//				System.out.println(fullName + ": gravityHangtime: " + gravityHangtime);
//				System.out.println(fullName + ": gGravity: " + handler.getGVar().getGGravity());
//				System.out.println(fullName + ": Math: " + (gravityHangtime - (0.1f * speed) * gravityHangTimeTick));
//				System.out.println(fullName + ": yMove: " + yMove);
//			}
//		
//		//Finally end hangtime to apply full gravity to player
//		} else {
//			hangtime = false;
//			System.out.println("--------------------Now We here!!--------------------");
//			System.out.println(fullName + ": gravityHangtime: " + gravityHangtime);
//			System.out.println(fullName + ": gGravity: " + handler.getGVar().getGGravity());
//			System.out.println(fullName + ": Math: " + (gravityHangtime - (0.1f * speed) * gravityHangTimeTick));
//			System.out.println(fullName + ": yMove: " + yMove);
//		}
		
		//Temp code because of the above (need player to fall)
		hangtime = false;
		
		//only allow gravity on Phase >= 3
		if(handler.getPhaseManager().getCurrentPhase() < 3) {
			yMove = 0;
			return;		
		}

		//if player is jumping and not in hangtime, apply gravity
		if(yMove <= 0 && !hangtime) {
			yMove = handler.getGVar().getGGravity();
//			System.out.println("--------------------Final destination?--------------------");
//			System.out.println(fullName + ": gravityHangtime: " + gravityHangtime);
//			System.out.println(fullName + ": gGravity: " + handler.getGVar().getGGravity());
//			System.out.println(fullName + ": Math: " + (gravityHangtime - (0.1f * speed) * gravityHangTimeTick));
//			System.out.println(fullName + ": yMove: " + yMove);
		}
	}
	
	//Moves creature using helpers
	public void move() {
		
		//Check all collisions
		/*X coordinate of creature, + where you want to move to, + x bound offset, 
		 * + bounds width since moving right and checking right side
		 */

		//Right Collision Check
		int tx = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH;
		if(!collisionWithTile(tx, (int) (y + bounds.y)/ Tile.TILEHEIGHT) && !collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT))
			collisionWithTileRight = false;
		else
			collisionWithTileRight = true;
		
		//Left Collision Check
		tx = (int) (x + xMove + bounds.x) / Tile.TILEWIDTH;
		if(!collisionWithTile(tx, (int) (y + bounds.y)/ Tile.TILEHEIGHT) && !collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT))
			collisionWithTileLeft = false;
		else
			collisionWithTileLeft = true;

		//Top Collision Check
		int ty = (int) (y + yMove + bounds.y) / Tile.TILEHEIGHT;
		if(!collisionWithTile((int) (x + bounds.x)/ Tile.TILEWIDTH, ty) && !collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, ty))
			collisionWithTileTop = false;
		else
			collisionWithTileTop = true;
		
		//Bottom Collision Check
		ty = (int) (y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT;
		if(!collisionWithTile((int) (x + bounds.x)/ Tile.TILEWIDTH, ty) && !collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, ty))
			collisionWithTileBottom = false;
		else {
			collisionWithTileBottom = true;
			canJump = true;
		}

		//Make sure xMove and yMove are not greater than tile size (so it doesn't overshoot)
		if(xMove > Tile.TILEWIDTH)
			xMove = Tile.TILEWIDTH / 2;
		if(xMove < -Tile.TILEWIDTH)
			xMove = -Tile.TILEWIDTH / 2;
		if(yMove > Tile.TILEHEIGHT)
			yMove = Tile.TILEHEIGHT / 2;
		if(yMove < -Tile.TILEHEIGHT)
			yMove = -Tile.TILEHEIGHT / 2;
		
		//If no collision, movement is allowed, otherwise stop
		if(!checkEntityCollisions(xMove, 0f))
			moveX();
		if(!checkEntityCollisions(0f, yMove))
			moveY();		
	}

	//Instead of moving both x and y in same move method, creating separate
	//move methods for x and y
	public void moveX() {
		
		//Moving right
		if(xMove > 0) {

			//Set facing right = true
			setFaceRight(true);
			
			int tx = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH;
			
			/*Check the tile upper right is moving in to, and lower right is moving in to
			 * If both tiles are NOT solid (thus ! in front of collision method), then go ahead and move!
			 */
			if(!collisionWithTileRight) {
				x += xMove;

				if(name.equals("PLAYER")) {
					
					handler.getGame().getStatTracker().setAvgSpeedNumerator(handler.getGame().getStatTracker().getAvgSpeedNumerator() + Math.abs(speed));
					handler.getGame().getStatTracker().setAvgSpeedDenomenator(handler.getGame().getStatTracker().getAvgSpeedDenomenator() + 1);
					handler.getGame().getStatTracker().setAvgSpeed(handler.getGame().getStatTracker().getAvgSpeedNumerator() / handler.getGame().getStatTracker().getAvgSpeedDenomenator());
					
					if(walking)
						handler.getGame().getStatTracker().setPlayerWalkDistance(handler.getGame().getStatTracker().getPlayerWalkDistance() + Math.abs(xMove));
					if(running)
						handler.getGame().getStatTracker().setPlayerRunDistance(handler.getGame().getStatTracker().getPlayerRunDistance() + Math.abs(xMove));
					if(jumping || hangtime)
						handler.getGame().getStatTracker().setJumpDistanceX(handler.getGame().getStatTracker().getJumpDistanceX() + Math.abs(xMove));
					if(!jumping && !hangtime && !canJump)
						handler.getGame().getStatTracker().setFallDistanceX(handler.getGame().getStatTracker().getFallDistanceX() + Math.abs(xMove));
				}
			}
			else
				//move player as close to the tile as possible without being inside of it
				//Note: We add a 1-pixel gap which allows the player to "slide" and not get stuck along the boundaries
				x = tx * Tile.TILEWIDTH - bounds.x - bounds.width - 1;
			
		//Moving left
		}else if(xMove < 0) {

			//Set facing left = true
			setFaceLeft(true);
			
			int tx = (int) (x + xMove + bounds.x) / Tile.TILEWIDTH;
			
			//Same check
			if(!collisionWithTileLeft) {
				x += xMove;

				if(name.equals("PLAYER")) {
					
					handler.getGame().getStatTracker().setAvgSpeedNumerator(handler.getGame().getStatTracker().getAvgSpeedNumerator() + Math.abs(speed));
					handler.getGame().getStatTracker().setAvgSpeedDenomenator(handler.getGame().getStatTracker().getAvgSpeedDenomenator() + 1);
					handler.getGame().getStatTracker().setAvgSpeed(handler.getGame().getStatTracker().getAvgSpeedNumerator() / handler.getGame().getStatTracker().getAvgSpeedDenomenator());

					if(walking)
						handler.getGame().getStatTracker().setPlayerWalkDistance(handler.getGame().getStatTracker().getPlayerWalkDistance() + Math.abs(xMove));
					if(running)
						handler.getGame().getStatTracker().setPlayerRunDistance(handler.getGame().getStatTracker().getPlayerRunDistance() + Math.abs(xMove));
					if(jumping || hangtime)
						handler.getGame().getStatTracker().setJumpDistanceX(handler.getGame().getStatTracker().getJumpDistanceX() + Math.abs(xMove));
					if(!jumping && !hangtime && !canJump)
						handler.getGame().getStatTracker().setFallDistanceX(handler.getGame().getStatTracker().getFallDistanceX() + Math.abs(xMove));
				}
			}
			else
				//move player as close to the tile as possible without being inside of it
				//Note: We weirdly don't have to add a 1-pixel gap for "sliding" to not get stuck along the boundaries. Don't ask me why...
				x = tx * Tile.TILEWIDTH + Tile.TILEWIDTH - bounds.x;
		} 
			
	}
	
	public void moveY() {
		
		//Moving up
		if(yMove < 0) {
			
			int ty = (int) (y + yMove + bounds.y) / Tile.TILEHEIGHT;

			if(!collisionWithTileTop) {
				y += yMove;
				
				if(name.equals("PLAYER")) {
					if(jumping || hangtime)
						handler.getGame().getStatTracker().setJumpDistanceY(handler.getGame().getStatTracker().getJumpDistanceY() + Math.abs(yMove));
					if(!jumping && !hangtime && !canJump)
						handler.getGame().getStatTracker().setFallDistanceY(handler.getGame().getStatTracker().getFallDistanceY() + Math.abs(yMove));
					}
			}
			else
				//move player as close to the tile as possible without being inside of it
				y = ty * Tile.TILEHEIGHT + Tile.TILEHEIGHT - bounds.y;
			
		//Moving down
		}else if(yMove > 0) {
			
			int ty = (int) (y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT;
			
			if(!collisionWithTileBottom) {
				y += yMove;

				if(name.equals("PLAYER")) {
					if(jumping || hangtime)
						handler.getGame().getStatTracker().setJumpDistanceY(handler.getGame().getStatTracker().getJumpDistanceY() + Math.abs(yMove));
					if(!jumping && !hangtime && !canJump)
						handler.getGame().getStatTracker().setFallDistanceY(handler.getGame().getStatTracker().getFallDistanceY() + Math.abs(yMove));
				}
			}
			else
				//move player as close to the tile as possible without being inside of it
				//Note: We add a 1-pixel gap which allows the player to "slide" and not get stuck along the boundaries
				y = ty * Tile.TILEHEIGHT - bounds.y - bounds.height - 1;
		}
	}
	
	//Takes in a tile array coordinate x/y and returns if that tile is solid
	protected boolean collisionWithTile(int x, int y) {

		//only allow collisions on Phase >= 4
		if(handler.getPhaseManager().getCurrentPhase() < 4)
			return false;
								
		return handler.getWorld().getTile(x,y).isSolid();
	}
	
	//Increase speed if running
	public void run() {
		running = true;
		walking = false;
		speed = handler.getGVar().getGSpeed() * 2; //Double speed
	}
	
	//Decrease speed if walking
	public void walk() {
		walking = true;
		running = false;
		speed = handler.getGVar().getGSpeed(); //Return speed to normal
	}
	
	//Things that happen to ALL creatures every tick
	public void tick() {

		flash();
	}

	/*************** GETTERS and SETTERS ***************/

	//Gets creature HP
	public int getHealth() {
		return health;
	}

	//Sets creature HP
	public void setHealth(int health) {
		this.health = health;
	}

	//Gets creature yMovement
	public float getyMove() {
		return yMove;
	}

	//Sets creature yMovement
	public void setYMove(float yMove) {
		this.yMove = yMove;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean isHangtime() {
		return hangtime;
	}

	public void setHangtime(boolean hangtime) {
		this.hangtime = hangtime;
	}

	public boolean isCanJump() {
		return canJump;
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isWalking() {
		return walking;
	}

	public void setWalking(boolean walking) {
		this.walking = walking;
	}

	public int getBULLET_MAX() {
		return BULLET_MAX;
	}

	public void setBULLET_MAX(int bULLET_MAX) {
		BULLET_MAX = bULLET_MAX;
	}

}
