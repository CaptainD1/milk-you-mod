package captaind1.milkyou.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AnimatedOverlayTexture {

    public static final ResourceLocation RENDER_BINDING = new ResourceLocation("minecraft", "crosshair");

    private int animationTick = 0;
    private boolean isAnimating = false;

    public final int animationLength;
    public final ResourceLocation textureLocation;
    public final int frameWidth;
    public final int frameHeight;
    public final int maxOpacity;
    public final double scale;

    public AnimatedOverlayTexture(ResourceLocation textureLocation, int numFrames, int width, int height, int maxOpacity, double scale) {
        this.textureLocation = textureLocation;
        animationLength = numFrames;
        frameWidth = width;
        frameHeight = height;
        this.maxOpacity = maxOpacity;
        this.scale = scale;
    }

    public void tick() {
        if (!isAnimating) return;
        if (animationTick >= animationLength) {
            stop();
            return;
        }

        ++animationTick;
    }

    public void render() {
        RenderSystem.setShaderTexture(0, textureLocation);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder renderer = tesselator.getBuilder();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

        int red = 255;
        int green = 255;
        int blue = 255;

        // Opacity is a parabola with zero points at the start and end of the animation and vertex at max opacity
        double a = animationLength/(float)2;
        double b = (animationTick - a);
        int opacity = (int) (maxOpacity * (-1/(a*a) * b*b + 1));

        double z = -90;

        // Determine frame to render by shifting texture UV coordinates
        float uvTop = (float) animationTick / (float) animationLength;
        float uvBottom = (float) (animationTick + 1) / (float) animationLength;

        renderer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        renderer.vertex(0.0D, frameHeight * scale, z).uv(0.0F, uvBottom).color(red, green, blue, opacity).endVertex();
        renderer.vertex(frameWidth * scale, frameHeight * scale, z).uv(1.0F, uvBottom).color(red, green, blue, opacity).endVertex();
        renderer.vertex(frameWidth * scale, 0.0D, z).uv(1.0F, uvTop).color(red, green, blue, opacity).endVertex();
        renderer.vertex(0.0D, 0.0D, z).uv(0.0F, uvTop).color(red, green, blue, opacity).endVertex();
        tesselator.end();
    }

    public void start() {
        isAnimating = true;
    }

    public void stop() {
        isAnimating = false;
        animationTick = 0;
    }

    public boolean isReady() {
        return animationTick == 0 && !isAnimating;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    @SubscribeEvent
    public void renderGameOverlay(RenderGuiOverlayEvent.Post event) {
        // Only render once per frame by rendering when a specific vanilla UI element is rendering
        // Well, this still appears to still run multiple times per frame, but it seems to work
        if (isAnimating && event.getOverlay().id().equals(RENDER_BINDING)) {
            render();
        }
    }
}
