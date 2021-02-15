package cybersec.cloud.inventario;

import java.net.URI;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

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
    public Response postProdotto(@QueryParam("codice") Optional<String> codice,
            @QueryParam("descrizione") Optional<String> descrizione,
            @QueryParam("quant") Optional<Integer> quant) {
        // Se "codice" non è specificato, 
        // restituisce errore (BAD REQUEST)
        if (!codice.isPresent()) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Il codice deve essere sempre indicato")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se "codice" è già utilizzato per altro prodotto, 
        // restituisce errore (CONFLICT)
        int i = indiceProdotto(codice.get());
        if (i != -1) {
            return Response.status(Status.CONFLICT)
                    .entity("codice " + codice.get() + " già utilizzato")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Imposta la descrizione (passata o default)
        String d;
        if (descrizione.isPresent()) d = descrizione.get();
        else d = descrDefault;
        // Imposta la quantita' (passata o iniziale)
        int q;
        if (quant.isPresent()) q = quant.get();
        else q = quantIniziale;
        // Aggiunge il prodotto 
        prodotti.add(new Prodotto(codice.get(),d,q));
        
        // Restituisce un messaggio di conferma creazione "codice"
        URI pUri = UriBuilder.fromResource(Inventario.class).path(codice.get()).build();
        return Response.created(pUri).build();
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
    public Response getProdotto(@PathParam("codice") String codice) {
        int i = indiceProdotto(codice);
        
        // Se non c'è un prodotto con "codice",
        // restituisce un messaggio di errore (NOT FOUND)
        if (i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Il codice " + codice + " non corrisponde a prodotti")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se c'è, invece, restituisce il prodotto (OK)
        return Response.ok().entity(prodotti.get(i)).build();
    }
    
    @PUT
    @Path("/{codice}")
    public Response putProdotto(@PathParam("codice") String codice,
            @QueryParam("descrizione") Optional<String> descrizione,
            @QueryParam("quant") Optional<Integer> quant) {
        int i = indiceProdotto(codice);
        
        // Se non c'è un prodotto con "codice",
        // restituisce un messaggio di errore(NOT FOUND)
        if (i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Il codice " + codice + " non corrisponde a prodotti")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se "quant" non è indicato,
        // restituisce un messaggio di errore (BAD REQUEST)
        if (!quant.isPresent()) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Quant deve essere sempre indicato")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Aggiorna la risorsa
        String d = prodotti.get(i).getDescrizione();
        if(descrizione.isPresent()) d = descrizione.get();
        prodotti.remove(i);
        prodotti.add(new Prodotto(codice,d,quant.get()));
        
        // Restituisce un messaggio di conferma aggiornamento (OK)
        return Response.ok()
                .entity("prodotto aggiornato")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
    
    @DELETE
    @Path("/{codice}")
    public Response deleteProdotto(@PathParam("codice") String codice) {
        int i = indiceProdotto(codice);
        
        // Se non c'è un prodotto con "codice",
        // restituisce un messaggio di errore(NOT FOUND)
        if (i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Il codice " + codice + " non corrisponde a prodotti")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Elimina il prodotto
        prodotti.remove(i);
        
        // Restituisce messaggio di conferma eliminazione (OK)
        return Response.ok()
                .entity("Prodotto " + codice + " eliminato")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
    
}
