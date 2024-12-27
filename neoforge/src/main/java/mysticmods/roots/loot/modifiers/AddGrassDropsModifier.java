package mysticmods.roots.loot.modifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddGrassDropsModifier extends LootModifier {
  private final Item dropItem;

  protected AddGrassDropsModifier(LootItemCondition[] conditionsIn, Item item) {
    super(conditionsIn);
    this.dropItem = item;
  }

  @SuppressWarnings("deprecation")
  @NotNull
  @Override
  protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
    generatedLoot.add(new ItemStack(dropItem));
    return generatedLoot;
  }

  @Override
  public Codec<? extends IGlobalLootModifier> codec() {
    return CODEC.get();
  }

  public static final Supplier<Codec<AddGrassDropsModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.dropItem)).apply(inst, AddGrassDropsModifier::new)));
}

