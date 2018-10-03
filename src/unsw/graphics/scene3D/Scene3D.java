package unsw.graphics.scene3D;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.scene.Camera;
import unsw.graphics.scene.SceneObject;

/**
 * A Scene consists of a scene tree and a camera attached to the tree.
 * 
 * Every object in the scene tree is updated on each display call.
 * Then the scene tree is rendered.
 *
 * You shouldn't need to modify this class.
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 * 
 * @edit Converting from 2D to 3D scene object system
 * 
 */

public class Scene3D {

    private Camera3D myCamera;

    private SceneObject3D root;
    private long myTime;

    /**
     * Construct a new scene with a camera attached to the root object.
     *
     */
    public Scene3D() {
        root = new SceneObject3D();
        myTime = System.currentTimeMillis();
        myCamera = new Camera3D(root);
    }


    public void draw(GL3 gl) {

        // set the view matrix based on the camera position
        myCamera.setView(gl); 
        
        // update the objects
        update();

        // draw the scene tree
        root.draw(gl, CoordFrame3D.identity());        
    }

    private void update() {
        
        // compute the time since the last frame
        long time = System.currentTimeMillis();
        float dt = (time - myTime) / 1000f;
        myTime = time;
        
        root.update(dt);      
    }

    public SceneObject3D getRoot() {
        return root;
    }
   
    public Camera3D getCamera() {
        return myCamera;
    }

    public void setCamera(Camera3D camera) {
        myCamera.destroy();
        this.myCamera = camera;
    }
	
	
}
