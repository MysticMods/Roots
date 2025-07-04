package mysticmods.roots.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.blockentity.BindableBlockEntity;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;

public class GramaryItem extends Item {
  public GramaryItem(Properties properties) {
    super(properties);
  }

  @Override
  public Component getName(ItemStack pStack) {
    GramaryMode mode = pStack.get(ModAttachments.GRAMARY_MODE);
    return Component.translatable("roots.item.gramary.with_mode", mode.getComponent());
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    ItemStack stack = context.getItemInHand();
    if (getMode(stack) != GramaryMode.BIND_POSITION) {
      return super.useOn(context);
    }

    Level level = context.getLevel();
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }

    BlockPos pos = context.getClickedPos();

    boolean hasBound = false;

    BlockPos boundPos = stack.get(ModAttachments.BOUND_POSITION);
    if (boundPos != BlockPos.ZERO) {
      hasBound = true; // TODO: What if we actually want to bind to ZERO?
    }

    BlockEntity blockEntity = level.getBlockEntity(pos);

    if (hasBound && blockEntity instanceof BindableBlockEntity bindable) {
      bindable.setBoundPosition(boundPos);
      // TODO: Send a message to say that it was bound
      if (context.getPlayer() != null) {
        context.getPlayer()
            .displayClientMessage(Component.translatable("roots.item.gramary.bound_block_entity", pos.getX(), pos.getY(), pos.getZ()), true);
      }
    } else {
      pos = pos.above();
      stack.set(ModAttachments.BOUND_POSITION, pos);
      if (context.getPlayer() != null) {
        context.getPlayer()
            .displayClientMessage(Component.translatable("roots.item.gramary.bound_block_position", pos.getX(), pos.getY(), pos.getZ()), true);
      }
    }

    return InteractionResult.SUCCESS;
  }

  public static GramaryMode getMode(ItemStack item) {
    if (item.has(ModAttachments.GRAMARY_MODE)) {
      return item.get(ModAttachments.GRAMARY_MODE);
    }

    return GramaryMode.NONE;
  }

  public enum GramaryMode implements StringRepresentable {
    NONE,
    ENTITY_INFO,
    BLOCK_ENTITY_INFO,
    BIND_POSITION;

    public static final Codec<GramaryMode> CODEC = StringRepresentable.fromEnum(GramaryMode::values);
    public static final IntFunction<GramaryMode> BY_ID = ByIdMap.continuous(GramaryMode::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, GramaryMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, GramaryMode::ordinal);

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase(Locale.ROOT);
    }

    public String getKey() {
      return "roots.item.gramary.mode." + this.getSerializedName();
    }

    public Component getComponent() {
      return Component.translatable(getKey());
    }

    public GramaryMode cycle() {
      GramaryMode[] modes = values();
      int nextOrdinal = (this.ordinal() + 1) % modes.length;
      return modes[nextOrdinal];
    }
  }
}
