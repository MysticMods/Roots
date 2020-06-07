package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class SpellWinterCloud extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(100);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 0.015));
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(600).setDescription("the duration of the spell effect on the player");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_winter_cloud");
  public static SpellWinterCloud instance = new SpellWinterCloud(spellName);

  private int duration;

  public SpellWinterCloud(ResourceLocation name) {
    super(name, TextFormatting.DARK_AQUA, 22f / 255f, 142f / 255f, 255f / 255f, 255f / 255f, 255f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DURATION);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(ModItems.dewgonia),
        new ItemStack(Item.getItemFromBlock(Blocks.SNOW)),
        new ItemStack(Item.getItemFromBlock(Blocks.SNOW)),
        new ItemStack(Items.SNOWBALL),
        new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta())
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    World world = player.world;
    player.addPotionEffect(new PotionEffect(ModPotions.freeze, duration, 0, false, false));
    world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.3f, 2f);
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.duration = properties.get(PROP_DURATION);
  }
}

