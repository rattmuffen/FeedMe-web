package st.rattmuffen.feedme.client.ui;

import st.rattmuffen.feedme.client.WebStorage;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddFeedPopup extends DialogBox implements ChangeHandler {

	MainPanel controller;

	Button addButton,closeButton,addCategoryButton;
	public TextBox addressField,newCategoryField;
	public ListBox categoryBox;
	
	HorizontalPanel addCatPanel;

	public AddFeedPopup(MainPanel c) {
		super(true);

		controller = c;
	}

	public void createAndShowGUI() {
		this.addStyleName("popup");
		this.setText("Add a feed");
		this.setAnimationEnabled(true);
		this.setGlassEnabled(true);

		VerticalPanel contents = new VerticalPanel();

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		addressField = new TextBox();
		addressField.addKeyUpHandler(controller.handler);
		
		HTML h = new HTML("Address:");
		hp.add(h);
		hp.add(addressField);
		contents.add(hp);

		hp = new HorizontalPanel();
		hp.setSpacing(5);

		categoryBox = new ListBox(false);
		categoryBox.addChangeHandler(this);
		categoryBox.addItem("Default");
		categoryBox.addItem("Technology");
		categoryBox.addItem("News");
		categoryBox.addItem("Misc");
		
		categoryBox.setSelectedIndex(0);
		
		for (String category : WebStorage.getAllCategoriesFromStorage()) {
			categoryBox.addItem(category);
		}
		
		categoryBox.addItem("Add new category...");

		h = new HTML("Category:");
		hp.add(h);
		hp.add(categoryBox);
		contents.add(hp);
		
		
		addCatPanel = new HorizontalPanel();
		addCatPanel.setSpacing(5);
		
		
		HTML h2 = new HTML("New category:");
		addCatPanel.add(h2);
		
		newCategoryField = new TextBox(); 
		addCategoryButton = new Button("Add");
		addCategoryButton.addClickHandler(controller.handler);
		
		addCatPanel.add(newCategoryField);
		addCatPanel.add(addCategoryButton);
		
		
		contents.add(addCatPanel);


		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setSpacing(5);
		hp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		addButton = new Button("Add feed");
		addButton.addClickHandler(controller.handler);
		closeButton = new Button("Close");
		closeButton.addClickHandler(controller.handler);
		
		hp2.add(addButton);
		hp2.add(closeButton);
		contents.add(hp2);

		setWidget(contents);

		this.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = (Window.getClientWidth() - offsetWidth) / 2;
				int top = (Window.getClientHeight() - offsetHeight) / 2;
				setPopupPosition(left, top);
			}
		});

		this.show();
		addressField.setFocus(true);
		categoryBox.setSize(addressField.getOffsetWidth() + "px", addressField.getOffsetHeight() + "px");
		addCatPanel.setVisible(false);
	}

	@Override
	public void onChange(ChangeEvent event) {
	
		if (event.getSource() == categoryBox) {
			int i = categoryBox.getSelectedIndex();
			if (i + 1 == categoryBox.getItemCount()) {
				addCatPanel.setVisible(true);
			} else {
				addCatPanel.setVisible(false);
			}
		}
	}

}
