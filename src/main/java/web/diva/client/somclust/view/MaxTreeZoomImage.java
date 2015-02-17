/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import web.diva.shared.beans.ClientClusterNode;

/**
 *
 * @author y-mok_000
 */
public class MaxTreeZoomImage extends Composite  implements MouseMoveHandler, MouseOutHandler {

    private final int squareL ;
    private ClientClusterNode mainNode;
    private final int type;
    private final HTML toolTip;
    private boolean selectedNode;

    @Override
    public final void onMouseMove(MouseMoveEvent event) {
        int y = (int) (event.getY());
        int x = ((int) (event.getX()));
         toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">" + " x " + x+ " y " + y + "   getClientX "+event.getClientX()+" relative y "+ event.getScreenX()+"</textarea>");
            toolTip.setVisible(true);
        
//        ClientClusterNode node = null;
//        x = this.getHeight()-x;
//        switch (type) {
//            case 1:
//                
//                node = getTooltipAt(y, x, mainNode);
//                break;
//            case 2:
//                node = getTooltipAt(x, y, mainNode);
//                break;
//
//        }
//         toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">" + " x " + x+ " y " + y + "   relative x "+event.getRelativeX(this.getElement())+" relative y "+ event.getRelativeY(this.getElement())+"</textarea>");
//            toolTip.setVisible(true);
//              if (node != null) {
//            selectedNode = true;
//          this.setStyleName("clusterTreeOverNode");
//            toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">" + "Merged at " + node.getValue() + " - Nodes: " + node.getMembers() +  " x " + x+ " y " + y +"</textarea>");
//            toolTip.setVisible(true);
//        } else {
////            selectedNode = false;
////            toolTip.setVisible(false);
////            this.setStyleName("clusterTreeOver");
//        }
    }

    @Override
    public final void onMouseOut(MouseOutEvent event) {
        toolTip.setVisible(false);
        selectedNode = false;
    }

    public ClientClusterNode getTooltipAt(int xcor, int ycor, ClientClusterNode trunk) {
        ClientClusterNode ret = null;
        if (trunk != null) {
            if (trunk.getX() > xcor - squareL && trunk.getX() < xcor + squareL
                    && trunk.getY() > ycor - squareL && trunk.getY() < ycor + squareL) {
                ret = trunk;
            } else {
                ret = getTooltipAt(xcor, ycor, trunk.getRight());
            }
            if (ret == null) {
                ret = getTooltipAt(xcor, ycor, trunk.getLeft());
            }
        }
        return ret;
    }
    
    Canvas canvas = Canvas.createIfSupported();
    Context2d context = canvas.getContext2d();

    Canvas backCanvas = Canvas.createIfSupported();
    Context2d backContext = backCanvas.getContext2d();

    int width;
    int height;
    Image image;
    ImageElement imageElement; 

    double zoom = 1;
    double totalZoom = 1;
    double offsetX = 0;
    double offsetY = 0;

    boolean mouseDown = false;
    double mouseDownXPos = 0;
    double mouseDownYPos = 0;

    public MaxTreeZoomImage(String url, ClientClusterNode node, int type,HTML toolTip,int squareL) {
        image = new Image(url);//super(url);
        initWidget(canvas);

        //width = Window.getClientWidth() - 50;

        width = image.getWidth() ;
        height = image.getHeight() ;


        //canvas.setWidth(width + "px");
        //canvas.setHeight(height + "px");
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);

        //backCanvas.setWidth(width + "px");
        //backCanvas.setHeight(height + "px");
        backCanvas.setCoordinateSpaceWidth(width);
        backCanvas.setCoordinateSpaceHeight(height);

        canvas.addMouseOutHandler(MaxTreeZoomImage.this);
        canvas.addMouseMoveHandler(MaxTreeZoomImage.this);
//        canvas.addMouseDownHandler(this);
//        canvas.addMouseUpHandler(this);

        this.imageElement = (ImageElement) image.getElement().cast();

        
        mainDraw();
        
        
        this.squareL= squareL;
        this.toolTip = toolTip ;
        toolTip.setVisible(false);
//        this.addMouseMoveHandler(MaxTreeZoomImage.this);
//        this.addMouseOutHandler(MaxTreeZoomImage.this);
        this.mainNode = node;
        this.type = type;
         this.setStyleName("clusterTreeOver");
      
        
         
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public ImageElement getImageElement() {
        return imageElement;
    }
    public void mainDraw() {
        backContext.drawImage(imageElement, 100, 100);

        buffer(backContext, context);
    }

    public void buffer(Context2d back, Context2d front) {
        front.beginPath();
        front.clearRect(0, 0, width, height);
        front.drawImage(back.getCanvas(), 0, 0);
    }
    
    public void updateTooltips( ClientClusterNode node){
    this.mainNode = node;
    }

    public boolean isSelectedNode() {
        return selectedNode;
    }

   

}
