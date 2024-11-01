#include veil:deferred_utils

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;

uniform sampler2D ProjectionDepthSampler;
uniform sampler2D ProjectionResults;

in vec2 texCoord;

uniform vec3 origin;
uniform vec3 direction;

uniform mat4 DepthMatrix;
uniform float BaseAspect;

uniform float ProjectorPlaneNear;
uniform float ProjectorPlaneFar;

uniform mat4 DepthMat;
uniform mat4 DepthModelMat;

uniform vec2 projectorOneTexel;

out vec4 fragColor;


bool isInProjectionSpace(vec2 projectorSpace) {
    return (projectorSpace.x > 0.005f && projectorSpace.y > 0.005f && projectorSpace.x < 0.995f && projectorSpace.y < 0.995f);
    //    return true;
}

float toDepthInProjectorDepthSpace(vec3 worldPosition) {
    vec3 relative = worldPosition - origin;

    vec4 clipSpacePos = (DepthMat * vec4(relative, 1.0f));

    float projectorSpace = (clipSpacePos.xyz / clipSpacePos.w).z;

    return (projectorSpace + 1.0f) / 2.0f;
}


vec2 toProjectorDepthSpace(vec3 worldPosition) {
    vec3 relative = worldPosition - origin;

    vec4 clipSpacePos = (DepthMat * vec4(relative, 1.0f));

    vec2 projectorSpace = (clipSpacePos.xyz / clipSpacePos.w).xy;

    return (projectorSpace + vec2(1.0f, 1.0f)) / vec2(2.0f, 2.0f);
}


float projectorDepthSampleToWorldDepth(float depthSample) {
    float f = depthSample * 2.0 - 1.0;
    return 2.0 * ProjectorPlaneNear * ProjectorPlaneFar / (ProjectorPlaneFar + ProjectorPlaneNear - f * (ProjectorPlaneFar - ProjectorPlaneNear));
}

vec3 projectorPosFromDepth(float depth, vec2 uv) {
    vec4 positionCS = vec4(uv, depth, 1.0) * 2.0 - 1.0;
    vec4 positionVS = DepthMat * positionCS;
    positionVS /= positionVS.w;

    return positionVS.xyz;
}

vec3 projectorViewToWorldSpace(vec3 positionVS) {
    return origin + positionVS;
}

uniform int ShouldMix;

void main() {
    vec4 original = vec4(0.0f);

    if (ShouldMix == 1) {
        original = texture(ProjectionResults, texCoord);
    }

    float depth = texture(DiffuseDepthSampler, texCoord).r;
    vec3 pos = viewToWorldSpace(viewPosFromDepth(depth, texCoord));
    vec2 projectorSpace = toProjectorDepthSpace(pos);

    fragColor = original;
    if (isInProjectionSpace(projectorSpace) && dot(direction, normalize(pos - origin)) > 0.0f) {
        float distanceFromOrigin = length(pos - origin);
        if (distanceFromOrigin > ProjectorPlaneFar) return;

        float distanceScaled = distanceFromOrigin / ProjectorPlaneFar;

        float projectorDepth = texture(ProjectionDepthSampler, projectorSpace).r;
        float currentDepth = toDepthInProjectorDepthSpace(pos);
        float difference = (projectorDepth - currentDepth) * ProjectorPlaneFar;

        bool fullOcclusion = true;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) continue;
                if (texture(ProjectionDepthSampler, projectorSpace + projectorOneTexel * vec2(x, y)).r > currentDepth) {
                    fullOcclusion = false;
                }
            }
        }

        float strength;
        if (difference < 0) {
            if (!fullOcclusion) {
                difference /= 100;
            } else {
                difference *= 10;
            }
            float scale = 0.5f/ProjectorPlaneFar;
            strength = (scale - abs(difference)) / scale;
            strength = min(strength, 1);
        } else {
            strength = 1;
        }
        strength *= 100;
        strength /= distanceFromOrigin;

        if (strength > 0f) {
            fragColor = max(original, original + (vec4(0.04f, 0.02f, 0.1f, 1.0f) * strength));
        }
    }
}
