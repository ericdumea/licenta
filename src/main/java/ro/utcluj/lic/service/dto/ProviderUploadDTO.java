package ro.utcluj.lic.service.dto;

public class ProviderUploadDTO {
    private String fileName;
    private String providerType;
    private Double price;

    public ProviderUploadDTO() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    @Override
    public String toString() {
        return "ProviderUploadDTO{" +
                "fileName='" + fileName + '\'' +
                ", type='" + providerType + '\'' +
                ", price=" + price +
                '}';
    }
}
