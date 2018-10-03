package unsw.graphics.world;



import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.scene3D.MeshSceneObject;
import unsw.graphics.scene3D.SceneObject3D;
import unsw.graphics.world.meshes.TerrainMeshGenerator;
import unsw.graphics.world.meshes.TreeMeshGenerator;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private int width;
    private int depth;
    private float[][] altitudes;
    private List<Tree> trees;
    private List<Road> roads;
    private Vector3 sunlight;
    
    private MeshSceneObject terrainSceneObject;
    
    private TriangleMesh treeMesh;
    private TriangleMesh terrainMesh;
    

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth, Vector3 sunlight) {
        this.width = width;
        this.depth = depth;
        altitudes = new float[width][depth];
        trees = new ArrayList<Tree>();
        roads = new ArrayList<Road>();
        this.sunlight = sunlight;
        
        
      
    }

    public List<Tree> trees() {
        return trees;
    }

    public List<Road> roads() {
        return roads;
    }

    public Vector3 getSunlight() {
        return sunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        sunlight = new Vector3(dx, dy, dz);      
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return altitudes[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, float h) {
        altitudes[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * @param x
     * @param z
     * @return
     */
    public float altitude(float x, float z) {
        float altitude = 0;

        // TODO: Implement this
        
        return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(float x, float z) {
        float y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        trees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(float width, List<Point2D> spine) {
        Road road = new Road(width, spine);
        roads.add(road);        
    }
    
    
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
    //Mesh Generation and Setter/Getter
    /////////// /////////// /////////// /////////// /////////// /////////// /////////// /////////// ///////////
    
    
    /**
     * Generates the terrain Mesh, storing the mesh in the terrain object
     */
    private void generateTerrainMesh(){
    	ArrayList<Point3D> vertices = TerrainMeshGenerator.generateVertexList(width, depth, altitudes);
    	ArrayList<Integer> indicies = TerrainMeshGenerator.generateIndiciesList(width, depth);
    	terrainMesh = new TriangleMesh(vertices, indicies, true);
    }
    
    /**
     * Generates the tree mesh, storing the mesh in the terrain object
     */
    private void generateTreeMesh(){
    	try{
	    	treeMesh = TreeMeshGenerator.generateBasicTreeMesh();
    	}
	    catch (IOException e){
    		System.out.println("Yoo tree file doesnt exist");
    	}
    }
    
    
    public TriangleMesh getTreeMesh(){
    	return treeMesh;
    }
    public TriangleMesh getTerrainMesh(){
    	return terrainMesh;
    }
    
    
 

    public void init(GL3 gl){
    	
        Shader shader = new Shader(gl, "shaders/vertex_phong.glsl",
                "shaders/fragment_phong.glsl");
        shader.use(gl);
        
        // Set the lighting properties
        Shader.setPoint3D(gl, "lightPos", new Point3D(0, 0, 5));
        Shader.setColor(gl, "lightIntensity", Color.WHITE);
        Shader.setColor(gl, "ambientIntensity", new Color(0.2f, 0.2f, 0.2f));
        
        // Set the material properties
        Shader.setColor(gl, "ambientCoeff", Color.WHITE);
        Shader.setColor(gl, "diffuseCoeff", new Color(0.5f, 0.5f, 0.5f));
        Shader.setColor(gl, "specularCoeff", new Color(0.8f, 0.8f, 0.8f));
        Shader.setFloat(gl, "phongExp", 16f);
        
        //generate meshes
        generateTerrainMesh();
        generateTreeMesh();
        treeMesh.init(gl);
        terrainMesh.init(gl);
        
        
    }
}
