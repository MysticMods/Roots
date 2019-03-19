package epicsquid.roots.spell;

import epicsquid.roots.entity.spell.EntityFireJet;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellIronLungs extends SpellBase {

  public SpellIronLungs(String name) {
    super(name, TextFormatting.BLUE, 64f / 255f, 64f / 255f, 64f / 255f, 192f / 255f, 32f / 255f, 255f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 24;

    addCost(HerbRegistry.getHerbByName("terra_moss"), 0.125f);
    addIngredients(
        new ItemStack(ModItems.terra_spores),
        new ItemStack(Items.REEDS),
        new ItemStack(Items.CLAY_BALL),
        new ItemStack(Items.GLASS_BOTTLE),
        new ItemStack(Items.IRON_INGOT)
    );
  }

  @Override
  public void cast(EntityPlayer player, List<SpellModule> modules) {
    player.setAir(300);
    if (player.world.isRemote) {
      player.playSound(SoundEvents.ENTITY_BOAT_PADDLE_WATER, 1, 1);
    }
  }
}
