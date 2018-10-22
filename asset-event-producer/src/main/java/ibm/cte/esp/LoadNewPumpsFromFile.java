package ibm.cte.esp;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import ibm.cte.esp.model.AssetEvent;

class LoadNewPumpsFromFile {

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		ApplicationConfig cfg = new ApplicationConfig();

		PumpSimulator ps = new PumpSimulator(cfg);
		ps.prepareProducer();
		FileReader input= new FileReader("src/test/resources/testpumps.json");
			Gson parser = new Gson();
			AssetEvent[] assets = parser.fromJson(new JsonReader(input), AssetEvent[].class);
			for( AssetEvent ae : assets) {
				System.out.println(ae.toString());
				ps.publishAsset(ae);
			}
		input.close();
	}

}
