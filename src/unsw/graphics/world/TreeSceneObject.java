package unsw.graphics.world;

import java.awt.Color;

import unsw.graphics.Texture;
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
	
	
	//shader colouring parameters setters
		public void setColor(Color c){ treeBase.setColor(c); }
		
		public void setTexture(Texture t) { treeBase.setTexture(t); }
		
		public void setAmbientColor(Color c){ treeBase.setAmbientColor(c); }
		public void setDiffuseColor(Color c){ treeBase.setDiffuseColor(c); }
		public void setSpecularColor(Color c){ treeBase.setSpecularColor(c); }
		
		public void setPhongExp(float phexp){ treeBase.setPhongExp(phexp); }
		
		
		//shader colouring parameters setters
		/*
		protected Color getColor(){ return surfaceColor; }
		
		protected Color getAmbientColor(){ return meshAmbientCoeff; }
		protected Color getDiffuseColor(){ return meshDiffuseCoeff; }
		protected Color getSpecularColor(){ return meshSpecularCoeff; }
		
		protected float getPhongExp(){ return meshPhongExp; }
		*/
	
	public void initTree(){
		treeBase.translate(0f, 2.4f, 0.3f);
		treeBase.scale(1/2f);
		
	}
	
	
}
