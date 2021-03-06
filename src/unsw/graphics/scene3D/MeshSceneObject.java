package unsw.graphics.scene3D;

import java.awt.Color;
import java.nio.ByteBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.geometry.TriangleMesh;

public class MeshSceneObject extends SceneObject3D{

	private TriangleMesh mesh;
	private Texture surfaceTexture;
	private Color surfaceColor;
	private static final Color default_color = Color.RED;
	private static final Color default_ambient =  new Color(0.5f, 0.5f, 0.5f);
	private static final Color default_specular = new Color(0.5f, 0.5f, 0.5f);
	private static final Color default_diffuse =  new Color(0.5f, 0.5f, 0.5f);
	private static final float default_exp = 16f;
	
	private Color meshAmbientCoeff;
	private Color meshSpecularCoeff;
	private Color meshDiffuseCoeff;
	private float meshPhongExp; 
	
	public MeshSceneObject(TriangleMesh meshIn, SceneObject3D parent){		
		super(parent);
		//default parameters
		meshAmbientCoeff = default_ambient;
		meshSpecularCoeff = default_specular;
		meshDiffuseCoeff = default_diffuse;
		meshPhongExp = default_exp;
		surfaceColor = default_color;
		surfaceTexture = null;
		
		mesh = meshIn;
		
	}

	//shader coloring and texturing parameters setters
	public void setTexture(Texture t) { surfaceTexture = t; }
	public void setColor(Color c){ surfaceColor = c; }
	
	public void setAmbientColor(Color c){ meshAmbientCoeff = c; }
	public void setDiffuseColor(Color c){ meshDiffuseCoeff = c; }
	public void setSpecularColor(Color c){ meshSpecularCoeff = c; }
	
	public void setPhongExp(float phexp){ meshPhongExp = phexp; }
	
	
	//shader colouring parameters setters
	protected Color getColor(){ return surfaceColor; }
	
	protected Color getAmbientColor(){ return meshAmbientCoeff; }
	protected Color getDiffuseColor(){ return meshDiffuseCoeff; }
	protected Color getSpecularColor(){ return meshSpecularCoeff; }
	
	protected float getPhongExp(){ return meshPhongExp; }

	
	@Override
	public void drawSelf(GL3 gl, CoordFrame3D frame){
		// If we aren't using a texture, set the color
		// otherwise set the texture
		if ( surfaceTexture == null ) {
			Shader.setInt(gl, "useTexture", 0);
			Shader.setPenColor(gl, surfaceColor);
		} else {
			Shader.setInt(gl, "tex", 0);
			Shader.setInt(gl, "useTexture", 1);
            gl.glActiveTexture(GL.GL_TEXTURE0);
            gl.glBindTexture(GL.GL_TEXTURE_2D, surfaceTexture.getId());
            
            Shader.setPenColor(gl, Color.WHITE);
		}
		
        // Set the material properties
        Shader.setColor(gl, "ambientCoeff", meshAmbientCoeff);
        Shader.setColor(gl, "diffuseCoeff", meshDiffuseCoeff);
        Shader.setColor(gl, "specularCoeff", meshSpecularCoeff);
        Shader.setFloat(gl, "phongExp", meshPhongExp);
		mesh.draw(gl, frame);
		
		// Switch back to default texture
	}
	
	
}
