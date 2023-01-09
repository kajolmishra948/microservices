package com.microproject.user.service.payload;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder // to build the class object in one line in any other class
public class ApiResponse {
    //jo bhi msg jayega vo yaha se jayega jab id nhi milega
    private String message;
    private boolean success;
    private HttpStatus status;
}
