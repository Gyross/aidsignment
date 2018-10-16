package unsw.graphics.world;

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
	}

	
}
