package cybersec.cloud.inventario;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class InventarioConfig extends Configuration {
    private int quantIniziale;
    private String descrDefault;
    
    @JsonProperty
    public void setQuantIniziale(int quantIniziale) {
        this.quantIniziale = quantIniziale;
    }
    
    @JsonProperty
    public void setDescrDefault(String descrDefault) {
        this.descrDefault = descrDefault;
    }

    @JsonProperty
    public int getQuantIniziale() {
        return quantIniziale;
    }

    @JsonProperty
    public String getDescrDefault() {
        return descrDefault;
    }
}
