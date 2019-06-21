package ro.utcluj.lic.service.dto;

public class ConsumerUploadDTO {

    private String fileName;
    private String name;
    private Double price;

    public ConsumerUploadDTO() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ConsumerUploadDTO{" +
                "fileName='" + fileName + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
