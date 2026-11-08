package UserInterface.Main;

import TheBusiness.Assignment9DataGenerator;
import TheBusiness.Business.Business;

/**
 * Assignment 9 - Shai Rashada-Parasram.
 * A single initialization path shared by the application and all reports.
 */
class ConfigureABusiness {
    static Business initialize() {
        return Assignment9DataGenerator.createBusiness();
    }
}
