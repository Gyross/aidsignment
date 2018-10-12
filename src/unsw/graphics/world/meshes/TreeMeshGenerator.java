package unsw.graphics.world.meshes;

import java.io.IOException;

import unsw.graphics.geometry.TriangleMesh;

public class TreeMeshGenerator {
	
	
	//to-do: add separate meshes for the trunk and foilage of the tree
	
	//to-do: texture this mesh
	/**
	 * Method that generates a triangle mesh for the tree
	 * @return
	 * @throws IOException
	 */
	public static TriangleMesh generateBasicTreeMesh() throws IOException{
		
		//to-do: texture this
		TriangleMesh tree = new TriangleMesh("res/models/tree.ply", true, true);
		return tree;
		
	}
	
}
