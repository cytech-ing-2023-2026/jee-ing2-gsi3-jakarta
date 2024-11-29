package fr.cyu.jee.dto;

import fr.cyu.jee.service.JpaRepository;
import fr.cyu.jee.service.ServiceKey;
import fr.cyu.jee.service.ServiceManager;

import java.util.Optional;
import java.util.function.Function;

public record DTOMapping<I, O>(Class<I> from, Class<O> to, DTOConversion<I, O> mapper) {

    boolean isCompatible(Class<?> input, Class<?> expected) {
        return from.isAssignableFrom(input) && expected.isAssignableFrom(to);
    }

    Optional<O> convert(I input) {
        return mapper.convert(input);
    }

    <I2> DTOMapping<I2, O> contramap(Class<I2> newFrom, DTOConversion<I2, I> inputMapper) {
        return new DTOMapping<>(newFrom, to, i2 -> inputMapper.convert(i2).flatMap(mapper::convert));
    }

    static <ID, T> DTOMapping<ID, T> jpa(Class<ID> idClass, JpaRepository<ID, T> repository) {
        return new DTOMapping<>(idClass, repository.getEntityClass(), repository::findById);
    }

    static <ID, T> DTOMapping<ID, T> jpaService(Class<ID> idClass, ServiceKey<? extends JpaRepository<ID, T>> key) {
        return jpa(idClass, ServiceManager.getInstance().getService(key));
    }
}
