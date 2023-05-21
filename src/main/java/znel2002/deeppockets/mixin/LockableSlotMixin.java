package znel2002.deeppockets.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import znel2002.deeppockets.Deeppockets;
import znel2002.deeppockets.LockableSlot;

@Mixin(Slot.class)
public abstract class LockableSlotMixin implements LockableSlot {

    private boolean deeppockets$active = true;

    @Override
    public boolean deeppockets$getActive() {
        return deeppockets$active;
    }

    @Override
    public void deeppockets$setActive(boolean active) {
        this.deeppockets$active = active;

    }

     // @Inject(method = "isEnabled", at = @At("HEAD"), cancellable = true)
     // private void Deeppockets$isEnabled(CallbackInfoReturnable<Boolean> cir) {
     //     cir.setReturnValue(this.deeppockets$active);
     // }

    @Inject(method = "canInsert", at = @At("HEAD"), cancellable = true)
    private void deeppockets$insertItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {

        cir.setReturnValue(deeppockets$active);
    }
    @Inject(method = "canTakeItems", at = @At("HEAD"), cancellable = true)
    private void deeppockets$takeItems(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(deeppockets$active);
    }
}