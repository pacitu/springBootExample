package springBootExample.model.json;

import java.util.ArrayList;

public class ResponseManager {

    ArrayList<String> errors;

    public ResponseManager() {
        this.errors = new ArrayList<>();
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public JsonResponse getSuccessResponse() {
        return new SuccessDto();
    }

    public JsonResponse getErrorsResponse() {
        return new ErrorsDto(this.errors);
    }

    public JsonResponse getResponse() {
        if (this.errors.size() != 0) {
            return this.getErrorsResponse();
        }
        return this.getSuccessResponse();
    }

    public boolean hasErrors() {
        return this.errors.size() != 0;
    }
}
