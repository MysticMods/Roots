package mysticmods.roots.api.item;

import com.google.common.base.Suppliers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class RootsArmorMaterial implements ArmorMaterial {
  private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
  private final String name;
  private final int durabilityMultiplier;
  private final int[] slotProtections;
  private final int enchantmentValue;
  private final SoundEvent sound;
  private final float toughness;
  private final float knockbackResistance;
  private final Supplier<Ingredient> repairIngredient;

  public RootsArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
    this.name = name;
    this.durabilityMultiplier = durabilityMultiplier;
    this.slotProtections = slotProtections;
    this.enchantmentValue = enchantmentValue;
    this.sound = sound;
    this.toughness = toughness;
    this.knockbackResistance = knockbackResistance;
    this.repairIngredient = Suppliers.memoize(repairIngredient::get);
  }

  @Override
  public int getDurabilityForSlot(EquipmentSlot pSlot) {
    return HEALTH_PER_SLOT[pSlot.getIndex()] * this.durabilityMultiplier;
  }

  @Override
  public int getDefenseForSlot(EquipmentSlot pSlot) {
    return this.slotProtections[pSlot.getIndex()];
  }

  @Override
  public int getEnchantmentValue() {
    return this.enchantmentValue;
  }

  @Override
  public SoundEvent getEquipSound() {
    return this.sound;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return this.repairIngredient.get();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public float getToughness() {
    return this.toughness;
  }

  @Override
  public float getKnockbackResistance() {
    return this.knockbackResistance;
  }
}
