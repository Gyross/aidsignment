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
import unsw.graphics.scene.MathUtil;
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
        
        //get an x,z coordinate within the terrain
        float my_x = MathUtil.clamp(x, 0, width-2);
        float my_z = MathUtil.clamp(z, 0, depth-2);
        
        //get the x,z offset within a tile
        float x_mod = my_x % 1;
        float z_mod = my_z % 1;
        
        
        //get the tile number
        int x_int = (int)(my_x - x_mod);
        int z_int = (int)(my_z - z_mod);
        
        //upper half of tile (lower z)
        if(x_mod < 1 - z_mod){
        	return triangleInterpolateUpper(
        			altitudes[x_int][z_int],
        			altitudes[x_int][z_int+1],
        			altitudes[x_int+1][z_int], 
        			altitudes[x_int+1][z_int+1],
        			x_mod, 
        			z_mod);
        }
        //lower half of tile (greater z)
        else{
        	return triangleInterpolateLower(
         			altitudes[x_int][z_int],
        			altitudes[x_int][z_int+1],
        			altitudes[x_int+1][z_int], 
        			altitudes[x_int+1][z_int+1],
        			x_mod, 
        			z_mod);	
        }       
    }
    
    
    /**
     * interpolate the altitude of a point in the lower half of a tile
     * @param topLeftAlt
     * @param botLeftAlt
     * @param topRightAlt
     * @param x
     * @param z
     * @return
     */
    private float triangleInterpolateLower(float topLeftAlt, float botLeftAlt, float topRightAlt, float botRightAlt, float x, float z ){
    	//Vector3 vec1 = new Vector3(0, topLeftAlt - botLeftAlt, 1);
    	//Vector3 vec2 = new Vector3(1, topRightAlt - botLeftAlt, 0);
    	//Vector3 base = new Vector3(0, botLeftAlt, 0);
    	
    	//due to the orientation of array, going downwards increases z, 

    	//start at base point
    	float height = botRightAlt;
    	//translate along the slope of the x direction
    	height += (botLeftAlt - botRightAlt)*(1-x);
    	//translate along the slope of the y direction
    	height += (topRightAlt - botRightAlt)*(1-z);
    	
    	return height;
    }

    /**
     * interpolate the altitude of a point in the lower half of a tile
     * @param topLeftAlt
     * @param botLeftAlt
     * @param topRightAlt
     * @param x
     * @param z
     * @return
     */
    private float triangleInterpolateUpper(float topLeftAlt, float botLeftAlt, float topRightAlt, float botRightAlt, float x, float z ){
    	//Vector3 vec1 = new Vector3(1, botRightAlt - botLeftAlt, 0);
    	//Vector3 vec2 = new Vector3(1, topRightAlt - botRightAlt, 0);
    	//Vector3 base = new Vector3(0, botRightAlt, 0);
    	//start at base point
    	float height = topLeftAlt;
    	//translate along the slope of the x direction
    	height += (topRightAlt - topLeftAlt)*(x);
    	//translate along the slope of the y direction
    	height += (botLeftAlt - topLeftAlt)*(z);
    	
    	return height;
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
    
    
    public void terrainPlace(SceneObject3D s, float x, float z){
    	float y = altitude(x, z);
    	s.setPosition(x, y, z);
    }
    

    public void addTrees(SceneObject3D terrain){
    	for(Tree t : trees){
    		TreeSceneObject tobj = new TreeSceneObject(getTreeMesh(), terrain);
    		terrainPlace(tobj, t.getPosition().getX(), t.getPosition().getZ());
    	}
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
