package mysticmods.roots.api.grove.power.consumer;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.grove.Rank;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TotalPowerRequest {
  private final TagKey<Grove> tagForPower;
  private final int totalRequired;
  private final int rate;
  @Nullable
  private final ValueForRank rankedRate;
  private int acquired;

  public TotalPowerRequest(TagKey<Grove> tagForPower, int totalRequired, @NotNull ValueForRank rankedRate) {
    this.tagForPower = tagForPower;
    this.totalRequired = totalRequired;
    this.rate = 0;
    this.rankedRate = rankedRate;
  }

  public TotalPowerRequest(int rate, int totalRequired, TagKey<Grove> tagForPower) {
    this.rate = rate;
    this.totalRequired = totalRequired;
    this.tagForPower = tagForPower;
    this.rankedRate = null;
  }

  public boolean fulfilled() {
    return acquired >= totalRequired;
  }

  public int rate(IGroveInstance grove) {
    if (rankedRate != null) {
      return rankedRate.value(grove.rankForRank());
    }

    return rate;
  }

  public void supply (IGroveInstance grove, int amount) {
    if (grove.is(this.tagForPower)) {
      this.acquired += amount;
    }
  }

  public PowerRequest requestForThisTick(IGroveInstance grove) {
    return new PowerRequest(tagForPower, Math.min(rate(grove), totalRequired));
  }

  public record ValueForRank(int unranked, int first, int second, int third, int fourth) {
    public int value(Rank rank) {
      return switch (rank) {
        case UNRANKED -> unranked;
        case FIRST -> first;
        case SECOND -> second;
        case THIRD -> third;
        case FOURTH -> fourth;
      };
    }
  }
}
