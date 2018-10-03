package unsw.graphics.scene3D;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix3;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.Vector4;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.scene.SceneObject;

/**
 * The camera is a SceneObject that can be moved, rotated and scaled like any other, as well as
 * attached to any parent in the scene tree.
 * 
 * TODO: You need to implement the setView() method.
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 * 
 * @edit Converting to a 3D sceneobject
 * @author Daniel Iosifidis
 */
public class Camera3D extends SceneObject3D{

    public Camera3D(SceneObject3D parent) {
        super(parent);
    }
    
    /**
     * Method to set the view matrix for the camera, accounting for the aspect ratio, and cameras position in a 
     * scene tree
     * @param gl
     */
    public void setView(GL3 gl) {
    	
    	//compute a view transform to account for the cameras aspect ratio
    	//calculation of the inverted frame transform parameters (ternary operator to prevent division by zero)
    	float invertedGlobalScale = (getGlobalScale() == 0) ? 0 : 1/getGlobalScale();
    	
    	//calculate the inverted global direction
    	Matrix4 invertedGlobalRotation = getGlobalRotationFrame().getInverseRot();
    	
    	Point3D invertedGlobalTranslate = new Point3D(
    			-getGlobalPosition().getX(), 
    			-getGlobalPosition().getY(), 
    			-getGlobalPosition().getZ()
    			);

    	//Apply the frame transforms to the identity frame
    	CoordFrame3D viewFrame = CoordFrame3D.identity()
    								.scale(invertedGlobalScale, invertedGlobalScale, invertedGlobalScale)
    								.rotateByFrame(invertedGlobalRotation)
    								.translate(invertedGlobalTranslate);
    	
    	//Set this matrix as the view matrix for the shader
    	Shader.setViewMatrix(gl, viewFrame.getMatrix());

    	
    }
    

    /**
     * Transforms a point from camera coordinates to world coordinates. Useful for things like mouse
     * interaction
     * 
     * @param x, y, z
     * @return
     */
    public Point3D fromView(float x, float y, float z) {
        Matrix4 mat = Matrix4.translation(getGlobalPosition())
                				.multiply(getRotationFrame())
                				.multiply(Matrix4.scale(getGlobalScale(), getGlobalScale(),getGlobalScale()));
        return mat.multiply(new Vector4(x,y,z,1)).asPoint3D();
    }
}
