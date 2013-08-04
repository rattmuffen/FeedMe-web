package st.rattmuffen.feedme.client.ui;

import com.google.gwt.user.cellview.client.CellTable;

public interface TableResources extends CellTable.Resources {
@Source({CellTable.Style.DEFAULT_CSS, "FeedTable.css"})
TableStyle cellTableStyle();
 
interface TableStyle extends CellTable.Style {}
}
