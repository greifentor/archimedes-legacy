package archimedes.legacy.gui;

/**
 * @author ollie (25.06.2020)
 */
public class Counter {

	private int count = 0;

	public Counter(int value) {
		super();
		this.count = value;
	}

	public int inc() {
		return count++;
	}

	public int getValue() {
		return count;
	}

}