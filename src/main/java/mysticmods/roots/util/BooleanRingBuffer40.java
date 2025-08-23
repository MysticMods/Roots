package mysticmods.roots.util;

public class BooleanRingBuffer40 {
  private final byte[] buffer = new byte[40]; // Holds 0 (false) or 1 (true)
  private int start = 0;    // Index of the oldest element
  private int size = 0;     // Current number of elements (max 10)
  private int countTrue = 0; // Cached count of `1` values

  /**
   * Adds a new value to the end of the buffer.
   * If the buffer is full, evicts the oldest value.
   */
  public void add(boolean value) {
    byte val = (byte) (value ? 1 : 0);

    if (size < buffer.length) {
      buffer[(start + size) % buffer.length] = val;
      size++;
    } else {
      int index = start;
      if (buffer[index] == 1) countTrue--;
      buffer[index] = val;
      start = (start + 1) % buffer.length;
    }

    if (val == 1) countTrue++;
  }

  /**
   * Returns the number of true (1) values currently in the buffer.
   */
  public int countTrue() {
    return countTrue;
  }

  /**
   * Gets the value at the specified index (0 = oldest).
   */
  public boolean get(int index) {
    if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
    return buffer[(start + index) % buffer.length] == 1;
  }

  public int size() {
    return size;
  }

  public void replaceLast(boolean value) {
    if (size == 0) return;
    buffer[(start + size - 1) % buffer.length] = (byte) (value ? 1 : 0);
  }
}
