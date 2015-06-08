
package com.heimdell.utils.traversal;

import java.util.*;

public abstract class Traversal {
	protected abstract int     classify(Point point);
	protected abstract boolean canGo   (int colorFrom, int colorTo);
	protected abstract void    dispatch(Point point);

	protected class Point implements Comparable<Point> {
		public int x, y, z, color;

		public Point(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public void makeClassified() {
			if (this.color == 0)
				this.color = classify(this);
		}

		public Point offset(int dx, int dy, int dz) {
			return new Point(x + dx, y + dy, z + dz);
		}

		@Override
		public int compareTo(Point point) {
			return point.color - color;
		}
	}

	protected final int NO_ENTER = 0;

	protected Vector<Point> vicinity(Point point) {
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

	protected Iterable<Point> auxTraverseFrom(Point point, int volumeLimit) {
		Queue<Point>
			queue   = new PriorityQueue();

		HashSet<Point>
			visited = new HashSet();

		queue.add(point);
		point.makeClassified();

		for (; volumeLimit > 0 && !queue.isEmpty(); volumeLimit--) {
			Point current = queue.remove();

			visited.add(current);

			if (current.color != NO_ENTER)
				for (Point near : vicinity(point)) {
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
