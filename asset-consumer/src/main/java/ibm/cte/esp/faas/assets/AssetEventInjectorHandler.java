package ibm.cte.esp.faas.assets;

public class AssetEventInjectorHandler {
	
	public String sayHello(io.kubeless.Event event, io.kubeless.Context context) {
        System.out.println(event.toString());
        return "Hello world! AFDFCH";
    }
}
