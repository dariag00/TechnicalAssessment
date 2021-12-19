package com.bsd.technicalassessment.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotEmpty
    private String fromIban;

    @NotEmpty
    private String toIban;

    @NotNull
    private BigDecimal amount;

}
