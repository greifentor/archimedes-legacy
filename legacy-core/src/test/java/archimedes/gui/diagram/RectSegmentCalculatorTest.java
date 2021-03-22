package archimedes.gui.diagram;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Polygon;
import java.awt.Rectangle;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import corent.base.Direction;

@ExtendWith(MockitoExtension.class)
public class RectSegmentCalculatorTest {

	private static final int HEIGHT = 50;
	private static final int WIDTH = 100;
	private static final int X = 10;
	private static final int Y = 10;

	@InjectMocks
	private RectSegmentCalculator unitUnderTest;

	@Nested
	class TestsOfMethod_getSegment_Rectangle_Direction {

		@Test
		void passAValidRectangleAndDirectionUP_returnsAPolygonWithTheCorrectCoordinatesForTheUpperSegment() {
			assertPolygonEquals(
					new Polygon(
							new int[] { X, X + WIDTH, X + WIDTH - (HEIGHT / 2), X + (HEIGHT / 2) },
							new int[] { Y, Y, Y + (HEIGHT / 2), Y + (HEIGHT / 2) },
							4),
					unitUnderTest.getSegment(new Rectangle(X, Y, WIDTH, HEIGHT), Direction.UP));
		}

		@Test
		void passAValidRectangleAndDirectionUP_moreHighThanWide_returnsAPolygonWithTheCorrectCoordinatesForTheUpperSegment() {
			int width = HEIGHT;
			int height = WIDTH;
			assertPolygonEquals(
					new Polygon(
							new int[] { X, X + width, X + width - (width / 2), X + (width / 2) },
							new int[] { Y, Y, Y + (width / 2), Y + (width / 2) },
							4),
					unitUnderTest.getSegment(new Rectangle(X, Y, width, height), Direction.UP));
		}

		@Test
		void passAValidRectangleAndDirectionDOWN_returnsAPolygonWithTheCorrectCoordinatesForTheUpperSegment() {
			assertPolygonEquals(
					new Polygon(
							new int[] { X, X + WIDTH, X + WIDTH - (HEIGHT / 2), X + (HEIGHT / 2) },
							new int[] { Y + HEIGHT, Y + HEIGHT, Y + HEIGHT - (HEIGHT / 2), Y + HEIGHT - (HEIGHT / 2) },
							4),
					unitUnderTest.getSegment(new Rectangle(X, Y, WIDTH, HEIGHT), Direction.DOWN));
		}

		@Test
		void passAValidRectangleAndDirectionDOWN_moreHighThanWide_returnsAPolygonWithTheCorrectCoordinatesForTheUpperSegment() {
			int width = HEIGHT;
			int height = WIDTH;
			assertPolygonEquals(
					new Polygon(
							new int[] { X, X + width, X + width - (width / 2), X + (width / 2) },
							new int[] { Y + height, Y + height, Y + height - (width / 2), Y + height - (width / 2) },
							4),
					unitUnderTest.getSegment(new Rectangle(X, Y, width, height), Direction.DOWN));
		}

		@Test
		void passAValidRectangleAndDirectionLEFT_returnsAPolygonWithTheCorrectCoordinatesForTheUpperSegment() {
			assertPolygonEquals(
					new Polygon(
							new int[] { X, X + (HEIGHT / 2), X + (HEIGHT / 2), X },
							new int[] { Y, Y + (HEIGHT / 2), Y + HEIGHT - (HEIGHT / 2), Y + HEIGHT },
							4),
					unitUnderTest.getSegment(new Rectangle(X, Y, WIDTH, HEIGHT), Direction.LEFT));
		}

		@Test
		void passAValidRectangleAndDirectionLEFT_moreHighThanWide_returnsAPolygonWithTheCorrectCoordinatesForTheUpperSegment() {
			int width = HEIGHT;
			int height = WIDTH;
			assertPolygonEquals(
					new Polygon(
							new int[] { X, X + (width / 2), X + (width / 2), X },
							new int[] { Y, Y + (width / 2), Y + height - (width / 2), Y + height },
							4),
					unitUnderTest.getSegment(new Rectangle(X, Y, width, height), Direction.LEFT));
		}

		@Test
		void passAValidRectangleAndDirectionRIGHT_returnsAPolygonWithTheCorrectCoordinatesForTheUpperSegment() {
			assertPolygonEquals(
					new Polygon(
							new int[] { X + WIDTH, X + WIDTH - (HEIGHT / 2), X + WIDTH - (HEIGHT / 2), X + WIDTH },
							new int[] { Y, Y + (HEIGHT / 2), Y + HEIGHT - (HEIGHT / 2), Y + HEIGHT },
							4),
					unitUnderTest.getSegment(new Rectangle(X, Y, WIDTH, HEIGHT), Direction.RIGHT));
		}

		@Test
		void passAValidRectangleAndDirectionRIGHT_moreHighThanWide_returnsAPolygonWithTheCorrectCoordinatesForTheUpperSegment() {
			int width = HEIGHT;
			int height = WIDTH;
			assertPolygonEquals(
					new Polygon(
							new int[] { X + width, X + width - (width / 2), X + width - (width / 2), X + width },
							new int[] { Y, Y + (width / 2), Y + height - (width / 2), Y + height },
							4),
					unitUnderTest.getSegment(new Rectangle(X, Y, width, height), Direction.RIGHT));
		}

	}

	private static void assertPolygonEquals(Polygon p0, Polygon p1) {
		assertEquals(p0.npoints, p1.npoints);
		assertArrayEquals(p0.xpoints, p1.xpoints);
		assertArrayEquals(p0.ypoints, p1.ypoints);
	}

}