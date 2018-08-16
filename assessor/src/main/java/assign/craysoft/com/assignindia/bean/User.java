package assign.craysoft.com.assignindia.bean;

import org.json.JSONObject;

/**
 * Created by ajay on 21/01/17.
 */

public class User extends Parent<User> {
    private String firstName = "Guest";
    private String lastName = "";
    private String email = "";
    private String id = "";
    private String phoneNumber = "";
    private String password = "";
    private String imageUrl = "";
    private String deviceType;
    private String type;


    public User(JSONObject object) {
        super(object);
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
