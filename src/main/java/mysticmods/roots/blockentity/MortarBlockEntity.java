package mysticmods.roots.blockentity;

import mysticmods.roots.RootsTags;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.mortar.MortarCrafting;
import mysticmods.roots.recipe.mortar.MortarInventory;
import mysticmods.roots.recipe.mortar.MortarRecipe;
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

import java.util.ArrayList;
import java.util.List;

public class MortarBlockEntity extends UseDelegatedBlockEntity implements InventoryBlockEntity {
  private final MortarInventory inventory = new MortarInventory() {
    @Override
    protected void onContentsChanged(int slot) {
      if (MortarBlockEntity.this.hasLevel() && !MortarBlockEntity.this.getLevel().isClientSide()) {
        MortarBlockEntity.this.revalidateRecipe();
        MortarBlockEntity.this.updateViaState();
      }
    }
  };
  private final MortarCrafting playerlessCrafting = new MortarCrafting(inventory, this, null);
  private final List<ItemStack> previousRecipeItems = new ArrayList<>();
  private MortarRecipe lastRecipe = null;
  private MortarRecipe cachedRecipe = null;
  private int uses = -1;

  public MortarBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  protected void revalidateRecipe() {
    boolean matched = false;
    if (cachedRecipe == null) {
      uses = -1;
      if (lastRecipe != null && lastRecipe.matches(playerlessCrafting, getLevel())) {
        cachedRecipe = lastRecipe;
        matched = true;
      } else {
        cachedRecipe = ResolvedRecipes.MORTAR.findRecipe(playerlessCrafting, getLevel());
      }
    }

    if (cachedRecipe != null) {
      // only test once
      if (matched || cachedRecipe.matches(playerlessCrafting, getLevel())) {
        if (uses == -1) {
          uses = 0;
        }
      } else {
        cachedRecipe = null;
        uses = -1;
      }
    }
  }

  @Override
  protected void saveAdditional(CompoundTag pTag) {
    super.saveAdditional(pTag);
    // TODO: reference this
    pTag.putInt("uses", uses);
    boolean previous = false;
    ListTag previousItems = new ListTag();
    for (ItemStack stack : previousRecipeItems) {
      if (!stack.isEmpty()) {
        previous = true;
        previousItems.add(stack.save(new CompoundTag()));
      }
    }

    if (previous) {
      pTag.put("previous_items", previousItems);
    }

    if (cachedRecipe != null) {
      pTag.putString("cached_recipe", cachedRecipe.getId().toString());
    }
    if (lastRecipe != null) {
      pTag.putString("last_recipe", lastRecipe.getId().toString());
    }
    pTag.put("inventory", inventory.serializeNBT());
  }

  @Override
  public void load(CompoundTag pTag) {
    super.load(pTag);
    // TODO: reference this
    if (pTag.contains("uses", Tag.TAG_INT)) {
      this.uses = pTag.getInt("uses");
    }
    if (pTag.contains("previous_items", Tag.TAG_LIST)) {
      previousRecipeItems.clear();
      ListTag previousItems = pTag.getList("previous_items", Tag.TAG_COMPOUND);
      for (int i = 0; i < previousItems.size(); i++) {
        previousRecipeItems.add(ItemStack.of(previousItems.getCompound(i)));
      }
    }
    if (pTag.contains("cached_recipe", Tag.TAG_STRING)) {
      ResourceLocation cachedId = new ResourceLocation(pTag.getString("cached_recipe"));
      cachedRecipe = ResolvedRecipes.MORTAR.getRecipe(cachedId);
    }
    if (pTag.contains("last_recipe", Tag.TAG_STRING)) {
      ResourceLocation lastId = new ResourceLocation(pTag.getString("last_recipe"));
      lastRecipe = ResolvedRecipes.MORTAR.getRecipe(lastId);
    }
    if (pTag.contains("inventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(pTag.getCompound("inventory"));
    }
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    super.onDataPacket(net, pkt);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      load(tag);
    }
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
    } else if (inHand.is(RootsTags.Items.MORTAR_ACTIVATION)) {
      if (cachedRecipe == null) {
        // should this revalidate?
        revalidateRecipe();
      }
      if (cachedRecipe != null && cachedRecipe.matches(playerlessCrafting, level)) {
        uses++;

        if (uses >= cachedRecipe.getTimes()) {
          // CRAFTING HAPPENS HERE
          MortarCrafting playerCrafting = new MortarCrafting(inventory, this, player);
          lastRecipe = cachedRecipe;
          previousRecipeItems.clear();
          previousRecipeItems.addAll(inventory.getItemsCopy());
          ItemStack result = cachedRecipe.assemble(playerCrafting);
          // process
          NonNullList<ItemStack> processed = cachedRecipe.process(inventory.getItemsAndClear());
          ItemUtil.Spawn.spawnItem(level, player.blockPosition(), result);
          for (ItemStack stack : processed) {
            ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
          }
          uses = -1;
          cachedRecipe = null;
          setChanged();
        }
      }
    } else {
      // insert
      player.setItemInHand(hand, inventory.insert(inHand));
    }

    return InteractionResult.SUCCESS;
  }

  public int getUses() {
    return uses;
  }

  @Override
  public ItemStackHandler getInventory() {
    return inventory;
  }
}
