package models;
import com.shachr.common.model.binding.annotations.Body;
import com.shachr.common.model.binding.annotations.Header;
import com.shachr.common.model.binding.annotations.QueryString;

import javax.validation.*;
import javax.validation.constraints.*;

/**
 * Created by shachar on 15/02/2016.
 */
public class RequestModel {

    @Body()
    @QueryString()
    @Valid
    public User user;

    @Header()
    @NotNull
    public String token;
}


