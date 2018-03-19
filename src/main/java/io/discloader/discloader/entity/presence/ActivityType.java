/**
 * 
 */
package io.discloader.discloader.entity.presence;

/**
 * @author perryberman
 *
 */
public enum ActivityType {
	GAME(0), STREAMING(1), LISTENING(2);
	private int num;

	ActivityType(int n) {
		num = n;
	}

	public int toInt() {
		return num;
	}

	public static ActivityType getActivityTypeFromInt(int n) {
		for (ActivityType a : values()) {
			if (a.num == n)
				return a;
		}
		return GAME;
	}

}
