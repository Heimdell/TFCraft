
package com.heimdell.utils.traversal;

import java.util.*;

public class InjectiveMap<Source, Target> {
	Map<Source, Collection<Target>>
		forth = new HashMap();

	Map<Target, Source>
		back  = new HashMap();

	public boolean hasTarget(Target target) {
		return back.containsKey(target);
	}

	public boolean hasSource(Source source) {
		return forth.containsKey(source);
	}

	public Source getSource(Target target) {
		return back.get(target);
	}

	public Iterable<Target> getTargets(Source source) {
		return forth.get(source);
	}

	public void put(Source source, Target target) {
		if (this.hasSource(source)) {
			forth.get(source).add(target);
			back.put(target, source);
		}
		else {
			forth.put(source, new HashSet());
			put(source, target);
		}
	}

	public final Set<Target> setOfTargets() {
		return back.keySet();
	}
}
