package unsw.graphics.world;

import java.awt.Color;

import com.jogamp.opengl.GL3;

import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point3D;

public class WorldLighting {
	//defaults
	private static final String default_vertexShader = "shaders/vertex_phong.glsl";
	private static final String default_fragmentShader = "shaders/fragment_phong.glsl";
	private static final Point3D default_point = new Point3D(0,0,1);
	private static final Point3D default_dir = new Point3D(0,0,1);
	private static final Color default_intensity = Color.white;
	
	private Shader shader;
	
	
	//shader
	private String vertexShader = default_vertexShader;
	private String fragmentShader = default_fragmentShader;
	
	//lighting variables
	private Point3D lightVec = default_dir;
	private Point3D lightPos = default_point;
	
	private Color lightIntensity = default_intensity;
	private Color AmbientIntensity = default_intensity;
	
	/**
	 * Constructor for the world lighting class
	 * @param use_sun
	 */
	public WorldLighting(boolean use_sun){
		if (use_sun){
			vertexShader = "shaders/vertex_phong_sun.glsl";
			fragmentShader = "shaders/fragment_phong_sun.glsl";
		}
		shader = null;
	}
	
	
	/**
	 * Method that creates a shader based on the given shaders
	 * @param gl
	 */
	public void initLighting(GL3 gl){
		shader = new Shader(gl, vertexShader, fragmentShader);
		shader.use(gl);
		updateWorldLighting(gl);
		
	}
	
	/**
	 * Method that resets the properties of the light
	 * @param gl
	 */
	public void updateWorldLighting(GL3 gl){
        // Set the lighting properties
        Shader.setPoint3D(gl, "lightVec", lightVec);
        Shader.setPoint3D(gl, "lightPos", lightPos);
        Shader.setColor(gl, "ambientIntensity", AmbientIntensity);
        Shader.setColor(gl, "lightIntensity", lightIntensity);
	}
	
	//property setters
	public void setLightVector(Point3D p){
		lightVec = p;
	}
	public void setLightPos(Point3D p){
		lightPos = p;
	}
	public void setAmbientInt(Color c){
		AmbientIntensity = c;
	}
	public void setLightInt(Color c){
		lightIntensity = c;
	}
	
	/**
	 * updates the sunlight lighting
	 * if intensities change with direction, they can be updated here
	 * @param gl
	 * @param dir
	 */
	public void updateSunlightLighting(GL3 gl, Point3D dir){	
		//float intensity = this.getSunlight().dotp(new Vector3(0,1,0));
	    //if(intensity < 0.2) intensity = 0.2f;
		//this.setLightInt(intensity,intensity,intensity);
		 
		this.setLightVector(dir);
	    this.updateWorldLighting(gl);
	    	
	 }
	
}
