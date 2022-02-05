package it.aspix.clientchat.prove.serializzazione;

/****************************************************************************
 * Un semplice oggetto di prova
 ***************************************************************************/
public class Citazione {
	private String autore;
	private String frase;
	private int secolo;
	
	public String getAutore() {
		return autore;
	}
	public void setAutore(String autore) {
		this.autore = autore;
	}
	public String getFrase() {
		return frase;
	}
	public void setFrase(String frase) {
		this.frase = frase;
	}
	public int getSecolo() {
		return secolo;
	}
	public void setSecolo(int secolo) {
		this.secolo = secolo;
	}
	
	@Override
	public String toString() {
		return "Citazione [autore=" + autore + ", frase=" + frase + ", secolo=" + secolo + "]";
	}
	
}
