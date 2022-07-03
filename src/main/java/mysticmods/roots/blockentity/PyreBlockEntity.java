package mysticmods.roots.blockentity;

import mysticmods.roots.RootsTags;
import mysticmods.roots.api.ClientTickBlockEntity;
import mysticmods.roots.api.InventoryBlockEntity;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.ServerTickBlockEntity;
import mysticmods.roots.api.recipe.ConditionalOutput;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ModRegistries;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.pyre.PyreCrafting;
import mysticmods.roots.recipe.pyre.PyreInventory;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;
import noobanidus.libs.noobutil.util.ItemUtil;
import noobanidus.libs.particleslib.client.particle.Particles;
import noobanidus.libs.particleslib.init.ModParticles;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// This controls the LIT state
public class PyreBlockEntity extends UseDelegatedBlockEntity implements ClientTickBlockEntity, ServerTickBlockEntity, InventoryBlockEntity {
  private final PyreInventory inventory = new PyreInventory() {
    @Override
    protected void onContentsChanged(int slot) {
      if (PyreBlockEntity.this.hasLevel() && !PyreBlockEntity.this.getLevel().isClientSide()) {
        PyreBlockEntity.this.revalidateRecipe();
        PyreBlockEntity.this.updateViaState();
      }
    }
  };
  private final PyreCrafting playerlessCrafting = new PyreCrafting(inventory, this, null);
  private final List<ItemStack> previousRecipeItems = new ArrayList<>();
  private PyreRecipe lastRecipe = null;
  private PyreRecipe cachedRecipe = null;
  private Ritual currentRitual = null;
  private List<ItemStack> storedItems = new ArrayList<>();
  private int lifetime = -1;

