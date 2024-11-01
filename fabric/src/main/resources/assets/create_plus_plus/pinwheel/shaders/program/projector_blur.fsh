out vec4 fragColor;
in vec2 texCoord;

uniform sampler2D DiffuseSampler0;
uniform sampler2D ProjectorLightSampler;
uniform vec2 InSize;

void main() {
    vec4 original = texture(DiffuseSampler0, texCoord);
    vec4 lit = texture(ProjectorLightSampler, texCoord);

    int radius = 15;
    int step = 5;
    vec4 totalColor = vec4(0f);
    float sum = 0;

    vec2 oneTexel = (1f / InSize);

    for (int x = -radius; x <= radius; x += step) {
        for (int y = -radius; y <= radius; y += step) {
            float dist = 101 - length(vec2(x, y));
            float strength = 10f / dist;
            totalColor += texture(ProjectorLightSampler, texCoord + vec2(x, y) * oneTexel) * strength;
            sum += strength;
        }
    }

    fragColor = original + min(lit + (totalColor / sum) * 0.5f, vec4(1f));
}