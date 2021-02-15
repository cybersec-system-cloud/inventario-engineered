package cybersec.cloud.inventario;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Prodotto {
    
    private String codice;
    private String descrizione;
    private int quant;
    
    public Prodotto() {
        // Ci pensa Jackson
    }
    
    public Prodotto(String codice, String descrizione, int quant) {
        this.codice = codice;
        this.descrizione = descrizione;
        this.quant = quant;
    }
    
    @JsonProperty
    public String getCodice() {
        return this.codice;
    }
    
    @JsonProperty
    public String getDescrizione() {
        return this.descrizione;
    }

    @JsonProperty
    public int getQuant() {
        return this.quant;
    }
    
}
