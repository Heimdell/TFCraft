
package com.heimdell.utils.traversal;

import java.util.*;

public abstract class Traversal {
	// assign the number to the block at given point
	// it will act as a priority
	protected abstract int classify(Point point);

	// check, if we could traverse from one color to another
	// for instance: 
	//     YES for wood -> wood, 
	//     YES for wood -> leaves, 
	//     YES for leaves -> leaves,
	//     NO  otherwise
	//
	// in dense forests it could wipe more leaves that supposed to;
	// however, we could classify leaves farther than +/-5 X/Z as air
	// to stop the algorithm
	protected abstract boolean canGo   (int colorFrom, int colorTo);

	// perform some action on traversed block:
	//   cut the wood, wipe the leaves, update the air, etc.
	protected abstract void    dispatch(Point point);

	protected class Point implements Comparable<Point> {
		public int x, y, z, color;

		public Point(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		// cache the classification
		public void makeClassified() {
			if (this.color == 0)
				this.color = classify(this);
		}

		public Point offset(int dx, int dy, int dz) {
			return new Point(x + dx, y + dy, z + dz);
		}

		// make sure that PriorityQueue will pop higher-colored points first
		@Override
		public int compareTo(Point point) {
			return point.color - color;
		}

		// ignore the color on comparison (we compare locations here)
		@Override
		public boolean equals(Object object) {
			if (object instanceof Point) {
				Point point = (Point) object;

				return x == point.x && y == point.y && z == point.z;
			}
			return false;
		}
	}

	// the "air" constant
	protected final int AIR = 0;

	private Vector<Point> vicinity(Point point) {
		Vector<Point>
			result = new Vector();

		for (int x = -1; x <= 1; x++) 
		for (int y = -1; y <= 1; y++)
		for (int z = -1; z <= 1; z++) {
			if (x != 0 || y != 0 || z != 0)
				result.add(point.offset(x, y, z));
		}

		return result;
	}

	private Iterable<Point> auxTraverseFrom(Point point, int volumeLimit) {
		Queue<Point>
			queue   = new PriorityQueue();

		// we will return this container
		HashSet<Point>
			visited = new HashSet();

		queue.add(point);
		point.makeClassified();

		for (; volumeLimit > 0 && !queue.isEmpty(); volumeLimit--) {
			Point current = queue.remove();

			visited.add(current);

			if (current.color != AIR)
				for (Point near : vicinity(point)) {
					// make sure we're no classifying same point twice
					if (!visited.contains(near)) {
						near.makeClassified();

						if (canGo(current.color, near.color))
							queue.add(near);
					}
				}
		}

		return visited;
	}

	public void run(Point from) {
		run(from, 10000);
	}

	public void run(Point from, int limith) {
		for (Point pt : auxTraverseFrom(from, limith)) {
			dispatch(pt);
		}
	}

}
