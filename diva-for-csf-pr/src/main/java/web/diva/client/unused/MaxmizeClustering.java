/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.unused;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.KnobType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.drawing.DrawImage;
import com.smartgwt.client.widgets.drawing.DrawPane;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;

/**
 *
 * @author y-mok_000
 */
public class MaxmizeClustering extends DrawPane{
    
    private final DrawImage rotatedSideTreeImg = new DrawImage();
    private int sideTreeLeft=0;
    public MaxmizeClustering(final int sideTreewidth, final int sideTreeHeight, final String sideTreeSrc, boolean clustColumn) {
        this.setCursor(Cursor.HAND);
        if (clustColumn) {
            sideTreeLeft = 70;
        }
//        this.setWidth(sideTreeHeight);
//        this.setHeight(sideTreeHeight);
        this.setMargin(0);  
//         
        setOverflow(Overflow.AUTO);  
        this.setAlwaysShowScrollbars(false);
        this.setOverflow(Overflow.AUTO);
        this.setAutoDraw(false);
       
        

        this.addDrawHandler(new DrawHandler() {
            @Override
            public void onDraw(DrawEvent event) {
                 rotatedSideTreeImg.setSrc(sideTreeSrc);

                rotatedSideTreeImg.setLineWidth(0);
                rotatedSideTreeImg.setTop(0);
                rotatedSideTreeImg.setLeft(0);
//                rotatedSideTreeImg.setWidth(sideTreewidth);
//                rotatedSideTreeImg.setHeight(sideTreeHeight);
//                rotatedSideTreeImg.setKnobs(KnobType.RESIZE);
//                rotatedSideTreeImg.hideKnobs(KnobType.RESIZE);
//                rotatedSideTreeImg.setKeepInParentRect(true);
                rotatedSideTreeImg.setDrawPane(MaxmizeClustering.this);
                rotatedSideTreeImg.setWidth(sideTreewidth);
                rotatedSideTreeImg.setHeight(sideTreeHeight);

               
//               rotatedSideTreeImg.rotateTo(90);      
            draw();
//               rotatedSideTreeImg.moveTo(0,0);
                

//                final DrawImage selectionRectangel = new DrawImage();
//                selectionRectangel.rotateTo(90.0);
//                selectionRectangel.setLineWidth(1);
//                selectionRectangel.setWidth(0);
//                selectionRectangel.setHeight(0);
//                selectionRectangel.setKeepInParentRect(true);
//                selectionRectangel.setKnobs(KnobType.RESIZE);
//                selectionRectangel.setUseMatrixFilter(true);
//                selectionRectangel.showKnobs(KnobType.RESIZE);
//                selectionRectangel.setDrawPane(MaxmizeClustering.this);
//                selectionRectangel.erase();

                
                addMouseMoveHandler(new com.smartgwt.client.widgets.events.MouseMoveHandler() {

                    @Override
                    public void onMouseMove(com.smartgwt.client.widgets.events.MouseMoveEvent event) {
                        if (event.isLeftButtonDown()) {
                            setHeight(sideTreewidth);
                            setWidth(sideTreeHeight);
//                            selectionRectangel.setRect(startUX, startUY, (event.getX() - drawPane.getAbsoluteLeft() - startUX), (event.getY() - drawPane.getAbsoluteTop() - startUY));
//                            selectionRectangel.showKnobs(KnobType.RESIZE);

                        } else {
////                            selectionRectangel.hideKnobs(KnobType.RESIZE);
                            try {  
                                rotatedSideTreeImg.rotateTo(90);
                                rotatedSideTreeImg.moveTo(0, 0);                       
                                rotatedSideTreeImg.draw();

//                                int pointY = event.getY() - drawPane.getAbsoluteTop();
//                                int pointX = event.getX() - drawPane.getAbsoluteLeft();
//                                pointX = pointX - 1 - tooltipInformationData.getPlotLeft() + tooltipInformationData.getyAxisFactor();
//                                pointY -= tooltipInformationData.getPlotTop() - 1;
//                                String tooltipStr = "";
//                                double modPointX = (pointX * tooltipInformationData.getxUnitPix()) + tooltipInformationData.getMinX();//xstart units from min         
//                                double modPointY = tooltipInformationData.getMaxY() - (pointY * tooltipInformationData.getyUnitPix());
//                                double modDotXSize = 2 * tooltipInformationData.getxUnitPix();
//                                double modDotYSize = 2 * tooltipInformationData.getyUnitPix();
//                                for (int x = 0; x < tooltipInformationData.getPoints()[0].length; x++) {
//                                    double tempPointX = tooltipInformationData.getPoints()[0][x];
//                                    double tempPointY = tooltipInformationData.getPoints()[1][x];
//                                    if ((tempPointX == modPointX || (tempPointX <= (modPointX + modDotXSize) && tempPointX >= modPointX - modDotXSize)) && (tempPointY == modPointY || (tempPointY <= (modPointY + modDotYSize) && tempPointY >= (modPointY - modDotYSize)))) {
//                                        tooltipStr = tooltipStr + tooltipInformationData.getRowIds()[x];
//                                        tooltipStr += ",";
//                                    }
//
//                                }
//                                if (!tooltipStr.equals("")) {
//                                    tooltipStr = tooltipStr.substring(0, (tooltipStr.length() - 2));
//                                    updateToolTip(tooltipStr);
//                                } else {
//                                    tooltipLabel.setText("");
//                                }
                            } catch (Exception e) {
                                Window.alert(e.getMessage());
                            }
//                            selectionRectangel.erase();
                        }
                    }

                });

                addMouseDownHandler(new com.smartgwt.client.widgets.events.MouseDownHandler() {

                    @Override
                    public void onMouseDown(com.smartgwt.client.widgets.events.MouseDownEvent event) {
//                         SC.say("down");

//                        startUX = event.getX() - drawPane.getAbsoluteLeft();
//                        startUY = event.getY() - drawPane.getAbsoluteTop();
//
//                        setCursor(Cursor.CROSSHAIR);
//                        rotatedSideTreeImg.erase();
//                        selectionRectangel.draw();
//                        drawPane.draw();

                    }
                });
                addMouseUpHandler(new com.smartgwt.client.widgets.events.MouseUpHandler() {

                    @Override
                    public void onMouseUp(com.smartgwt.client.widgets.events.MouseUpEvent event) {

//                        endUX = event.getX() - drawPane.getAbsoluteLeft();
//                        endUY = event.getY() - drawPane.getAbsoluteTop();
//                        selectionRectangel.hideKnobs(KnobType.RESIZE);
//
//                        setCursor(Cursor.ARROW);
//
//                        if (zoom) {
//                            zoomIn(startUX, startUY, endUX, endUY);
//                        } else {
//                            getSelection(startUX, startUY, endUX, endUY);
//                        }
//                        selectionRectangel.erase();
//                        drawPane.draw();
                    }
                });

            }
        });
    
    }
    
}
