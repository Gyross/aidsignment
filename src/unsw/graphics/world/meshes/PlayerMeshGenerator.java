package unsw.graphics.world.meshes;

import java.io.IOException;

import unsw.graphics.geometry.TriangleMesh;

public class PlayerMeshGenerator {

	//todo: texture this mesh
	/**
	 * Method that generates a triangle mesh for the player
	 * @return
	 * @throws IOException
	 */
	public static TriangleMesh generateBasicPlayerMesh() throws IOException{
		
		//todo: add texture
		TriangleMesh bunny = new TriangleMesh("res/models/bunny_res3.ply", true, true);
		return bunny;
			
	}
		

}
