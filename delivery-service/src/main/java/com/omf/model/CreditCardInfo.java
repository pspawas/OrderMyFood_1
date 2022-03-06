package com.omf.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Document
public class CreditCardInfo {
    private String customerName;
    private String customerCellNo;
    private int expiredMonth;
    private int expiredYear;
    private int securityCode;
}
