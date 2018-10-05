package unsw.graphics.world;

import java.awt.Color;

import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.scene3D.MeshSceneObject;
import unsw.graphics.scene3D.SceneObject3D;

public class TreeSceneObject extends SceneObject3D {

	private MeshSceneObject treeBase;
	
	public TreeSceneObject(TriangleMesh treeMesh,SceneObject3D parent){
		super(parent);
		
		treeBase = new MeshSceneObject(treeMesh, this);
		initTree();
		
	}
	
	public void initTree(){
		treeBase.translate(0f, 2.4f, 0.3f);
		treeBase.scale(1/2f);
		
	}
	
	public void setColor(Color c){
		treeBase.setColor(c);
	}
	
}
