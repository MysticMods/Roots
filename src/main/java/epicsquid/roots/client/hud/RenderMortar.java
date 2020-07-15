package epicsquid.roots.client.hud;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.tileentity.TileEntityMortar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderMortar {
  public static void render(Minecraft mc, BlockPos pos, IBlockState state, RenderGameOverlayEvent.Post event) {
    World world = mc.world;

    if (!(world.getTileEntity(pos) instanceof TileEntityMortar)) return;

    TileEntityMortar te = (TileEntityMortar) world.getTileEntity(pos);
    if (te == null) return;
    List<ItemStack> toRender = new ArrayList<>();
    for (int i = 0; i < te.inventory.getSlots(); i++) {
      ItemStack stack = te.inventory.getStackInSlot(i);
      if (!stack.isEmpty()) {
        toRender.add(stack);
      }
    }
    if (toRender.isEmpty()) return;

    MortarRecipe mortarRecipe = ModRecipes.getMortarRecipe(toRender);
    SpellBase spellRecipe = ModRecipes.getSpellRecipe(toRender);

    ScaledResolution res = event.getResolution();
    int x = ((res.getScaledWidth() / 2));
    int y = res.getScaledHeight() / 2;
    float angle = -90;
    int radius = 24;
    float anglePer = 360f / toRender.size();

    if (mortarRecipe != null) {
      ItemStack output = mortarRecipe.getResult();
      RenderHelper.enableGUIStandardItemLighting();
      mc.getRenderItem().renderItemIntoGUI(output, x + radius + 16, y - 8);
      if (output.getCount() > 1) {
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, output, x + radius + 16, y - 8, null);
      }
      RenderHelper.disableStandardItemLighting();
    } else if (spellRecipe != null) {
      ItemStack output = new ItemStack(ModItems.spell_dust, 1);
      RenderHelper.enableGUIStandardItemLighting();
      mc.getRenderItem().renderItemIntoGUI(output, x + radius + 24, y - 14);
      String s = spellRecipe.getTextColor() + I18n.format("roots.spell." + spellRecipe.getName() + ".name");
      mc.fontRenderer.drawStringWithShadow(s, (float) x + radius + 24, y + 5, 16777215);
      RenderHelper.disableStandardItemLighting();
    }

    RenderHelper.enableGUIStandardItemLighting();
    for (int i = 0; i < toRender.size(); i++) {
      double xPos = x + Math.cos(angle * Math.PI / 180D) * radius - 8;
      double yPos = y + Math.sin(angle * Math.PI / 180D) * radius - 8;
      GlStateManager.translate(xPos, yPos, 0);
      mc.getRenderItem().renderItemIntoGUI(toRender.get(i), 0, 0);
      GlStateManager.translate(-xPos, -yPos, 0);

      angle += anglePer;
    }
    RenderHelper.disableStandardItemLighting();
  }
}
