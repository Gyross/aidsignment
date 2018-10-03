package unsw.graphics.scene3D;

import java.awt.Color;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.TriangleMesh;

public class MeshSceneObject extends SceneObject3D{

	private TriangleMesh mesh;

	
	public MeshSceneObject(TriangleMesh meshIn, SceneObject3D parent){		
		super(parent);
		mesh = meshIn;
	}
	/*
	public MeshSceneObject(TriangleMesh meshIn){		
		mesh = meshIn;
	}
	*/
	
	@Override
	public void drawSelf(GL3 gl, CoordFrame3D frame){
		Shader.setPenColor(gl, Color.red);
		mesh.draw(gl, frame);
		System.out.println("drawn");
	}
}
