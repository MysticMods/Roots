package mysticmods.roots.event.neoforge;

/*@EventBusSubscriber(modid = RootsAPI.MODID)
public class ShoulderHandler {
  @SubscribeEvent
  public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    Player player = event.getEntity();
    Level world = event.getLevel();
    if (!world.isEmptyBlock(event.getPos().above())) {
      return;
    }

    if (!world.isClientSide() && event.getHand() == InteractionHand.MAIN_HAND && player.isCrouching() && player.getMainHandItem().isEmpty()) {
      player.getCapability(Capabilities.PLAYER_SHOULDER_CAPABILITY).ifPresent(cap -> {
        if (cap.isShouldered()) {
          EntityType<?> type = cap.getEntityType();
          if (type != null) {
            Entity animal = type.create(world);
            if (animal != null) {
              animal.load(cap.getAnimalSerialized());
              BlockPos pos = event.getPos();
              animal.setPos(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
              world.addFreshEntity(animal);
              player.swing(InteractionHand.MAIN_HAND);
              cap.drop();
              try {
                PlayerShoulderCapability.setLeftShoulder.invokeExact(player, new CompoundTag());
              } catch (Throwable throwable) {
                RootsAPI.LOG.error("Unable to unset player having a shoulder entity", throwable);
              }
              event.setCanceled(true);
              ClientBoundShoulderRidePacket message = new ClientBoundShoulderRidePacket(player, cap);
              Networking.send(PacketDistributor.ALL.noArg(), message);
            }
          }
        }
      });
    }
  }

  @SubscribeEvent
  public static void onDeath(LivingDeathEvent event) {
    LivingEntity living = event.getEntity();
    if (living instanceof Player player) {
      Level world = player.level;
      LazyOptional<IPlayerShoulderCapability> laycap = player.getCapability(Capabilities.PLAYER_SHOULDER_CAPABILITY);
      if (laycap.isPresent()) {
        IPlayerShoulderCapability cap = laycap.orElseThrow(IllegalStateException::new);
        if (cap.isShouldered()) {
          EntityType<?> type = cap.getEntityType();
          if (type != null) {
            Entity animal = type.create(world);
            if (animal != null) {
              animal.load(cap.getAnimalSerialized());
              Vec3 pos = player.position();
              animal.setPos(pos.x, pos.y, pos.z);
              world.addFreshEntity(animal);
              player.swing(InteractionHand.MAIN_HAND);
              cap.drop();
              try {
                PlayerShoulderCapability.setLeftShoulder.invokeExact(player, new CompoundTag());
              } catch (Throwable throwable) {
                RootsAPI.LOG.error("Unable to unset player having a shoulder entity", throwable);
              }
              event.setCanceled(true);
              ClientBoundShoulderRidePacket message = new ClientBoundShoulderRidePacket(player, cap);
              Networking.send(PacketDistributor.ALL.noArg(), message);
            }
          }
        }
      }
    }
  }
}*/
