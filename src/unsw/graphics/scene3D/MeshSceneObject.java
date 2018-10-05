package unsw.graphics.scene3D;

import java.awt.Color;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.TriangleMesh;

public class MeshSceneObject extends SceneObject3D{

	private TriangleMesh mesh;
	private Color surfaceColor;
	private static final Color default_color = Color.RED;
	
	public MeshSceneObject(TriangleMesh meshIn, SceneObject3D parent){		
		super(parent);
		mesh = meshIn;
		surfaceColor = default_color;
	}

	public void setColor(Color c){
		surfaceColor = c;
	}
	
	
	@Override
	public void drawSelf(GL3 gl, CoordFrame3D frame){
		Shader.setPenColor(gl, surfaceColor);
		mesh.draw(gl, frame);
	}
}
