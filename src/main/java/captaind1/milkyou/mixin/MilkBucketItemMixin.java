package captaind1.milkyou.mixin;

import captaind1.milkyou.MilkYouMod;
import captaind1.milkyou.common.event.EventHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin extends Item {

    public MilkBucketItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Inject(at=@At("HEAD"), method= "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;")
    private void use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (level.isClientSide && MilkYouMod.isModEnabled()) EventHandler.lukeMilkAnimation.start();
    }
}
