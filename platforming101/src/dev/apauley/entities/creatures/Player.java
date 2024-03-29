package dev.apauley.entities.creatures;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.sun.org.apache.xalan.internal.xsltc.dom.EmptyFilter;

import dev.apauley.entities.Entity;
import dev.apauley.entities.statics.Tree;
import dev.apauley.general.Handler;
import dev.apauley.gfx.Animation;
import dev.apauley.gfx.Assets;
import dev.apauley.gfx.Text;
import dev.apauley.inventory.Inventory;

/*
 * The player that our users will control.
 */

public class Player extends Creature{

	//Animations
	private Animation animJump, animCrouch, animRight, animLeft;
	private int animSpeed = 500;
	
	//Tracks ammo in gun
	private int ammo = BULLET_MAX;
	
	//Indicates gun is empty
	private int emptyGunTimer_DEFAULT = 25, emptyGunTimer = 0;
	
	//Player Inventory
	private Inventory inventory;
	
	public Player(Handler handler, float x, float y) {
		super(handler, x, y, handler.getGVar().get_DEFAULT_CREATURE_WIDTH(), handler.getGVar().get_DEFAULT_CREATURE_HEIGHT(), 0, 0, "PLAYER", "PLAYER");

		DEFAULT_HEALTH = DEFAULT_HEALTH * 2;
		health = DEFAULT_HEALTH;
		
		//Boundary box for player
		bounds.x = 1;
		bounds.y = 1;
		bounds.width = handler.getGVar().get_DEFAULT_CREATURE_WIDTH() - 2;
		bounds.height = handler.getGVar().get_DEFAULT_CREATURE_HEIGHT() - 2;
		
		if(handler.getWorld() != null) {
			//Rename player with number appended based on playerCount
			id = handler.getWorld().getEntityManager().getPlayerCount();

			//Increment player count
			handler.getWorld().getEntityManager().setPlayerCount(handler.getWorld().getEntityManager().getPlayerCount() + 1);
		} else {
			id=0;
		}

		fullName = name + id;
		
		//Animations
//		animJump 	= new Animation(animSpeed, Assets.player_jump);
//		animCrouch 	= new Animation(animSpeed, Assets.player_crouch);
//		animRight 	= new Animation(animSpeed, Assets.player_right);
//		animLeft 	= new Animation(animSpeed, Assets.player_left);
		
		//Set up attackCooldowns
		attackCooldown = (long) (handler.getGVar().getCooldownDefault() * speed * 2);
		attackTimer = attackCooldown;

		//Inventory
		inventory = new Inventory(handler);
	}

	@Override
	public void tick() {
		super.tick();

		//decrement emptyGunTimer if > 0
		if(handler.getPhaseManager().getCurrentPhase() > 18 && emptyGunTimer > 0)
			emptyGunTimer--;
		
		if(handler.getPhaseManager().getCurrentPhase() > 20) {
			if(!active)
				return;
		}
		
		//Animations
//		animJump.tick();
//		animCrouch.tick();
//		animRight.tick();
//		animLeft.tick();

		//Gets movement using speed
		getInput();		

		//Gravity's effect on the player
		gravity();

		//Sets position using movement
		move();
		
		//Attack
		checkAttacks();
		
		//Shoot
		shoot();
		
		//only allow camera to follow player (and subsequent code) on Phase > x
		if(handler.getPhaseManager().getCurrentPhase() < 9)
			return;		
		
		//Centers camera on player
		handler.getGameCamera().centerOnEntity(this);
		
		//Inventory
		inventory.tick();
	}
	
