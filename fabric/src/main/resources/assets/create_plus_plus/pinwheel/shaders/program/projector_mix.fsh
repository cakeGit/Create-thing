out vec4 fragColor;
in vec2 texCoord;

uniform sampler2D DiffuseSampler0;
uniform sampler2D ProjectorLightSampler;

vec4 saturate(vec4 colorRBGA) {
    vec3 colorRGB = colorRBGA.rgb;
    float meanColor = (colorRGB.r + colorRGB.g + colorRGB.b) / 3f;
    vec3 diff = (colorRGB - meanColor);
    float strength = 1.0f;
    colorRGB = (diff * 2f) + vec3(meanColor);
    return vec4(colorRGB, 1);
}

void main() {
    vec4 lightColor = texture(ProjectorLightSampler, texCoord);
    vec4 original = texture(DiffuseSampler0, texCoord);
    fragColor = lightColor * 0.2f + (saturate(original) * lightColor * 5f);
}