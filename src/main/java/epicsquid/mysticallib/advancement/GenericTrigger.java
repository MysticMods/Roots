package epicsquid.mysticallib.advancement;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class GenericTrigger<T> implements ICriterionTrigger<GenericTrigger.Instance<T>> {
  private final ResourceLocation id;
  private final Map<PlayerAdvancements, Listeners<T>> listeners = Maps.newHashMap();
  private final IGenericPredicate<T> predicate;

  public GenericTrigger(String id, IGenericPredicate<T> predicate) {
    this(new ResourceLocation(id), predicate);
  }

  public GenericTrigger(ResourceLocation id, IGenericPredicate<T> predicate) { //}, IGenericPredicate basePredicate) {
    this.id = id;
    this.predicate = predicate;
  }

  @Override
  @Nonnull
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public void addListener(@Nonnull PlayerAdvancements advancementsIn, @Nonnull Listener<Instance<T>> listener) {
    Listeners<T> list = listeners.get(advancementsIn);

    if (list == null) {
      list = new Listeners<>(advancementsIn);
      listeners.put(advancementsIn, list);
    }

    list.add(listener);
  }

  @Override
  public void removeListener(@Nonnull PlayerAdvancements advancementsIn, @Nonnull Listener<Instance<T>> listener) {
    Listeners<T> list = listeners.get(advancementsIn);

    if (list != null) {
      list.remove(listener);

      if (list.isEmpty()) {
        listeners.remove(advancementsIn);
      }
    }
  }

  @Override
  public void removeAllListeners(@Nonnull PlayerAdvancements advancementsIn) {
    listeners.remove(advancementsIn);
  }

  @Override
  @Nonnull
  public Instance<T> deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
    return new Instance<>(getId(), predicate.deserialize(json));
  }

  public void trigger(EntityPlayerMP player, T condition) {
    Listeners<T> list = listeners.get(player.getAdvancements());

    if (list != null) {
      list.trigger(player, condition);
    }
  }

  public static class Instance<T> extends AbstractCriterionInstance {
    IGenericPredicate<T> predicate;

    Instance(ResourceLocation location, IGenericPredicate<T> predicate) {
      super(location);

      this.predicate = predicate;
    }

    public boolean test(EntityPlayerMP player, T event) {
      return predicate.test(player, event);
    }
  }

  public static class Listeners<T> {
    PlayerAdvancements advancements;
    Set<Listener<Instance<T>>> listeners = Sets.newHashSet();

    Listeners(PlayerAdvancements advancementsIn) {
      this.advancements = advancementsIn;
    }

    public boolean isEmpty() {
      return listeners.isEmpty();
    }

    public void add(Listener<Instance<T>> listener) {
      listeners.add(listener);
    }

    public void remove(Listener<Instance<T>> listener) {
      listeners.remove(listener);
    }

    void trigger(EntityPlayerMP player, T condition) {
      List<Listener<Instance<T>>> list = Lists.newArrayList();

      for (Listener<Instance<T>> listener : listeners) {
        if (listener.getCriterionInstance().test(player, condition)) {
          list.add(listener);
        }
      }

      if (list.size() != 0) {
        for (Listener<Instance<T>> listener : list) {
          listener.grantCriterion(advancements);
        }
      }
    }
  }
}