	//When player shoots,bullets are created
	public void shoot() {
	
		//only allow attacks on Phase > x
		if(handler.getPhaseManager().getCurrentPhase() < 7)
			return;		
				
		//if Gun is empty, cannot shoot (old way)
		if(bulletsFired >= BULLET_MAX)
			return;
	
		//Generate bullets from player if left mouse is clicked
		//if(handler.getMouseManager().isLeftPressed() && handler.getWorld().getEntityManager().getEntities().size() < 30) { //Used to only allow unlimited bullets per click
		if(handler.getMouseManager().keyJustPressed(MouseEvent.BUTTON1)) { //Used to only allow one bullet per click
			//if game is paused, don't check further
			if(handler.getGVar().getGSpeed() == 0)
				return;
					
			//if Gun is empty, cannot shoot (new way): Display *RELOAD* to indicate
			if(handler.getPhaseManager().getCurrentPhase() > 18 && ammo <= 0) {
				
				//Set emptyGunTimer to start decrimenting and display for only that long
				emptyGunTimer = emptyGunTimer_DEFAULT;

				return;
			}
			
			float xMove = 10f;
			float yMove = 0f;
			
			//Get player direction(s) to get yMoves of bullets (don't need left and right since that's implied in mouse click side)
			if(handler.getWorld().getEntityManager().getPlayer().isFaceTop())
				yMove = -10f;
			if(handler.getWorld().getEntityManager().getPlayer().isFaceBottom())
				yMove = 10f;
					
			//if mouse.X > player.X, put on right of player
			if(handler.getMouseManager().getMouseX() + handler.getGameCamera().getxOffset() > handler.getWorld().getEntityManager().getPlayer().getX()) {
				handler.getWorld().getEntityManager().getEntitiesLimbo().add(new Bullet(handler, handler.getWorld().getEntityManager().getPlayer().getX() + handler.getWorld().getEntityManager().getPlayer().getWidth()
																				  , handler.getWorld().getEntityManager().getPlayer().getY() + handler.getWorld().getEntityManager().getPlayer().getHeight() / 2 - Assets.obj1.getHeight()/3, xMove, yMove, Assets.yellow, "BULLET", group, this));
				handler.getWorld().getEntityManager().getPlayer().setFaceRight(true);
				handler.getWorld().getEntityManager().getPlayer().setFaceLeft(false);
				
			//otherwise, put on left of player
			} else {
				handler.getWorld().getEntityManager().getEntitiesLimbo().add(new Bullet(handler, handler.getWorld().getEntityManager().getPlayer().getX() - Assets.obj1.getWidth()/2 //Not sure why * 2 tbh
																				  , handler.getWorld().getEntityManager().getPlayer().getY() + handler.getWorld().getEntityManager().getPlayer().getHeight() / 2 - Assets.obj1.getHeight()/3, -xMove, yMove, Assets.yellow, "BULLET", group, this));
				handler.getWorld().getEntityManager().getPlayer().setFaceLeft(true);
				handler.getWorld().getEntityManager().getPlayer().setFaceRight(false);
			}
			
			//remove bullet from gun
			if(handler.getPhaseManager().getCurrentPhase() > 18) {
				bulletsFired++;
				ammo--;
			}

		}
	}	
	
