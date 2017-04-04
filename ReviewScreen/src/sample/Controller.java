package sample;


import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sun.java2d.pipe.AlphaPaintPipe;

import java.sql.*;
import java.util.ArrayList;
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
        ResultSet rset = stmt.executeQuery("SELECT AID, MIN(CNT(FID))");//select worker with smallest inbox
        String status = rset.getString("STATUS");
        status = "Approved";
        int uRows = stmt.executeUpdate("INSERT INTO")
        //What I want to do: update the active application to approve
        //Find correct app in DB, Update Status Field, Update Comments
        //remove from worker inbox
    }

    @FXML
    void setReject() throws SQLException{
        //use SQL to set status to rejected; append comments
    }



    //creates a list of unassigned applications
    private static ArrayList<String> getUnassigForms() throws ClassNotFoundException, SQLException {
        Connection conn=DBConnection.getDBConnection().getConnection();
        Statement stm;
        stm = conn.createStatement();
        String sql = "SELECT * FROM ALCOHOL WHERE ALCOHOL.STATUS = 'Unassigned'"; // Use Select _ from _ Where _ format and set this statement = sql
        ArrayList<String> unassforms = new ArrayList<>();
        ResultSet unassAlc = stm.executeQuery(sql);
        ResultSetMetaData rsmd = unassAlc.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while(unassAlc.next()){
            int i = 1;
            while(i <= columnCount){
                unassforms.add(unassAlc.getString('id'));//TODO: replace with actual column name
            }
        }
        return unassforms;
    }

    //finds the worker with the least amount of applications in their inbox and
    //returns that worker
    Worker getsmallWorker(ArrayList<CForms> unassigForms) throws ClassNotFoundException, SQLException{
        Connection conn=DBConnection.getDBConnection().getConnection();
        Statement stm;
        stm = conn.createStatement();
        String sql = "";
        for (i = 0; i <= unassigForms().size(); i++){
            Statement stm = null;
            ResultSet smallWorker = stm.executeQuery("SELECT AID.MIN(CNT(FID)) FROM REVIEWS");
            addToInbox(smallWorker, unassigForms[i]);
        }
    }


    //adds an application to a worker
    //alters the status of the application to assigned
    //pushes the changes to the worker and the application
    void addToInbox(Worker w, Application apptoassgn){
        //add the unassigned app to worker
        w.addToInbox(apptoassgn);
        //alter status of application to assigned and push changes to application
        SELECT FROM ALCOHOL WHERE ALCOHOL.FID = apptoassgn.fid;
        apptoassgn.status = "assigned";
        UPDATE SELECTED App;
        SELECT FROM WORKERS WHERE WORKER.AID = w.aid;
        UPDATE SELECTED WORKER;
        //push changes to worker
    }

    //adds all the unassigned forms to workers inboxes
    void addAllUnassigned(){
        while (getUnassigForms() != null) {
            Worker smallestInbox = getsmallWorker(getUnassigForms());
            addToInbox(smallestWorker);
        }
    }

}
