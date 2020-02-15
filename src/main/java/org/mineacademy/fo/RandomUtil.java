package org.mineacademy.fo;

import lombok.experimental.UtilityClass;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;

import java.util.*;
import java.util.function.Predicate;

/**
 * Utility class for generating random numbers.
 */
@UtilityClass
public class RandomUtil {

	/**
	 * The random instance for this class
	 */
	private final Random random = new Random();

	/**
	 * The basic anglo-saxon alphabet used for getting random text
	 */
	private final char[] ENGLISH_LETTERS = new char[]{
		'a', 'b', 'c', 'd', 'e', ' ',
		'f', 'g', 'h', 'i', 'j',
		'k', 'l', 'm', 'n', 'o',
		'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'y', 'z',
		'!', '?', ',', '.', ' '
	};

	/**
	 * Symbols for chat colors using the & character
	 */
	private final char[] CHAT_COLORS = new char[]{
		'0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e',
		'f', 'k', 'l', 'n', 'o'
	};

	/**
	 * Return the random instance
	 *
	 * @return
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * Return true if the given percent was matched
	 *
	 * @param percent the percent, from 0 to 100
	 * @return
	 */
	public boolean chance(int percent) {
		return random.nextDouble() * 100D < percent;
	}

	/**
	 * Return true if the given percent was matched
	 *
	 * @param percent the percent, from 0.00 to 1.00
	 * @return
	 */
	public boolean chanceD(double percent) {
		return random.nextDouble() < percent;
	}

	/**
	 * Returns a string that consist of alphanumerical a-z characters, !, ?, ,, .
	 * and whitespace of desired length from {@link #ENGLISH_LETTERS}
	 *
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public String nextString(int minLength, int maxLength) {
		String message = "";

		for (int i = 0; i < minLength + random.nextInt(maxLength); i++)
			message += ENGLISH_LETTERS[random.nextInt(ENGLISH_LETTERS.length)];

		return message;
	}

	/**
	 * Returns a random dye color
	 *
	 * @return
	 */
	public DyeColor nextDyeColor() {
		return DyeColor.values()[random.nextInt(DyeColor.values().length)];
	}

	/**
	 * Returns a random chat color in this format: & + the color character
	 * Example: &e for yellow
	 *
	 * @return
	 */
	public String nextChatColor() {
		return "&" + CHAT_COLORS[RandomUtil.getRandom().nextInt(CHAT_COLORS.length)];
	}

	/**
	 * Returns a random integer in bounds
	 *
	 * @param min
	 * @param max
	 * @return
	 */
	public int nextBetween(int min, int max) {
		Valid.checkBoolean(min <= max, "Min !< max");

		return min + nextInt(max - min + 1);
	}

	/**
	 * Returns a random integer, see {@link Random#nextInt(int)}
	 *
	 * @param boundExclusive
	 * @return
	 */
	public int nextInt(int boundExclusive) {
		return random.nextInt(boundExclusive);
	}

	/**
	 * Returns a random true/false by 50% chance
	 *
	 * @return
	 */
	public boolean nextBoolean() {
		return random.nextBoolean();
	}

	/**
	 * Return a random item in array
	 *
	 * @param <T>
	 * @param items
	 * @return
	 */
	public <T> T nextItem(T... items) {
		return items[nextInt(items.length)];
	}

	/**
	 * Return a random item in list
	 *
	 * @param <T>
	 * @param items
	 * @return
	 */
	public <T> T nextItem(Collection<T> items) {
		return nextItem(items, null);
	}

	/**
	 * Return a random item in list only among those that match the given condition
	 *
	 * @param <T>
	 * @param items
	 * @param condition the condition applying when selecting
	 * @return
	 */
	public <T> T nextItem(Collection<T> items, Predicate<T> condition) {
		final List<T> list = items instanceof List ? (List<T>) items : new ArrayList<>(items);

		// Remove values failing the condition
		if (condition != null)
			for (final Iterator it = list.iterator(); it.hasNext(); ) {
				final T item = (T) it.next();

				if (!condition.test(item))
					it.remove();
			}

		return list.get(nextInt(list.size()));
	}

	/**
	 * Returns a random location
	 *
	 * @param origin
	 * @param radius
	 * @param is3D,  true for sphere, false for cylinder search
	 * @return
	 */
	public Location nextLocation(Location origin, double radius, boolean is3D) {
		final double randomRadius = random.nextDouble() * radius;
		final double theta = Math.toRadians(random.nextDouble() * 360);
		final double phi = Math.toRadians(random.nextDouble() * 180 - 90);

		final double x = randomRadius * Math.cos(theta) * Math.sin(phi);
		final double z = randomRadius * Math.cos(phi);
		final Location newLoc = origin.clone().add(x, is3D ? randomRadius * Math.sin(theta) * Math.cos(phi) : 0, z);

		return newLoc;
	}

	/**
	 * Return a random x location within that chunk
	 *
	 * @param chunk
	 * @return
	 */
	public int nextChunkX(Chunk chunk) {
		return RandomUtil.nextInt(16) + (chunk.getX() << 4) - 16;
	}

	/**
	 * Return a random z location within that chunk
	 *
	 * @param chunk
	 * @return
	 */
	public int nextChunkZ(Chunk chunk) {
		return RandomUtil.nextInt(16) + (chunk.getZ() << 4) - 16;
	}
}
