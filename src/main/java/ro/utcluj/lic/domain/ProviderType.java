package ro.utcluj.lic.domain;

public class ProviderType {

    private String type;

    private int percentage;

    public ProviderType() {
    }

    public ProviderType(String type, int percentage) {
        this.type = type;
        this.percentage = percentage;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
