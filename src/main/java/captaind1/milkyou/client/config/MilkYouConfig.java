package captaind1.milkyou.client.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MilkYouConfig {

    public static class Client {
        public final ForgeConfigSpec.DoubleValue MARK_OPACITY;
        public final ForgeConfigSpec.DoubleValue MARK_SCALE;
        public final ForgeConfigSpec.DoubleValue LUKE_OPACITY;
        public final ForgeConfigSpec.DoubleValue LUKE_SCALE;
        public final ForgeConfigSpec.IntValue MILK_YOU_COOLDOWN;
        public final ForgeConfigSpec.ConfigValue<Boolean> APRIL_FOOLS_MODE;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("I can milk you");
            this.MARK_OPACITY = builder.comment("Opacity for Markiplier animation")
                    .worldRestart()
                    .defineInRange("markiplier opacity", 1.0, 0.0, 1.0);
            this.MARK_SCALE = builder.comment("Scale factor for Markiplier animation")
                    .worldRestart()
                    .defineInRange("markiplier scale", 1.0, 0.0, 2.0);
            this.LUKE_OPACITY = builder.comment("Opacity for Luke Skywalker animation")
                    .worldRestart()
                    .defineInRange("luke opacity", 0.5, 0.0, 1.0);
            this.LUKE_SCALE = builder.comment("Scale factor for Luke Skywalker animation")
                    .worldRestart()
                    .defineInRange("luke scale", 1.0, 0.0, 1.0);
            this.MILK_YOU_COOLDOWN = builder.comment("Cooldown in ticks between times the 'I can milk you' animation can play")
                    .worldRestart()
                    .defineInRange("animation cooldown", 200, 0, 10000);
            this.APRIL_FOOLS_MODE = builder.comment("If true, mod is only active on April 1st. Otherwise, it's always active.")
                    .worldRestart().define("april fools mode", false);
            builder.pop();
        }
    }

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = clientSpecPair.getLeft();
        CLIENT_SPEC = clientSpecPair.getRight();
    }
}
