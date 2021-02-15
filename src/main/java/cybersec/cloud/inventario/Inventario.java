package cybersec.cloud.inventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/inventario")
@Produces(MediaType.APPLICATION_JSON)
public class Inventario {
    
    private final List<Prodotto> prodotti;
    private final int quantIniziale;
    private final String descrDefault;
    
    public Inventario(int quantIniziale, String descrDefault) {
        prodotti = new ArrayList<Prodotto>();
        this.quantIniziale = quantIniziale;
        this.descrDefault = descrDefault;
    }
    
    @POST
    public void postProdotto(@QueryParam("codice") String codice,
            @QueryParam("descrizione") Optional<String> descrizione,
            @QueryParam("quant") Optional<Integer> quant) {
        // Imposta la descrizione (passata o default)
        String d;
        if (descrizione.isPresent()) d = descrizione.get();
        else d = descrDefault;
        // Imposta la quantita' (passata o iniziale)
        int q;
        if (quant.isPresent()) q = quant.get();
        else q = quantIniziale;
        // Aggiunge il prodotto 
        prodotti.add(new Prodotto(codice,d,q));
    }
    
    private int indiceProdotto(String codice) {
        for(int i=0; i<prodotti.size(); i++) {
            if(prodotti.get(i).getCodice().equals(codice))
                return i;
        }
        return -1;
    }
     
    @GET
    @Path("/{codice}")
    public Prodotto getProdotto(@PathParam("codice") String codice) {
        int i = indiceProdotto(codice);
        if (i != -1)
            return prodotti.get(i);
        return null;
    }
    
    @PUT
    @Path("/{codice}")
    public void putProdotto(@PathParam("codice") String codice,
            @QueryParam("descrizione") Optional<String> descrizione,
            @QueryParam("quant") int quant) {
        int i = indiceProdotto(codice);
        if (i != -1) {
            String d = prodotti.get(i).getDescrizione();
            if(descrizione.isPresent()) d = descrizione.get();
            prodotti.remove(i);
            prodotti.add(new Prodotto(codice,d,quant));
        }
    }
    
    @DELETE
    @Path("/{codice}")
    public void deleteProdotto(@PathParam("codice") String codice) {
        int i = indiceProdotto(codice);
        if (i != -1)
            prodotti.remove(i);
    }
    
}