	//Check is user is pressing attacking key. If so, generate attack
	public void checkAttacks() {
		
		//only allow attacks on Phase > x
		if(handler.getPhaseManager().getCurrentPhase() < 5)
			return;		
		
		//Update AttackTimer
		attackTimer += System.currentTimeMillis() - lastAttackTimer;
		lastAttackTimer = System.currentTimeMillis();
		
		//check if can attack yet
		if(attackTimer < attackCooldown)
			return;
		
		//Player cannot attack while in inventory
		if(inventory.isActive())
			return;
		
		//collision bounds rectangle (Gets coordinates for collision box of player)
		Rectangle cb = getCollisionBounds(0, 0);
		
		//attack rectangle
		Rectangle ar = new Rectangle();
		
		//How close the player needs to be to hit target
		int arSize = 20;
		ar.width = arSize;
		ar.height = arSize;
		
		if(handler.getKeyManager().aUp) {
			ar.x = cb.x + cb.width / 2 - arSize / 2; //Could just do cb.x BUT if you ever want attack that is a different size, this code is more flexible to always be in the right place
			ar.y = cb.y + - arSize;
		}else if(handler.getKeyManager().aDown) {
			ar.x = cb.x + cb.width / 2 - arSize / 2; //Could just do cb.x BUT if you ever want attack that is a different size, this code is more flexible to always be in the right place
			ar.y = cb.y + cb.height;
		}else if(handler.getKeyManager().aLeft) {
			ar.x = cb.x - arSize; 
			ar.y = cb.y + cb.height / 2 - arSize / 2; //Could just do cb.y BUT if you ever want attack that is a different size, this code is more flexible to always be in the right place
		}else if(handler.getKeyManager().aRight) {
			ar.x = cb.x + cb.width; 
			ar.y = cb.y + cb.height / 2 - arSize / 2; //Could just do cb.y BUT if you ever want attack that is a different size, this code is more flexible to always be in the right place
		} else { //If none of the above code happens, means we are not attacking, so exit method before executing subsequent code
			return;
		}
		
		//If Attacking, begin following code

		//For every entity
		for(Entity e : handler.getWorld().getEntityManager().getEntities()) {
			
			//We don't want to hurt ourselves, so if we loop to ourselves, just continue
			if(e.equals(this))
				continue;
			
			//If entity's collision bounds intersects with attack rectangle register a hit against entity
			if(e.getCollisionBounds(0, 0).intersects(ar)) {
				e.hurt(1, this);

				//Reset attackTimer
				attackTimer = 0;		
			
				return;
			}
		}
	}
		
	//The process that occurs when an entity dies
	@Override	
	public void die() {
		System.out.println("You Lose");
		handler.getGame().getStatTracker().setDeathCount(handler.getGame().getStatTracker().getDeathCount() + 1);
		setActive(false);
		
		//Moving player far away so nothing tries to shoot a ghost of where it once was.
		y += handler.getGVar().getMaxDistance() * 9/10;
	}
		
