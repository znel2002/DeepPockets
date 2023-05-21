package znel2002.deeppockets.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.texture.atlas.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import znel2002.deeppockets.Deeppockets;
import znel2002.deeppockets.LockableSlot;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {


    private static final Identifier SLOT_LOCK_TEXTURE = new Identifier(Deeppockets.MOD_ID, "textures/gui/lock_overlay.png");
    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @ModifyConstant(method = "onMouseClick(I)V", constant = @Constant(intValue = 40))
    private int changeOffhandSlot(int og) {
        return 58;
    }

    @ModifyConstant(method = "handleHotbarKeyPressed", constant = @Constant(intValue = 40))
    private int changeOffhandSlot2(int og) {
        return 58;
    }

    // @ModifyConstant(method = "drawSlotHighlight", constant = @Constant(intValue = -2130706433))
    // private static int deeppockets$changeGradientVariable(int og) {
    //     return -40121;
    // }

    // Change the variable sprite of type Sprite  to x in the methode drawSlot

    @Inject(at = @At("HEAD"), method = "drawSlot")
    public void drawSlot(MatrixStack matrices, Slot slot, CallbackInfo info) {

        if(this.client != null) {
            LockableSlot slot2 = (LockableSlot) slot;
            // cast to LockableSlot so we can use our custom methods

            // If the slot is locked text should be unlocked and the lock should be drawn
            // drawTextWithShadow(matrices, this.textRenderer, slot2.deeppockets$getActive() ? "Unlocked"+ slot.id : "Locked" + slot.id, slot.x + 19, slot.y + 6, 0xFFFFFF);

            int level = 0;

            assert this.client.player != null;

            for (ItemStack stack :  this.client.player.getInventory().armor) {
                for (NbtElement element : stack.getEnchantments()) {
                    if (element.toString().contains("protection")) {
                        if (element.toString().split(",")[1].split(":")[1].charAt(0) - '0' > level) {
                            level = element.toString().split(",")[1].split(":")[1].charAt(0) - '0';
                        }
                    }
                }
            }

            if (slot.inventory == this.client.player.getInventory()) {
                if(slot.id - 46 >= 0 && slot.id - 46 >= level) {
                    RenderSystem.setShaderTexture(0, SLOT_LOCK_TEXTURE);
                    drawTexture(matrices, slot.x, slot.y, 0, 0, 16, 16);
                    // Locked if true and unlocked if false
                }
            }
        }
    }
}
