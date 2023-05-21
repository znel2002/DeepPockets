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
import znel2002.deeppockets.Deeppockets;
import znel2002.deeppockets.LockableSlot;
import znel2002.deeppockets.client.DeeppocketsClient;

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
        for (int n = 0; n < 7; ++n) {
            this.addSlot(new Slot(inventory, n + 36, 175, 5 - (n * -18)));
            ((LockableSlot)this.slots.get(n+36)).deeppockets$setActive(false);
            // cast to LockableSlot so we can use our custom methods

        }
        Deeppockets.LOGGER.info("Added 7 more rows to player inventory");
    }

    @Inject(method = "onClosed", at = @At("HEAD"))
    private void close(PlayerEntity player, CallbackInfo info) {

        // Set Deep Pockets level to the level of protection of the armor

        int level = 0;

        for (ItemStack stack : player.getArmorItems()) {
            Deeppockets.LOGGER.info("Armor level: " + stack.getEnchantments());
            for (NbtElement element : stack.getEnchantments()) {
                if (element.toString().contains("protection")) {
                    if (element.toString().split(",")[1].split(":")[1].charAt(0) - '0' > level) {
                        level = element.toString().split(",")[1].split(":")[1].charAt(0) - '0';
                    }
                }
            }
        }
        DeeppocketsClient.LEVEL = level;


        // loop for all levels
        // for (int i = 0; i < DeeppocketsClient.LEVEL; i++) {
        //     LockableSlot slot = (LockableSlot) this.slots.get(i);
        //     slot.deeppockets$setActive(true);
        // }
    }
}
