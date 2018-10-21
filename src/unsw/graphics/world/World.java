package unsw.graphics.world;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.GL3;

import unsw.graphics.Application3D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleFan3D;
import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.scene3D.MathUtil;
import unsw.graphics.scene3D.Camera3D;
import unsw.graphics.scene3D.MeshSceneObject;
import unsw.graphics.scene3D.Scene3D;
import unsw.graphics.scene3D.SceneObject3D;
import unsw.graphics.world.player.PlayerController;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 * 
 * @author Daniel Iosifidis
 * Added a bunch of stuff ye
 * @author Andrew Ross
 */
public class World extends Application3D implements MouseListener, KeyListener{

	//store the terrain
    private Terrain terrain;
    private WorldLighting lighting;
    
    private Texture grassTexture;
    private Texture rockTexture;
    private Texture marbleTexture;
    private Texture snowTexture;
    
    
    //mouse position parameter
    private Point2D myMousePoint = null;
    
    //scene tree
    private Scene3D scene;
    private Camera3D camera;
    private SceneObject3D cameraHolderInner;
    private SceneObject3D cameraHolderOuter;
    
    private SceneObject3D playerObject;
    private PlayerController pc;
        
    private float perspectiveDistance = 200;
    
    

    
    //constructor
    public World(Terrain terrain) {
    	super("Assignment 2", 800, 600);
        this.terrain = terrain;
        this.lighting = new WorldLighting(true);
        
        //create a scene
        scene = new Scene3D();
        
        //create a camera
        cameraHolderOuter = new SceneObject3D(scene.getRoot());
        cameraHolderInner = new SceneObject3D(cameraHolderOuter);
        camera = new Camera3D(cameraHolderInner);
        scene.setCamera(camera);
        
        //player object
        playerObject = new SceneObject3D(cameraHolderOuter);
        this.pc = new PlayerController(camera, cameraHolderInner, 
        		cameraHolderOuter, playerObject);
        
        //create the scene
    }
   
    
    private void createScene(GL3 gl){
        // Load textures
        grassTexture  = new Texture(gl, "res/textures/grass.bmp", "bmp", false);
        rockTexture   = new Texture(gl, "res/textures/rock.bmp", "bmp", false);
        marbleTexture = new Texture(gl, "res/textures/BrightPurpleMarble.png", "png", false);
        snowTexture   = new Texture(gl, "res/textures/snow.jpg", "jpg", false);
        
        
        //add terrain object
        MeshSceneObject terrainObj = new MeshSceneObject(
        		terrain.getTerrainMesh(), scene.getRoot());
        
		// terrainObj.setColor(Color.GREEN); 
        terrainObj.setTexture(snowTexture);
		terrainObj.setAmbientColor(new Color(0.1f, 0.1f, 0.1f));
		terrainObj.setDiffuseColor(new Color(0.8f, 0.8f, 0.8f));
		terrainObj.setSpecularColor(new Color(0.1f, 0.1f, 0.1f));
        terrain.addTrees(terrainObj);
        terrain.addRoads(terrainObj);

 
        
        //add player object
        MeshSceneObject playerObj = new MeshSceneObject(
        		terrain.getPlayerMesh(), playerObject);
        
        // playerObj.setColor(Color.white);  
        playerObj.setTexture(marbleTexture);
        playerObj.scale(3);
        
        
        MeshSceneObject skyBox = new MeshSceneObject(
        		WorldTestObjects.genSkyBox(gl, 80, 80, 160),scene.getRoot());
        skyBox.setColor(new Color(0.7f, 0.8f, 1));
        skyBox.setAmbientColor(new Color(0.3f, 0.3f, 0.4f));
        skyBox.setDiffuseColor(new Color(0.7f, 0.7f, 0.8f));
        skyBox.setSpecularColor(new Color(0, 0, 0));
        skyBox.setParent(cameraHolderOuter);
        
    }
    

    
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        World world = new World(terrain);
        world.start();   
    }

	@Override
	public void display(GL3 gl) {
		super.display(gl);
		
		
		
		
		//rotate and move the camera
		pc.rotateCamera();
		pc.translateCamera();
		
		//fix the camera to the terrain
		pc.fixCameraYPosition(terrain.altitude(
				cameraHolderOuter.getPosition().getX(),
				cameraHolderOuter.getPosition().getZ()
		));
		
		//update sunlight
		//terrain.rotateSunlight(0.1f);
		terrain.rotateSunlight(0.5f);


		//update positional lighting
		lighting.updateLightPos(camera.getGlobalPosition());
		lighting.updateSunlightLighting(gl, terrain.getSunlight().asPoint3D());
		
		
		//scene.draw(gl, camera.getGlobalPosition(), perspectiveDistance);    
		scene.draw(gl);
        
	}
	@Override
	public void destroy(GL3 gl) {
		super.destroy(gl);
		
	}
	

	@Override
	public void init(GL3 gl) {

		getWindow().addMouseListener(this);
		getWindow().addKeyListener(this);
		
		super.init(gl);
		terrain.init(gl);
		
		lighting.initLightingColor(this.terrain);
		lighting.initLighting(gl);
		
        createScene(gl);
	}
	

	@Override
	public void reshape(GL3 gl, int width, int height) {
        super.reshape(gl, width, height);
        
        //set the projection matrix to a perspective view with a 70 fov
        Shader.setProjMatrix(gl, 
        		Matrix4.perspective(70, width/(float)height, 0.2f, 200));

	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) { }

	@Override
	public void mouseMoved(MouseEvent e) {
		 myMousePoint = new Point2D(e.getX(), e.getY());
	}

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseWheelMoved(MouseEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		
		case KeyEvent.VK_W: 	pc.wKeyPressed();	break; 
		case KeyEvent.VK_S: 	pc.sKeyPressed(); 	break;
		
		case KeyEvent.VK_A:		pc.aKeyPressed(); 	break;
		case KeyEvent.VK_D: 	pc.dKeyPressed(); 	break;
		
		case KeyEvent.VK_SPACE:	pc.spacePressed(); 	break;
		case KeyEvent.VK_SHIFT: pc.shiftPressed(); 	break;
		
		
		case KeyEvent.VK_UP: 	pc.upArrowPressed(); 	break; 
		case KeyEvent.VK_DOWN: 	pc.downArrowPressed(); 	break;
		
		case KeyEvent.VK_LEFT:	pc.leftArrowPressed(); 	break;
		case KeyEvent.VK_RIGHT: pc.rightArrowPressed();	break;
		
		
		case KeyEvent.VK_COMMA:	pc.commaPressed(); 	break;
		case KeyEvent.VK_PERIOD: pc.periodPressed(); break;
		
		case KeyEvent.VK_0: pc.num0press(); break; 
		case KeyEvent.VK_9: pc.num9press(); break; 
		case KeyEvent.VK_1: pc.num1press(); break; 
		case KeyEvent.VK_2: pc.num2press(); break; 
		case KeyEvent.VK_3: terrain.rotateSunlight(45); break;
		
		//window quitting function
		case KeyEvent.VK_Q: this.getWindow().destroy();
		
		default: break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){

		case KeyEvent.VK_W: 	pc.wKeyReleased();	break; 
		case KeyEvent.VK_S: 	pc.sKeyReleased(); 	break;
		
		case KeyEvent.VK_A:		pc.aKeyReleased(); 	break;
		case KeyEvent.VK_D: 	pc.dKeyReleased(); 	break;
		
		case KeyEvent.VK_SPACE:	pc.spaceReleased(); break;
		case KeyEvent.VK_SHIFT: pc.shiftReleased(); break;
		
		
		case KeyEvent.VK_UP: 	pc.upArrowReleased(); 	break; 
		case KeyEvent.VK_DOWN: 	pc.downArrowReleased(); break;
		
		case KeyEvent.VK_LEFT:	pc.leftArrowReleased(); break;
		case KeyEvent.VK_RIGHT: pc.rightArrowReleased();break;
		
		
		case KeyEvent.VK_COMMA:	pc.commaReleased(); 	break;
		case KeyEvent.VK_PERIOD: pc.periodReleased(); 	break;
		
		default: break;
		}
		
	}
}
