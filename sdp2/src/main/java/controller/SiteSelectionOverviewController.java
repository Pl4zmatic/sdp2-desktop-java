package controller;

import domein.Session;
import domein.site.Site;

public class SiteSelectionOverviewController extends SiteSelectionController {

    @Override
    protected void selectPlant(Site plant) {
        Session.setCurrentSite(plant);
        SceneSwitcher.switchScene("/view/OverviewPlants.fxml");
    }
}