package unsw.graphics.scene3D;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.Vector4;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.scene.MathUtil;

/**
 * A SceneObject is an object that can move around in the world.
 * 
 * SceneObjects form a scene tree.
 * 
 * Each SceneObject is offset from its parent by a translation, a rotation and a scale factor. 
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 * 
 * @edit Conversion from 2D to 3D
 * @author Daniel Iosifidis
 */
public class SceneObject3D {
	
	// the links in the scene tree
    private SceneObject3D myParent;
    private List<SceneObject3D> myChildren;

    // the local transformation
    private Point3D myTranslation;
    private Matrix4 myRotationFrame; 
    private float myScale;
    
    // Is this part of the tree showing?
    private boolean amShowing;  
    
    
    /**
     * Special constructor for creating the root node. Do not use otherwise.
     */
    public SceneObject3D() {
        myParent = null;
        myChildren = new ArrayList<SceneObject3D>();
        
        //default frame
        myRotationFrame = Matrix4.identity();
        myScale = 1;
        myTranslation = new Point3D(0,0,0);

        //initially showing
        amShowing = true;
    }

    /**
     * Public constructor for creating SceneObjects, connected to a parent.
     *  
     * New objects are created at the same location, orientation and scale as the parent.
     *
     * @param parent
     */
    public SceneObject3D(SceneObject3D parent) {
        myParent = parent;
        myParent.myChildren.add(this);
        myChildren = new ArrayList<SceneObject3D>();

        //default frame
        myRotationFrame = Matrix4.identity();
        myScale = 1;
        myTranslation = new Point3D(0,0,0);

        // initially showing
        amShowing = true;
    }

    /**
     * Remove an object and all its children from the scene tree.
     */
    public void destroy() {
	    List<SceneObject3D> childrenList = new ArrayList<SceneObject3D>(myChildren);
        for (SceneObject3D child : childrenList) {
            child.destroy();
        }
        if(myParent != null)
                myParent.myChildren.remove(this);
    }

    /**
     * Get the parent of this scene object
     * 
     * @return
     */
    public SceneObject3D getParent() {
        return myParent;
    }

    /**
     * Get the children of this object
     * 
     * @return
     */
    public List<SceneObject3D> getChildren() {
        return myChildren;
    }
    
    
    
    
    
    
    
    
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
    //ROTATION
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// 
    
    
    //Rotating along an  axis
    public void rotateX(float rot){
    	myRotationFrame = myRotationFrame.multiply(Matrix4.rotationX(rot));
    }
    public void rotateY(float rot){
    	myRotationFrame = myRotationFrame.multiply(Matrix4.rotationY(rot));
    }
    public void rotateZ(float rot){
    	myRotationFrame = myRotationFrame.multiply(Matrix4.rotationZ(rot));
    }
    
    //frame setter, getter and rotator
    public void rotateByFrame(Matrix4 rotM){
    	myRotationFrame = myRotationFrame.multiply(rotM);		
    }
    public Matrix4 getRotationFrame(){
    	return myRotationFrame;
    }
    public void setRotationFrame(Matrix4 m){
    	myRotationFrame = m;
    }
    
    
    
    
    
    
    
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
    //SCALE
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
   
    //scale getter, setter and scaler methods
    public float getScale(){ 
    	return myScale; 
    }
    public void setScale(float scale) { 
    	myScale = scale; 
    }
    public void scale(float factor) { 
    	myScale *= factor;  
    }

    
    
    
    
    
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
    //POSITION
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
    
    /**
     * Get the local position of the object 
     * @return
     */
    public Point3D getPosition() {
        return myTranslation;
    }

    /**
     * Set the local position of the object
     * @param x,y,z
     */
    public void setPosition(float x, float y, float z) {
        setPosition(new Point3D(x,y,z));
    }

    /**
     * Set the local position of the object
     * @param point
     */
    public void setPosition(Point3D p) {
        myTranslation = p;
    }

    /**
     * Move the object by the specified offset in local coordinates
	 * @param dx, dy, dz
     */
    public void translate(float dx, float dy, float dz) {
        myTranslation = myTranslation.translate(dx, dy, dz);
    }

    
    
    
    
    
    
    
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
    //Display
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
    
    /**
     * Test if the object is visible
     * 
     * @return
     */
    public boolean isShowing() {
        return amShowing;
    }

