package unsw.graphics.world.helper;

import java.util.ArrayList;

import unsw.graphics.geometry.Point3D;

public class TerrainCreationHelper {

	
	public void generateControlPoints(int width, int height, float[][] altitudes){
		ArrayList<ArrayList<Point3D>> controlPoints = new ArrayList<ArrayList<Point3D>>();
		
		for(int i = 0; i<3*height; i++){ controlPoints.add(new ArrayList<Point3D>()); }
		
		
	}
	
	
}
