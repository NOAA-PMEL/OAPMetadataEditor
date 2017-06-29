package gov.noaa.pmel.sdig.client.widgets;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class NameTextBox extends Composite {
	
	TextBox lastNameTextBox = null;
	TextBox middleInitTextbox = null;
	TextBox firstNameTextBox = null;
	HorizontalPanel panel = null;
	
	
	
	public @UiConstructor NameTextBox() {
		// super();
		
		panel = new HorizontalPanel();
		Label firstNameLabel = new Label("First name");
		Label laststNameLabel = new Label("Last name");
		Label middleInitLabel = new Label("Middle initial");
	
		
		this.lastNameTextBox = new TextBox();
		this.middleInitTextbox = new TextBox();
		this.firstNameTextBox = new TextBox();
		
		panel.add(this.firstNameTextBox);
		panel.add(firstNameLabel);
		
		panel.add(this.middleInitTextbox);
		panel.add(middleInitLabel);
		
		panel.add(this.lastNameTextBox);
		panel.add(laststNameLabel);

		
		initWidget(panel);
//		GWT.log("--------------------------------The title: " + this.textBoxTitle);
//		GWT.log("--------------------------------The default Text: " + this.defaultText);
	}
	
	

	

}
