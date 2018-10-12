package unsw.graphics.world.meshes;

import java.io.IOException;

import unsw.graphics.geometry.TriangleMesh;

public class MeshManager {

	private TriangleMesh playerMesh;
	private TriangleMesh terrainMesh;
	private TriangleMesh treeMesh;
	
	public MeshManager(){
		playerMesh = null;
		terrainMesh = null;
		treeMesh = null;
	}
	
	/**
	 * Method to generate the player Mesh
	 * @throws currently handles the exeption by printing
	 */
	public void generatePlayerMesh(){
		try{ playerMesh = PlayerMeshGenerator.generateBasicPlayerMesh(); }
		catch(IOException e){ 
			System.out.println("ERROR: PLAYER MESH NOT FOUND");
			playerMesh = null;
		}
	}
	
	/**
	 * Method to generate the treeMesh using the tree mesh generator
	 * @throws currently handles the exeption by printing
	 */
	public void generateTreeMesh(){
		try{ treeMesh = TreeMeshGenerator.generateBasicTreeMesh(); }
		catch(IOException e){ 
			System.out.println("ERROR: TREE MESH NOT FOUND");
			treeMesh = null;
		}
	}
	/**
	 * Method to generate the terrain mesh using terrain parameters
	 * @param width
	 * @param height
	 * @param altitudes
	 */
	public void generateTerrainMesh(int width, int height, float[][] altitudes){
		terrainMesh = TerrainMeshGenerator.generateTerrainMesh(width, height, altitudes);
	}
	
	public TriangleMesh getPlayerMesh(){
		return this.playerMesh;
	}
	
	public TriangleMesh getTreeMesh(){
		return this.treeMesh;
	}
	public TriangleMesh getTerrainMesh(){
		return this.terrainMesh;
	}
}
