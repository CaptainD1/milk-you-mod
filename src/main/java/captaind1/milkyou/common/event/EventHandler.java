package captaind1.milkyou.common.event;

import captaind1.milkyou.MilkYouMod;
import captaind1.milkyou.client.config.MilkYouConfig;
import captaind1.milkyou.client.gui.overlay.AnimatedOverlayTexture;
import captaind1.milkyou.common.registry.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Level;

public class EventHandler {

    public static int animationCooldown;

    public static final ResourceLocation MARKIPLIER_LOCATION =
            new ResourceLocation(MilkYouMod.MODID, "textures/misc/milk_you.png");
    public static final ResourceLocation LUKE_MILK_LOCATION =
            new ResourceLocation(MilkYouMod.MODID, "textures/misc/luke_milk.png");
    // TODO: Calculate frame count automatically and add MCMETA file or something to handle width and height
    public static AnimatedOverlayTexture milkyouAnimation;
    public static AnimatedOverlayTexture lukeMilkAnimation;

    private static int cooldownTick = 0;

    public static void register(IEventBus eventBus) {
        eventBus.register(milkyouAnimation);
        eventBus.register(lukeMilkAnimation);
    }

    public static void init() {
        animationCooldown = MilkYouConfig.CLIENT.MILK_YOU_COOLDOWN.get();
        milkyouAnimation = new AnimatedOverlayTexture(MARKIPLIER_LOCATION, 21, 256, 256,
                (int) (MilkYouConfig.CLIENT.MARK_OPACITY.get() * 255),
                MilkYouConfig.CLIENT.MARK_SCALE.get());
        lukeMilkAnimation = new AnimatedOverlayTexture(LUKE_MILK_LOCATION, 51, 611, 256,
                (int) (MilkYouConfig.CLIENT.LUKE_OPACITY.get() * 255),
                MilkYouConfig.CLIENT.LUKE_SCALE.get());

        // Load textures on startup to prevent lag spike the first time animations play after launch
        TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        MilkYouMod.LOGGER.log(Level.INFO, "Loading Markiplier Texture");
        texturemanager.getTexture(MARKIPLIER_LOCATION);
        MilkYouMod.LOGGER.log(Level.INFO, "Loading Luke Texture");
        texturemanager.getTexture(LUKE_MILK_LOCATION);
    }

    @SubscribeEvent
    public static void onPlayerClientTick(TickEvent.PlayerTickEvent event) {
        // Only if client side and start of tick should the animations actually tick
        if (event.player.isLocalPlayer() && event.phase == TickEvent.Phase.START) {
            lukeMilkAnimation.tick();
            milkyouAnimation.tick();

            // If the milk animation isn't currently running and not on cooldown...
            if (milkyouAnimation.isReady() && cooldownTick == 0) {
                // ...check if player is holding a bucket...
                if (event.player.getInventory().getSelected().is(Items.BUCKET)) {
                    // ...and looking at a cow while the mod is enabled
                    Entity lookingAt = Minecraft.getInstance().crosshairPickEntity;
                    if (lookingAt != null && lookingAt.getType().equals(EntityType.COW) && MilkYouMod.isModEnabled()) {
                        event.player.playSound(SoundRegistry.MILK_YOU.get());
                        milkyouAnimation.start();
                        cooldownTick = 1;
                    }
                }
            } else if (cooldownTick > 0) {
                ++cooldownTick;
                if (cooldownTick >= animationCooldown) {
                    cooldownTick = 0;
                }
            }
        }
    }
}
