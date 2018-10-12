package unsw.graphics.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.world.helper.RoadCreationHelper;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Point2D> points;
    private List<Point2D> normals;
    private float width;
    private int length_segments;
    private int width_segments;
    
    
    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(float width, List<Point2D> spine) {
        this.width = width;
        this.points = spine;
        length_segments = this.size()*128;
        width_segments = 32;
        
        normals = this.generateNormals();
 
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return width;
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return (points.size() / 6) + 1;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public Point2D controlPoint(int i) {
        return points.get(i);
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public Point2D point(float s) {
    	//get a value between 0 and 1
        int i = (int)Math.floor(s);
        float t = s - i;
        i *= 3;

        //s an integer
        if(t == 0){
        	return new Point2D(points.get(i).getX(), points.get(i).getY());
        } 
        
        //t:0->1 p:p3i->p3i+3
        //go from the start of a bezier interval to the end (which is the start of the next)
        Point2D p0 = points.get(i++);
        Point2D p1 = points.get(i++);
        Point2D p2 = points.get(i++);
        Point2D p3 = points.get(i++);

        float x = b(0, t) * p0.getX() + b(1, t) * p1.getX() + b(2, t) * p2.getX() + b(3, t) * p3.getX();
        float y = b(0, t) * p0.getY() + b(1, t) * p1.getY() + b(2, t) * p2.getY() + b(3, t) * p3.getY();        
        
        return new Point2D(x, y);
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private float b(int i, float t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

    
    private ArrayList<Point2D> generateNormals(){
    	
    	ArrayList<Point2D> curve_points = new ArrayList<Point2D>();
    	ArrayList<Point2D> normals = new ArrayList<Point2D>();
    	
    	float t;
    	for(int i = 0; i<= this.length_segments; i++){
    		t = (float)( i*this.size() )/this.length_segments;
    		curve_points.add(point(t));
    	}
    	
    	//add normal to first point
    	Point2D p0 = curve_points.get(1).translate(
    			-curve_points.get(0).getX(), 
    			-curve_points.get(0).getY());
    	
    	normals.add(new Point2D(-p0.getY(), p0.getX()));
    	    	

    	//add all intermediate normals
    	for(int i = 1; i<this.length_segments; i++){
    		Point2D p1 = curve_points.get(i+1).translate(
    				-curve_points.get(i).getX(), 
    				-curve_points.get(i).getY());
    		
    		Point2D p2 = curve_points.get(i).translate(
    				-curve_points.get(i-1).getX(), 
    				-curve_points.get(i-1).getY());
    		
    		
    		Point2D p3 = new Point2D((p1.getX() + p2.getX())/2,
    								(p1.getY() + p2.getY())/2);

    		normals.add(new Point2D(-p3.getY(), p3.getX()));
    	}
    	
    	//add normal to last point
    	Point2D p4 = curve_points.get(this.length_segments).translate(
    			-curve_points.get(this.length_segments-1).getX(), 
    			-curve_points.get(this.length_segments-1).getY());
    	
    	normals.add(new Point2D(-p4.getY(), p4.getX()));
    
    	
    	//normalise the normals
    	for(int i = 0; i <= this.length_segments; i++){
    		Point2D normal = normals.get(i);
    		float x = normal.getX();
    		float y = normal.getY();
    		
    		Vector3 v = new Vector3(x, y, 0);
    		v = v.normalize();
    		
    		normal = new Point2D(v.getX(), v.getY());
    		normals.set(i, normal);
    	}
    	
    	return normals;
    }
    
    public void draw(GL3 gl, Terrain terrain, CoordFrame3D frame){
    	this.normals = this.generateNormals();
    	float t;
    	ArrayList<Point2D> curve_points = new ArrayList<Point2D>();
    	ArrayList<Point2D> frontRow = new ArrayList<Point2D>();
    	ArrayList<Point2D> backRow;

    	//generate the list of curve points
    	for(int i = 0; i<=this.length_segments; i++){
    		t = (float)(i*this.size())/this.length_segments;
    		
    		//generate the point and transform it
    		curve_points.add(RoadCreationHelper.transformPoint(point(t), frame));
    	}
    	
    	
    	
    	
    	//generate the first row
    	for (int j = 0; j < this.width_segments; j++){
    		float x = normals.get(0).getX();
    		float y = normals.get(0).getY();
			frontRow.add(curve_points.get(0).translate(
					x*width*(float)(j-(float)(width_segments/2))/(float)width_segments,
					y*width*(float)(j-(float)(width_segments/2))/(float)width_segments));
		}
    	
    	//for every proceeeding row
    	for(int i = 1; i<= this.length_segments; i++){
    		t = (float)(i*this.size())/this.length_segments;

    		//make the back row the previous front row
    		backRow = frontRow;
    		frontRow = new ArrayList<Point2D>();

    		//calculate the current front row
    		float x1, y1;
        	for (int j = 0; j < this.width_segments; j++){
        		
        		x1 = normals.get(i).getX();
        		y1 = normals.get(i).getY();
    			frontRow.add(curve_points.get(i).translate(
    					x1*width*(float)(j-(float)(width_segments/2))/(float)width_segments,
    					y1*width*(float)(j-(float)(width_segments/2))/(float)width_segments));
    		}
        	
        	
        	//get vertices for strip
        	ArrayList<Point3D> vertices = new ArrayList<Point3D>();
        	ArrayList<Integer> indicies = new ArrayList<Integer>();
        	
        	//frontrow
        	for(int k = 0; k < this.width_segments; k++){
        		float x = frontRow.get(k).getX();
        		float z = frontRow.get(k).getY();
        		float delta = (k == 0 || k == width_segments) ? -0.01f : 0.01f;
        		float y = terrain.altitude(x, z) + delta;
        		
        		Point3D p = new Point3D(x,y,z);
        		vertices.add(p);
        	}
        	
        	//backrow
        	for(int k = 0; k < this.width_segments; k++){
        		float x = backRow.get(k).getX();
        		float z = backRow.get(k).getY();
        		float delta = (k == 0 || k == width_segments) ? -0.01f : 0.01f;
        		float y = terrain.altitude(x, z) + delta;
        		
        		Point3D p = new Point3D(x,y,z);
        		vertices.add(p);
        	}
        	
        	//generate an indice list
        	for(int k = 0; k < this.width_segments-1; k++){
        		
        		indicies.add(k+width_segments);
        		indicies.add(k+width_segments+1);
        		indicies.add(k+1);
        		
        		indicies.add(k+1);
        		indicies.add(k);
        		indicies.add(k+width_segments);

        		
        	}

        	TriangleMesh tm = new TriangleMesh(vertices, indicies, true);
        	tm.init(gl);
        	Shader.setPenColor(gl, new Color(0.2f, 0.2f, 0.2f));
        	tm.draw(gl, frame);
        	tm.destroy(gl);
    	}
    }

}
