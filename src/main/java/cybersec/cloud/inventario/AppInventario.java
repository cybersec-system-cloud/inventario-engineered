package cybersec.cloud.inventario;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class AppInventario extends Application<InventarioConfig> {
    
    public static void main(String[] args) throws Exception {
        new AppInventario().run(args);
    }

    @Override
    public void run(InventarioConfig c, Environment e) throws Exception {
        final Inventario risorsaInventario = new Inventario(
                c.getQuantIniziale(),
                c.getDescrDefault()
        );
        e.jersey().register(risorsaInventario);
    }
    
}