  public PyreBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
    RootsAPI.LOG.info(cachedRecipe);
    ItemStack inHand = player.getItemInHand(hand);
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }
    if (inHand.isEmpty()) {
      // extract
      ItemStack popped = inventory.pop();
      if (!popped.isEmpty()) {
        ItemUtil.Spawn.spawnItem(level, getBlockPos(), popped);
      }
      // TODO: starting a ritual while one is already active
    } else if (inHand.is(RootsTags.Items.PYRE_ACTIVATION)) {
      if (cachedRecipe == null) {
        // should this revalidate?
        revalidateRecipe();
      }
      if (cachedRecipe != null && cachedRecipe.matches(playerlessCrafting, level)) {
        Ritual newRitual = cachedRecipe.getRitual();
        if (newRitual == null) {
          currentRitual = ModRituals.CRAFTING.get();
        } else {
          currentRitual = newRitual;
        }
        PyreCrafting playerCrafting = new PyreCrafting(inventory, this, player);
        lastRecipe = cachedRecipe;
        previousRecipeItems.clear();
        previousRecipeItems.addAll(inventory.getItemsCopy());
        storedItems.clear();
        if (currentRitual == ModRituals.CRAFTING.get()) {
          storedItems.add(cachedRecipe.assemble(playerCrafting));
          // TODO: conditional outputs
          for (ConditionalOutput conditionalOutput : cachedRecipe.getConditionalOutputs() ) {
            ItemStack output = conditionalOutput.getResult(level.getRandom());
            if (!output.isEmpty()) {
              storedItems.add(output);
            }
          }
        }
        // process
        NonNullList<ItemStack> processed = cachedRecipe.process(inventory.getItemsAndClear());
        for (ItemStack stack : processed) {
          ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
        }
        cachedRecipe = null;
        startRitual(player);
        setChanged();
        updateViaState();
      }
    } else {
      // insert
      player.setItemInHand(hand, inventory.insert(inHand));
    }

    return InteractionResult.SUCCESS;
  }

  public void startRitual(Player player) {
    if (currentRitual != null) {
      this.lifetime = currentRitual.getDuration();
    } else {
      RootsAPI.LOG.error("tried to start a ritual but the ritual is null");
    }
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    super.onDataPacket(net, pkt);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      load(tag);
    } else {
      lastRecipe = null;
      cachedRecipe = null;
      currentRitual = null;
      storedItems.clear();
    }
  }

  protected void revalidateRecipe() {
    boolean matched = false;
    if (cachedRecipe == null) {
      if (lastRecipe != null && lastRecipe.matches(playerlessCrafting, getLevel())) {
        cachedRecipe = lastRecipe;
        matched = true;
      } else {
        cachedRecipe = ResolvedRecipes.PYRE.findRecipe(playerlessCrafting, getLevel());
      }
    }

    if (cachedRecipe != null) {
      // only test once
      if (!matched && !cachedRecipe.matches(playerlessCrafting, getLevel())) {
        cachedRecipe = null;
      }
    }
  }

  @Override
  protected void saveAdditional(CompoundTag pTag) {
    super.saveAdditional(pTag);
    ListTag previousItems = new ListTag();
    for (ItemStack stack : previousRecipeItems) {
      if (!stack.isEmpty()) {
        previousItems.add(stack.save(new CompoundTag()));
      }
    }

    if (!previousItems.isEmpty()) {
      pTag.put("previous_items", previousItems);
    }

    if (cachedRecipe != null) {
      pTag.putString("cached_recipe", cachedRecipe.getId().toString());
    }
    if (lastRecipe != null) {
      pTag.putString("last_recipe", lastRecipe.getId().toString());
    }
    if (currentRitual != null) {
      pTag.putString("current_ritual", ModRegistries.RITUAL_REGISTRY.get().getKey(currentRitual).toString());
    }

    ListTag storedItems = new ListTag();
    for (ItemStack stack : this.storedItems) {
      if (!stack.isEmpty()) {
        storedItems.add(stack.save(new CompoundTag()));
      }
    }
    if (!storedItems.isEmpty()) {
      pTag.put("stored_items", storedItems);
    }
    pTag.putInt("lifetime", lifetime);
    pTag.put("inventory", inventory.serializeNBT());
  }

  @Override
  public void load(CompoundTag pTag) {
    super.load(pTag);
    previousRecipeItems.clear();
    if (pTag.contains("previous_items", Tag.TAG_LIST)) {
      ListTag previousItems = pTag.getList("previous_items", Tag.TAG_COMPOUND);
      for (int i = 0; i < previousItems.size(); i++) {
        previousRecipeItems.add(ItemStack.of(previousItems.getCompound(i)));
      }
    }
    if (pTag.contains("cached_recipe", Tag.TAG_STRING)) {
      ResourceLocation cachedId = new ResourceLocation(pTag.getString("cached_recipe"));
      cachedRecipe = ResolvedRecipes.PYRE.getRecipe(cachedId);
    }
    if (pTag.contains("last_recipe", Tag.TAG_STRING)) {
      ResourceLocation lastId = new ResourceLocation(pTag.getString("last_recipe"));
      lastRecipe = ResolvedRecipes.PYRE.getRecipe(lastId);
    }
    if (pTag.contains("inventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(pTag.getCompound("inventory"));
    }
    if (pTag.contains("current_ritual", Tag.TAG_STRING)) {
      ResourceLocation ritualId = new ResourceLocation(pTag.getString("current_ritual"));
      currentRitual = ModRegistries.RITUAL_REGISTRY.get().getValue(ritualId);
    }
    if (pTag.contains("lifetime", Tag.TAG_INT)) {
      lifetime = pTag.getInt("lifetime");
    }
    storedItems.clear();
    if (pTag.contains("stored_items", Tag.TAG_LIST)) {
      ListTag incomingStoredItems = pTag.getList("stored_items", Tag.TAG_COMPOUND);
      for (int i = 0; i < incomingStoredItems.size(); i++) {
        storedItems.add(ItemStack.of(incomingStoredItems.getCompound(i)));
      }
    }
  }

  @Override
  public ItemStackHandler getInventory() {
    return inventory;
  }

  public List<ItemStack> popStoredItems () {
    List<ItemStack> result = new ArrayList<>(storedItems);
    storedItems.clear();
    return result;
  }

  public int getLifetime() {
    return lifetime;
  }

  @Nullable
  public Ritual getCurrentRitual() {
    return currentRitual;
  }

  @Override
  // TODO: handle client ticking
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    Random pRandom = pLevel.getRandom();
    if (pState.is(RootsTags.Blocks.PYRES) && pState.getValue(PyreBlock.LIT)) {
      Particles.create(ModParticles.FIERY_PARTICLE.get())
          .addVelocity(0.00525f * (pRandom.nextFloat() - 0.5f), 0, 0.00525f * (pRandom.nextFloat() - 0.5f))
          .setAlpha(0.5f, 0.2f)
          .setScale(0.8f + 0.5f * pRandom.nextFloat())
          .setColor(230 / 255.0f, 55 / 255.0f, 16 / 255.0f, 230 / 255.0f, 83 / 255.0f, 16 / 255.0f)
          .setLifetime(80)
          .disableGravity()
          .setSpin(0)
          .spawn(pLevel, pPos.getX() + 0.5f + 0.3f * (pRandom.nextFloat() - 0.5f), pPos.getY() + 0.625f + 0.125f * pRandom.nextFloat(), pPos.getZ() + 0.5f + 0.3f * (pRandom.nextFloat() - 0.5f));
    }

    if (currentRitual != null && lifetime > 0) {
      currentRitual.animateTick(this);
    }
  }

  @Override
  public void serverTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (currentRitual != null && lifetime > 0) {
      lifetime--;
      setChanged();
      if (lifetime <= 0) {
        currentRitual = null;
        if (pState.is(RootsTags.Blocks.PYRES) && pState.hasProperty(PyreBlock.LIT)) {
          pLevel.setBlock(pPos, pState.setValue(PyreBlock.LIT, false), 3);
        } else {
          updateViaState();
        }
      } else {
        currentRitual.ritualTick(this);
        if (pState.is(RootsTags.Blocks.PYRES) && pState.hasProperty(PyreBlock.LIT) && !pState.getValue(PyreBlock.LIT)) {
          pLevel.setBlock(pPos, pState.setValue(PyreBlock.LIT, true), 3);
        }
      }
    }
  }
}
