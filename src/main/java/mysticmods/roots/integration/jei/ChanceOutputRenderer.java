package mysticmods.roots.integration.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nullable;
import java.util.List;

public class ChanceOutputRenderer implements IIngredientRenderer<ChanceOutput> {
	@Override
	public void render(GuiGraphics guiGraphics, @Nullable ChanceOutput ingredient) {
		render(guiGraphics, ingredient, 0, 0);
	}

	@Override
	public void render(GuiGraphics guiGraphics, @Nullable ChanceOutput ingredient, int posX, int posY) {
		if (ingredient != null) {
			RenderSystem.enableDepthTest();

			Minecraft minecraft = Minecraft.getInstance();
			Font font = getFontRenderer(minecraft, ingredient);
			guiGraphics.renderFakeItem(ingredient.getOutput(), posX, posY);
			guiGraphics.renderItemDecorations(font, ingredient.getOutput(), posX, posY);
			RenderSystem.disableBlend();
		}
	}

	@SuppressWarnings("removal")
	@Override
	public List<Component> getTooltip(ChanceOutput chance, TooltipFlag tooltipFlag) {
    ItemStack ingredient = chance.getOutput();
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
		return ingredient.getTooltipLines(tooltipContext, player, tooltipFlag);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, ChanceOutput ingredient, TooltipFlag tooltipFlag) {
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
		List<Component> tooltipLines = ingredient.getOutput().getTooltipLines(tooltipContext, player, tooltipFlag);
    tooltipLines.add(Component.literal("Chance: " + ingredient.getChance() * 100 + "%"));
		tooltip.addAll(tooltipLines);
	}

	@Override
	public int getWidth() {
		return 16;
	}

	@Override
	public int getHeight() {
		return 16;
	}
}
