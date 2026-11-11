package mysticmods.roots.api.tooltip;

import mysticmods.roots.util.TooltipUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.List;

public class TooltipBuilder<T extends TooltipBuilder.AcceptsTooltip> {
  private String simpleTooltip = "";
  private final List<String> textSegments = new ArrayList<>();
  private final List<NumberFormatter<?>> propertyFormatters = new ArrayList<>();
  private final T properties;

  public TooltipBuilder(T properties) {
    this.properties = properties;
  }

  public TooltipBuilder<T> simple(String simpleTooltip) {
    this.simpleTooltip = simpleTooltip;
    return this;
  }

  public TooltipBuilder<T> segment(NumberFormatter<?> property) {
    this.propertyFormatters.add(property);
    return this;
  }

  public TooltipBuilder<T> segment(String segmentText) {
    this.textSegments.add(segmentText);
    return this;
  }

  public TooltipBuilder<T> linebreak() {
    this.textSegments.add("%LINEBREAK%");
    return this;
  }

  public TooltipBuilder<T> segment(String segmentText, NumberFormatter<?> property) {
    this.textSegments.add(segmentText);
    this.propertyFormatters.add(property);
    return this;
  }

  private String[] getGeneratedTooltip () {
    if (textSegments.isEmpty()) {
      return new String[]{simpleTooltip};
    }

    return TooltipUtil.convertToPositionalFormatters(String.join(" ", textSegments)).split("%LINEBREAK%");
  }

  public T endTooltip() {
    properties.accept(simpleTooltip, getGeneratedTooltip(), propertyFormatters);
    return properties;
  }

  @FunctionalInterface
  public interface AcceptsTooltip extends TriConsumer<String, String[], List<NumberFormatter<?>>> {
  }
}
