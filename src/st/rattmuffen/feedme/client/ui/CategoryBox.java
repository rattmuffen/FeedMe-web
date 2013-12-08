package st.rattmuffen.feedme.client.ui;

import st.rattmuffen.feedme.client.WebStorage;

import com.google.gwt.user.client.ui.ListBox;

public class CategoryBox extends ListBox {
	
	public CategoryBox(boolean b) {
		super(b);
		
		for (String defaultCategory : Constants.DEFAULT_CATEGORIES) {
			this.addItem(defaultCategory);
		}
		for (String category : WebStorage.getAllCategoriesFromStorage()) {
			this.addItem(category);
		}
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
