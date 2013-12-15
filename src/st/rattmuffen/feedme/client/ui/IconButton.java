package st.rattmuffen.feedme.client.ui;

import com.google.gwt.user.client.ui.Button;

public class IconButton extends Button {
	
	String icon,text;
	
	public IconButton(String text, String icon) {
		this("<i class=\"fa " + icon + "\"></i> " + text);
		
		this.icon = icon;
		this.text = text;
	}
	
	public IconButton(String text) {
		super(text);
		this.addStyleName("iconButton");
	}
	
	public void setIcon(String icon) {
		super.setHTML("<i class=\"fa " + icon + "\"></i> " + text);
		this.icon = icon;
	}
	
	public void setText(String text) {
		super.setHTML("<i class=\"fa " + icon + "\"></i> " + text);
		this.text = text;
	}
	
}