    /**
     * Set the showing flag to make the object visible (true) or invisible (false).
     * This flag should also apply to all descendents of this object.
     * 
     * @param showing
     */
    public void show(boolean showing) {
        amShowing = showing;
    }

    /**
     * Update the object and all it's children. This method is called once per frame. 
     * 
     * @param dt The amount of time since the last update (in seconds)
     */
    public void update(float dt) {
        updateSelf(dt);
        
        // Make a copy of all the children to avoid concurrently modification issues if new objects
        // are added to the scene during the update.
        List<SceneObject3D> children = new ArrayList<SceneObject3D>(myChildren);
        for (SceneObject3D so : children) {
            so.update(dt);
        }
    }

    /** 
     * Update the object itself. Does nothing in the default case. Subclasses can override this
     * for animation or interactivity.
     * 
     * @param dt
     */
    public void updateSelf(float dt) {
        // Do nothing by default
    }

    /**
     * Draw the object (but not any descendants)
     * 
     * This does nothing in the base SceneObject class. Override this in subclasses.
     * 
     * @param gl
     */
    public void drawSelf(GL3 gl, CoordFrame3D frame) {
        // Do nothing by default
    }

    

    /**
     * Draw the object and all of its descendants recursively.
     * 
     * 
     * @param gl
     */
    public void draw(GL3 gl, CoordFrame3D frame, Point3D camGlobCoord, float camGlobLen) {
        Point3D p = this.getGlobalPosition()
        		.translate(MathUtil.invert(camGlobCoord).asHomogenous().trim());
        float len = MathUtil.getSize(p);
        //don't draw if it is not showing
        if (!amShowing) {
            return;
        }
        
        // coordinate frame computation
        
        //create a new frame (a copy of the old frame)
        CoordFrame3D thisFrame = new CoordFrame3D(frame.getMatrix());
        
        //transform the copy to the frame of this object
        CoordFrame3D transformedFrame = thisFrame
        					.translate(getPosition())
        					.rotateByFrame(getRotationFrame())
        					.scale(getScale(), getScale(), getScale());
        
        
        //for objects sufficiently close
        if(len < camGlobLen){
        	//draw this object   
        	drawSelf(gl, transformedFrame);
        }
        //draw the children of this object recursively
        int n_children = getChildren().size();
        for (int i = 0; i < n_children; i++) {
        	getChildren().get(i).draw(gl, transformedFrame, camGlobCoord, camGlobLen);
        }
        	
    }
    
    /**
     * Draw the object and all of its descendants recursively.
     * 
     * 
     * @param gl
     */
    public void draw(GL3 gl, CoordFrame3D frame) {
        
        //don't draw if it is not showing
        if (!amShowing) {
            return;
        }
        
        // coordinate frame computation
        
        //create a new frame (a copy of the old frame)
        CoordFrame3D thisFrame = new CoordFrame3D(frame.getMatrix());
        
        //transform the copy to the frame of this object
        CoordFrame3D transformedFrame = thisFrame
        					.translate(getPosition())
        					.rotateByFrame(getRotationFrame())
        					.scale(getScale(), getScale(), getScale());
        
        
        // drawing

        //draw this object   
        drawSelf(gl, transformedFrame);
        
        //draw the children of this object recursively
        int n_children = getChildren().size();
        for (int i = 0; i < n_children; i++) {
        	getChildren().get(i).draw(gl, transformedFrame);
        }
        	
    }
    
    
    
    
    
    
    
    
    
    
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
    //Global
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
    
    /**
     * Compute the object's position in world coordinates
     * 
     * @return a point in world coordinates
     */
    public Point3D getGlobalPosition() {
    	//get transform frame
    	return getPositionInFrame(getGlobalFrame());
    }
    public Point3D getPositionInFrame(CoordFrame3D frame) {
    	Point3D point = getFrameTranslation(frame);
    	return point;
    }

