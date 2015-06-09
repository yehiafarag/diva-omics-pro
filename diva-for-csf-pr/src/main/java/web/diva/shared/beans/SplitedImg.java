/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author y-mok_000
 */
public class SplitedImg implements IsSerializable {

    private String img1Url;
    private String img2Url;
    private String img3Url;
    private String img4Url;
    private int heightFirst;

    public String getImg3Url() {
        return img3Url;
    }

    public void setImg3Url(String img3Url) {
        this.img3Url = img3Url;
    }

    public String getImg4Url() {
        return img4Url;
    }

    public void setImg4Url(String img4Url) {
        this.img4Url = img4Url;
    }
    private int heightLast;

    public String getImg1Url() {
        return img1Url;
    }

    public void setImg1Url(String img1Url) {
        this.img1Url = img1Url;
    }

    public String getImg2Url() {
        return img2Url;
    }

    public void setImg2Url(String img2Url) {
        this.img2Url = img2Url;
    }

    public int getHeightFirst() {
        return heightFirst;
    }

    public void setHeightFirst(int heightFirst) {
        this.heightFirst = heightFirst;
    }

    public int getHeightLast() {
        return heightLast;
    }

    public void setHeightLast(int heightLast) {
        this.heightLast = heightLast;
    }

    
}
