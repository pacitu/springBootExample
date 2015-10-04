package springBootExample.model.json;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;

public class UserDto implements JsonResponse {

    private long id;

    @Email
    @NotNull
    private String email;

    @Size(min = 2, max = 30)
    @NotNull
    private String firstName;

    @Size(min = 2, max = 30)
    @NotNull
    private String lastName;

    //TODO extend validation and add validator that converts the string to date and back and compares the result.
    @NotNull
    @Pattern(regexp="^[12][0-9]{3}-\\d{2}-\\d{2}$", message="The date must be in yyyy-mm-dd format. Ex. 1985-04-04")
    private String dateOfBirth;

    public UserDto() {

    }

    public UserDto(long id, String email, String firstName, String lastName, String dateOfBirth) {

        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}