package mysticmods.roots.integration.jei.ingredient.damage;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.integration.jei.ingredient.block.SimpleBlockType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record RootsDamageType(Holder<DamageType> type, ItemStack icon) {
  public static final Codec<RootsDamageType> CODEC = DamageType.CODEC.xmap(RootsDamageType::new, RootsDamageType::type);

  public RootsDamageType(Holder<DamageType> type) {
    this(type, ItemStack.EMPTY);
  }

  public static List<RootsDamageType> fromTag (TagKey<DamageType> tag) {
    List<RootsDamageType> result = new ArrayList<>();
    var registry = Minecraft.getInstance().player.connection.registryAccess().registry(Registries.DAMAGE_TYPE);
    if (!registry.isPresent()) {
      RootsAPI.LOG.error("Could not get damage type registry");
      return Collections.emptyList();
    }
    registry.get().getTagOrEmpty(tag).forEach(o -> result.add(new RootsDamageType(o)));
    return result;
  }
}
