package fr.cyu.jee.dto;

import fr.cyu.jee.model.UserType;
import fr.cyu.jee.model.Teacher;
import fr.cyu.jee.service.ServiceKey;
import fr.cyu.jee.service.ServiceManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DTOUtil {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private static List<DTOMapping<?, ?>> MAPPINGS;

    private static List<DTOMapping<?, ?>> getMappings() {
        if(MAPPINGS == null) {
            MAPPINGS = List.of(
                    new DTOMapping<>(String.class, Integer.class, DTOConversion.STRING_TO_INT),
                    new DTOMapping<>(String.class, Double.class, DTOConversion.STRING_TO_DOUBLE),
                    new DTOMapping<>(String.class, UserMenuDTO.MenuType.class, DTOConversion.fromThrowing(UserMenuDTO.MenuType::valueOf)),
                    new DTOMapping<>(String.class, UserType.class, DTOConversion.fromThrowing(UserType::valueOf)),
                    new DTOMapping<>(String.class, LocalDate.class, DTOConversion.fromThrowing(LocalDate::parse)),
                    new DTOMapping<>(String.class, LocalDateTime.class, DTOConversion.fromThrowing(LocalDateTime::parse)),
                    new DTOMapping<>(String.class, Duration.class, DTOConversion.fromThrowing(str -> {
                        String[] hoursAndMinutes = str.trim().split(":");
                        return Duration.ofHours(Integer.parseInt(hoursAndMinutes[0])).plusMinutes(Integer.parseInt(hoursAndMinutes[1]));
                    })),
                    new DTOMapping<>(Integer.class, Teacher.class, id -> ServiceManager.getInstance().getService(ServiceKey.USER_REPOSITORY).findTeacherById(id))
                            .contramap(String.class, DTOConversion.STRING_TO_INT),
                    DTOMapping.jpaService(Integer.class, ServiceKey.COURSE_REPOSITORY).contramap(String.class, DTOConversion.STRING_TO_INT),
                    DTOMapping.jpaService(Integer.class, ServiceKey.GRADE_REPOSITORY).contramap(String.class, DTOConversion.STRING_TO_INT),
                    DTOMapping.jpaService(Integer.class, ServiceKey.SUBJECT_REPOSITORY).contramap(String.class, DTOConversion.STRING_TO_INT),
                    DTOMapping.jpaService(Integer.class, ServiceKey.USER_REPOSITORY).contramap(String.class, DTOConversion.STRING_TO_INT)
            );
        }

        return MAPPINGS;
    }

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
                    final Object value = map.get(propertyName);
                    if(!descriptor.getPropertyType().isAssignableFrom(value.getClass())) {
                        Optional<DTOMapping<Object, Object>> compatibleMapping = getMappings()
                                .stream()
                                .filter(mapping -> mapping.isCompatible(value.getClass(), descriptor.getPropertyType()))
                                .map(mapping -> (DTOMapping<Object, Object>) mapping)
                                .findFirst();

                        if(compatibleMapping.isPresent()) {
                            Optional<Object> result = compatibleMapping.get().convert(value);
                            if(result.isPresent()) descriptor.getWriteMethod().invoke(obj, result.get());
                            else return Optional.empty();
                        }
                        else return Optional.empty();
                    } else {
                        descriptor.getWriteMethod().invoke(obj, value);
                    }
                }
            }
            return Optional.of(obj);
        } catch (Exception e) {
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
}
