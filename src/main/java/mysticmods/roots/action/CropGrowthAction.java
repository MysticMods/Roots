package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.growth.GrowthRecord;
import mysticmods.roots.util.GrowthUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;
import java.util.Set;

public class CropGrowthAction implements GroveAction {
  @Override
  public boolean test(GroveContext context) {
    GrowthRecord record = GrowthUtil.getGrowthRecord(context.blockState());
    if (record == null) {
      return false;
    }

    if (context.oldBlockState().isAir() && !context.blockState().isAir()) {
      return true;
    }

    IntegerProperty age = record.ageProperty().orElse(null);
    if (age == null) {
      return false;
    }

    if (!context.oldBlockState().hasProperty(age) || !context.blockState().hasProperty(age)) {
      return false;
    }

    int oldAge = context.oldBlockState().getValue(age);
    int newAge = context.blockState().getValue(age);

    if (newAge > oldAge) {
      RootsAPI.LOG.error("CropGrowthAction fired by '{}' with new block state '{}'",
          context.player().getName().getString(), context.blockState());
      return true;
    }
    return false;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, BlockPos position, BlockState blockState,
                        BlockState oldBlockState, InteractionHand hand, ItemStack item,
                        @Nullable ISpellInstance spell, @Nullable
                        IRitualInstance ritual) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.POSITION,
        GroveContext.BLOCK_STATE, GroveContext.OLD_BLOCK_STATE, GroveContext.HAND, GroveContext.ITEM);

    public Context(ServerLevel level, ServerPlayer player, BlockPos position, BlockState blockState, BlockState oldBlockState, InteractionHand hand, ItemStack item) {
      this(level, player, position, blockState, oldBlockState, hand, item, null, null);
    }

    public Context(ServerLevel level, ServerPlayer player, BlockPos position, BlockState blockState, BlockState oldBlockState, InteractionHand hand, ItemStack item, ISpellInstance spell) {
      this(level, player, position, blockState, oldBlockState, hand, item, spell, null);
    }

    public Context(ServerLevel level, ServerPlayer player, BlockPos position, BlockState blockState, BlockState oldBlockState, InteractionHand hand, ItemStack item, IRitualInstance ritual) {
      this(level, player, position, blockState, oldBlockState, hand, item, null, ritual);
    }

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      return switch (type) {
        case BLOCK -> blockState().is(TagKey.create(Registries.BLOCK, tag));
        case SPELL -> spell() != null && spell().getSpell().is(TagKey.create(RootsRegistries.Keys.SPELLS, tag));
        case RITUAL -> ritual() != null && ritual().getRitual().is(TagKey.create(RootsRegistries.Keys.RITUALS, tag));
        default -> false;
      };
    }
  }
}
