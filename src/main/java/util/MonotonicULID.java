package main.java.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Monotonic instance of ULID. ULID spec defines monotonicity behavior as if a ULID is to be
 * generated in the same millisecond, the entropy(random) component is to be incremented by 1-bit in
 * the least significant bit with carryover.<br>
 * <br>
 * In practice this behavior is however applicable to ULID's generated from the same source (in
 * Java, from the same instance), else an external synchronization is needed. Hence, instance of
 * this class will produce ULID in monotonic order only if called from the same instance.<br>
 * <br>
 * Usage example:<br>
 * <br>
 *
 * <pre>
 * MonotonicULID ulid = new MonotonicULID();
 * String ulid1 = ulid.next();
 * String ulid2 = ulid.next();
 * String ulid3 = ulid.next();
 * </pre>
 *
 * @see <a href="https://github.com/ulid/spec">ULID</a>
 *
 * @author azam
 * @since 1.0.3
 */
public class MonotonicULID {
  private final Random random;
  private long lastTimestamp;
  private byte[] lastEntropy;

  /**
   * Generate a monotonic ULID generator instance, backed by {@link java.security.SecureRandom}
   * instance.
   */
  public MonotonicULID() {
    this(new SecureRandom());
  }

  /**
   * Generate a monotonic ULID generator instance.
   *
   * @param random {@link java.util.Random} instance
   */
  public MonotonicULID(Random random) {
    if (random == null)
      throw new IllegalArgumentException("java.util.Random instance must not be null");
    this.random = random;
    this.lastEntropy = new byte[ULID.ENTROPY_LENGTH];
    this.lastTimestamp = -1L;
  }

  /**
   * Generate ULID string monotonicly. If this method is called within the same millisecond, last
   * entropy will be incremented by 1 and the ULID string of incremented value is returned.<br>
   * <br>
   * This method will throw a {@link java.lang.IllegalStateException} exception if incremented value
   * overflows entropy length (80b-its/10-bytes)
   *
   * @return ULID string
   */
  public synchronized String generate() {
    long now = System.currentTimeMillis();
    if (now == this.lastTimestamp) {
      // Entropy is big-endian (network byte order) per ULID spec
      // Increment last entropy by 1
      boolean carry = true;
      for (int i = ULID.ENTROPY_LENGTH - 1; i >= 0; i--) {
        if (carry) {
          byte work = this.lastEntropy[i];
          work = (byte) (work + 0x01);
          carry = this.lastEntropy[i] == (byte) 0xff && carry;
          this.lastEntropy[i] = work;
        }
      }
      // Last byte has carry over
      if (carry) {
        // Throw error if entropy overflows in same millisecond per ULID spec
        throw new IllegalStateException("ULID entropy overflowed for same millisecond");
      }
    } else {
      // Generate new entropy
      this.lastTimestamp = now;
      this.random.nextBytes(this.lastEntropy);
    }
    return ULID.generate(this.lastTimestamp, this.lastEntropy);
  }

  /**
   * Generate ULID binary monotonicly. If this method is called within the same millisecond, last
   * entropy will be incremented by 1 and the ULID string of incremented value is returned.<br>
   * <br>
   * This method will throw a {@link java.lang.IllegalStateException} exception if incremented value
   * overflows entropy length (80b-its/10-bytes)
   *
   * @return ULID binary
   */
  public synchronized byte[] generateBinary() {
    long now = System.currentTimeMillis();
    if (now == this.lastTimestamp) {
      // Entropy is big-endian (network byte order) per ULID spec
      // Increment last entropy by 1
      boolean carry = true;
      for (int i = ULID.ENTROPY_LENGTH - 1; i >= 0; i--) {
        if (carry) {
          byte work = this.lastEntropy[i];
          work = (byte) (work + 0x01);
          carry = this.lastEntropy[i] == (byte) 0xff && carry;
          this.lastEntropy[i] = work;
        }
      }
      // Last byte has carry over
      if (carry) {
        // Throw error if entropy overflows in same millisecond per ULID spec
        throw new IllegalStateException("ULID entropy overflowed for same millisecond");
      }
    } else {
      // Generate new entropy
      this.lastTimestamp = now;
      this.random.nextBytes(this.lastEntropy);
    }
    return ULID.generateBinary(this.lastTimestamp, this.lastEntropy);
  }
}
