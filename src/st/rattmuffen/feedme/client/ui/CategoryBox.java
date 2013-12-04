package st.rattmuffen.feedme.client.ui;

import com.google.gwt.user.client.ui.ListBox;

public class CategoryBox extends ListBox {
	
	public CategoryBox(boolean b) {
		super(b);
	}

	//Can't believe that there isn't a built in indexOf...
	public int indexOf(String s) {
		for (int i=0; i<this.getItemCount(); i++) {
		    if (this.getItemText(i).equals(s)) {
		        return i;
		    }
		}
		return -1;
	}

}
