package kalah.SeedStorage;

/**
 * 种子处理对象
 */
public class SeedCollection {
	private int _seeds;

	public SeedCollection(int seeds) {
		_seeds = seeds;
	}
	//移动种子
	public int sowSeed(int seedsToSow) {
		_seeds += 1;
		return seedsToSow - 1;
	}
	//收集种子
	public int collectSeeds() {
		int harvest = _seeds;
		_seeds = 0;
		return harvest;
	}

	public void storeSeeds(int seeds) {
		_seeds += seeds;
	}

	public boolean hasOneLeft() {
		if (_seeds == 1) {
			return true;
		}
		return false;
	}

	public boolean isEmpty() {
		if (_seeds == 0) {
			return true;
		}
		return false;
	}

	public int getSeeds() {
		return _seeds;
	}

	@Override
	public String toString() {
		return "" + _seeds;
	}
}
