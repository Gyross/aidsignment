package unsw.graphics.world.helper;

import java.awt.Color;
import java.util.ArrayList;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.Vector4;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.world.Road;
import unsw.graphics.world.Terrain;

public class RoadCreationHelper {

	
	public static Point2D transformPoint(Point2D p, CoordFrame3D c){
		Point3D p3D = new Point3D(p.getX(), 0, p.getY());
		Vector4 v = p3D.asHomogenous();
		v = c.getMatrix().multiply(v);
		p3D = v.asPoint3D();	
		return new Point2D(p3D.getX(), p3D.getZ());
	}
	
	/**
	 * Method that will generate a list points along the length of the road
	 * @param r
	 * @return
	 */
	public static ArrayList<Point2D> generateCurvePoints(Road r){
		ArrayList<Point2D> curve_points = new ArrayList<Point2D>();
    	float t;
    	for(int i = 0; i<= r.getLengthSegments(); i++){
    		t = (float)( i*r.size() )/r.getLengthSegments();
    		curve_points.add(r.point(t));
    	}
		return curve_points;
	}
	
	
	public static ArrayList<Point2D> generateCurveNormals(Road r){
		ArrayList<Point2D> curve_points = generateCurvePoints(r);
		ArrayList<Point2D> normals = new ArrayList<Point2D>();
		
		//add normal to first point
    	Point2D p0 = curve_points.get(1).translate(
    			-curve_points.get(0).getX(), 
    			-curve_points.get(0).getY());
    	
    	normals.add(new Point2D(-p0.getY(), p0.getX()));
    	    	

    	//add all intermediate normals
    	for(int i = 1; i < r.getLengthSegments(); i++){
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
    	Point2D p4 = curve_points.get(r.getLengthSegments()).translate(
    			-curve_points.get(r.getLengthSegments()-1).getX(), 
    			-curve_points.get(r.getLengthSegments()-1).getY());
    	
    	normals.add(new Point2D(-p4.getY(), p4.getX()));
    	//normalise everything
    	normalizePoints(normals);
    	return normals;
	}
	
	private static void normalizePoints(ArrayList<Point2D> normals){
		
		//normalise the normals
    	for(int i = 0; i < normals.size(); i++){
    		Point2D normal = normals.get(i);
    		float x = normal.getX();
    		float y = normal.getY();
    		
    		Vector3 v = new Vector3(x, y, 0);
    		v = v.normalize();
    		
    		normal = new Point2D(v.getX(), v.getY());
    		normals.set(i, normal);
    	}
	}
	
	
	private static ArrayList<Point2D> generatePointRow(Point2D normal, Point2D curvePoint, double width, int width_segments){
		//generate a row
		ArrayList<Point2D> pointRow = new ArrayList<Point2D>();
    	for (int j = 0; j < width_segments; j++){
    		float x = normal.getX();
    		float y = normal.getY();
			pointRow.add(curvePoint.translate(
					x*(float)width*(float)(j-(float)(width_segments/2))/(float)width_segments,
					y*(float)width*(float)(j-(float)(width_segments/2))/(float)width_segments));
		}
		return pointRow;
	}
	
	private static ArrayList<Point3D> generate3DPointRow(Road r, Terrain t, ArrayList<Point2D> row){
		ArrayList<Point3D> row3D = new ArrayList<Point3D>();
		
    	for(int k = 0; k < r.getWidthSegments(); k++){
    		float x = row.get(k).getX();
    		float z = row.get(k).getY();
    		float delta = (k == 0 || k == r.getWidthSegments()) ? -0.01f : 0.01f;
    		float y = t.altitude(x, z) + delta;
    		
    		Point3D p = new Point3D(x,y,z);
    		row3D.add(p);
    	}
    	return row3D;
	}
	
	public static TriangleMesh generateRoadMesh(Road r, Terrain terrain){
		ArrayList<Point2D> normals = generateCurveNormals(r);
		ArrayList<Point2D> curvePoints = generateCurvePoints(r);
		
		//mesh lists
		ArrayList<Point3D> vertices = new ArrayList<Point3D>();
		ArrayList<Integer> indicies = new ArrayList<Integer>();
		
		//row lists
		ArrayList<Point2D> frontRow;
		ArrayList<Point2D> backRow;
		
		ArrayList<Point3D> frontRow3D;
		ArrayList<Point3D> backRow3D;
		
		//generate first row
		frontRow = generatePointRow(normals.get(0), curvePoints.get(0),
								r.width(), r.getWidthSegments());
		//generate the points on the terrain of this row
		frontRow3D = generate3DPointRow(r, terrain, frontRow);
		
		//add all vertices in the front row to the vertex list
		vertices.addAll(frontRow3D);

		
		//for every proceeeding row
    	for(int i = 1; i <= r.getLengthSegments(); i++){

    		//make the back row the previous front row
    		backRow3D = frontRow3D;
    		
    		//calculate the current front row
    		frontRow = generatePointRow(normals.get(i), curvePoints.get(i),
					r.width(), r.getWidthSegments());
    		//generate the list of terrain points
        	frontRow3D = generate3DPointRow(r, terrain, frontRow);
        	
        	//add all vertices
        	vertices.addAll(frontRow3D);

        	
        	//generate an indice list
        	for(int k = 0; k < r.getWidthSegments()-1; k++){
        		
        		indicies.add(k+0+r.getWidthSegments()*i);
        		indicies.add(k+0+r.getWidthSegments()*(i-1));
        		indicies.add(k+1+r.getWidthSegments()*i);
        		
        		
        		indicies.add(k+1+r.getWidthSegments()*(i-1));
        		indicies.add(k+1+r.getWidthSegments()*i);
        		indicies.add(k+0+r.getWidthSegments()*(i-1));
     

        	}

    	}
    	TriangleMesh tm = new TriangleMesh(vertices, indicies, true);    	
    	return tm;
    	
	}
	
	

}