	//Takes user input and performs various actions
	private void getInput() {

		//only allow movement on Phase > x
		if(handler.getPhaseManager().getCurrentPhase() < 5)
			return;		
		
		//Very important that every time we call this method we set xMove and yMove to 0
		xMove = 0;
		yMove = 0;
		
		//Player cannot move while in inventory
		if(inventory.isActive())
			return;
		
		//adjusts speeds based on whether player is running/walking or not (can only be adjusted when on the ground)
		if(collisionWithTileBottom && handler.getPhaseManager().getCurrentPhase() > 7) {
			if(handler.getKeyManager().run && !running) {
				run();
				handler.getGame().getStatTracker().setRunCountPlayer(handler.getGame().getStatTracker().getRunCountPlayer() + 1);
			}
			else if(!handler.getKeyManager().run && !walking) {
				walk();
			}
		}
		
		//Handles player Movement
		if(handler.getKeyManager().up)
			setFaceTop(true);
		if(handler.getKeyManager().down)
			setFaceBottom(true);
		if(handler.getKeyManager().right)
			xMove = speed;
		if(handler.getKeyManager().left)
			xMove = -speed;
		
		//Check whether player CAN jump, if so, jump
		if(handler.getKeyManager().jump && jumping && canJump)
			
			//negate running to increase jump
			if(running)
				yMove += -(speed/2 * 5);
			else
				yMove += -(speed * 5);

		//Reset top/bottom facing directions if not held
		if(!handler.getKeyManager().up)
			setFaceTop(false);
		if(!handler.getKeyManager().down)
			setFaceBottom(false);
		//Reset left/right if facing opposite direction
		if(handler.getKeyManager().right)
			setFaceLeft(false);
		if(handler.getKeyManager().left)
			setFaceRight(false);
		
		//This section is still a little iffy since can do a QUICK double jump, which you shouldn't be able to do. Granted, the double jump is weaker than a normal full jump, but still...
		//If jump was just pressed, set jumping to true
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_SPACE) && canJump && collisionWithTileBottom) {
			jumping = true;
			jumpTimer = 0;
			gravityHangtime = 0;
			gravityHangTimeTick = 0;
			handler.getGame().getStatTracker().setJumpCountPlayer(handler.getGame().getStatTracker().getJumpCountPlayer() + 1);
		}

		//If Jumping and jump is released, start hangtime and reset jumping
		if(!handler.getKeyManager().jump && !jumping) {
			hangtime = true;
		}
		
		//if game is paused, don't check further (newer implementations)
		if(handler.getGVar().getGSpeed() == 0)
			return;
				
		//Reload weapon
		if(handler.getPhaseManager().getCurrentPhase() > 18) {
			
			if(handler.getKeyManager().reload && handler.getKeyManager().keyJustPressed(KeyEvent.VK_R)) {

				//Only increment reload if ammo <> BULLET_MAX
				if(ammo != BULLET_MAX)
					handler.getGame().getStatTracker().setReloadCountPlayer(handler.getGame().getStatTracker().getReloadCountPlayer() + 1);

				bulletsFired = 0;
				ammo = BULLET_MAX;				
			}
		}
	}

	@Override
	public void render(Graphics g) {

		//Flash player if hit
		if(handler.getPhaseManager().getCurrentPhase() > 12) {

			if(flash > 0)
				g.drawImage(Assets.white, (int) (x - handler.getGameCamera().getxOffset()), (int)  (y - handler.getGameCamera().getyOffset()), width, height, null);	
			else
				//Temporarily doing no animation:
				g.drawImage(Assets.player, (int) (x - handler.getGameCamera().getxOffset()), (int)  (y - handler.getGameCamera().getyOffset()), width, height, null);	

			//Draw Player to screen WITH animation
			//g.drawImage(getCurrentAnimationFrame(), (int) (x - handler.getGameCamera().getxOffset()), (int)  (y - handler.getGameCamera().getyOffset()), width, height, null);
		} else if(handler.getPhaseManager().getCurrentPhase() > 8) {

			//Temporarily doing no animation:
			g.drawImage(Assets.player, (int) (x - handler.getGameCamera().getxOffset()), (int)  (y - handler.getGameCamera().getyOffset()), width, height, null);	

			//Draw Player to screen WITH animation
			//g.drawImage(getCurrentAnimationFrame(), (int) (x - handler.getGameCamera().getxOffset()), (int)  (y - handler.getGameCamera().getyOffset()), width, height, null);

		//Don't have camera follow player
		} else {

			//Draw Player to screen WITHOUT animation
			g.drawImage(Assets.player, (int) x, (int) y, width, height, null);			
		}

	}
	
	//works just like inventory, but it renders things AFTER the other render, so that this is on top
	public void postRender(Graphics g) {

		//Inventory
		inventory.render(g);		
	}
	
	//Reset Player for phase changes and such
	public void resetPlayer() {
		x = handler.getWorld().getSpawnX();
		y = handler.getWorld().getSpawnY();
		xMove = 0;
		yMove = 0;
	}
	

	/*************** GETTERS and SETTERS ***************/

	//Gets current animation frame depending on movement/other
	private BufferedImage getCurrentAnimationFrame() {
		if(xMove < 0) {
			return animLeft.getCurrentFrame();
		} else  if (xMove > 0) {
			return animRight.getCurrentFrame();
		} else  if (yMove < 0) {
			return animCrouch.getCurrentFrame();
		} else 
			return animJump.getCurrentFrame();
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public int getEmptyGunTimer_DEFAULT() {
		return emptyGunTimer_DEFAULT;
	}

	public void setEmptyGunTimer_DEFAULT(int emptyGunTimer_DEFAULT) {
		this.emptyGunTimer_DEFAULT = emptyGunTimer_DEFAULT;
	}

	public int getEmptyGunTimer() {
		return emptyGunTimer;
	}

	public void setEmptyGunTimer(int emptyGunTimer) {
		this.emptyGunTimer = emptyGunTimer;
	}

}
