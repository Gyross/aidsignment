
out vec4 outputColor;

uniform vec4 input_color;

uniform mat4 view_matrix;

// Light properties
uniform vec3 lightVec;
uniform vec3 lightPos;

uniform vec3 lightIntensity;
uniform vec3 sunLightIntensity;
uniform vec3 ambientIntensity;

// Material properties
uniform vec3 ambientCoeff;
uniform vec3 diffuseCoeff;
uniform vec3 specularCoeff;
uniform float phongExp;


in vec4 viewPosition;
in vec3 m;

void main()
{
    
	vec4 s4 = (view_matrix*vec4(lightPos,1) - viewPosition);
	vec3 s = s4.xyz;

	// Compute the s, v and r vectors
    vec3 s_sun = normalize(view_matrix*vec4(lightVec,0)).xyz;
	vec3 s_light = normalize(view_matrix*vec4(lightPos,1) - viewPosition).xyz;

    vec3 v = normalize(-viewPosition.xyz);

    vec3 r_sun = normalize(reflect(-s_sun,m));
	vec3 r_light = normalize(reflect(-s_light,m));


	//light intensities
    vec3 ambient = ambientIntensity*ambientCoeff;

    vec3 diffuse_sun = max(sunLightIntensity*diffuseCoeff*dot(m,s_sun), 0.0);
	vec3 diffuse_light = max(lightIntensity*diffuseCoeff*dot(m,s_light), 0.0);

    vec3 specular_sun;
 	vec3 specular_light;

    // Only show specular reflections for the front face
    if (dot(m,s_sun) > 0)
        specular_sun = max(sunLightIntensity*specularCoeff*pow(dot(r_sun,v),phongExp), 0.0);
    else
        specular_sun = vec3(0);

    // Only show specular reflections for the front face
    if (dot(m,s_light) > 0)
        specular_light = max(lightIntensity*specularCoeff*pow(dot(r_light,v),phongExp), 0.0);
    else
        specular_light = vec3(0);

    vec3 intensity1 = ambient + diffuse_sun + specular_sun;
	vec3 intensity2 = diffuse_light + specular_light;

	intensity2 = intensity2 * min(1, 100/( pow(dot(s,s),1) ));
	intensity2 = intensity2 * (2*pow(max(dot(v, vec3(0,0,1)), 0), 10) + 0.2);

	vec3 intensity = min(intensity1 + intensity2, 1);

    outputColor = vec4(intensity,1)*input_color;
}
