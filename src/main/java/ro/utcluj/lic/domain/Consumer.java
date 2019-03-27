package ro.utcluj.lic.domain;

import java.math.BigDecimal;
import java.util.List;

public class Consumer {
    private Integer id;

    private List<BigDecimal> power;

    public Consumer() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<BigDecimal> getPower() {
        return power;
    }

    public void setPower(List<BigDecimal> power) {
        this.power = power;
    }
}
