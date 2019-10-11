package archimedes.legacy.scheme;

import archimedes.legacy.model.TabellenspaltenModel;

public class TSMSortContainer implements Comparable {

	public String sorter = null;
	public TabellenspaltenModel tsm = null;

	public TSMSortContainer(String sorter, TabellenspaltenModel tsm) {
		super();
		this.sorter = sorter;
		this.tsm = tsm;
	}

	/* Implementierung des Interfaces Comparable. */

	public int compareTo(Object obj) {
		return this.sorter.toLowerCase().compareTo(((TSMSortContainer) obj).sorter.toLowerCase());
	}

}