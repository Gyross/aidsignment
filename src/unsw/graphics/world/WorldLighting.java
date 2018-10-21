package unsw.graphics.world;

import java.awt.Color;

import com.jogamp.opengl.GL3;

import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.scene3D.SceneObject3D;

public class WorldLighting {
	//defaults
	private static final String default_vertexShader = "shaders/vertex_phong.glsl";
	private static final String default_fragmentShader = "shaders/fragment_phong.glsl";
	private static final Point3D default_point = new Point3D(0,0,1);
	private static final Point3D default_dir = new Point3D(0,0,1);
	private static final Color default_intensity = Color.white;
	private static final Color darkness = Color.BLACK;
	
	private Shader shader;
	
	
	//shader
	private String vertexShader = default_vertexShader;
	private String fragmentShader = default_fragmentShader;
	
	//lighting variables
	private Point3D lightVec = default_dir;
	private Point3D lightPos = default_point;
	
	private float cutOff = 0.8f;
	private float torchAttenuation = 20;
	
	private Color sunLightIntensity = default_intensity;
	private Color lightIntensity = default_intensity;
	private Color AmbientIntensity = default_intensity;
	
	private static boolean torchOn;

	
	/**
	 * Constructor for the world lighting class
	 * @param use_sun
	 */
	public WorldLighting(boolean use_sun){
		if (use_sun){
			vertexShader = "shaders/vertex_tex_sun.glsl";
			fragmentShader = "shaders/fragment_tex_sun.glsl";
		}
		shader = null;
		
		torchOn = false;
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
        Shader.setColor(gl, "lightIntensity", ((torchOn) ? lightIntensity: darkness));
        Shader.setColor(gl, "sunLightIntensity", sunLightIntensity);
        
        Shader.setFloat(gl, "cutOff", cutOff);
        Shader.setFloat(gl, "torchAttenuation", torchAttenuation);
        
        
	}
	
	//property settersq
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
	public void setSunLightInt(Color c){
		sunLightIntensity = c;
	}
	public void setTorchAtten(float f){
		torchAttenuation = f;
	}
	public void setTorchCutOff(float f){
		cutOff = f;
	}
	
	/**
	 * updates the sunlight lighting
	 * if intensities change with direction, they can be updated here
	 * @param gl
	 * @param dir
	 */
	public void updateSunlightLighting(GL3 gl, Point3D dir){	
		Vector3 v = dir.asHomogenous().trim();
		float factor = (1 + v.dotp(new Vector3(1,0,0)))/2;
		float factor2 = (1 + v.dotp(new Vector3(0,1,0)))/2;
		float factor3  = v.dotp(new Vector3(0,1,0)) > 0 ? v.dotp(new Vector3(0,1,0)):0;
		float intensity = (float) Math.pow(factor2, 2/5);
		
	    
	    Color t1 = new Color(0.3f, 0.3f, 1f);
	    Color t2 = new Color(1f, 1f, 1f);
	    Color t3 = new Color(1f, 13/16f, 0f);
	    
	    
	    /*
	    float t1Sig = intensity*(v.dotp(new Vector3(1,0,0))/(2*256);
	    float t2Sig = intensity*v.dotp(new Vector3(0,1,0))/(2*256);
	    float t3Sig = intensity*v.dotp(new Vector3(-1,0,0))/(2*256);
	      */
	    float t1Sig = (1-factor3)*factor*intensity/256;
	    float t2Sig = factor3*intensity/256;
	    float t3Sig = (1-factor3)*(1-factor)*intensity/256;
	    
	    if(t1Sig < 0) t1Sig = 0;
	    if(t2Sig < 0) t2Sig = 0;
	    if(t3Sig < 0) t3Sig = 0;
	    Color sunlightColor = new Color(
	    		t1.getRed()*t1Sig 	+ t2.getRed()*t2Sig  	+ t3.getRed()*t3Sig,
	    		t1.getGreen()*t1Sig + t2.getGreen()*t2Sig  	+ t3.getGreen()*t3Sig,
	    		t1.getBlue()*t1Sig 	+ t2.getBlue()*t2Sig  	+ t3.getBlue()*t3Sig);
	  
	    
		this.setSunLightInt(sunlightColor);
		this.setLightVector(dir);
	    this.updateWorldLighting(gl);
	    	
	 }
	
    /**
     * Method to initialise the lighting to the desired parameters
     */
    public void initLightingColor(Terrain terrain){	
    	this.setLightVector(terrain.getSunlight().asPoint3D());
    	this.setAmbientInt(new Color(0.7f, 0.7f, 0.7f));
    	this.setSunLightInt(Color.WHITE);
    	this.setLightInt(Color.WHITE);	
    }


	public void updateLightPos(Point3D position) {
		this.setLightPos(position);
	}
	
	public static void toggleTorch(){
		torchOn = !torchOn;
	}

}
