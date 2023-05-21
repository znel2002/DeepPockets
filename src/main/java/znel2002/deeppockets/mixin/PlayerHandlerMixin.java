package znel2002.deeppockets.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import znel2002.deeppockets.Deeppockets;
import znel2002.deeppockets.LockableSlot;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerHandlerMixin extends ScreenHandler {

    protected PlayerHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 39))
    private int armorIndexChange(int og) {
        return og + 7;
    }

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 40))
    private int offhandIndexChange(int og) {
        return og + 7;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addMoreRows(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo info) {

        int level = 0;
        Iterable<ItemStack> items = inventory.armor;


        for (ItemStack stack :  items) {

            for (NbtElement element : stack.getEnchantments()) {
                if (element.toString().contains("protection")) {
                    if (element.toString().split(",")[1].split(":")[1].charAt(0) - '0' > level) {
                        level = element.toString().split(",")[1].split(":")[1].charAt(0) - '0';
                    }
                }
            }
        }

        for (int n = 0; n < 7; ++n) {
            Slot slot = new Slot(inventory, n + 36, 175, 5 - (n * -18));


            ((LockableSlot)slot).deeppockets$setActive(false);
            Deeppockets.LOGGER.info(String.valueOf(((LockableSlot)slot).deeppockets$getActive()));
            this.addSlot(slot);

        }
        Deeppockets.LOGGER.info("Added 7 more rows to player inventory");
    }


    @Inject(method = "canUse", at = @At("HEAD"))
    private void canUse(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        int level = 0;
        Iterable<ItemStack> items = player.getArmorItems();


        for (ItemStack stack :  items) {

            for (NbtElement element : stack.getEnchantments()) {
                if (element.toString().contains("protection")) {
                    if (element.toString().split(",")[1].split(":")[1].charAt(0) - '0' > level) {
                        level = element.toString().split(",")[1].split(":")[1].charAt(0) - '0';
                    }
                }
            }
        }

        for (int i = 0; i < 7; i++) {
            if(i < level) {
                ((LockableSlot) this.slots.get(i + 46)).deeppockets$setActive(true);

            }
            else{
                ((LockableSlot) this.slots.get(i + 46)).deeppockets$setActive(false);
            }

            Deeppockets.LOGGER.info(String.valueOf(((LockableSlot)this.slots.get(i+46)).deeppockets$getActive()));
        }
    }
}