package mysticmods.roots.item;

import com.google.common.collect.Multimap;
import mysticmods.roots.Roots;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.armor.ArmorModel;
import mysticmods.roots.config.ConfigManager;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import noobanidus.libs.noobutil.material.MaterialType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

public class AntlerHatItem extends ModifiedArmorItem {
  public AntlerHatItem(Properties builder) {
    super(Roots.ANTLER_MATERIAL, EquipmentSlot.HEAD, builder);
  }

  @Override
  public Map<Attribute, AttributeModifier> getModifiers() {
    return super.getModifiers();
  }

  @Override
  public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
    Multimap<Attribute, AttributeModifier> map = super.getDefaultAttributeModifiers(equipmentSlot);

    if (equipmentSlot == EquipmentSlot.HEAD && ConfigManager.HAT_CONFIG.getAntlerHealthBonus() != -1) {
      map.put(Attributes.MAX_HEALTH, this.getOrCreateModifier(Attributes.MAX_HEALTH, () -> new AttributeModifier(MaterialType.ARMOR_MODIFIERS[slot.getIndex()], "Antler Health Boost", ConfigManager.HAT_CONFIG.getAntlerHealthBonus(), AttributeModifier.Operation.ADDITION)));
    }

    return map;
  }

  @Override
  public void onArmorTick(ItemStack stack, Level world, Player player) {
    if (ConfigManager.HAT_CONFIG.getAntlerFrequency() == -1) {
      return;
    }

    if (!world.isClientSide && player.getHealth() < (ConfigManager.HAT_CONFIG.getAntlerThreshold() == -1 ? player.getMaxHealth() : player.getMaxHealth() - ConfigManager.HAT_CONFIG.getAntlerThreshold()) && world.random.nextInt(ConfigManager.HAT_CONFIG.getAntlerFrequency()) == 0) {
      if (player.getEffect(MobEffects.REGENERATION) != null) {
        return;
      }
      player.heal(ConfigManager.HAT_CONFIG.getAntlerHealing());
      player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ConfigManager.HAT_CONFIG.getAntlerRegenDuration(), ConfigManager.HAT_CONFIG.getAntlerRegenAmplifier()));

      ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
      if (ConfigManager.HAT_CONFIG.getAntlerDamage() != -1) {
        head.hurtAndBreak(ConfigManager.HAT_CONFIG.getAntlerDamage(), player, (breaker) -> breaker.broadcastBreakEvent(EquipmentSlot.HEAD));
      }
    }
  }

  @Nullable
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
    return RootsAPI.MODID + ":textures/model/armor/antler_hat.png";
  }

  @Override
  public void initializeClient(Consumer<IClientItemExtensions> consumer) {
    consumer.accept(new IClientItemExtensions() {
      @Override
      public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        HumanoidModel<?> result = ArmorModel.getModel(itemStack);
        if (result == null) {
          return original;
        }

        return result;
      }
    });
  }
}
