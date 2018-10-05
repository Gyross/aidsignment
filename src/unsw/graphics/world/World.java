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
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.TriangleFan3D;
import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.scene3D.Camera3D;
import unsw.graphics.scene3D.MeshSceneObject;
import unsw.graphics.scene3D.Scene3D;
import unsw.graphics.scene3D.SceneObject3D;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 * 
 * @author Daniel Iosifidis
 * Added a bunch of stuff ye
 */
public class World extends Application3D implements MouseListener, KeyListener{

	//store the terrain
    private Terrain terrain;
    
    //mouse position parameter
    private Point2D myMousePoint = null;
    
    //scale for rotation speed
    private static final float ROTATION_SCALE = 1.5f;
    
    //scene tree
    private Scene3D scene;
    private Camera3D camera;
    private SceneObject3D cameraHolder;
        
    //key press parameters
    private float dx = 0;
    private float dy = 0;
    private float dz = 0;
    
    private float dyt = 0;
    private float dxt = 0;
    private float dzt = 0;
    
    private boolean toggleVert;
    
    private float thetaY = 0;

    
    //constructor
    public World(Terrain terrain) {
    	super("Assignment 2", 800, 600);
        this.terrain = terrain;
 

        //create a scene
        scene = new Scene3D();
        
        //create a camera
        cameraHolder = new SceneObject3D(scene.getRoot());
        camera = new Camera3D(cameraHolder);
        scene.setCamera(camera);
        
        toggleVert = false;

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

		
		//move the camera holder
		
		//calculate translation (to move in the correct direction)
		float fx = dx*(float) Math.cos(thetaY*Math.PI/180) + dz*(float) Math.sin(thetaY*Math.PI/180);
		float fz = -dx*(float) Math.sin(thetaY*Math.PI/180) + dz*(float) Math.cos(thetaY*Math.PI/180);
		
		cameraHolder.translate(fx/20, dy/20, fz/20);
		float camX = cameraHolder.getPosition().getX();
		float camZ = cameraHolder.getPosition().getZ();
		if(!toggleVert) {
			terrain.terrainPlace(cameraHolder, camX, camZ);
			cameraHolder.translate(0,2,0);
		}
		
		//rotations
		camera.rotateX(dxt*ROTATION_SCALE);
		camera.rotateZ(dzt*ROTATION_SCALE);
		
		//rotate y whilst keeping track of the angle
		cameraHolder.rotateY(dyt*ROTATION_SCALE);
		thetaY += dyt*ROTATION_SCALE;
		
		
		
		scene.draw(gl);    
		
		//drawCubes(gl, 5, 0.5f);
        
	}
	@Override
	public void destroy(GL3 gl) {
		super.destroy(gl);
		
	}
	
	private void drawCubes(GL3 gl, int n, float s){
		//draw some cubes in an n*n*n grid to test the display
        CoordFrame3D frame = CoordFrame3D.identity();
        
        for(int i = 0; i < n*n*n; i++){
        	if (i == (n+1)/2) continue;
        	int a = (i%n)-(n-1)/2;
        	int b = (i/n)%n-(n-1)/2;
        	int c = (i/(n*n))%n-(n-1)/2;
        	frame = CoordFrame3D.identity() 
        		.translate(3*a, 3*b, 3*c)
                .scale(s, s, s);
        	drawCube(gl, frame);
        }
	}
	
	private void drawCube(GL3 gl, CoordFrame3D frame) {
        TriangleFan3D face = new TriangleFan3D(-1,-1,1, 1,-1,1, 1,1,1, -1,1,1);
        
        // Front
        Shader.setPenColor(gl, Color.RED);
        face.draw(gl, frame);
        
        // Left
        Shader.setPenColor(gl, Color.BLUE);
        face.draw(gl, frame.rotateY(-90));
        
        // Right
        Shader.setPenColor(gl, Color.GREEN);
        face.draw(gl, frame.rotateY(90));
        
        // Back
        Shader.setPenColor(gl, Color.CYAN);
        face.draw(gl, frame.rotateY(180));
        
        // Bottom
        Shader.setPenColor(gl, Color.YELLOW);
        face.draw(gl, frame.rotateX(-90));
        
        // Top
        Shader.setPenColor(gl, Color.MAGENTA);
        face.draw(gl, frame.rotateX(90));
    }
	
	
	@Override
	public void init(GL3 gl) {
		getWindow().addMouseListener(this);
		getWindow().addKeyListener(this);
		
		super.init(gl);
		terrain.init(gl);
		
        //populate the scene
        MeshSceneObject terrainObj = new MeshSceneObject(terrain.getTerrainMesh(), scene.getRoot());
        terrainObj.setColor(Color.GREEN);  
        terrain.addTrees(terrainObj);
	}
	

	@Override
	public void reshape(GL3 gl, int width, int height) {
        super.reshape(gl, width, height);
        
        //set the projection matrix to a perspective view with a 70 fov
        Shader.setProjMatrix(gl, Matrix4.perspective(70, width/(float)height, 0.5f, 200));

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
		
		case KeyEvent.VK_W: 	dz = -1; 	break; 
		case KeyEvent.VK_S: 	dz = 1; 	break;
		
		case KeyEvent.VK_A:		dx = -1; 	break;
		case KeyEvent.VK_D: 	dx = 1;		break;
		
		case KeyEvent.VK_SPACE:	dy = 1; 	break;
		case KeyEvent.VK_SHIFT: dy = -1;	break;
		
		
		case KeyEvent.VK_UP: 	dxt = 1; 	break; 
		case KeyEvent.VK_DOWN: 	dxt = -1; 	break;
		
		case KeyEvent.VK_LEFT:	dyt = 1; 	break;
		case KeyEvent.VK_RIGHT: dyt = -1;	break;
		
		case KeyEvent.VK_COMMA:	dzt = 1; 	break;
		case KeyEvent.VK_PERIOD: dzt = -1;	break;
		
		case KeyEvent.VK_0: toggleVert = !toggleVert; 
		
		default: break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){

		case KeyEvent.VK_W: 	dz = 0; 	break; 
		case KeyEvent.VK_S: 	dz = 0; 	break;
		
		case KeyEvent.VK_A:		dx = 0; 	break;
		case KeyEvent.VK_D: 	dx = 0;		break;
		
		case KeyEvent.VK_SPACE:	dy = 0; 	break;
		case KeyEvent.VK_SHIFT: dy = 0;		break;
		
		
		case KeyEvent.VK_UP: 	dxt = 0; 	break; 
		case KeyEvent.VK_DOWN: 	dxt = 0; 	break;
		
		case KeyEvent.VK_LEFT:	dyt = 0; 	break;
		case KeyEvent.VK_RIGHT: dyt = 0;	break;
		
		case KeyEvent.VK_COMMA:	dzt = 0; 	break;
		case KeyEvent.VK_PERIOD: dzt = 0;	break;
		
		default: break;
		}
		
	}
}
