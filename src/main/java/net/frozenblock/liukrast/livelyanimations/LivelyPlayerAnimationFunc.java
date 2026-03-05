package net.frozenblock.liukrast.livelyanimations;

public class LivelyPlayerAnimationFunc {

    public static float legAnimation(final double time) {
        final float var1 = (float) (time / Math.PI);
        final float var2 = (float) (var1 - Math.floor(var1 / 2) * 2);
        return (float) (var2 < 1 ? Math.cos(Math.PI * var1) : Math.pow(Math.cos(Math.PI * (var2/2 - 1)), 4) * 2 -1);
    }

    public static float interpolate(float a, float b, float x) {
        if(x <= 0) return a;
        if(x >= 1) return b;
        final float dist = b - a;
        final float raw = (float) (x < 0.5f ? 2 * Math.pow(x, 2) : 1 - 2 * Math.pow(x - 1, 2));
        return raw * dist + a;
    }

    public static float smoothTo(float n, float k, float t) {
        return n + k * (t - n);
    }
}
