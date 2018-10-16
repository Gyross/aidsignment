package unsw.graphics.world.player;

import unsw.graphics.scene.MathUtil;
import unsw.graphics.scene3D.Camera3D;
import unsw.graphics.scene3D.SceneObject3D;
import unsw.graphics.world.WorldLighting;

public class PlayerController {
	private float dx;
    private float dy;
    private float dz;
    
    private float dyt;
    private float dxt;
    private float dzt;
    
    private boolean toggleVerticalMovement;
    private boolean toggleSpeed;
    private boolean primitiveMovement;
    private boolean toggleFirstPerson;
    
    private float thetaY;
    
    private float ROTATION_SCALE;
    private float vert_offset;
    
    Camera3D camera;
    SceneObject3D cameraHolderInner;
    SceneObject3D cameraHolderOuter;
    
    SceneObject3D playerObject;
    
    public PlayerController(Camera3D cam, SceneObject3D inner, 
    		SceneObject3D outer, SceneObject3D player){
    	dx = 0;
        dy = 0;
        dz = 0;
        
        dyt = 0;
        dxt = 0;
        dzt = 0;
        
        toggleVerticalMovement = false;
        toggleSpeed = false;
        toggleFirstPerson = true;
        primitiveMovement = false;
        
        //camera link
        camera = cam;
        cameraHolderInner = inner;
        cameraHolderOuter = outer;
        playerObject = player;
        playerObject.show(!toggleFirstPerson);
        
        //coefficients
        ROTATION_SCALE = 1.5f;
        vert_offset = 1f;
        
		cameraHolderOuter.rotateY(225);
		thetaY = MathUtil.normaliseAngle(thetaY + 225);
    }
    
    //misc methods
    private float getSpeedCoeff(){ return toggleSpeed ? 4 : 1; }
    public void setPrimitiveMovement(){ primitiveMovement = true; }
    public void setNaturalMovement(){ primitiveMovement = false; }
    
    
    
    //camera changing methods
    public void fixCameraYPosition(float y){
    	if(toggleVerticalMovement) return;
    	
    	//get the x and z coordinates,
    	float camX = cameraHolderOuter.getPosition().getX();
		float camZ = cameraHolderOuter.getPosition().getZ();
		//then set the position for y
    	cameraHolderOuter.setPosition(camX, y , camZ);
    	cameraHolderInner.setPosition(0, (toggleFirstPerson) ? vert_offset : vert_offset/2, 0);
    }
    //rotations
    public void rotateCamera(){
    	
    	float speed = getSpeedCoeff();
    	
		if(!primitiveMovement){
			cameraHolderInner.rotateX(dxt*ROTATION_SCALE*speed);
			cameraHolderInner.rotateZ(dzt*ROTATION_SCALE*speed);
		}
		
		//rotate y whilst keeping track of the angle
		cameraHolderOuter.rotateY(dyt*ROTATION_SCALE*speed);
		thetaY = MathUtil.normaliseAngle(dyt*ROTATION_SCALE*speed + thetaY);    	
    }
    
    public void translateCamera(){
    	if(primitiveMovement){
    		dz = dxt;
    		dxt = 0;
    	}
		//calculate translation (to move in the correct direction)
		float fx = dx*(float) Math.cos(thetaY*Math.PI/180) + dz*(float) Math.sin(thetaY*Math.PI/180);
		float fz = -dx*(float) Math.sin(thetaY*Math.PI/180) + dz*(float) Math.cos(thetaY*Math.PI/180);
    	float speed = getSpeedCoeff();
    	
    	cameraHolderOuter.translate(fx*speed/20, dy*speed/20, fz*speed/20);
    	
    }
    
    
    
    
    
    //KEY MANAGEMENT METHODS
    
    //arrow press/release methods
    public void downArrowPressed()	{ dxt = -1; }
    public void upArrowPressed()	{ dxt =  1; }
    public void leftArrowPressed()	{ dyt =  1; }
    public void rightArrowPressed()	{ dyt = -1; }
    
    public void downArrowReleased()	{ if(dxt == -1) dxt = 0; }
    public void upArrowReleased()	{ if(dxt ==  1) dxt = 0; }
    public void leftArrowReleased()	{ if(dyt ==  1) dyt = 0; }
    public void rightArrowReleased(){ if(dyt == -1) dyt = 0; }
    
    
    //wasd press/release methods
    public void wKeyPressed()	{ dz = -1; }
    public void sKeyPressed()	{ dz = 1;  }
    public void aKeyPressed()	{ dx = -1;  }
    public void dKeyPressed()	{ dx = 1; }
    
    public void wKeyReleased()	{ if(dz == -1) dz = 0; }
    public void sKeyReleased()	{ if(dz == 1 ) dz = 0; }
    public void aKeyReleased()	{ if(dx == -1 ) dx = 0; }
    public void dKeyReleased()	{ if(dx == 1) dx = 0; }
    
    
    //shift/space/comma/period press/release
    public void shiftPressed()	{ dy  = -1; }
    public void spacePressed()	{ dy  =  1; }
    public void commaPressed()	{ dzt =  1; }
    public void periodPressed()	{ dzt = -1; }
    
    public void shiftReleased()	{ if(dy  == -1) dy  = 0; }
    public void spaceReleased()	{ if(dy  == 1 ) dy  = 0; }
    public void commaReleased()	{ if(dzt == 1 ) dzt = 0; }
    public void periodReleased(){ if(dzt == -1) dzt = 0; }
    
    //numkey toggles
    public void num9press(){ toggleVerticalMovement = !toggleVerticalMovement; }
    public void num0press(){ toggleSpeed = !toggleSpeed; }
    public void num1press(){ 
    	camera.translate(0,0, (toggleFirstPerson) ? 2f : -2f);
    	toggleFirstPerson = !toggleFirstPerson;
    	playerObject.show(!toggleFirstPerson);
    	
    }
    public void num2press(){
    	WorldLighting.toggleTorch();
    }
}
