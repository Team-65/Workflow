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

    /**
     * Approves an application. This function is triggered on the "approve"
     * button push. It sets the status of the application to "Approved" and
     * removes it from the inbox.
     *
     * @throws SQLException
     */
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
        sql = "UPDATE REVIEWS SET inbox.remove(apptoassgn.indexOf()) WHERE id = w.id";
        stm.executeUpdate(sql);
        addAllUnassigned();
    }

    /**
     * SetReject is triggered on the "reject" button push.
     * On that button press, it updates the status of the
     * application to "Rejected" and removes it from the inbox.
     *
     * @throws SQLException
     */
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
        sql = "UPDATE REVIEWS SET inbox.remove(apptoassgn.indexOf()) WHERE id = w.id";
        stm.executeUpdate(sql);
        addAllUnassigned();
    }

    /**
     * getUnassigForms retrieves a list of the IDs of the unassigned forms
     * in the database.
     *
     * @return Returns an ArrayList of strings representing the IDs of the unassigned forms.
     * @throws ClassNotFoundException
     * @throws SQLException
     */

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
                unassforms.add(unassAlc.getString((i-1),'id'));//TODO: replace with actual column name
                i++;
            }
        }
        return unassforms;
    }

    /**
     * getSmallWorker returns the government worker with the smallest inbox.
     *
     * @return Returns the government account with the fewest applications in its inbox.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    Worker getSmallWorker() throws ClassNotFoundException, SQLException{//TODO: find out fields + name for govt. worker
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


    /**
     * Assigns an application to a government account's inbox.
     *
     * @param w Government account to assign an application to.
     * @param apptoassgn String representing the ID of the application to be assigned.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    void addToInbox(Worker w, String apptoassgn) throws ClassNotFoundException, SQLException{
        Connection conn=DBConnection.getDBConnection().getConnection();
        Statement stm;
        stm = conn.createStatement();
        //update alcohol status
        String sql = "UPDATE ALCOHOL SET status = 'assigned' WHERE id = apptoassgn";
        stm.executeUpdate(sql);
        //update inbox for worker
        index = w.inbox.size();
        sql = "UPDATE REVIEWS SET inbox.add(index+1, apptoassgn) WHERE id = w.id";
        stm.executeUpdate(sql);
    }

    /**
     * Gets a list of unassigned forms then assigns them to government account inboxes.
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    //adds all the unassigned forms to workers inboxes
    void addAllUnassigned() throws ClassNotFoundException, SQLException{
        ArrayList<String> unassigForms = getUnassigForms();
        for (int i = 0; i <= unassigForms.size(); i++) {
            addToInbox(getSmallWorker(), unassigForms.get(i));
        }
    }

}
