package com.example.shared.result;

public class FillResult extends ServiceResult {

    /**
     * Creates a successful or failed FillResult Object
     * Set successful to false when there is an Invalid username or generations parameter, Internal server error
     *
     * @param errorMessage A Description of the success, or the error
     * @param successful   A boolean value whether the FillService was successful or not
     */
    public FillResult(String errorMessage, boolean successful) {
        super(errorMessage, successful);
    }
}
