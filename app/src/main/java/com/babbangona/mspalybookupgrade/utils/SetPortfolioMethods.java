package com.babbangona.mspalybookupgrade.utils;

import java.util.Set;

public class SetPortfolioMethods {

    /**
     * This method is used to add a staff ID to an MSS portfolio
     * @param portfolio Set of all the MIKs
     * @param staff_id Staff I to be added
     * @return Set of MSS portfolio with added MIK
     */
    public Set<String> addStaff(Set<String> portfolio, String staff_id) {
        portfolio.add(staff_id);
        return portfolio;
    }

    /**
     * This method is used to delete a staff ID from an MSS portfolio
     * @param portfolio Set of all the MIKs
     * @param staff_id Staff ID to be deleted
     * @return Set of MSS portfolio with removed MIK
     */
    public Set<String> removeStaff(Set<String> portfolio, String staff_id) {
        portfolio.remove(staff_id);
        return portfolio;
    }
}
