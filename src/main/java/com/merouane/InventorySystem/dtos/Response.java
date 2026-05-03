package com.merouane.InventorySystem.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.merouane.InventorySystem.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int status;
    private String message;


    private String token;
    private UserRole role;
    private String expirationDate;


    private Integer totalPages;
    private Long totalElements;


    private UserDTO user;
    private List<UserDTO> users;


    private SupplierDTO supplier;
    private List<SupplierDTO> suppliers;


    private ProductDTO product;
    private List<ProductDTO> products;


    private CategoryDTO category;
    private List<CategoryDTO> categories;

    private TransactionDTO transaction;
    private List<TransactionDTO> transactions;


    private  final LocalDateTime timestamp = LocalDateTime.now();


}
