package captaind1.milkyou;

import captaind1.milkyou.client.config.MilkYouConfig;
import captaind1.milkyou.common.event.EventHandler;
import captaind1.milkyou.common.registry.SoundRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

@Mod(MilkYouMod.MODID)
public class MilkYouMod {
    public static final String MODID = "milkyou";
    public static Logger LOGGER = LogManager.getLogger(MODID);

    public MilkYouMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        SoundRegistry.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MilkYouConfig.CLIENT_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EventHandler.init();
            EventHandler.register(MinecraftForge.EVENT_BUS);
        }
    }

    public static boolean isModEnabled() {
        // Check for April Fools mode
        if (MilkYouConfig.CLIENT.APRIL_FOOLS_MODE.get()) {
            LocalDate date = LocalDate.now();
            MilkYouMod.LOGGER.log(Level.INFO, date.getMonthValue() + " " + date.getDayOfMonth());
            return date.getMonthValue() == 4 && date.getDayOfMonth() == 1;
        }
        return true;
    }
}