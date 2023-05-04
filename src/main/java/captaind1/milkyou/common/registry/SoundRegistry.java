package captaind1.milkyou.common.registry;

import captaind1.milkyou.MilkYouMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {

    public SoundRegistry() {}

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MilkYouMod.MODID);

    public static final RegistryObject<SoundEvent> MILK_YOU =
            registerSound("milk_you", "milk_you");

    private static RegistryObject<SoundEvent> registerSound(String name, String location) {
        ResourceLocation resourceLocation = new ResourceLocation(MilkYouMod.MODID, location);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(resourceLocation));
    }

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}
