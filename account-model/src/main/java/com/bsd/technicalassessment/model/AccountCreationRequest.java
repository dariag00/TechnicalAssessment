package com.bsd.technicalassessment.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class AccountCreationRequest {

    @NotEmpty
    private String iban;

    private BigDecimal initialBalance;

}
