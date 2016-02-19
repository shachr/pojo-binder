package com.shachr.common.model.binding;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.shachr.common.model.binding.annotations.Body;
import com.shachr.common.model.binding.annotations.Header;
import com.shachr.common.model.binding.annotations.QueryString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationBeanDeserializerModifier extends BeanDeserializerModifier {

    private final static ArrayList<Class<?>> _allAnnotations ;
    static{
        _allAnnotations = new ArrayList();
        _allAnnotations.add(Header.class);
        _allAnnotations.add(QueryString.class);
        _allAnnotations.add(Body.class);
    }

    private boolean _filterOut ;
    private List<Class<?>> _annotationClass;
    private List<Class<?>> _otherAnnotationClass;

    public AnnotationBeanDeserializerModifier(boolean filterOut, Class<?>... annotationClass){
        _filterOut = filterOut;
        _annotationClass = Arrays.asList(annotationClass);
        _otherAnnotationClass = _allAnnotations
                .stream()
                .filter(anno -> !_annotationClass.contains(anno))
                .collect(Collectors.toList());
    }
    @Override
    public List<BeanPropertyDefinition> updateProperties(
            DeserializationConfig config, BeanDescription beanDesc,
            List<BeanPropertyDefinition> propDefs) {

        List<BeanPropertyDefinition> newPropDefs = new ArrayList<>();
        for(BeanPropertyDefinition propDef : propDefs) {
            Stream<Class<?>> annoStream = _annotationClass.stream();
            boolean hasAnnotation = annoStream.filter(anno -> propDef.getMutator().hasAnnotation(anno)).count()>0;
            if (( !_filterOut && hasAnnotation ) || ( _filterOut && !hasAnnotation ) )
                newPropDefs.add(propDef);
            else {
                Stream<Class<?>> otherAnnoStream = _otherAnnotationClass.stream();
                boolean hasOtherAnnotation = otherAnnoStream.filter(anno -> propDef.getMutator().hasAnnotation(anno)).count()>0;
                if (!hasOtherAnnotation)
                    newPropDefs.add(propDef);
            }
        }
        return newPropDefs;
    }
}


