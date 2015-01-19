/*
 * this class represents the som clustering button layout
 */
package web.diva.client.unused;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 *
 * @author Yehia Farag
 */
public class SomClusterBtn extends Label {

    private final SomClustPanel somClustPanel;

    

    @SuppressWarnings("LeakingThisInConstructor")
    public SomClusterBtn() {
        super("Hierarchical Clustering");
        somClustPanel = new SomClustPanel();
        this.addStyleName("clickable");
        this.setHeight("20px");
        this.setWidth("Hierarchical Clustering".length() * 6 + "px");
        this.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                somClustPanel.show();
            }
        });

    }
    public void hidePanel(){
    somClustPanel.hide();
    }
      public void showPanel(){
    somClustPanel.show();
    }
      
      public void setClickListener(com.smartgwt.client.widgets.events.ClickHandler handler){
      somClustPanel.getOkBtn().addClickHandler(handler);
      }
      
      public int getLinkage(){
          return somClustPanel.getLinkageValue();
      }
      public int getDistanceMeasure(){
          return somClustPanel.getDistanceMeasureValue();
      }

}