    /**
     * Gets the translation coordinate of a frame
     * @param frame frame to get the translation of
     * @return
     */
    public Point3D getFrameTranslation(CoordFrame3D frame){
    	//get transform matrix
    	Matrix4 mat = frame.getMatrix();

    	//get z discriminatory matrix
    	float[] values = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
    	Matrix4 posFinderMat = new Matrix4(values);

    	//Calculate the matrix containing the point
    	Matrix4 pointMat = mat.multiply(posFinderMat);

    	//generate the getTranslation vector
    	float[] vecValue = {0,0,0,1};
    	
    	//find the position vector of the translation based by multiplying the z discriminated 
    	//matrix by the z finding vector
    	Vector4 posVec = pointMat.multiply(new Vector4(vecValue));

    	return posVec.asPoint3D();
    }
    
    
    /**
     * helper method to compute the global coordinate frame given the current scene object
     * @return
     */
    public CoordFrame3D getGlobalFrame(){
    	
    	//construct a temporary frame
    	CoordFrame3D frame = CoordFrame3D.identity();
    	
    	//get the global frame of the parent sceneObject in the tree (if this is not the root)
    	if(getParent() != null) frame = getParent().getGlobalFrame();
    	
    	//create a new frame as the transformations of this frame on the parent frame
    	return frame.translate(getPosition())
					.rotateByFrame(getRotationFrame())
    				.scale(getScale(), getScale(), getScale());		
    	
    }
    
    
    
    /**
     * Get the global rotation frame of this object
     * @return
     */
    public Matrix4 getGlobalRotationFrame(){
    	
    	//construct a temporary frame
    	Matrix4 frameMat = Matrix4.identity();
    	
    	//get the parent objects rotation frame if a parent exists
    	if(getParent() != null) frameMat = getParent().getGlobalRotationFrame();
    	
    	//create a new frame as the rotation of this frame on the parent frame
    	return frameMat.multiply(myRotationFrame);
    }
    

    /**
     * Compute the object's scale in global terms
     * 
     * @return the global scale of the object 
     */
    public float getGlobalScale() {
    
    	//the root has a default scale of 1, recursively find a rescaling frames as each frame is scaled
    	//from its parent
    	float scale = getScale();
        if (getParent() != null) scale = scale*getParent().getGlobalScale();
        
        return scale;
    }

    /**
     * Change the parent of a scene object.
     * 
     * @param parent
     */
    public void setParent(SceneObject3D parent) {
    	
    	//find the global translation, scale and rotation of this object
    	CoordFrame3D myGlobalFrame = getGlobalFrame();
    	Point3D myGlobalPoint = getPositionInFrame(myGlobalFrame);
    	Matrix4 myGlobalRotation = getGlobalRotationFrame();
    	float myGlobalScale = getGlobalScale();

    	//re-parent
        myParent.myChildren.remove(this);
        myParent = parent;
        myParent.myChildren.add(this);
        
        //reset the frame of this object
        setPosition(0,0,0);
        setRotationFrame(Matrix4.identity());
        setScale(1);
        
        //get the transforms of the parent
        CoordFrame3D parentGlobalFrame = getGlobalFrame();
        Point3D parentGlobalPoint = getPositionInFrame(parentGlobalFrame);
        Matrix4 parentGlobalRotation = getGlobalRotationFrame();
        float parentGlobalScale = getGlobalScale();
 
        //update the new frame (invert parent global, and apply my global)        
        CoordFrame3D newFrame = new CoordFrame3D(Matrix4.identity());
 
        //transform the frame back from the new parents global frame then transform
        //forward by the global frame to get the scale in new scene tree
        
        //translate back
        newFrame = newFrame
        			.scale(1/parentGlobalScale, 1/parentGlobalScale, 1/parentGlobalScale)
        			.rotateByFrame(parentGlobalRotation.getInverseRot())
        			.translate(-parentGlobalPoint.getX(), -parentGlobalPoint.getY(), -parentGlobalPoint.getZ());
        
       //translate forward
        newFrame = newFrame
        			.translate(myGlobalPoint.getX(), myGlobalPoint.getY(), myGlobalPoint.getZ())
        			.rotateByFrame(myGlobalRotation)
        			.scale(myGlobalScale, myGlobalScale, myGlobalScale);

        //translate to the calculated translation
        Point3D newPoint = getFrameTranslation(newFrame);
        translate(newPoint.getX(), newPoint.getY(), newPoint.getZ());
     
        //rotate backwards then rotate forward
        rotateByFrame(parentGlobalRotation.getInverseRot());
        rotateByFrame(myGlobalRotation);	

        //scale back from the new parents global scale then forward
        //by the global scale to get the scale in new scene tree
        scale(1/parentGlobalScale);
        scale(myGlobalScale);
    }
   
	
	/**
	 * Gets the root node of the scene
	 * @return root sceneObject
	 */
    protected SceneObject3D getRoot(){
    	SceneObject3D root = this;
    	
    	if (root.getParent() != null) root = root.getRoot();
    	return root;
    }
}
