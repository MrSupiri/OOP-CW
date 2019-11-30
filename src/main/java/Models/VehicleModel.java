package Models;

@SuppressWarnings("unused")
public class VehicleModel {
    private String type;
    private String make;
    private String model;

    public VehicleModel(String type, String make, String model) {
        this.type = type;
        this.make = make;
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "{" +
                "type='" + type + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
