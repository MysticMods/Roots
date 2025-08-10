package mysticmods.roots.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.config.ConfigManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class ElementalCropExtraDropsModifier extends LootModifier {
  public ElementalCropExtraDropsModifier(LootItemCondition[] conditionsIn) {
    super(conditionsIn);
  }

  @Override
  protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
    BlockState block = context.getParamOrNull(LootContextParams.BLOCK_STATE);
    if (block != null) {
      var held = block.getBlockHolder();
      Item extraCrop = held.getData(DataMaps.EXTRA_CROP_DATA);
      Float chance = held.getData(DataMaps.EXTRA_CROP_CHANCE);
      if (chance == null) {
        chance = ConfigManager.ELEMENTAL_CROP_EXTRA_DROPS_CHANCE.get().floatValue();
      }
      if (extraCrop != null && context.getRandom().nextFloat() < chance) {
        ItemStack extraCropStack = new ItemStack(extraCrop);
        generatedLoot.add(extraCropStack);
      }
    }
    return generatedLoot;
  }

  @Override
  public MapCodec<? extends IGlobalLootModifier> codec() {
    return CODEC;
  }

  public static final MapCodec<ElementalCropExtraDropsModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
          IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(glm -> glm.conditions))
      .apply(instance, ElementalCropExtraDropsModifier::new));
}
