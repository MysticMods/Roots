package epicsquid.roots.util;

// Copied with permission from Athenaeum by CodeTaylor, Apache License
// https://github.com/codetaylor/athenaeum/blob/master/src/main/java/com/codetaylor/mc/athenaeum/util/StringHelper.java

public class StringHelper {

  public static String capitalizeFirstLetter(String input) {

    return input.substring(0, 1).toUpperCase() + input.substring(1);
  }

  public static String lowercaseFirstLetter(String input) {

    return input.substring(0, 1).toLowerCase() + input.substring(1);
  }

  public static String ticksToHMS(int ticks) {

    int totalSecs = ticks / 20;
    int hours = totalSecs / 3600;
    int minutes = (totalSecs % 3600) / 60;
    int seconds = totalSecs % 60;

    if (hours > 0) {
      return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    } else {
      return String.format("%02d:%02d", minutes, seconds);
    }
  }

  private StringHelper() {
    //
  }

}
