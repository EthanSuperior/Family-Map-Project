package com.example.shared.result;

public class LoadResult extends ServiceResult {
    /**
     * Creates a successful or failed LoadResult Object
     * Set successful to false when there is Invalid request data (missing values, invalid values, etc.), Internal server error
     *
     * @param errorMessage A Description of the success, or the error
     * @param successful   A boolean value of whether the LoadService was successful or not
     */
    public LoadResult(String errorMessage, boolean successful) {
        super(errorMessage, successful);
    }
}
