package  it.slagyom.src.Tool;

public class Parchment extends Tool {
	public int level;
	//public boolean raccolta;

	public Parchment(int level) {
		this.level = level;
		if (level == 1)
			super.setInfo("Pergamena di livello 1");
		if (level == 2)
			super.setInfo("Pergamena di livello 2");
		raccolta = false;
	}

	public int getLevel() {
		return level;
	}
}
