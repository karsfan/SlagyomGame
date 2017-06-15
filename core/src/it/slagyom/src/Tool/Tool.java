package  it.slagyom.src.Tool;

public class Tool {
	public static enum tools{POTION, PARCHMENT, COIN};
	public boolean raccolta = false;
	String info;
	
	public String getInfo() {
		return info;

	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public int getLevel(){	
		return 0;
	}


}
