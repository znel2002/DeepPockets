package znel2002.deeppockets.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
// Mixin into AdvancementInventoryScreen
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import znel2002.deeppockets.Deeppockets;

@Mixin(AbstractInventoryScreen.class)
public abstract class InventoryEffectsMixin {

    @ModifyVariable(method = "drawStatusEffects", at = @At("STORE"), ordinal = 2)
    private int deeppockets$moveEffectsRight(int j) {
       return j + 60;
    }

}
