
package com.heimdell.utils.traversal;

import java.util.*;

public abstract class Traversal {
	protected abstract int     classify(Point point);
	protected abstract boolean canGo   (int colorFrom, int colorTo);
	protected abstract void    dispatch(int color,     Point point);

	protected class Point {
		public int x, y, z;

		public Point(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Point offset(int dx, int dy, int dz) {
			return new Point(x + dx, y + dy, z + dz);
		}
	}

	protected final int NO_ENTER = 0;

	protected Vector<Point> vicinity(Point point) {
		Point[] candidates = new Point[] {
			point.offset( 1,  0,  0),
			point.offset(-1,  0,  0),
			point.offset( 0,  0,  1),
			point.offset( 0,  0, -1),
			point.offset( 0,  1,  0),
			point.offset( 0, -1,  0),
		};

		Vector<Point>
			result = new Vector();

		for (Point candidate : candidates) {
			result.add(candidate);
		}

		return result;
	}

	protected InjectiveMap<Integer, Point> auxTraverseFrom(Point point, int volumeLimit) {
		Queue<Point>
			queue   = new ArrayDeque();

		InjectiveMap<Integer, Point>
			mapping = new InjectiveMap();

		HashSet<Point>
			visited = new HashSet();

		mapping.put(classify(point), point);
		queue  .add(point);

		for (; volumeLimit > 0 && !queue.isEmpty(); volumeLimit--) {
			Point current = queue.remove();

			visited.add(current);

			int color = mapping.getSource(current);

			if (color != 0)
				for (Point near : vicinity(point)) {
					if (!visited.contains(near)) {
						if (!mapping.hasTarget(near))
							mapping.put(classify(near), near);

						if (canGo(color, mapping.getSource(near)))
							queue.add(near);
					}
				}
		}

		return mapping;
	}

	public void run(Point from) {
		run(from, 10000);
	}

	public void run(Point from, int limith) {
		InjectiveMap<Integer, Point>
			mapping = auxTraverseFrom(from, limith);

		for (Point pt : mapping.setOfTargets()) {
			Integer color = mapping.getSource(pt);

			dispatch(color, pt);
		}
	}

}
