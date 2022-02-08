package it.aspix.serverchat;

import java.util.ArrayList;

/****************************************************************************
 * FIXME: Le proprietà sono public, non è bello perniente ma per adesso.
 ***************************************************************************/
public class Messaggio {
	public String tipo;
	public String nome;
	public String password;
	public String risultato;
	public String testo;
	ArrayList<String> persone;
	
	public Messaggio() {
		persone = new ArrayList<String>();
	}
	

	public Messaggio(String tipo, String altro) {
		super();
		this.tipo = tipo;
		if(this.tipo.equals("rispostaLogin")) {
			this.risultato = altro;
		} else if(this.tipo.equals("messaggio")) {
			this.testo = altro;
		} else if(this.tipo.equals("errore")) {
			this.testo = altro;
		}
	}
	
    public Messaggio(ArrayList<String> lista) {
        super();
        this.tipo = "persone";
        this.persone = lista;
    }


	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRisultato() {
		return risultato;
	}

	public void setRisultato(String risultato) {
		this.risultato = risultato;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

    public ArrayList<String> getPersone() {
        return persone;
    }

    public void setPersone(ArrayList<String> persone) {
        this.persone = persone;
    }
	
}
