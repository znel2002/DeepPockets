package znel2002.deeppockets.mixin;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import znel2002.deeppockets.Deeppockets;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    @Shadow @Final private RecipeBookWidget recipeBook;
    private static final Identifier Full = new Identifier(Deeppockets.MOD_ID,"textures/gui/level7.png");

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    //@Inject(at = @At("HEAD"), method = "render")
    //private void init(CallbackInfo info) {
    //    Deeppockets.LOGGER.info("This line is printed by an example mod mixin!");
//
//
    // }


    @Inject(method = "isClickOutsideBounds", at = @At("TAIL"), cancellable = true)
    private void addRows(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue( info.getReturnValue() && mouseY >= (double)(top + this.backgroundWidth + 25) );

    }

    @Inject(at = @At("RETURN"), method = "drawBackground")
    private void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo info) {
        RenderSystem.setShader(GameRenderer::getRenderTypeTextProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, Full);
        // Log x and y

        // Deeppockets.LOGGER.info("x: " + mouseX + " y: " + mouseY);
        DrawableHelper.drawTexture(matrices, 565, 171, 0, 0, 23, 133, 23, 133);
    }

    // @Inject(at = @At("RETURN"), method = "init")
    // private void init(CallbackInfo info) {
    //     // Add button to show
    //     this.addDrawableChild(new TexturedButtonWidget(this.x + 124, this.height / 2 - 22, 20, 18, 0, 0, 19, new Identifier("textures/gui/recipe_button.png"), (button) -> {
    //         Deeppockets.LOGGER.info("Button clicked");
    //         button.setPosition(this.x + 124 ,this.height / 2 - 22);
    //     }));
    // }
}