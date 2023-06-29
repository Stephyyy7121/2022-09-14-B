/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
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

    @FXML // fx:id="btnComponente"
    private Button btnComponente; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSet"
    private Button btnSet; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="txtDurata"
    private TextField txtDurata; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader
    
    private boolean grafoCreato = false;

    @FXML
    void doComponente(ActionEvent event) {
    	
    	txtResult.clear();
    	if (!grafoCreato) {
    		txtResult.setText("Il grafo non e' stato creato");
    	}
    	
    	Album album = cmbA1.getValue();
    	
    	if (album == null) {
    		txtResult.appendText("Inserire un album");
    	}
    	
    	List<Album> connessa = this.model.getComponente(album);
    	txtResult.appendText("Componente connessa - " + album.getTitle() + "\nDimensione componente = " + connessa.size()+ "\n#Album componente = " + this.model.numeroBrani(connessa));
    	if (connessa.contains(album)) {
    		txtResult.appendText("\nYes");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	String input = txtDurata.getText();
    	
    	if (input == "") {
    		txtResult.setText("Non e' stato inserito un valore");
    	}
    	
    	Double durata = 0.0;
    	try {
    		durata = Double.parseDouble(input);
    	}catch (NumberFormatException e) {
    		txtResult.setText("Non e' stato inserito un valore accettabile");
    		return ;
    	}
    	
    	this.model.creaGrafo(durata);
    	this.grafoCreato = true;
    	
    	txtResult.appendText("Grafo creato !\n#Vertici: " + this.model.getNumVertici() + "\n#Archi: " + this.model.getNumArchi());
    	
    	this.cmbA1.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doEstraiSet(ActionEvent event) {
    	
    	txtResult.clear();
    	if (!grafoCreato) {
    		txtResult.setText("Il grafo non e' stato creato");
    	}
    	
    	String input = this.txtX.getText();
    	Album album = cmbA1.getValue();
    	if (album == null) {
    		txtResult.appendText("Inserire un album");
    	}
    	
    	double dTOT = 0.0;
    	try {
    		dTOT = Double.parseDouble(input);
    	}catch (NumberFormatException e) {
    		return;
    	}
    	List<Album> finale = this.model.getAlbumFinale(dTOT, album);
    	for (Album a : finale) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnComponente != null : "fx:id=\"btnComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSet != null : "fx:id=\"btnSet\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDurata != null : "fx:id=\"txtDurata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
