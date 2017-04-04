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
        Connection conn=DBConnection.getDBConnection().getConnection();
        Statement stm;
        stm = conn.createStatement();
        //get comments
        String comments = commentsField.getText();
        //update alcohol status
        String sql = "UPDATE ALCOHOL SET status = 'approved', comments = 'comments'  WHERE id = 'apptoassgn'";
        stm.executeUpdate(sql);
        //update inbox for worker
        sql = "UPDATE REVIEWS SET inbox.remove(apptoassgn) WHERE id = w.id";
        stm.executeUpdate(sql);
    }

    @FXML
    void setReject() throws SQLException{
        Connection conn=DBConnection.getDBConnection().getConnection();
        Statement stm;
        stm = conn.createStatement();
        //get comments
        String comments = commentsField.getText();
        //update alcohol status
        String sql = "UPDATE ALCOHOL SET status = 'rejected', comments = 'comments' WHERE id = 'apptoassgn'";
        stm.executeUpdate(sql);
        //update inbox for worker
        sql = "UPDATE REVIEWS SET inbox.remove(apptoassgn) WHERE id = w.id";
        stm.executeUpdate(sql);
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

    //goes through a list of unassigned applications
    //finds worker with the least amount of applications
    Worker getsmallWorker() throws ClassNotFoundException, SQLException{//TODO: find out fields + name for govt. worker
        Connection conn=DBConnection.getDBConnection().getConnection();
        Statement stm;
        stm = conn.createStatement();
        String sql = "SELECT AID.MIN(CNT(FID)) FROM REVIEWS";
        ResultSet smallWorker = stm.executeQuery(sql);
        Worker worker = Worker();
        worker.aid = smallWorker.getString('id');//TODO: replace w/ actual syntax for this
        worker.inbox = smallWorker.getArray('inbox');//TODO: Find actual fields
        return worker;
    }


    //adds an application to a worker
    //alters the status of the application to assigned
    //pushes the changes to the worker and the application
    void addToInbox(Worker w, String apptoassgn) throws ClassNotFoundException, SQLException{
        Connection conn=DBConnection.getDBConnection().getConnection();
        Statement stm;
        stm = conn.createStatement();
        //update alcohol status
        String sql = "UPDATE ALCOHOL SET status = 'assigned' WHERE id = 'apptoassgn'";
        stm.executeUpdate(sql);
        //update inbox for worker
        sql = "UPDATE REVIEWS SET inbox.add(apptoassgn) WHERE id = w.id";
        stm.executeUpdate(sql);
    }

    //adds all the unassigned forms to workers inboxes
    void addAllUnassigned(){
        ArrayList<String> unassigForms = getUnassigForms();
        while (unassigForms != null) {
            int i = 0;
            Worker smallestInbox = getsmallWorker(getUnassigForms());
            addToInbox(smallestWorker, unassigForms.get(i));
            i++;
        }
    }

}
