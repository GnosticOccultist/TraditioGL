package fr.traditio.gl.opengl;

import java.util.BitSet;
import java.util.Objects;

final class DefineSet {

	private final BitSet defined;

	DefineSet(int defineCount) {
		this.defined = new BitSet(defineCount);
	}

	DefineSet(DefineSet defines) {
		this.defined = (BitSet) defines.defined.clone();
	}

	public boolean isSet(int id) {
		return defined.get(id);
	}

	public void set(int id, int val) {
		defined.set(id, true);
	}

	public void unset(int id) {
		defined.clear(id);
	}

	public boolean set(int id, boolean val) {
		var old = isSet(id);
		if (val) {
			set(id, 1);
		} else {
			unset(id);
		}

		return val != old;
	}

	public void clear() {
		defined.clear();
	}

	@Override
	public int hashCode() {
		return defined.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		DefineSet other = (DefineSet) obj;
		return Objects.equals(defined, other.defined);
	}

}
