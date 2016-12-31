package gui;

import javafx.stage.Stage;

/**
 * @author Sanchit Sharma
 * To be honest, a lot more should be under the interface, such as refreshList(), etc.
 */
public interface Controller {
	
	void initalize();
	void populateList();
	public void start(Stage Stage) throws Exception;
	public void setStage(Stage Stage);
	
	

}
