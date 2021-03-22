package archimedes.gui.diagram;

import java.awt.Polygon;
import java.awt.Rectangle;

import corent.base.Direction;

/**
 * A calculator for rectangle segments.
 *
 * @author ollie (22.03.2021)
 */
public class RectSegmentCalculator {

	public Polygon getSegment(Rectangle rect, Direction direction) {
		if (direction == Direction.UP) {
			int halfHeight = Math.min(rect.height / 2, rect.width / 2);
			return new Polygon(
					new int[] { rect.x, rect.x + rect.width, rect.x + rect.width - halfHeight, rect.x + halfHeight },
					new int[] { rect.y, rect.y, rect.y + halfHeight, rect.y + halfHeight },
					4);
		} else if (direction == Direction.DOWN) {
			int halfHeight = Math.min(rect.height / 2, rect.width / 2);
			return new Polygon(
					new int[] { rect.x, rect.x + rect.width, rect.x + rect.width - halfHeight, rect.x + halfHeight },
					new int[] {
							rect.y + rect.height,
							rect.y + rect.height,
							rect.y + rect.height - halfHeight,
							rect.y + rect.height - halfHeight },
					4);
		} else if (direction == Direction.LEFT) {
			int halfWidth = Math.min(rect.height / 2, rect.width / 2);
			return new Polygon(
					new int[] { rect.x, rect.x + halfWidth, rect.x + halfWidth, rect.x },
					new int[] { rect.y, rect.y + halfWidth, rect.y + rect.height - halfWidth, rect.y + rect.height },
					4);
		} else if (direction == Direction.RIGHT) {
			int halfWidth = Math.min(rect.height / 2, rect.width / 2);
			return new Polygon(
					new int[] {
							rect.x + rect.width,
							rect.x + rect.width - halfWidth,
							rect.x + rect.width - halfWidth,
							rect.x + rect.width },
					new int[] { rect.y, rect.y + halfWidth, rect.y + rect.height - halfWidth, rect.y + rect.height },
					4);
		}
		return new Polygon();
	}

}