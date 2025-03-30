package mysticmods.roots.api.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.spell.ISpellInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
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
  Parameter NEW_ITEM = new Parameter(RootsAPI.rl("new_item"), GroveContext::newItem);
  Parameter HAND = new Parameter(RootsAPI.rl("hand"), GroveContext::hand);
  Parameter POSITION = new Parameter(RootsAPI.rl("position"), GroveContext::position);
  Parameter NEW_POSITION = new Parameter(RootsAPI.rl("new_position"), GroveContext::newPosition);
  Parameter BLOCK_ENTITY = new Parameter(RootsAPI.rl("block_entity"), GroveContext::blockEntity);
  Parameter BLOCK_STATE = new Parameter(RootsAPI.rl("block_state"), GroveContext::blockState);
  Parameter OLD_BLOCK_STATE = new Parameter(RootsAPI.rl("old_block_state"), GroveContext::oldBlockState);
  Parameter ENTITY_TARGET = new Parameter(RootsAPI.rl("target"), GroveContext::target);
  Parameter ENTITY_SECONDARY = new Parameter(RootsAPI.rl("secondary"), GroveContext::secondary);
  Parameter ENTITY_TERTIARY = new Parameter(RootsAPI.rl("tertiary"), GroveContext::tertiary);
  Parameter RECIPE = new Parameter(RootsAPI.rl("recipe"), GroveContext::recipe);
  Parameter MENU = new Parameter(RootsAPI.rl("menu"), GroveContext::menu);
  Parameter DAMAGE = new Parameter(RootsAPI.rl("damage"), GroveContext::damage);
  Parameter FEATURE = new Parameter(RootsAPI.rl("feature"), GroveContext::feature);
  Parameter TRIAL = new Parameter(RootsAPI.rl("trial"), GroveContext::trial);

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
  default ItemStack newItem() {
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
  default BlockPos newPosition () {
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
  default AbstractContainerMenu menu() {
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

  static boolean hasParameter (GroveContext context, Parameter type) {
    Object result = type.parameter().getParameter(context);
    return result != null;
  }

  @FunctionalInterface
  interface ParameterType {
    Object getParameter (GroveContext context);
  }

  record Parameter (ResourceLocation name, ParameterType parameter) {

  }
}
