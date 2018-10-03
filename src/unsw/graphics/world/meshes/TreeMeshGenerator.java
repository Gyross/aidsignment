package unsw.graphics.world.meshes;

import java.io.IOException;

import unsw.graphics.geometry.TriangleMesh;

public class TreeMeshGenerator {
	
	public static TriangleMesh generateBasicTreeMesh() throws IOException{
		
		TriangleMesh tree = new TriangleMesh("res/models/tree.ply", true, true);
		return tree;
		
	}
	
}
