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
public class FullDataObject implements IsSerializable{
    private ProfilePlotResults profilePlotResult;
    private PCAImageResult pcaImgRsult;
    private RankResult rankResult;
    private SomClusteringResult somClustResult;

    public ProfilePlotResults getProfilePlotResult() {
        return profilePlotResult;
    }

    public void setProfilePlotResult(ProfilePlotResults profilePlotResult) {
        this.profilePlotResult = profilePlotResult;
    }

    public PCAImageResult getPcaImgRsult() {
        return pcaImgRsult;
    }

    public void setPcaImgRsult(PCAImageResult pcaImgRsult) {
        this.pcaImgRsult = pcaImgRsult;
    }

    public RankResult getRankResult() {
        return rankResult;
    }

    public void setRankResult(RankResult rankResult) {
        this.rankResult = rankResult;
    }

    public SomClusteringResult getSomClustResult() {
        return somClustResult;
    }

    public void setSomClustResult(SomClusteringResult somClustResult) {
        this.somClustResult = somClustResult;
    }
    
}
