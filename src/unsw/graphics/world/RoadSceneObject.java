package unsw.graphics.world;

import java.awt.Color;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.scene3D.MeshSceneObject;
import unsw.graphics.scene3D.SceneObject3D;
import unsw.graphics.world.helper.RoadCreationHelper;

public class RoadSceneObject extends MeshSceneObject{

	private Road road;
	private Terrain terrain;
	
	public RoadSceneObject(TriangleMesh tm, Road road, Terrain t, SceneObject3D s){
		super(tm, s);
		this.road = road;
		this.terrain = t;
		this.setSpecularColor(new Color(0.2f, 0.2f, 0.2f));
	}
	
	@Override
	public void draw(GL3 gl, CoordFrame3D c){
		gl.glEnable(gl.GL_POLYGON_OFFSET_FILL);
		gl.glPolygonOffset(-1, -1);
		super.draw(gl, c);
		gl.glPolygonOffset(0, 0);
		gl.glDisable(gl.GL_POLYGON_OFFSET_FILL);
	}

	
}
