package com.example.shared.result;

public class ClearResult extends ServiceResult {

    /**
     * Creates a successful or failed ClearResult Object
     * Set successful to false when there is an Internal server error
     *
     * @param errorMessage A Description of the success, or the error
     * @param successful   A boolean value of whether the clearService was successful or not
     */
    public ClearResult(String errorMessage, boolean successful) {
        super(errorMessage, successful);
    }
}
