/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simply.build.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 *
 * @author siddhartha
 */
public class FXMLDocumentController implements Initializable {
    private final String USER_AGENT = "Mozilla/5.0";
@FXML
ChoiceBox chMethods;

@FXML
TextField tfPayload,tfUrl;

@FXML
TextArea teResponse;
@FXML
ProgressBar pbLoading;

@FXML
Text txtResCode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadMethodsForChoiceList();
        onActionCreate();
    }    

    private void loadMethodsForChoiceList() {
        chMethods.setValue("GET");
        chMethods.setItems(FXCollections.observableArrayList(
    "GET", "POST"));
    }

    private void onActionCreate() {
        chMethods.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if(newValue.intValue() ==1){
                tfPayload.setDisable(false);
                //System.out.println("Hi");
            }else{
                // tfPayload.setEditable(true);
                tfPayload.setDisable(true);
            }
        });
    }
    
    @FXML
    private void btnSend(Event e) throws MalformedURLException, IOException {
        
        if(!tfUrl.getText().isEmpty()){
         pbLoading.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            callGetRequest();
        }else{
           Alert alert = new Alert(AlertType.INFORMATION, "Url Can't be blank!", ButtonType.CANCEL);
        alert.showAndWait();
        }
          
           
           
    }

    private void callGetRequest(){
         
        try {
            
            String url = tfUrl.getText();
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            // optional default is GET
            con.setRequestMethod((String) chMethods.getValue());
            
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters;
            pbLoading.setProgress(0);
            if(!tfPayload.isDisable()){
                urlParameters = tfPayload.getText();
            }else{
                urlParameters = "";
            }
            
            
            // Send post request
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(urlParameters);
                wr.flush();
            }
            int responseCode = con.getResponseCode();
            System.out.println("\nSending "+chMethods.getValue()+" request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            
            StringBuffer response;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            txtResCode.setText("Response Code : " + responseCode);
            teResponse.setStyle("-fx-text-fill: Black;");
            teResponse.setText(response.toString());
            // 
        }   catch (MalformedURLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            teResponse.setText(ex.getMessage());
            teResponse.setStyle("-fx-text-fill: Red;");
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            teResponse.setText(ex.getMessage());
            teResponse.setStyle("-fx-text-fill: Red;");
        }
    }
    
}
