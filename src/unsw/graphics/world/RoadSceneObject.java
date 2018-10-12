package unsw.graphics.world;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.scene3D.MeshSceneObject;
import unsw.graphics.scene3D.SceneObject3D;

public class RoadSceneObject extends MeshSceneObject{

	private Road road;
	private Terrain terrain;
	
	public RoadSceneObject(Road road, Terrain t, SceneObject3D s){
		super(null, s);
		this.road = road;
		this.terrain = t;
	}
	
	
	@Override
	public void drawSelf(GL3 gl, CoordFrame3D frame){
		
		//set the color
		Shader.setPenColor(gl, getColor());
		
        // Set the material properties
        Shader.setColor(gl, "ambientCoeff", this.getAmbientColor());
        Shader.setColor(gl, "diffuseCoeff", this.getDiffuseColor());
        Shader.setColor(gl, "specularCoeff", this.getSpecularColor());
        Shader.setFloat(gl, "phongExp", this.getPhongExp());
		
		road.draw(gl, terrain, frame);
	}
	
}
