package sample;


import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sun.java2d.pipe.AlphaPaintPipe;

import java.sql.*;
import java.util.Scanner;

public class Controller{
    @FXML
    Button approve;
    @FXML
    Button reject;
    @FXML
    Button goBack;
    @FXML
    TextField repID;
    @FXML
    TextField registryNo;
    @FXML
    TextField prodSource;
    @FXML
    TextField prodType;
    @FXML
    TextField address;
    @FXML
    TextField phoneNo;
    @FXML
    TextField email;
    @FXML
    TextField dateApp;
    @FXML
    TextField nameApp;
    @FXML
    TextArea commentsField;
    //for when switching to this scene from inbox
    /*@FXML
    void setViewedApplication(){
        repID.setText("Sample ID");
        registryNo.setText("0000");
        prodSource.setText("TEST");
        prodType.setText("TEST");
        address.setText("00 Test Address");
        phoneNo.setText("Test");
        email.setText("Sample@test");
        dateApp.setText("00/00/TEST");
        nameApp.setText("TEST");
    }*/

    @FXML
    void setGoBack(){
        //primaryStage.setScene(scene1); //scene 1 here is the inbox
        //should switch to inbox controller from here
    }

    @FXML
    void setApprove() throws SQLException{
        Statement stmt = null;
        ResultSet rset = stmt.executeQuery("SELECT AID, MIN(CNT(FID))");
        String status = rset.getString("STATUS");
        status = "Approved";
        int uRows = stmt.executeUpdate("INSERT INTO")
        //What I want to do: update the active application to approve
        //Find correct app in DB, Update Status Field, Update Comments
    }

    @FXML
    void setReject() throws SQLException{
        //use SQL to set status to rejected; append comments
    }

    void assignApp(){
        Statement stmt = null;
        ResultSet unassApp = SELECT FIRST FROM ALCOHOL WHERE ALCOHOL.STATUS = '+"unassigned"+'
        ResultSet smallWorker = stmt.executeQuery("SELECT AID.MIN(CNT(FID)) FROM REVIEWS GROUP BY AID");
        smallWorker.addToInbox(unassApp);
    }

    void addToInbox(Worker w, Application apptoassgn){
        //add the unassigned app to worker
        w.Inbox.add(apptoassgn);
        //alter status of application to assigned
        SELECT FROM ALCOHOL WHERE ALCOHOL.FID = apptoassgn.fid;
        apptoassgn.status = "assigned";
        UPDATE SELECTED App;
        SELECT FROM WORKERS WHERE WORKER.AID = w.aid;
        UPDATE SELECTED WORKER;
        //push changes to worker
    }
}
