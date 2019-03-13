package epicsquid.roots.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.Roots;
import epicsquid.roots.capability.pouch.PouchItemHandler;
import epicsquid.roots.gui.GuiHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPouch extends ItemBase {

  private int inventorySlots;
  private int herbSlots;

  public ItemPouch(@Nonnull String name, int inventorySlots, int herbSlots) {
    super(name);
    this.inventorySlots = inventorySlots;
    this.herbSlots = herbSlots;
    this.setMaxStackSize(1);
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
    return new PouchItemHandler(inventorySlots, herbSlots);
  }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
    player.openGui(Roots.getInstance(), GuiHandler.POUCH_ID, world, 0, 0, 0);
    return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    //    if (stack.hasTagCompound()) {
    //      if (stack.getTagCompound().hasKey("quantity")) {
    //        tooltip.add(I18n.format( HerbRegistry.getHerbByName(stack.getTagCompound().getString("plant")).getItem().getUnlocalizedName() + ".name") + I18n.format("roots.tooltip.pouch_divider") + (int) Math
    //            .ceil(stack.getTagCompound().getDouble("quantity")));
    //      }
    //    }
  }
}