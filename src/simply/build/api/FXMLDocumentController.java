/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simply.build.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
                tfPayload.setDisable(true);
                //System.out.println("Hi");
            }else{
                // tfPayload.setEditable(true);
                tfPayload.setDisable(false);
            }
        });
    }
    
    @FXML
    private void btnSend(Event e) throws MalformedURLException, IOException {
 
            callGetRequest();
            
           
    }

    private void callGetRequest() throws MalformedURLException, IOException {
       String url = tfUrl.getText();
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            // optional default is GET
            con.setRequestMethod((String) chMethods.getValue());
            
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
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
            
         teResponse.setText(response.toString());
    }
    
}
