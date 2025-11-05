package mysticmods.roots.api.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface GroveContext {
  Parameter LEVEL = new Parameter(RootsAPI.rl("level"), GroveContext::level);
  Parameter PLAYER = new Parameter(RootsAPI.rl("player"), GroveContext::player);
  Parameter SPELL = new Parameter(RootsAPI.rl("spell"), GroveContext::spell);
  Parameter RITUAL = new Parameter(RootsAPI.rl("ritual"), GroveContext::ritual);
  Parameter ITEM = new Parameter(RootsAPI.rl("item"), GroveContext::item);
  Parameter OLD_ITEM = new Parameter(RootsAPI.rl("old_item"), GroveContext::oldItem);
  Parameter HAND = new Parameter(RootsAPI.rl("hand"), GroveContext::hand);
  Parameter POSITION = new Parameter(RootsAPI.rl("position"), GroveContext::position);
  Parameter NEW_POSITION = new Parameter(RootsAPI.rl("new_position"), GroveContext::newPosition);
  Parameter BLOCK_ENTITY = new Parameter(RootsAPI.rl("block_entity"), GroveContext::blockEntity);
  Parameter BLOCK_STATE = new Parameter(RootsAPI.rl("block_state"), GroveContext::blockState);
  Parameter OLD_BLOCK_STATE = new Parameter(RootsAPI.rl("old_block_state"), GroveContext::oldBlockState);
  Parameter TARGET_ENTITY = new Parameter(RootsAPI.rl("target"), GroveContext::target);
  Parameter SECONDARY_ENTITY = new Parameter(RootsAPI.rl("secondary"), GroveContext::secondary);
  Parameter TERTIARY_ENTITY = new Parameter(RootsAPI.rl("tertiary"), GroveContext::tertiary);
  Parameter RECIPE_ID = new Parameter(RootsAPI.rl("recipe_id"), GroveContext::recipeId);
  Parameter RECIPE = new Parameter(RootsAPI.rl("recipe"), GroveContext::recipe);
  Parameter DAMAGE = new Parameter(RootsAPI.rl("damage"), GroveContext::damage);
  Parameter TRIAL = new Parameter(RootsAPI.rl("trial"), GroveContext::trial);
  Parameter COSTING = new Parameter(RootsAPI.rl("costing"), GroveContext::costing);
  Parameter OFFER = new Parameter(RootsAPI.rl("offer"), GroveContext::offer);
  Parameter SPELL_MODIFIER = new Parameter(RootsAPI.rl("spell_modifier"), GroveContext::spellModifier);

  @Nonnull
  ServerLevel level();

  @Nonnull
  ServerPlayer player();

  @Nullable
  default ISpellInstance spell() {
    return null;
  }

  @Nullable
  default IRitualInstance ritual() {
    return null;
  }

  // This is explicitly nullable to specify that no item is being used, as opposed to
  // instances where an itemstack is generally used, but the itemstack is empty.
  @Nullable
  default ItemStack item() {
    return null;
  }

  @Nullable
  default ItemStack oldItem() {
    return null;
  }

  @Nullable
  default InteractionHand hand() {
    return null;
  }

  @Nullable
  default BlockPos position() {
    return null;
  }

  @Nullable
  default BlockPos newPosition() {
    return null;
  }

  @Nullable
  default BlockEntity blockEntity() {
    return null;
  }

  @Nullable
  default BlockState blockState() {
    return null;
  }

  @Nullable
  default BlockState oldBlockState() {
    return null;
  }

  @Nullable
  default Entity target() {
    return null;
  }

  @Nullable
  default Entity secondary() {
    return null;
  } // Direct entity

  @Nullable
  default Entity tertiary() {
    return null;
  }

  @Nullable
  default Recipe<?> recipe() {
    return null;
  }

  @Nullable
  default ResourceLocation recipeId() {
    return null;
  }

  @Nullable
  default DamageSource damage() {
    return null;
  }

  @Nullable
  default ConfiguredFeature<?, ?> feature() {
    return null;
  }

  @Nullable
  default TrialSpawner trial() {
    return null;
  }

  @Nullable
  default Costing costing() {
    return null;
  }

  @Nullable
  default MerchantOffer offer() {
    return null;
  }

  @Nullable
  default Modifier spellModifier() {
    return null;
  }

  @Nullable
  default Modifier ritualModifier () {
    return null;
  }

  @Nullable
  default Modifier genericModifier () {
    return null;
  }

  boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag);

  static boolean hasParameter(GroveContext context, Parameter type) {
    Object result = type.parameter().getParameter(context);
    return result != null;
  }

  @FunctionalInterface
  interface ParameterType {
    Object getParameter(GroveContext context);
  }

  record Parameter(ResourceLocation name, ParameterType parameter) {

  }
}
