package  it.slagyom.src.Tool;

public class Potion extends Tool{
	public static enum Use {WEAPON,HEALTH};
	
	Use use;
	public String getUse() {
		return (String) use.toString();
	}
	int level;
	
	public Potion(Use use,int level) {
		this.level=level;
		this.use=use;
		if(this.use.toString()=="WEAPON"){
			if(level==1)
				super.setInfo("Pozione di livello 1. Incrementa i PP di 10 di un'arma di livello 1");
			if(level==2)
				super.setInfo("Pozione di livello 2. Incrementa i PP di 10 di un'arma di livello 2");
			if(level==3)
				super.setInfo("Pozione di livello 3. Incrementa i PP di 10 di un'arma di livello 3");
		}
		else if(this.use.toString()=="HEALTH"){
			if(level==1)
				super.setInfo("Pozione di livello 1. Incrementa gli HP di 10");
			if(level==2)
				super.setInfo("Pozione di livello 2. Incrementa gli HP di 20");
			if(level==3)
				super.setInfo("Pozione di livello 3. Incrementa gli HP di 30");
		}
		
	}
	public int getLevel(){
		return level;
	}

}
