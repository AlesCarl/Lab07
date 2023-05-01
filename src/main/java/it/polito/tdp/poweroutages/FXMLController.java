/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.ComparatoreByData;
import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOut;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="cmbNerc"
    private ComboBox<Nerc> cmbNerc; // Value injected by FXMLLoader

    @FXML // fx:id="txtYears"
    private TextField txtYears; // Value injected by FXMLLoader

    @FXML // fx:id="txtHours"
    private TextField txtHours; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private Model model;
    
    
    
          
    @FXML
    void doRun(ActionEvent event) {
    	txtResult.clear();
    	
    	try {
			Nerc selectedNerc = cmbNerc.getSelectionModel().getSelectedItem();
			
			if (selectedNerc == null) {
				txtResult.setText("Seleziona un NERC ");
				return;
			}

			int maxY = Integer.parseInt(txtYears.getText());
			//controlli validità omessi

			int maxH = Integer.parseInt(txtHours.getText());
			//controlli validità omessi

		
					
			List<PowerOut> worstCase = model.eventiSelezionati(maxY, maxH, selectedNerc);
			worstCase.sort(new ComparatoreByData());
			txtResult.clear();
			
			txtResult.setText("Totale persone coinvolte: "+model.getMaxPersone() +"\n");

			for (PowerOut ee : worstCase) {
				
				txtResult.appendText(ee.toString()); 
				
			}

		} catch (NumberFormatException e) {
			txtResult.setText("Insert a valid number of years and of hours");
		}

    	
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        // Utilizzare questo font per incolonnare correttamente i dati;
        txtResult.setStyle("-fx-font-family: monospace");
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.setComboBox();
    }
    
    List<Nerc> temp = new LinkedList<Nerc>();
    
    public void setComboBox() {
    	
    	temp= model.getNercList();
    	this.cmbNerc.getItems().addAll(temp);  
    	
    }
}
