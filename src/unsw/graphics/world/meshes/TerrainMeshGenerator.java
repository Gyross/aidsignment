package unsw.graphics.world.meshes;

import java.util.ArrayList;

import unsw.graphics.geometry.Point3D;

public class TerrainMeshGenerator {

	/**
	 * Method to generate a list of vertices from the altitudes data for the terrain
	 * @param width
	 * @param height
	 * @param altitudes
	 * @return - the list of vertices
	 */
	public static ArrayList<Point3D> generateVertexList(int width, int height, float[][] altitudes){
		//prevent trivial error
		
        for(int i=0; i< width; i++){
        	for(int j=0; j<width; j++){
        		System.out.println("altitudes[" + i +"][" + j +"] = " + altitudes[i][j]);
        	}
        }
		
		if(height == 0 || width == 0) return null;
		
		//space coordinates
		int x;
		float y;
		int z;
		
		//list to contain all vertices in the desired order 
		ArrayList<Point3D> vertices = new ArrayList<Point3D>();
		
		//iteration through all altitudes to generate the terrain vertices
		for (int i = 0; i<width*height; i++){
			
			//compute the x and y values for this vertex index
			x = i % width;
			z = (i - x)/height;
			
			//retrieve the z value from the altitudes array
			y = altitudes[x][z];
			Point3D p = new Point3D(x, y, z);
			
			//add the point to the list
			System.out.println("POINT: " + p.getX()+","+p.getY()+","+p.getZ());
			vertices.add(p);
		}
		return vertices;
	};
	
	/**
	 * Method that generates the indices list for the faces of the triangle mesh
	 * @param width
	 * @param height
	 * @return - the list of indices
	 */
	public static ArrayList<Integer> generateIndiciesList(int width, int height){
		
		//prevent trivial error
		if(height == 0 || width == 0) return null;
		
		//space coordinates
		int x;
		int z;
		
		//list to contain all indices in the desired order
		ArrayList<Integer> indicies = new ArrayList<Integer>();
		
		//iteration through all vertices
		for(int i = 0; i<width*height; i++){
			
			//compute the x and y values for this vertex index
			x = i % width;
			z = (i - x)/height;
			
			//prevent overflowing (trying to create a triangle that doesn't exist)
			if(x+1 < width && z+1 < height){
				
				indicies.add(i);
				indicies.add(i+width);
				indicies.add(i+1);
				System.out.println("INDICES: " + i+","+(i+width)+","+(i+1));
			}
	
			//prevent overflowing (trying to create a triangle that doesn't exist)
			if(x+1 < width && z-1 >= 0){
					
				indicies.add(i);
				indicies.add(i+1);
				indicies.add(i+1-width);
				System.out.println("INDICES: " + i+","+(i+1)+","+(i+1-width));
			}
		}
		return indicies;
	}
	
	
	
	
}
