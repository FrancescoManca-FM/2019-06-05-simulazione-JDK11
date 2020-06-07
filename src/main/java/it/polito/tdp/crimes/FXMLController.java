/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.District;
import it.polito.tdp.crimes.model.DistrictPeso;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	txtResult.clear();
    	int anno = 0;
    	try {
    		anno = this.boxAnno.getValue();
    	}catch(NumberFormatException nfe) {
    		nfe.printStackTrace();
    		throw new RuntimeException("Errore inserimento anno");
    	}
    	this.model.creaGrafo(anno);
    	txtResult.appendText("GRAFO CREATO!\n NUMERO VERTICI: "+this.model.verticiSize()+"\n NUMERO ARCHI: "+this.model.archiSize()+"\n");
    	
    	for(District d : this.model.getVertici()) {
    		txtResult.appendText("DISTRETTO: "+d.getId()+"\nVICINI: \n");
    		List<DistrictPeso> vicini = this.model.getDistrettiPerPeso(d);
    		for(DistrictPeso dp : vicini) {
    			txtResult.appendText(dp.getD2().getId()+",  DISTANZA: "+dp.getDistanza()+"\n");
    		}
    	}
    	boxMese.getItems().addAll(this.model.getMesi());
    	boxGiorno.getItems().addAll(this.model.getGiorni());
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	if(boxAnno.getValue()==null || boxMese.getValue()==null || boxGiorno.getValue()==null) {
    		txtResult.appendText("Devi selezionare anno mese e giorno");
    		return;
    	}
    	this.model.initSim(boxAnno.getValue(), boxMese.getValue(), boxGiorno.getValue(), Integer.parseInt(txtN.getText()));
    	this.model.runSim();
    	txtResult.appendText("SIMULAZIONE EFFETTUATA\n NUMERO CRIMINI MAL GESTITI: "+this.model.getCriminiMalGestiti()+"\n NUMERO CRIMINI TOTALI: "+this.model.getCrimini());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Integer> anni = this.model.getAnni();
    	this.boxAnno.getItems().addAll(anni);
    }
}
