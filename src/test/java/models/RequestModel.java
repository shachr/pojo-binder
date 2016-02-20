package models;
import com.shachr.common.model.binding.annotations.Body;
import com.shachr.common.model.binding.annotations.Header;
import com.shachr.common.model.binding.annotations.Query;

import javax.validation.*;
import javax.validation.constraints.*;

/**
 * Created by shachar on 15/02/2016.
 */
public class RequestModel {

    @Body()
    @Query()
    @Valid
    public User user;

    @Header()
    @NotNull
    public String token;
}


