
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    private ComboBox<Country> boxStati;
    
    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	boxStati.getItems().clear();
    	try {
    		int anno=Integer.parseInt(txtAnno.getText());
    		if(anno>2016 || anno<1816) {
    			txtResult.setText("Inserire un anno compreso nell'intervallo 1816-2016!");
    			return;
    		}else {
    			model.creaGrafo(anno);
    			txtResult.appendText("#Vertici: "+ model.getNVertici()+"\n");
    			txtResult.appendText("#Archi: "+ model.getNArchi()+"\n");
    			
    			txtResult.appendText("Numero componenti connesse: "+ model.getNumberOfConnectedComponents()+"\n");
    			
    			Map<Country, Integer> stats = model.getCountryCounts();
    			
    			txtResult.appendText("Stati confinanti per Stato:\n");
    			for (Country country : stats.keySet())
    				txtResult.appendText( country.getNomeCom()+" : " +stats.get(country)+"\n");	
    		}
    		boxStati.getItems().addAll(model.getCountries());
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un valore numerico valido");
    		return;
    	}
    }
    
    @FXML
    void doStatiRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	List<Country> result=model.fermateRaggiungibili(boxStati.getValue());
    	txtResult.appendText("#Fermate ragiungibili da "+boxStati.getValue()+": "+result.size()+"\n");
    	txtResult.appendText("Fermate raggiungibili da: "+boxStati.getValue()+"\n");
    	for(Country c:result) {
    		if(!c.equals(boxStati.getValue()))
    		txtResult.appendText(c+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxStati != null : "fx:id=\"boxStati\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
