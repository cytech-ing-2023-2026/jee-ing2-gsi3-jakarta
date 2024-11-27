package fr.cyu.jee.dto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DTOUtil {

    private static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> Optional<T> convertToBean(Map<String, Object> map, Class<T> clazz) {
        BeanInfo beanInfo;
        T obj;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
            obj = clazz.getConstructor().newInstance();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();

                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    descriptor.getWriteMethod().invoke(obj, value);
                }
            }
            return Optional.of(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static <T> Set<ConstraintViolation<T>> validate(T bean) {
        return VALIDATOR.validate(bean);
    }

    public static <T> DTOResult<T> decodeValid(Map<String, Object> map, Class<T> clazz) {
        Optional<T> decoded = convertToBean(map, clazz);
        if(decoded.isEmpty()) return new DTOResult.DecodingFailure<>();
        else {
            Set<ConstraintViolation<T>> violations = validate(decoded.get());
            return violations.isEmpty() ? new DTOResult.Success<>(decoded.get()) : new DTOResult.ValidationFailure<>(violations);
        }
    }

    public static <T> DTOResult<T> decodeValid(HttpServletRequest request, Class<T> clazz) {
        Map<String, Object> map = new HashMap<>();
        request.getAttributeNames().asIterator().forEachRemaining(name -> map.put(name, request.getAttribute(name)));
        return decodeValid(map, clazz);
    }
}
