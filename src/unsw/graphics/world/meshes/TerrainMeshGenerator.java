package unsw.graphics.world.meshes;

import java.util.ArrayList;

import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

public class TerrainMeshGenerator {

	
	//todo: texturing
	
	/**
	 * Method that generates the terrain mesh based off a vertices and indicies list
	 * these lists are built off terrain parameters
	 * 
	 * @note need to handle texturing here
	 * 
	 * @param width
	 * @param height
	 * @param altitudes
	 * @return
	 */
	public static TriangleMesh generateTerrainMesh(int width, int height, float[][] altitudes){
		ArrayList<Point3D> vertices = generateVertexList(width, height, altitudes);
		ArrayList<Integer> indicies = generateIndiciesList(width, height);
		//todo: texture stuff
		
		return new TriangleMesh(vertices, indicies, true);
	}
	
	
	
	/**
	 * Method to generate a list of vertices from the altitudes data for the terrain
	 * @param width
	 * @param height
	 * @param altitudes
	 * @return - the list of vertices
	 */
	public static ArrayList<Point3D> generateVertexList(int width, int height, float[][] altitudes){
		//prevent trivial error
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
			vertices.add(p);
		}
		return vertices;
	};
	
	/**
	 * Method to generate the texture coordinates for vertices in the mesh
	 * @param width
	 * @param height
	 * @param altitudes
	 * @return - the list of vertices
	 */
	public static ArrayList<Point2D> generateTextureCoordinateList(int width, int height){
		//prevent trivial error
		if(height == 0 || width == 0) return null;
		
		//list to contain all vertices in the desired order 
		ArrayList<Point2D> texCoordinates = new ArrayList<Point2D>();
		
		int x;
		int z;
		
		
		//iteration through all altitudes to generate the terrain vertices
		for (int i = 0; i<width*height; i++){
			
			//compute the x and z values for this vertex index
			x = i % width;
			z = (i - x)/height;
			
			Point2D p = new Point2D(x,z);
			
			//add the point to the list
			texCoordinates.add(p);
		}
		return texCoordinates;
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
			}
	
			//prevent overflowing (trying to create a triangle that doesn't exist)
			if(x+1 < width && z-1 >= 0){
					
				indicies.add(i);
				indicies.add(i+1);
				indicies.add(i+1-width);
			}
		}
		return indicies;
	}
	
	
	/**
	 * Method to generate the surface normals for a mesh
	 * 
	 * This method is redundant
	 * @param indicies
	 * @param vertices
	 * @return
	 */
	public static ArrayList<Vector3> generateNormalList(ArrayList<Integer> indicies, ArrayList<Point3D> vertices){
		ArrayList<Vector3> normals = new ArrayList<Vector3>();
		int size = indicies.size();
		size = size/3;
		for(int j = 0; j<3; j++)
		for(int i = 0; i<size; i++){
			
			int i0 = indicies.get(i);
			int i1 = indicies.get(i+1);
			int i2 = indicies.get(i+2);
			
			Point3D p0 = vertices.get(i0);
			Point3D p1 = vertices.get(i1);
			Point3D p2 = vertices.get(i2);
			
			Vector3 v1 = p1.minus(p0);
			Vector3 v2 = p2.minus(p0);

			Vector3 n = v1.cross(v2).normalize();
			normals.add(n);
		}

		return normals;
	}
	
	
	
}
