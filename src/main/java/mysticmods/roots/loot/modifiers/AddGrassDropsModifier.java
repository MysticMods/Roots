package mysticmods.roots.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AddGrassDropsModifier extends LootModifier {
  private final Holder<Item> dropItem;

  public AddGrassDropsModifier(LootItemCondition[] conditionsIn, Holder<Item> item) {
    super(conditionsIn);
    this.dropItem = item;
  }

  @NotNull
  @Override
  protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
    generatedLoot.add(new ItemStack(dropItem));
    return generatedLoot;
  }

  public Holder<Item> getDropItem() {
    return dropItem;
  }

  @Override
  public MapCodec<? extends IGlobalLootModifier> codec() {
    return CODEC;
  }

  public static final MapCodec<AddGrassDropsModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(glm -> glm.conditions),
      ItemStack.ITEM_NON_AIR_CODEC.fieldOf("item").forGetter(AddGrassDropsModifier::getDropItem)).apply(instance, AddGrassDropsModifier::new));
}

