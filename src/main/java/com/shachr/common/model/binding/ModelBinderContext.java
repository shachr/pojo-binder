package com.shachr.common.model.binding;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shachar on 14/02/2016.
 * TODO:
 * 1. extend jackson to accept new annotation (@Header, @Body, @QueryString, @PathParam)
 *    http://wiki.fasterxml.com/JacksonFeatureModules
 *
 * 2. Support jackson injection :
 *    http://wiki.fasterxml.com/JacksonFeatureValueInjection
 *
 *  helpful article:
 *  http://www.baeldung.com/jackson-annotations
 */
public class ModelBinderContext extends HashMap<String,Object>{

}
