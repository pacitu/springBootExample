package springBootExample.model.json;

import java.util.ArrayList;

/**
 *
 */
public class ErrorsDto implements JsonResponse {
    private ArrayList<String> errors;

    public ErrorsDto(ArrayList<String> errors) {
        this.errors = errors;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }
}
