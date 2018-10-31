#version 120

uniform sampler2D DiffuseSampler;
uniform vec3 rgba;
uniform float greybase;
uniform vec3 one = vec3(1.0, 1.0, 1.0);

vec3 rgb2hsv(vec3 c);
vec3 hsv2rgb(vec3 c);

void main()
{
    vec4 sample = texture2D(DiffuseSampler, vec2(gl_TexCoord[0]));
    vec3 diff = one - rgba;
    vec3 minDiff = vec3(greybase, greybase, greybase);
    vec3 saturation = diff * ((sample.xyz - minDiff) / (one - minDiff));
    gl_FragColor = vec4(rgba + saturation, sample.a);
}
