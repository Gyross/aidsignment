package unsw.graphics.scene;

import unsw.graphics.Vector3;
import unsw.graphics.Vector4;
import unsw.graphics.geometry.Point3D;

/**
 * A collection of useful math methods 
 *
 * @author malcolmr
 */
public class MathUtil {

	
	private static final Vector4 default_direction = new Vector4(0, 0, 1, 0);
    /**
     * Normalise an angle to the range [-180, 180)
     * 
     * @param angle 
     * @return
     */
    public static float normaliseAngle(float angle) {
        return ((angle + 180f) % 360f + 360f) % 360f - 180f;
    }

    /**
     * Clamp a value to the given range
     * 
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
       
    /**
     * Normalise a vector3
     * @param vec
     * @return
     */
    public static Vector3 normalise(Vector3 vec){
    	
    	float r = (float) Math.sqrt(vec.dotp(vec));
    	if(r == 0){ return new Vector3(0,0,1); }
    	
    	float x = vec.getX()/r;
    	float y = vec.getY()/r;
    	float z = vec.getZ()/r;
    	
    	return new Vector3(x,y,z);
    	
    }
    /**
     * Normalise a vector4 with a zero as the 4th component
     * @param vec
     * @return
     */
    public static Vector4 normalise(Vector4 vec){
    	
    	Vector3 v = vec.trim();
    	float r = (float) Math.sqrt(v.dotp(v));
    	if(r == 0){ return default_direction; }
    	
    	float x = v.getX()/r;
    	float y = v.getY()/r;
    	float z = v.getZ()/r;
    	
    	return new Vector4(x,y,z, 0);
    }
    
    public static Vector4 invertDiretion(Vector4 vec){
    	Vector3 trimmed = vec.trim();
    	float x = trimmed.getX();
    	float y = trimmed.getY();
    	float z = trimmed.getZ();
    	
    	return normalise(new Vector4(-x, -y, -z, 0));
    }
 
    
    //get rotation helper methods
    /**
     * Get the rotation of a vec4 about the x axis
     * @param vec
     * @return
     */
    public static float getXRotation(Vector4 vec){
    	Vector3 v = vec.trim();
    	
    	float h1 = v.getY();
    	float w1 = v.getZ();
    	
    	float h0 = default_direction.asPoint3D().getY();
    	float w0 = default_direction.asPoint3D().getZ();
    	
    	float a1 = getInclineAngle(h1, w1);	
    	float a0 = getInclineAngle(h0, w0);	
    	
    	return normaliseAngle(a1 - a0);
    }
    
    /**
     * Get the rotation of a vec4 about the x axis
     * @param vec
     * @return
     */
    public static float getYRotation(Vector4 vec){
    	Vector3 v = vec.trim();
    	
    	float h1 = v.getZ();
    	float w1 = v.getX();
    	
    	float h0 = default_direction.asPoint3D().getZ();
    	float w0 = default_direction.asPoint3D().getX();
    	
    	float a1 = getInclineAngle(h1, w1);	
    	float a0 = getInclineAngle(h0, w0);	
    	
    	return normaliseAngle(a1 - a0);
    }
    
    /**
     * Get the rotation of a vec4 about the x axis
     * @param vec
     * @return
     */
    public static float getZRotation(Vector4 vec){
    	Vector3 v = vec.trim();
    	
    	float h1 = v.getX();
    	float w1 = v.getY();
    	
    	float h0 = default_direction.asPoint3D().getX();
    	float w0 = default_direction.asPoint3D().getY();
   
    	float a1 = getInclineAngle(h1, w1);	
    	float a0 = getInclineAngle(h0, w0);	
    	
    	return normaliseAngle(a1 - a0);
    }
    
    /**
     * Method that gets the incline angle in radians
     * @param vert
     * @param hor
     * @return
     */
    private static float getInclineAngle(float vert, float hor){
    	float angleRad;
    	if		(hor > 0) { angleRad = (float) Math.atan(vert/hor); }
    	else if	(hor < 0) { angleRad = (float) (Math.atan(vert/hor) + Math.PI); }
    	else if (vert > 0) 	{ angleRad = (float) Math.PI/2; } 
    	else if (vert < 0) 	{ angleRad = (float) -Math.PI/2; } 
    	else 				{ angleRad = (float) 0; }
    	
    	return (float)((angleRad*180)/Math.PI);  	
    }
    
    public static Point3D invert(Point3D p){
       	float x = -p.getX();
    	float y = -p.getY();
    	float z = -p.getZ();
    	return new Point3D(x,y,z);
    }
    
    public static float getSize(Point3D p){
    	float x = p.getX();
    	float y = p.getY();
    	float z = p.getZ();
    	
    	return (float) (Math.sqrt(x*x +y*y +z*z));
    }
    
    
}
