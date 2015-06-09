
package com.heimdell.utils.traversal;

public class Test {

	public static void _main(String[] args) {
		
		final int [][] space = new int [][] {
			new int [] { 0,0,1,0,1,0,0 },
			new int [] { 0,1,2,1,2,1,0 },
			new int [] { 1,2,2,1,2,2,1 },
			new int [] { 0,1,2,1,2,1,0 },
			new int [] { 0,0,0,2,0,0,0 },
			new int [] { 0,0,0,2,0,0,0 },
			new int [] { 0,0,0,2,0,0,0 },
		};

		final Integer[] blocksCut = new Integer[] { 0 };

		Traversal traversal = new Traversal() {
			protected void dispatch(Point point) {
				if (   point.z != 0 
					|| point.x <  0
					|| point.y <  0
					|| point.x >= 7
					|| point.y >= 7
				   )
					return;

				if (space[point.x][point.y] == 2)
					blocksCut[0] += 1;

				space[point.x][point.y] = 2 - space[point.x][point.y];
			}

			protected int classify(Point point) {
				if (   point.z != 0 
					|| point.x <  0
					|| point.y <  0
					|| point.x >= 7
					|| point.y >= 7
				   )
					return 0;

				return space[point.x][point.y];
			}
		};

		for (int[] row : space) {
			for (int cell : row) {
				System.out.print("" + cell);
			}
			System.out.print("\n");
		}

		System.out.println("->");

		traversal.run(6, 3, 0);

		for (int[] row : space) {
			for (int cell : row) {
				System.out.print("" + cell);
			}
			System.out.print("\n");
		}

		System.out.println("8< " + blocksCut[0]);

	}

}
