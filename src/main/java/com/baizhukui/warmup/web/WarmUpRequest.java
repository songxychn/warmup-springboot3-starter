package com.baizhukui.warmup.web;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author songxychina@gmail.com
 * @date 2024/09/08
 */
@Data
public class WarmUpRequest {
    @AssertTrue
    private boolean validTrue;
    @AssertFalse
    private boolean validFalse;

    @NotBlank
    @Pattern(regexp = "warm up")
    private String validString;

    @Min(10)
    @Max(20)
    private int validNumber;

    @NotNull
    private BigDecimal validBigDecimal;
}
